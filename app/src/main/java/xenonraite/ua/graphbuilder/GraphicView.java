package xenonraite.ua.graphbuilder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import xenonraite.ua.graphbuilder.model.GraphData;
import xenonraite.ua.graphbuilder.model.Langraje;
import xenonraite.ua.graphbuilder.model.tasks.TaskMNK;

/**
 * Created by Sontik on 08.12.2015.
 */
public class GraphicView extends View {


    public final static int Y_MIN = 0;
    public final static int Y_MAX = 10;
    public final static int X_MIN = 0;
    public final static int X_MAX = 6;

    int width = 0;
    int height = 0;

    private final int margin = 40;
    private final int line_width = 3;
    private final int shadow_line_width = 3;
    private final int INC_LENGTH = 25;

    private int x_pix_min = 0;
    private int y_pix_min = 0;
    private int x_pix_max = 0;
    private int y_pix_max = 0;

    private final int GRAPH_Y_MAX_MODEL = 10;
    private final int GRAPH_X_MAX_MODEL = 5;

    private int x_period = 0;
    private int y_period = 0;
    private int x_line_length = 0;

    private TaskMNK taskMNK = null;
    private static float[] MNKresult = null;

    public static final float GRAHP_PAINT_STEP = 0.25F;
    public static final float GRAHP_START_POINT = 0.5F;


    public GraphicView(Context context, int width, int height) {
        super(context);
        this.width = width;
        this.height = height;

        // line x an y
        this.x_pix_min = margin;
        this.y_pix_min = height - margin;
        this.x_pix_max = width - margin;
        this.y_pix_max = margin;
        //--

        x_line_length = x_pix_max - x_pix_min;
        x_period = x_line_length / GRAPH_X_MAX_MODEL - 10; //graphic fix
        y_period = (y_pix_max - y_pix_min) / GRAPH_Y_MAX_MODEL;

        //set input touch
        setinput();


    }

    public void setTask(TaskMNK taskMNK){
        this.taskMNK = taskMNK;
    }

    public void setTaskUnlink(){
        this.taskMNK = null;
    }
    private void setinput() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float x = event.getX();
                float y = event.getY();


                float x_tmp = (x - margin - x_period) / x_period;

                int x_clean = (int) (x_tmp + 0.5);

                GraphData.getInstance().setPointData(x_clean, ((y - y_pix_min) / y_period));

                invalidate();


