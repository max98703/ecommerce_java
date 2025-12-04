package Helper;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InputHelper {
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    //handle integer input
    public static int checkInt(Scanner sc, String message) {
        while (true) {
            System.out.print(message);

            try {
                int value = sc.nextInt();
                sc.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("That doesn't look like a whole number. Please try again.");
                sc.nextLine(); // clear wrong input
            }
        }
    }

    //handle double input
    public static double checkDouble(Scanner sc, String message) {
        while (true) {
            System.out.print(message);

            try {
                double value =  sc.nextDouble();
                sc.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("That amount seems invalid. Please enter a valid number.");
                sc.nextLine(); // clear wrong input
            }
        }
    }
    
    //handle email input
    public static String checkEmail(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String email = sc.nextLine().trim();
            if (EMAIL_PATTERN.matcher(email).matches()) {
                return email;
            } else {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        }
    }
}
