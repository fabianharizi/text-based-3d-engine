public class Camera {
  int[] position;
  int[] orientation;

  public void loadWorld(World w){
    for (int[] face : w.faces_plane) {
      // Select 3 points of the face

      int[] v1 = w.vertices_plane.elementAt(face[0]);
      int[] v2 = w.vertices_plane.elementAt(face[1]);
      int[] v3 = w.vertices_plane.elementAt(face[2]);

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
       |  Area of the triangle when we know 3 points can be found using the Shoelace formula: 
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
              if(i >= 0 && i < w.view.length && j >= 0 && j < w.view.length){
                w.view[i][j] = 1;
              }
            }
          }
        }
      }
    }
  }

  public void render(int[][] view, int size){
    for(int j = size-1; j > 0; j--){
      for(int i = 0; i < size-1; i++){
        if(view[i][j] == 1){
          System.out.print("@ ");
        } else {
          System.out.print("  ");
        }
      }
      System.out.println();
    }
  }
}
