import java.util.Random;
import java.util.Vector;

public class World {
  int size = 32;

  int[][] x;
  int[][] y;
  int[][] z;
  int[] origin;

  Vector<int[]> vertices_plane = new Vector<>();  // Reflection of 3D vertices in the 2D plane
  Vector<int[]> faces_plane = new Vector<>();     // Reflection of 3D faces in the 2D plane 

  char[] gradient = {'.',',','-','~',':',';','=','!','*','#','$','@'};


  public World(int size){
    this.size = size;

    // Ranges of each axis {start coords}, {end coords}
    // x1[0][0] y1[0][1] x2[1][0] y2[1][1]
    origin = new int[] {size/4, size/4};
    x = new int[][] {origin, {size-1, 0}}; 
    y = new int[][] {origin, {size/2, size-1}}; 
    z = new int[][] {origin, {0, 0}};
  }


  public void loadObject(Object o){
    Vector<Vertex> vertices = o.getVertices();
    Vector<Face> faces = o.getFaces();

    for (Vertex vertex : vertices) {
      // Initialize coord to 0,0
      int[] coord = {0,0};
      int length = size - 1;

      // Prepare vector transformations for each axis x, y, z based on ranges
      double[][] transformations = {
        {(vertex.x * (x[1][0] - x[0][0]) / length + x[0][0]) - origin[0], (vertex.x * (x[1][1] - x[0][1]) / length + x[0][1]) - origin[1]},
        {(vertex.y * (y[1][0] - y[0][0]) / length + y[0][0]) - origin[0], (vertex.y * (y[1][1] - y[0][1]) / length + y[0][1]) - origin[1]},
        {(vertex.z * (z[1][0] - z[0][0]) / length + z[0][0]) - origin[0], (vertex.z * (z[1][1] - z[0][1]) / length + z[0][1]) - origin[1]},
        {origin[0], origin[1]}
      };

      // Add all vectors to the coordinate
      for (double[] vector : transformations) {
        coord[0] += Math.round(vector[0]);
        coord[1] += Math.round(vector[1]);
      }

      vertices_plane.add(coord);
    }

    for (Face face : faces) {
      int[] vertices_index_in_face = {
        vertices.indexOf(face.v1),
        vertices.indexOf(face.v2),
        vertices.indexOf(face.v3)
      };

      faces_plane.add(vertices_index_in_face);
    }


    for (int[] vertex : vertices_plane) {
      System.out.println("v " + vertex[0] + " " + vertex[1]);
    }
    for (int[] face : faces_plane) {
      System.out.println("f " + face[0] + " " + face[1] + " " + face[2]);
    }
  }

  public void renderPlane(){
    int[][] plane = new int[size][size];

    // We need to sort the faces from their largest z-value (furthest -> closest)

    /*
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    */

    for (int[] face : faces_plane) {
      // Select 3 points of the face

      int light = new Random().nextInt(gradient.length);

      int[] v1 = vertices_plane.elementAt(face[0]);
      int[] v2 = vertices_plane.elementAt(face[1]);
      int[] v3 = vertices_plane.elementAt(face[2]);

      /*--------------------------------------
       |
       |  TRIANGLE RASTERIZATION
       |  
       |  Render faces using the barycentric coordinate technique
       |  Point P inside triangle T(A,B,C) can be expressed as 
       |    P = αA + βB + γC
       |
       |  The point is inside the triangle if all α, β and γ are in [0;1],
       |  Otherwise, it is outside the triangle
       |    
       |  Area of the triangle when we know 3 points can be found using the  Shoelace formula: 
       |    S = |x1(y2-y3) + x2(y3-y1) + x3(y1-y2)| / 2
       |
       +-------------------------------------*/

      
      // Find the bounding box of the face (so we don't have to go through the whole array)
      int[][] box = {
        {Math.min(Math.min(v1[0], v2[0]), v3[0]), Math.min(Math.min(v1[1], v2[1]), v3[1])}, 
        {Math.max(Math.max(v1[0], v2[0]), v3[0]), Math.max(Math.max(v1[1], v2[1]), v3[1])}
      };

      // Calculates the common denominator to avoid division by 0 (when all points are in a straight line)
      double denom = (double)((v2[1] - v3[1]) * (v1[0] - v3[0]) + (v3[0] - v2[0]) * (v1[1] - v3[1]));

      if(denom != 0.0){
        for(int i = box[0][0]; i <= box[1][0]; i++){
          for(int j = box[0][1]; j <= box[1][1]; j++){

            // Formula to find the weighted coordinates
            double a = ((v2[1]-v3[1])*(i-v3[0])+(v3[0]-v2[0])*(j-v3[1])) / denom; 
            double b = ((v3[1]-v1[1])*(i-v3[0])+(v1[0]-v3[0])*(j-v3[1])) / denom;
            double c = 1.0 - a - b;

            if((a >= 0 && a <= 1) && (b >= 0 && b <= 1) && (c >= 0 && c <= 1)){
              plane[i][j] = light;
            }
          }
        }
      }
    }

    for(int i = size - 1; i > -1; i--){
      for(int j = 0; j < size; j++){
        System.out.print(gradient[plane[i][j]] + " ");
      }
      System.out.println();
    }
  }
  
  private double triangleArea(int[] v1, int[] v2, int[] v3){
    return Math.abs(v1[0]*(v2[1] - v3[1]) + v2[0]*(v3[1] - v1[1]) + v3[0]*(v1[1] - v2[1])) / 2;
  }
}
