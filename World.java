import java.util.Vector;

public class World {
  int size = 32;

  double[][] x;
  double[][] y;
  double[][] z;
  double[] origin;

  Vector<int[]> vertices_plane = new Vector<>();  // Reflection of 3D vertices in the 2D plane
  Vector<int[]> faces_plane = new Vector<>();     // Reflection of 3D faces in the 2D plane 


  public World(int size){
    this.size = size;

    // Ranges of each axis {start coords}, {end coords}
    // x1[0][0] y1[0][1] x2[1][0] y2[1][1]
    origin = new double[] {size/4, size/4};
    x = new double[][] {origin, {size-1, 0}}; 
    y = new double[][] {origin, {size/2, size-1}}; 
    z = new double[][] {origin, {0, 0}};
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

    // Render just the edges of the faces

    for (int[] face : faces_plane) {
      int[] v1 = vertices_plane.elementAt(face[0]);
      int[] v2 = vertices_plane.elementAt(face[1]);
      int[] v3 = vertices_plane.elementAt(face[2]);
      double k;

      // v1->v2
      k = (v2[1]-v1[1])/(v2[0]-v1[0]);
      for (int i = v1[0]; i != v2[0]; i -= Integer.signum(v1[0] - v2[0])) {
        plane[i][(int)(k * i)] = 1;
      }

      // v2->v3
      k = (v3[1]-v2[1])/(v3[0]-v2[0]);
      for (int i = v2[0]; i != v3[0]; i -= Integer.signum(v2[0] - v3[0])) {
        plane[i][(int)(k * i)] = 1;
      }

      // v1->v3
      k = (v3[1]-v1[1])/(v3[0]-v1[0]);
      for (int i = v1[0]; i != v3[0]; i -= Integer.signum(v1[0] - v3[0])) {
        plane[i][(int)(k * i)] = 1;
      }
    }

    for(int i = size - 1; i > -1; i--){
      for(int j = 0; j < size; j++){
        if (plane[j][i] == 1) {
          System.out.print("■");
        } else if (plane[j][i] == 2){
          System.out.print("0");
        } else {
          System.out.print(".");
        }
        System.out.print(" ");
      }
      System.out.println();
    }
  }
}
