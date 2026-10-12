public class Main{
  public static void main(String[] args){
    World w = new World(33);
    Object o = new Object();

    o.loadFile("sphere.obj");

    w.loadObject(o);
    w.render();
  }
}