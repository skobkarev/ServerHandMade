package code;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();


    public static void main(String[] args) throws IOException {

        System.out.println("Enter host name. If local - 8080");

        int port = ConsoleHelper.readInt();

            ServerSocket serverSocket = new ServerSocket(port);
            try{
            System.out.println("code.Server is started");
            while (true) {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                handler.start();
            }
        }catch (Exception e) {
                serverSocket.close();
            }

    }

    private static class Handler extends Thread {

        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            connection.send(new Message(MessageType.NAME_REQUEST));
            Message mes = connection.receive();
            String userName = mes.getData();

            while ((!(mes.getType().equals(MessageType.USER_NAME))) || (userName.equals("")) || (connectionMap.containsKey(userName))) {
            connection.send(new Message(MessageType.NAME_REQUEST));
            mes = connection.receive();
            userName = mes.getData();
            }
//
            connectionMap.put(userName, connection);
            connection.send(new Message(MessageType.NAME_ACCEPTED));

            return userName;
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            for(Map.Entry<String, Connection> m : connectionMap.entrySet()) {
                String name = m.getKey();
                if(!(name.equals(userName))){
                connection.send(new Message(MessageType.USER_ADDED, name));}
            }
        }


        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            Message message = null;

            while (true) {
                message = connection.receive();


                if (message.getType() == MessageType.TEXT) {
                    String mes = userName + ": " + message.getData();
                    sendBroadcastMessage(new Message(MessageType.TEXT, mes));
                } else {
                    ConsoleHelper.writeMessage("Error");
                }

            }
        }

        public void run(){


            SocketAddress address = socket.getRemoteSocketAddress();
            System.out.println("connection with address is seted " + address);

            Connection connection = null;
            String name = null;

            try {
               connection = new Connection(socket);

               name = serverHandshake(connection);

               sendBroadcastMessage(new Message(MessageType.USER_ADDED, name));

               notifyUsers(connection, name);

               serverMainLoop(connection, name);

            }  catch (ClassNotFoundException cnfe) {
                if(!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ConsoleHelper.writeMessage("error during the server connection");
                //--------------------------------
            } catch (IOException ioe){
                if(!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ConsoleHelper.writeMessage("error during the server connection");
                //--------------------------------
            }

            if(name != null) {
                connectionMap.remove(name);
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED, name));
            }
            ConsoleHelper.writeMessage("code.Connection killed");
        }

    }

    public static void sendBroadcastMessage(Message message){

        for(Map.Entry<String, Connection> m : connectionMap.entrySet()){
           try{m.getValue().send(message);}catch (IOException e) {
               System.out.println("message was not sended");;
           }
        }
    }

}

