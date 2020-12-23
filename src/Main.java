import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        boolean loop = true;

        while(loop) {
            System.out.println("Would you like to Login or Register?");
            Scanner scanner = new Scanner(System.in);
            String str = scanner.next();
            char c = str.charAt(0);
            String fullName = null, username = null, email = null, password = null;
            UserService userService = new UserService();
            switch (c) {
                case 'l':
                case 'L':
                    System.out.println("You have chosen to Login.");

                    boolean loop3 = true;
                    while (loop3) {
                        System.out.println("Please type a username:");
                        Scanner sc = new Scanner(System.in);
                        username = sc.next();
                        if(username.length() > 10){
                            loop3=false;
                        }
                    }

                    loop3 = true;
                    while(loop3) {
                        System.out.println("Please type a password.");
                        Scanner sc = new Scanner(System.in);
                        password = sc.next();
                        if(userService.validPassword(password)) {
                            loop3 = false;
                        }
                    }

                    if (userService.loginValidation("database.txt", username, password)) {
                        System.out.println("You have logged in successfully.");
                        loop = false;
                    }
                    break;
                case 'r':
                case 'R':
                    System.out.println("You have chosen to Register.");

                    boolean loop2 = true;
                    while (loop2) {
                        System.out.println("Please type a full name.");
                        Scanner sc2 = new Scanner(System.in);
                        fullName = sc2.nextLine();
                        String[] words = fullName.split(" ");
                        if (words.length == 2 && words[0].matches("^[a-zA-Z]*$") &&
                                words[1].matches("^[a-zA-Z]*$"))
                            loop2 = false;
                    }

                    loop2 = true;
                    while (loop2) {
                        System.out.println("Please type a username.");
                        Scanner sc2 = new Scanner(System.in);
                        username = sc2.next();
                        if(username.length() > 10){
                            loop2=false;
                        }
                    }

                    loop2 = true;
                    while (loop2) {
                        System.out.println("Please type an email.");
                        Scanner sc2 = new Scanner(System.in);
                        email = sc2.next();
                        boolean result = userService.validateEmail(email);
                        if(result){
                            //email is valid
                            loop2 = false;
                        }
                    }

                    loop2 = true;
                    while (loop2) {
                        System.out.println("Please type a password.");
                        Scanner sc2 = new Scanner(System.in);
                        password = sc2.next();
                        if(userService.validPassword(password)){
                            loop2 = false;
                        }
                    }

                    if (userService.newUser("database.txt", fullName, username, email, password)){
                        System.out.println("You have registered successfully.");
                    loop = false;
                }
                 break;
                default:
                    System.out.println("Incorrect information typed.");
                    System.out.println("Please type either \"Login\" or \"Register\"");
                    str = scanner.next();
            }
        }
    }
}
