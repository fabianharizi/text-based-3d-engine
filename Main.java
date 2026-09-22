public class Main{
  public static void main(String[] args){
    World w = new World(50);
    Object o = new Object();

    o.loadFile("Cube.txt");

    w.loadObject(o);
    w.renderPlane();
  }
}