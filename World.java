import java.util.Vector;

public class World {
  int size = 32;
  Object[] objects;
  int[][] plane;
  int[][] x;
  int[][] y;
  int[][] z;
  int[] center;

  public World(int size){
    this.size = size;
    plane = new int[size][size];

    // Ranges of each axis {start coords}, {end coords}
    // x1[0][0] y1[0][1] x2[1][0] y2[1][1]
    center = new int[] {size/2, size/3};
    x = new int[][] {center, {size-1, 0}}; 
    y = new int[][] {center, {size/2, size-1}}; 
    z = new int[][] {center, {0, 0}};
  }


  public void loadObject(Object o){
    Vector<Vertex> vertices = o.getVertices();

    for (Vertex vertex : vertices) {
      // Initialize coord from the center
      int[] coord = center;
      int length = size - 1;

      // Prepare vector transformations for each axis x, y, z based on ranges
      double[][] transformations = {
        {vertex.x * (x[1][0] - x[0][0]) / length + x[0][0], vertex.x * (x[1][1] - x[0][1]) / length + x[0][1]},
        {vertex.y * (y[1][0] - y[0][0]) / length + y[0][0], vertex.y * (y[1][1] - y[0][1]) / length + y[0][1]},
        {vertex.z * (z[1][0] - z[0][0]) / length + z[0][0], vertex.z * (z[1][1] - z[0][1]) / length + z[0][1]}
      };

      // Add all vectors to the coordinate
      for (double[] vector : transformations) {
        coord[0] += Math.round(vector[0]);
        coord[1] += Math.round(vector[1]);
      }
      // Map this new coordinate to the 2D plane
      System.out.println(coord[0] + " " + coord[1]);
      plane[coord[0]][coord[1]] = 1;
    }
  }

  public void renderPlane(){
    for(int i = size - 1; i > -1; i--){
      for(int j = 0; j < size; j++){
        if (plane[i][j] == 1) {
          System.out.print("■");
        } else {
          System.out.print(".");
        }
        System.out.print(" ");
      }
      System.out.println();
    }
  }
}
