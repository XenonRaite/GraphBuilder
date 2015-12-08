package xenonraite.ua.graphbuilder.model.tasks;

import android.os.AsyncTask;

import xenonraite.ua.graphbuilder.GraphicView;
import xenonraite.ua.graphbuilder.model.Calc_mnk;

/**
 * Created by Sontik on 08.12.2015.
 */
public class TaskMNK extends AsyncTask<String, Integer, Integer> {
    Calc_mnk calc_mnk;
    GraphicView view;

    public TaskMNK(GraphicView view){
        this.view = view;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
    }


    @Override
    protected void onPostExecute(Integer integer) {


        view.setMNKResult(calc_mnk.getResult());
        view.setTaskUnlink();
        view.invalidate();
    }

    @Override
    protected Integer doInBackground(String... parameter) {
        calc_mnk = new Calc_mnk();
        calc_mnk.calc();
        return 0;
    }
}