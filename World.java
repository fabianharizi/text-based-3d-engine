import java.util.Arrays;

public class World {
  int size = 32;
  Object[] objects;
  int[][] space;
  int[][] x;
  int[][] y;
  int[][] z;

  public World(int size){
    this.size = size;
    space = new int[size][size];
    Arrays.fill(space, 0);

    // {start coords}, {end coords}
    x = new int[][] {{size-1, 0}, {size-1, size-1}};
    y = new int[][] {{size-1, 0}, {0, 0}}; 
    z = new int[][] {{size-1, 0}, {size/2, size/2}};
  }


  public void loadObject(Object o){
    
  }
}
