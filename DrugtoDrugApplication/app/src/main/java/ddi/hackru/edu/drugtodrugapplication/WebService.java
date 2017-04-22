package ddi.hackru.edu.drugtodrugapplication;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jasper Bae on 4/22/2017.
 */

public class WebService
{

    public interface OnRequestAdversitiesRespondedListener
    {
        void onRequestAdversitiesResponded(List<Adversity> adversities);
    }

    public interface OnRequestRxNormIdRespondedListener
    {
        void onRequestRxNormIdResponded(Medication medication);
    }

    private static final class Instance
    {
        private static final WebService instance = new WebService();
    }

    private OnRequestAdversitiesRespondedListener onRARListener;
    private OnRequestRxNormIdRespondedListener onRIRListener;

    private WebService(){}

    public void setOnRequestAdversitiesRespondedListener(OnRequestAdversitiesRespondedListener listener)
    {
        this.onRARListener = listener;
    }

    public void setOnRequestRxNormIdRespondedListener(OnRequestRxNormIdRespondedListener listener)
    {
        this.onRIRListener = listener;
    }

    public void requestRxNormId(String drugName) throws IOException
    {
        AsyncTask<String, Void, InputStream> task = new AsyncTask<String, Void, InputStream>() {
            @Override
            protected InputStream doInBackground(String... params)
            {
                return null;
            }

            @Override
            protected void onPostExecute(InputStream stream) {
                super.onPostExecute(stream);
            }
        };
        task.execute(new String[]{ drugName });
    }

    public void requestAdversities(String drugName) throws IOException
    {
        return null;
    }

    public static WebService getInstance()
    {
        return Instance.instance;
    }

}
