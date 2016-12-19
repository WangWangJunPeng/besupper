package com.qingke.easyjava.jdbcapp.util;
import java.io.InputStream;
import java.util.Scanner;

public class QingkeConsole {

    public static String NEW_LINE = "\n";

    // private
    private static Scanner scanner;
    
    static {
        scanner = new Scanner(System.in);
    }

    private QingkeConsole(InputStream is) {
        scanner = new Scanner(is);
    }

    public static void print(String line) {
        System.out.print(line);
    }

    public static void println(Object object) {
        System.out.println(object);
    }
    
    public static void println(String line) {
        System.out.println(line);
    }

    public static String askUserInput(String prompt) {
        return askUserInput(prompt, false);
    }

    public static String askUserInput(String prompt, boolean isNullable) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (!"".equals(input) || isNullable) {
                return input;
            }
            System.out.println("Invalid input. Empty value is not allowed!");
        }
    }
    
    public static int askUserNumber(String prompt) {
        
        while (true) {
            String input = askUserInput(prompt, false);

            try {
                int intVal = Integer.parseInt(input);
                return intVal;
            } catch (Exception e) {
                println("Invalid input number!" + input);
            }
        }
    }
    
    public static int askUserNumberWithDefault(String prompt, int defaultVal) {

        try {
            String input = askUserInput(prompt, false);
            return Integer.parseInt(input);
        } catch (Exception e) {
            return defaultVal;
        }
    }
    
    public static long askUserLong(String prompt) {
        while (true) {
            String input = askUserInput(prompt, false);

            try {
                long intVal = Long.parseLong(input);
                return intVal;
            } catch (Exception e) {
                println("Invalid input number!" + input);
            }
        }
    }

    public static void terminate() {
        println("Bye Bye");
        System.exit(0);
    }
}
