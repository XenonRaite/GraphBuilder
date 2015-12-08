package xenonraite.ua.graphbuilder.model;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;

import xenonraite.ua.graphbuilder.GraphicView;

/**
 * Created by Sontik on 08.12.2015.
 */
public class Calc_mnk {

    int K = 3;
    float[] a = new float[K+1];
    float[] b= new float[K+1];
    float[][] sums = new float[K+1][];
    int N = GraphicView.X_MAX-1;


    public Calc_mnk(){
    }

    public void calc(){


        int i=0,j=0,k=0;
        //clean
        for(i=0; i<K+1; i++){
            sums[i] = new float[K+1];
        }
        for(i=0; i<K+1; i++){
            a[i]=0;
            b[i]=0;
            for(j=0; j<K+1; j++){
                sums[i][j] = 0;
            }
        }
        //init square sums matrix
        for(i=0; i<K+1; i++){
            for(j=0; j<K+1; j++){
                sums[i][j] = 0;
                for(k=0; k<N; k++){
                    sums[i][j] += Math.pow(k + 1, i + j);//x = k +1
                }
            }
        }
        //init free coefficients column
        for(i=0; i<K+1; i++){
            for(k=0; k<N; k++){
                b[i] += Math.pow(k+1, i) * getYbyX(k);
            }
        }

        //check if there are 0 on main diagonal and exchange rows in that case
        float temp=0;
        for(i=0; i<K+1; i++){
            if(sums[i][i]==0){
                for(j=0; j<K+1; j++){
                    if(j==i) continue;
                    if(sums[j][i] !=0 && sums[i][j]!=0){
                        for(k=0; k<K+1; k++){
                            temp = sums[j][k];
                            sums[j][k] = sums[i][k];
                            sums[i][k] = temp;
                        }
                        temp = b[j];
                        b[j] = b[i];
                        b[i] = temp;
                        break;
                    }
                }
            }
        }

        //process rows
        for(k=0; k<K+1; k++){
            for(i=k+1; i<K+1; i++){
                if(sums[k][k]==0){
                    return;
                }
                float M = sums[i][k] / sums[k][k];
                for(j=k; j<K+1; j++){
                    sums[i][j] -= M * sums[k][j];
                }
                b[i] -= M*b[k];
            }
        }
        //printmatrix();
        for(i=(K+1)-1; i>=0; i--){
            float s = 0;
            for(j = i; j<K+1; j++){
                s = s + sums[i][j]*a[j];
            }
            a[i] = (b[i] - s) / sums[i][i];
        }


    }

    public float[] getResult(){
        return a;
    }

    public float getYbyX(int x){
        return GraphData.getInstance().getPointData(x);
    }
}
