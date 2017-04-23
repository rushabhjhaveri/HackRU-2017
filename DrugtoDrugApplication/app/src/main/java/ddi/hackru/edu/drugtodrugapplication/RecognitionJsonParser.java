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
            JSONArray regionsArray = root.getJSONArray(REGION_TAG);
            for(int i = 0; i < regionsArray.length(); i++)
            {
                JSONArray lines = regionsArray.getJSONObject(i).getJSONArray(LINES_TAG);
                for(int j = 0; j < lines.length(); j++)
                {
                    JSONArray words = lines.getJSONObject(j).getJSONArray(WORDS_TAG);
                    for(int k = 0; k < words.length(); k++)
                    {
                        JSONObject object = words.getJSONObject(k);
                        builder.append(" " + object.getString("text") + " ");
                    }
                }
                builder.append('\n');
            }
        }catch(JSONException e)
        {
            e.printStackTrace();
        }

        return builder.toString();
    }

}
