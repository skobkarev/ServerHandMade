package code;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {

    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message){
        System.out.println(message);
    }

    public static String readString() {
        String s = null;
        while(s == null){
        try{s = bufferedReader.readLine();}catch (IOException e){
            System.out.println("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
           }
        }
        return s;
    }


    public static int readInt(){
        int number = 0;
        try{number = Integer.parseInt(readString());} catch (NumberFormatException nfe){
            System.out.println("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            try{number = Integer.parseInt(readString());}catch (NumberFormatException nfe1){
                System.out.println("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            }
        }
        return number;
    }

}
