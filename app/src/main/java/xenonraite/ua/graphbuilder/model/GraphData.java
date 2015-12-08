package xenonraite.ua.graphbuilder.model;


import xenonraite.ua.graphbuilder.GraphicView;

/**
 * Created by Sontik on 08.12.2015.
 */
public class GraphData {

    private static GraphData instance;

    private float[] data = new float[GraphicView.X_MAX+1];

    private GraphData(){

        for(int x = 0; x < GraphicView.X_MAX+1;x++){
            data[x] = 0;
        }
    }

    public static GraphData getInstance(){
        if(instance == null){
            instance = new GraphData();
        }
        return instance;
    }

    public float getPointData(int n){
        if(n >= GraphicView.X_MAX+1)
            return 0;
        return data[n];
    }

    public float[] getData(){
        return data;
    }

    public synchronized void setPointData(int n,float data){
        this.data[n]=data;
    }

}
