package client;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class BotClient extends Client {

    public class BotSocketThread extends SocketThread {


        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        protected void processIncomingMessage(String message) throws IOException {
            System.out.println(message);

            if(message.contains(":")) {
                 String[] array = message.split(": ", 2);
                 String senderName = array[0];
                 String text = array[1];

                if (text.equals("дата" )) {
                    SimpleDateFormat sdf = new SimpleDateFormat("d.MM.YYYY" );
                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();
                    String date = sdf.format(today);
                    String halfAnswer = senderName + ": " + date;
                    String answer = String.format("Информация для %s", halfAnswer);
                    sendTextMessage(answer);
                }

                if (text.equals("день" )) {
                    SimpleDateFormat sdf = new SimpleDateFormat("d" );
                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();
                    String date = sdf.format(today);
                    String halfAnswer = senderName + ": " + date;
                    String answer = String.format("Информация для %s", halfAnswer);
                    sendTextMessage(answer);
                }

                if (text.equals("месяц" )) {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM" );
                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();
                    String date = sdf.format(today);
                    String halfAnswer = senderName + ": " + date;
                    String answer = String.format("Информация для %s", halfAnswer);
                    sendTextMessage(answer);
                }

                if (text.equals("год" )) {
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY" );
                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();
                    String date = sdf.format(today);
                    String halfAnswer = senderName + ": " + date;
                    String answer = String.format("Информация для %s", halfAnswer);
                    sendTextMessage(answer);
                }

                if (text.equals("время" )) {
                    SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss" );
                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();
                    String date = sdf.format(today);
                    String halfAnswer = senderName + ": " + date;
                    String answer = String.format("Информация для %s", halfAnswer);
                    sendTextMessage(answer);
                }

                if (text.equals("час" )) {
                    SimpleDateFormat sdf = new SimpleDateFormat("H" );
                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();
                    String date = sdf.format(today);
                    String halfAnswer = senderName + ": " + date;
                    String answer = String.format("Информация для %s", halfAnswer);
                    sendTextMessage(answer);
                }

                if (text.equals("минуты" )) {
                    SimpleDateFormat sdf = new SimpleDateFormat("m" );
                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();
                    String date = sdf.format(today);
                    String halfAnswer = senderName + ": " + date;
                    String answer = String.format("Информация для %s", halfAnswer);
                    sendTextMessage(answer);
                }

                if (text.equals("секунды" )) {
                    SimpleDateFormat sdf = new SimpleDateFormat("s" );
                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();
                    String date = sdf.format(today);
                    String halfAnswer = senderName + ": " + date;
                    String answer = String.format("Информация для %s", halfAnswer);
                    sendTextMessage(answer);
                }
            }
        }
    }

    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    protected String getUserName() {
        int a = 1;
        int numberBot = (int) (Math.random()*100);
        String name = String.format("date_bot_%d", numberBot);
        return name;
    }

    public static void main(String[] args) {
        new BotClient().run();
    }
}

