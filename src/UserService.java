import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",Pattern.CASE_INSENSITIVE);

    public User[] fillUserInfo(List<String> read){

        int defaultUserMembers = 4;
        int index = 0;
        User[] users = new User[defaultUserMembers];
        for(int i=0;i<read.size();i++){
            String[] member = read.get(i).split(",");
            if(member.length == defaultUserMembers){
                users[index++] = new User(member[0],member[1],member[2], member[3]);
            } else {
                System.out.println("Row " + i + " does not have all the user information.");
            }
        }
        return users;
    }

    public  static boolean validateEmail(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public static boolean loginValidation(String fileName, String username, String password) {
        String path = "C:\\Users\\ACER\\IdeaProjects\\Homework10\\src\\" + fileName;
        try {
            File file = new File(path);
            boolean exists = file.exists();
            if (exists) {
                List<String> read = FileService.read(path);

                UserService userService = new UserService();
                User[] users = userService.fillUserInfo(read);

                int count = 0;
                String encryptPass = userService.md5(password);
                for (int i = 0; i < read.size(); i++) {
                    if (users[i].getUsername().equals(username)) {
                        count++;
                    }
                    if (users[i].getPassword().equals(encryptPass)) {
                        count++;
                    }
                }

                if (count == 2) return true;
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        System.out.println("Wrong username or password.");
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

    public static boolean validPassword(String password){
        int countUpperCase = 0, countDigit = 0;
        if(password.length() > 8){
            for(int i=0;i<password.length();i++) {
                if(Character.isUpperCase(password.charAt(i))){
                    countUpperCase++;
                }
                if(Character.isDigit(password.charAt(i))){
                    countDigit++;
                }
            }
            if(countUpperCase >= 2 && countDigit >= 3)
                return true;
        }
        return false;
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

    public static boolean checkUser(String fileName, String fullName, String username, String email, String password) {
        String path = "C:\\Users\\ACER\\IdeaProjects\\Homework10\\src\\" + fileName;

        try {
            List<String> read = FileService.read(path);

            UserService userService = new UserService();
            User[] users = userService.fillUserInfo(read);

            boolean temp = false;
            for (int i = 0; i < read.size(); i++) {
                if (users[i].getEmail().equals(email) || users[i].getUsername().equals(username)) {
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
}
