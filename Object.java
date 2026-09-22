import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Object {
  Face[] faces;

  public void loadFile(String pathname){
    File obj = new File(pathname);

    try (Scanner reader = new Scanner(obj)) {
      while (reader.hasNextLine()) {
        String data = reader.nextLine();
        System.out.println(data);
      }
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}