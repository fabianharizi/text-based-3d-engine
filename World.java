public class World {
  int size = 32;
  Object[] objects;

  public World(int size){
    this.size = size;
  }

  public void renderCoordSys(){
    for(int i = 0; i < size; i++){
      for(int j = 0; j < size; j++){
        System.out.print("■ ");
      }
      System.out.println();
    }
  }
}
