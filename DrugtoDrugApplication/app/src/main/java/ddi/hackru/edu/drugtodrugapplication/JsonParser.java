package ddi.hackru.edu.drugtodrugapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Jasper Bae on 4/22/2017.
 */

public class JsonParser
{

    private static final String INTERACTION_TYPE_GROUP_TAG = "interactionTypeGroup";
    private static final String INTERACTION_TYPE_TAG = "interactionType";
    private static final String INTERACTION_PAIR_TAG = "interactionPair";
    private static final String INTERACTION_CONCEPT_TAG = "interactionConcept";
    private static final String INTERACTION_MIN_CONCEPT_TAG = "minConcept";

    public List<Adversity> parseAdversities(InputStream in)
    {
        String json = convertStreamToString(in);
        List<Adversity> adversities = new ArrayList<Adversity>();

        try {
            JSONObject root = new JSONObject(json);
            JSONArray interactionTypeGroup = root.getJSONArray(INTERACTION_TYPE_GROUP_TAG);
            for(int i = 0; i < interactionTypeGroup.length(); i++)
            {
                JSONArray interactionType = interactionTypeGroup.getJSONObject(i).getJSONArray(INTERACTION_TYPE_TAG);
                for(int j = 0; j < interactionType.length(); j++)
                {
                    JSONArray interactionPairs = interactionType.getJSONObject(j).getJSONArray(INTERACTION_PAIR_TAG);
                    for(int k = 0; k < interactionPairs.length(); k++)
                    {
                        JSONObject pair = interactionPairs.getJSONObject(k);
                        String otherDrugName = "";
                        String rxnormId = "";

                        JSONArray array = pair.getJSONArray(INTERACTION_CONCEPT_TAG);
                        // get the second
                        JSONObject otherMedication = array.getJSONObject(1).getJSONObject(INTERACTION_MIN_CONCEPT_TAG);
                        otherDrugName = otherMedication.getString("name");
                        rxnormId = otherMedication.getString("rxcuid");

                        String severity = pair.getString("severity");
                        String description = pair.getString("description");


                    }
                }
            }
        }catch(JSONException e)
        {
            e.printStackTrace();
        }

        return adversities;
    }

    private String convertStreamToString(InputStream is)
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();

        try {
            String line = "";
            while ((line = br.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }finally
        {
            try {
                br.close();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }

}
