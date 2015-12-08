package xenonraite.ua.graphbuilder.model;


/**
 * Created by Sontik on 08.12.2015.
 */
public class Lagranje {



    public static float formulaLagraje(float x, float[] x_array, float[] y_array){
        int size = x_array.length;
        float sum = 0;
        for(int i = 0; i < size; i++){
            float mul = 1;
            for(int j = 0; j < size; j++){
                if(i != j){
                    mul *= (x-x_array[j])/(x_array[i]-x_array[j]);
                }
            }
            sum += y_array[i]*mul;
        }
        return sum;
    }

}
