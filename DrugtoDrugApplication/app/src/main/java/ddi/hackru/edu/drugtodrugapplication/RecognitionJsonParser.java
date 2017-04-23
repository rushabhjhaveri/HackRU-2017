package ddi.hackru.edu.drugtodrugapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jasper Bae on 4/22/2017.
 */

public class RecognitionJsonParser
{

    private static final String REGION_TAG = "regions";
    private static final String LINES_TAG = "lines";
    private static final String WORDS_TAG = "words";

    public String parseRecognition(String json)
    {
        StringBuilder builder = new StringBuilder();

        try
        {
            JSONObject root = new JSONObject(json);
            JSONObject resultObject = root.getJSONObject("recognitionResult");
            JSONArray lines = resultObject.getJSONArray(LINES_TAG);
            for(int j = 0; j < lines.length(); j++)
            {
                builder.append(lines.getJSONObject(j).getString("text"));
            }
            //builder.append('\n');
        }catch(JSONException e)
        {
            e.printStackTrace();
        }

        return builder.toString();
    }

}
