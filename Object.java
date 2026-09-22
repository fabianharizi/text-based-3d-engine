import java.util.Vector;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Object {
  Vector<Vertex> vertices = new Vector<>();
  Vector<Face> faces = new Vector<>();

  public void loadFile(String pathname){
    File obj = new File(pathname);

    try (Scanner reader = new Scanner(obj)) {
      while (reader.hasNextLine()) {
        String data = reader.nextLine();

        if (data.charAt(0) == 'v') {
          String[] v = data.split(" ");
          
          vertices.add(new Vertex(
            Integer.parseInt(v[1]), 
            Integer.parseInt(v[2]), 
            Integer.parseInt(v[3])
          ));
        } else if (data.charAt(0) == 'f') {
          String[] f = data.split(" ");
          
          faces.add(new Face(
            vertices.get(Integer.parseInt(f[1]) - 1), 
            vertices.get(Integer.parseInt(f[2]) - 1), 
            vertices.get(Integer.parseInt(f[3]) - 1)
          ));
        } else {
          System.err.println("File is reading an non-existing type, reading " + data.charAt(0));
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("An error occurred.");
      e.printStackTrace();
    }
  }
}