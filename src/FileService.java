import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileService {

    static List<String> read(String path) throws IOException {
        return Files.readAllLines(Paths.get(path));
    }

    static void write(String path, String text) throws IOException {
         Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
    }

    public static boolean createFile(String fileName){
        try {
            File file = new File("C:\\Users\\ACER\\IdeaProjects\\Homework10\\src\\" + fileName);
            if (file.createNewFile()) {
                System.out.println("The following file has been created: " + file.getName());
            } else {
                System.out.println("File already exists.");
                return false;
            }

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            System.exit(0);
        }
        return true;
    }
}
