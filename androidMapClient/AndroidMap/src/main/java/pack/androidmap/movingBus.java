package pack.androidmap;

/**
 * Created by Lefteris on 17/6/2015.
 */
public class movingBus {
    private int index;
    private float distance;
    private String id;

    public void setIndex(Integer index){
        this.index=index;
    }
    public void setDistance(float distance){ this.distance=distance; }
    public void setId(String id){
        this.id=id;
    }

    public int getIndex(){
        return this.index;
    }
    public float getDistance(){
        return this.distance;
    }
    public String getId(){
        return this.id;
    }
}
