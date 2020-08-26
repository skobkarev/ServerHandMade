package client;



import code.Connection;
import code.ConsoleHelper;
import code.Message;
import code.MessageType;

import java.io.IOException;
import java.net.Socket;




public class Client {

    public Connection connection;
    volatile private boolean clientConnected = false;

    public static void main(String[] args) {
        new Client().run();
    }

    public class SocketThread extends Thread{


        protected void processIncomingMessage(String message) throws IOException {
            System.out.println(message);
        }

        protected void informAboutAddingNewUser(String userName) {
            System.out.println("User connected: " + userName);
        }

        protected void informAboutDeletingNewUser(String userName) {
            System.out.println("User left: " + userName);
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            synchronized (Client.this){
                Client.this.clientConnected = clientConnected;
                Client.this.notify();
            }
        }

        protected void clientHandshake() throws IOException, ClassNotFoundException {

            while (true) {
                Message message = connection.receive();

                if (message.getType() == (MessageType.NAME_REQUEST)) {
                    String name = getUserName();
                    connection.send(new Message(MessageType.USER_NAME, name));
                }
                if (message.getType() == (MessageType.NAME_ACCEPTED)) {
                        notifyConnectionStatusChanged(true);
                        return;
                }
                if(!(message.getType() == (MessageType.NAME_REQUEST)) &
                    !(message.getType() == (MessageType.NAME_ACCEPTED))) throw new IOException("Unexpected code.MessageType");
            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException {


            while (true) {
                Message message = connection.receive();

                if (message.getType() == (MessageType.TEXT)) {
                    processIncomingMessage(message.getData());
                }

                if (message.getType() == (MessageType.USER_ADDED)) {
                    informAboutAddingNewUser(message.getData());
                }

                if (message.getType() == (MessageType.USER_REMOVED)) {
                    informAboutDeletingNewUser(message.getData());
                }

                if (!(message.getType() == (MessageType.TEXT)) &
                        !(message.getType() == (MessageType.USER_ADDED)) &
                        !(message.getType() == (MessageType.USER_REMOVED)))
                    throw new IOException("Unexpected code.MessageType" );
            }
        }


        public void run(){
             try {
                 connection = new Connection(new Socket(getServerAddress(), getServerPort()));
                 clientHandshake();
                 clientMainLoop();
             } catch (IOException e) {
                 notifyConnectionStatusChanged(false);
             } catch (ClassNotFoundException e) {
                 notifyConnectionStatusChanged(false);
             }
        }
    }

        protected String getServerAddress(){
            ConsoleHelper.writeMessage("enter server address");
            return ConsoleHelper.readString();
        }

        protected int getServerPort(){
            ConsoleHelper.writeMessage("enter server port");
            return ConsoleHelper.readInt();
        }

        protected String getUserName(){
            ConsoleHelper.writeMessage("enter UserName");
            return ConsoleHelper.readString();
        }

        protected boolean shouldSendTextFromConsole(){
            return true;
        }

        protected SocketThread getSocketThread(){
            SocketThread socketThread = new SocketThread();
            return socketThread;
        }

        protected void sendTextMessage(String text) {

            try {
                Message message = new Message(MessageType.TEXT, text);
                connection.send(message);
            } catch (IOException e) {
               ConsoleHelper.writeMessage("соединение не было установлено");
               clientConnected = false;
            }
        }

        public void run() {

            SocketThread socketThread = getSocketThread();
            socketThread.setDaemon(true);
            socketThread.start();

            synchronized (this){
                try {
                    wait();
                } catch (InterruptedException e) {
                    ConsoleHelper.writeMessage("socketThread exception");
                    return;
                }
            }

            if(clientConnected)
                ConsoleHelper.writeMessage("Соединение установлено\nДля выхода наберите команду 'exit'.");
            else ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");

            while(clientConnected){
            String mes = ConsoleHelper.readString();
            if(mes.equals("exit")) break;
            if(shouldSendTextFromConsole()) sendTextMessage(mes);
            }

        }

}

