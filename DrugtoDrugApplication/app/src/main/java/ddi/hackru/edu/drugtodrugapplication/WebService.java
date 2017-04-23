package ddi.hackru.edu.drugtodrugapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    public void requestPhotoRecognition(String imageToUrl)
    {
        AsyncTask<String, Void, Medication> task = new AsyncTask<String, Void, Medication>()
        {
            @Override
            protected Medication doInBackground(String... params)
            {
                CloseableHttpClient httpClient = HttpClients.createDefault();

                try
                {
                    URIBuilder uriBuilder = new URIBuilder("https://westus.api.cognitive.microsoft.com/vision/v1.0/ocr");
                    uriBuilder.setParameter("language", "unk");
                    uriBuilder.setParameter("detectOrientation", "true");

                    URI uri = uriBuilder.build();
                    HttpPost post = new HttpPost(uri);

                    post.addHeader("Content-Type", "application/octet-stream");
                    post.addHeader("Ocp-Apim-Subscription-Key", "b2d7d2471e2142a79a2c4a6d3b9b0778");

                    File file = new File(params[0]);
                    FileEntity reqEntity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM);
                    post.setEntity(reqEntity);

                    HttpResponse response = httpClient.execute(post);
                    HttpEntity entity = response.getEntity();

                    if(entity != null)
                    {
                        String r = EntityUtils.toString(entity);
                        System.out.println("E:" + r);
                    }
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
