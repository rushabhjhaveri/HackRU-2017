package ddi.hackru.edu.drugtodrugapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

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
        AsyncTask<String, Void, Medication> task = new AsyncTask<String, Void, Medication>() {
            @Override
            protected Medication doInBackground(String... params)
            {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://rxnav.nlm.nih.gov/REST/rxcui?name=" + params[0]).openConnection();
                    connection.setRequestMethod("GET");

                    InputStream stream = connection.getInputStream();
                    try {
                        XmlParser parser = new XmlParser();
                        Medication medication = parser.parseMedication(stream);
                        return medication;
                    }catch(XmlPullParserException e)
                    {
                        e.printStackTrace();
                    }catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }catch(IOException e)
                {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Medication medication)
            {
                if(onRIRListener != null)
                    onRIRListener.onRequestRxNormIdResponded(medication);
            }
        };
        task.execute(drugName);
    }

    public void requestAdversities(String rxnormid)
    {
        AsyncTask<String, Void, List<Adversity>> task = new AsyncTask<String, Void, List<Adversity>>() {
            @Override
            protected List<Adversity> doInBackground(String... params)
            {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("https://rxnav.nlm.nih.gov/REST/interaction/interaction.json?rxcui=" + params[0]).openConnection();
                    connection.setRequestMethod("GET");

                    InputStream inputstream = connection.getInputStream();
                    JsonParser parser = new JsonParser();
                    List<Adversity> adversityList = parser.parseAdversities(inputstream);
                    return adversityList;
                }catch(IOException e)
                {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(List<Adversity> adversityList)
            {
                if(onRARListener != null)
                    onRARListener.onRequestAdversitiesResponded(adversityList);
            }
        };
        task.execute(rxnormid);
    }

    public void requestPhotoRecognition(final String imageToUrl)
    {
        AsyncTask<String, Void, Medication> task = new AsyncTask<String, Void, Medication>()
        {
            @Override
            protected Medication doInBackground(String... params)
            {
                try
                {
                    URL url = new URL("https://westus.api.cognitive.microsoft.com/vision/v1.0/ocr");
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    String urlParameters = "language=unk&detectOrientation=true";

                    connection.setRequestProperty("Content-Type", "application/octet-stream");
                    connection.setRequestProperty("Ocp-Apim-Subscription-Key", "b2d7d2471e2142a79a2c4a6d3b9b0778");
/*
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(urlParameters);
                    outputStream.flush();*/

                    //Bitmap bitmap = BitmapFactory.decodeFile(imageToUrl);

                    Bitmap bitmap = Picasso.with(Constants.context).load(params[0]).get();

                    BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();

                    int responseCode = connection.getResponseCode();
                    System.out.println("Response Code:" + responseCode);

                    StringBuilder json = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while((line = br.readLine()) != null)
                    {
                        json.append(line).append('\n');
                    }
                    br.close();

                    RecognitionJsonParser parser = new RecognitionJsonParser();
                    String word = parser.parseRecognition(json.toString());

                    // Find medication...
                    requestRxNormId(word);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

                return null;
            }
        };
        task.execute(imageToUrl);
    }

    public static WebService getInstance()
    {
        return Instance.instance;
    }

}
