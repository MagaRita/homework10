import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) throws IOException {

        boolean loop = true;

        while(loop) {
            System.out.println("Would you like to Login or Register?");
            Scanner scanner = new Scanner(System.in);
            String str = scanner.next();
            char c = str.charAt(0);
            String fullName = null, username = null, email = null, password = null;
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
                        if (password.length() > 8 && password.matches("^(?=(?:.*[A-Z].*){2})(?=(?:.*\\d.*){3}).*$")) {
                            loop3 = false;
                        }
                    }

                    if (loginValidation("database.txt", username, password)) {
                        Login login = new Login(username, password);
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
                        boolean result = validateEmail(email);
                        if(result){
                            //email is valid
                            loop2 = false;
                        }
                    }

                    //password && 2 uppercase letters && 3 numbers
                    loop2 = true;
                    while (loop2) {
                        System.out.println("Please type a password.");
                        Scanner sc2 = new Scanner(System.in);
                        password = sc2.next();
                        if(password.length() > 8 && password.matches("^(?=(?:.*[A-Z].*){2})(?=(?:.*\\d.*){3}).*$")){
                            loop2 = false;
                        }
                    }

                    if (newUser("database.txt", fullName, username, email, password)){

                    Register registerNewUser = new Register(fullName, username, email, password);
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

    public  static boolean validateEmail(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public static boolean newUser(String fileName, String fullName, String username, String email, String password) {
        if (FileService.createFile(fileName)) {
            String path = "C:\\Users\\ACER\\IdeaProjects\\Homework10\\src\\" + fileName;

            try {
                Files.write(Paths.get(path), fullName.getBytes());
                FileService.write(path, "," + username);
                FileService.write(path, "," + email);
                FileService.write(path, "," + md5(password) + "\n");
            } catch (Exception exception) {
                System.out.println("An error occurred.");
                exception.printStackTrace();
            }

            return true;
        }
        else return  checkUser(fileName, fullName, username, email, password);
    }

    public static boolean loginValidation(String fileName, String username, String password) {
        String path = "C:\\Users\\ACER\\IdeaProjects\\Homework10\\src\\" + fileName;
        try {
            File file = new File(path);
            boolean exists = file.exists();
        if (exists) {
                List<String> read = FileService.read(path);

                Set<String> set = new HashSet<>(read);

                int count = 0;
                String encryptPass = md5(password);
                for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                    String f = it.next();
                    if (f.contains(username)) {
                        count++;
                    }
                    if(f.contains(encryptPass)){
                        count++;
                    }
                }

                if (count == 2)
                    return true;
            }} catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        System.out.println("Wrong username or password.");
        return false;
    }

    public static boolean checkUser(String fileName, String fullName, String username, String email, String password) {
            String path = "C:\\Users\\ACER\\IdeaProjects\\Homework10\\src\\" + fileName;

            try {
                //harcnel
                List<String> read = FileService.read(path);
                Set<String> set = new HashSet<>(read);

                boolean temp = false;
                for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
                    String f = it.next();
                    if (f.contains(email) || f.contains(username)) {
                        temp = true;
                    }
                }

                if (!temp) {

                    try {
                        FileService.write(path,  fullName);
                        FileService.write(path, "," + username);
                        FileService.write(path, "," + email);
                        FileService.write(path, "," + md5(password) + "\n");
                        return true;

                    } catch (Exception exception) {
                        System.out.println("An error occurred.");
                        exception.printStackTrace();
                    }
                } else {
                    System.out.println("You are already logged in. Please Login.");
                }

            } catch (Exception e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            return false;
    }

    public static String md5(String password) {

        String md5 = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes(), 0, password.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }
}
