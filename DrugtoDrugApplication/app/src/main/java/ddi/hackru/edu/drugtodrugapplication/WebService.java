package ddi.hackru.edu.drugtodrugapplication;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Jasper Bae on 4/22/2017.
 *
 * Responsible for http requests to the REST APIs
 */
public class WebService
{

    /**
     * Listens to whenever the WebService receives the list of adversities
     */
    public interface OnRequestAdversitiesRespondedListener
    {
        /**
         * Method invoked when the WebService receives the list of adversities
         * @param adversities
         *  the list of adversity gathered from the JSON response
         */
        void onRequestAdversitiesResponded(List<Adversity> adversities);
    }

    /**
     * Listens to whenever the WebService receives the Medication
     */
    public interface OnRequestRxNormIdRespondedListener
    {
        /**
         * Method invoked when the WebService receives the Medication
         * @param medication
         *  the medication gathered from the XML response
         */
        void onRequestRxNormIdResponded(Medication medication);
    }

    /**
     * Singleton Class Instance of WebService
     */
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

    public void requestRxNormId(String drugName)
    {
        AsyncTask<String, Void, InputStream> task = new AsyncTask<String, Void, InputStream>() {
            @Override
            protected InputStream doInBackground(String... params)
            {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://rxnav.nlm.nih.gov/REST/rxcui?name=" + params[0]).openConnection();
                    connection.setRequestMethod("GET");

                    return connection.getInputStream();
                }catch(IOException e)
                {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(InputStream stream)
            {
                try {
                    XmlParser parser = new XmlParser();
                    Medication medication = parser.parseMedication(stream);

                    if(onRIRListener != null)
                        onRIRListener.onRequestRxNormIdResponded(medication);
                }catch(XmlPullParserException e)
                {
                    e.printStackTrace();
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        task.execute(drugName);
    }

    public void requestAdversities(String rxnormid)
    {
        AsyncTask<String, Void, InputStream> task = new AsyncTask<String, Void, InputStream>() {
            @Override
            protected InputStream doInBackground(String... params)
            {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://rxnav.nlm.nih.gov/REST/interaction/interaction.json?rxcui=" + params[0]).openConnection();
                    connection.setRequestMethod("GET");

                    return connection.getInputStream();
                }catch(IOException e)
                {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(InputStream stream)
            {
                JsonParser parser = new JsonParser();
                List<Adversity> adversityList = parser.parseAdversities(stream);

                if(onRARListener != null)
                    onRARListener.onRequestAdversitiesResponded(adversityList);
            }
        };
        task.execute(rxnormid);
    }

    public static WebService getInstance()
    {
        return Instance.instance;
    }

}
