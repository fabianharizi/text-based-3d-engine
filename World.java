import java.util.Vector;

public class World {
  int size = 32;

  // Render view properties
  int[][] x;
  int[][] y;
  int[][] z;
  int[] origin;

  // Image of the 3D space in the 2D view
  public int[][] view = new int[size][size];
  public Vector<int[]> vertices_plane = new Vector<>();  // Reflection of 3D vertices in the 2D plane
  public Vector<int[]> faces_plane = new Vector<>();     // Reflection of 3D faces in the 2D plane 

  // Install camera to the world
  Camera cam = new Camera();

  public World(int size){
    this.size = size;

    // Ranges of each axis {start coords}, {end coords}
    // x1[0][0] y1[0][1] x2[1][0] y2[1][1]
    this.origin = new int[] {size/2, size/3};
    this.x = new int[][] {origin, {size-1, 0}}; 
    this.y = new int[][] {origin, {size/2, size-1}}; 
    this.z = new int[][] {origin, {0, 0}};
    this.view = new int[size][size];
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
  }

  public void render(){
    cam.loadWorld(this);
    cam.render(view, size);
  }
}