                if(!checkOnRelise()&&taskMNK==null){
                    return true;
                }
                taskmnkstart();
                return true;
            }

        });
    }

    private void taskmnkstart() {
        if(taskMNK != null && !taskMNK.isCancelled()){
            taskMNK.cancel(true);

            taskMNK = new TaskMNK(this);
            taskMNK.execute();
        }
        taskMNK = new TaskMNK(this);
        taskMNK.execute();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGraphic(canvas);
    }

    private void drawGraphic(Canvas canvas) {

        Paint paint = new Paint();
        paint.setStrokeWidth(line_width);
        paint.setColor(Color.BLACK);

        Paint paint_shadow_line = new Paint();
        paint_shadow_line.setStrokeWidth(shadow_line_width);
        paint_shadow_line.setColor(0xFFBBBBBB);

        //draw grid and scale
        int line_pos = 0;
        for (int x = 1; x <= GRAPH_X_MAX_MODEL; x++) {

            line_pos = 1 * x * x_period + margin;

            //shadow line
            canvas.drawLine(line_pos, y_pix_min + INC_LENGTH, line_pos, y_pix_max - INC_LENGTH, paint_shadow_line);
            //scale
            canvas.drawLine(line_pos, y_pix_min + 10, line_pos, y_pix_min - 10, paint);
            //num text
            canvas.drawText("" + x, line_pos - 5, y_pix_min + 30, paint);
        }

        //draw X line
        canvas.drawLine(x_pix_min - INC_LENGTH, y_pix_min, x_pix_max + INC_LENGTH, y_pix_min, paint);
        //draw Y line
        canvas.drawLine(x_pix_min, y_pix_min + INC_LENGTH, x_pix_min, y_pix_max - INC_LENGTH, paint);


        //draw arrow
        Paint arrow_paint = new Paint();
        arrow_paint.setStrokeWidth(4);
        arrow_paint.setColor(Color.BLACK);
        arrow_paint.setStyle(Paint.Style.FILL_AND_STROKE);
        arrow_paint.setAntiAlias(true);

        Path path = new Path();
        path.setFillType(FillType.EVEN_ODD);

        path.moveTo(x_pix_max + INC_LENGTH, y_pix_min);
        path.lineTo(x_pix_max - 7, y_pix_min + 10);
        path.lineTo(x_pix_max - 7, y_pix_min - 10);
        path.lineTo(x_pix_max + INC_LENGTH, y_pix_min);
        path.close();

        canvas.drawPath(path, arrow_paint);

        path = new Path();
        path.setFillType(FillType.EVEN_ODD);
        path.moveTo(x_pix_min, y_pix_max - INC_LENGTH);
        path.lineTo(x_pix_min - 10, y_pix_max);
        path.lineTo(x_pix_min + 10, y_pix_max);
        path.lineTo(x_pix_min, y_pix_max - INC_LENGTH);

        path.close();

        canvas.drawPath(path, arrow_paint);
        //--
        //draw input data
        for(int k = 0;k<GRAPH_X_MAX_MODEL;k++){
            dravPointOfGraph(canvas,x_period*(k+1)+margin,y_pix_min+ y_period* GraphData.getInstance().getPointData(k),GraphData.getInstance().getPointData(k));
        }

        //draw result
        if(MNKresult != null){
            Log.d("TEST", "Draw result");

            Paint dots = new Paint();
            dots.setStrokeWidth(5);
            dots.setColor(Color.BLACK);

            Paint lineMNK = new Paint();
            lineMNK.setStyle(Paint.Style.STROKE);
            lineMNK.setStrokeWidth(3);
            lineMNK.setColor(Color.RED);
            lineMNK.setAntiAlias(true);

            Path pathMNK = new Path();
            int polinom_scale = 3;
            boolean isFirst = true;
            for(float x_mnk = GRAHP_START_POINT;x_mnk<X_MAX-GRAHP_START_POINT;x_mnk=x_mnk+GRAHP_PAINT_STEP){

                float y_temp = 0;
                for(int i = 0;i<=polinom_scale;i++){
                    y_temp = y_temp + MNKresult[i]*((float)Math.pow(x_mnk,i));
                }

                if(isFirst == true){
                    pathMNK.moveTo(x_mnk * x_period + margin, y_pix_min + y_temp*y_period );
                    canvas.drawPoint(x_mnk * x_period + margin,y_pix_min + y_temp*y_period, dots);

                    canvas.drawPoint(x_mnk, y_temp,dots);

                    isFirst = false;
                    continue;
                }

                canvas.drawPoint(x_mnk * x_period + margin,y_pix_min +y_temp*y_period,dots);
                pathMNK.lineTo(x_mnk*x_period+margin, y_pix_min + y_temp*y_period);
            }
            canvas.drawPath(pathMNK, lineMNK);


            //and draw langranje

            float[] xArray= new float[X_MAX-1];
            for(int i = 0;i<xArray.length;i++){
                xArray[i] = i+1;
            }


            isFirst = true;
            Path pathLagranje = new Path();
            for(float point = GRAHP_START_POINT; point<X_MAX-GRAHP_START_POINT;point = point+GRAHP_PAINT_STEP/2){


                if(isFirst){
                    isFirst= false;
                    pathLagranje.moveTo(point*x_period+margin,y_pix_min + Langraje.formulaLangraje(point,xArray,GraphData.getInstance().getData())*y_period);
                    canvas.drawPoint(point*x_period+margin,y_pix_min + Langraje.formulaLangraje(point,xArray,GraphData.getInstance().getData())*y_period, dots);
                }else{
                    pathLagranje.lineTo(point*x_period+margin,y_pix_min +Langraje.formulaLangraje(point,xArray,GraphData.getInstance().getData())*y_period);
                    canvas.drawPoint(point*x_period+margin,y_pix_min + Langraje.formulaLangraje(point,xArray,GraphData.getInstance().getData())*y_period, dots);
                }

            }
            lineMNK.setColor(0xFFffa500);
            lineMNK.setAntiAlias(true);
            canvas.drawPath(pathLagranje,lineMNK);

        }

    }

    private boolean checkOnRelise() {
        for (int _x = 0; _x < X_MAX - 1; _x++) {
            if (GraphData.getInstance().getPointData(_x) == 0) {
                return false;
            }
        }
        return true;
    }

    private void dravPointOfGraph(Canvas canvas, int x, float y,float y_value) {

        Paint paint_point = new Paint();
        paint_point.setStrokeWidth(4);
        paint_point.setColor(0xFF423189);
        paint_point.setStyle(Paint.Style.FILL_AND_STROKE);
        paint_point.setAntiAlias(true);

        Path path = new Path();
        path.setFillType(FillType.EVEN_ODD);

        path.moveTo(x + 4, y);
        path.lineTo(x, y + 4);
        path.lineTo(x - 4, y);
        path.lineTo(x, y - 4);
        path.lineTo(x + 4, y);

        if(y != y_pix_min){
            Paint text = new Paint();
            canvas.drawText(""+y_value,x+6,y,text);
        }

        path.close();

        canvas.drawPath(path, paint_point);
    }

    public void setMNKResult(float[] result){
        MNKresult = result;
    }
}
