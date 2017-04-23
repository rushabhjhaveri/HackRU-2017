package ddi.hackru.edu.drugtodrugapplication;
import java.io.*;
import java.util.*;
/**
 * Created by Rushabh on 4/22/2017.
 */


/*
This program reads the text file from the image has been scanned and stores it in respective objects
and variables
 */
public class TextReader {
    //instance variables

    /*
    The model prescription will have five columns - sample as follows:
    Index No.  Drug Name  Drug ID  Strength  Recommended Dosage

    Note: keep this format in mind.
     */

    //@param: file name (passed as string)
    public void readFile(String docFile)throws FileNotFoundException{
        String phrase = "";
        StringTokenizer tokenizer;
        String token;
        int index;
        String drugName= null;
        String drugID = null;
        float strength = 0;
        float dosage = 0;
        float consumption;
        int ctr = 0;
        Scanner sc = new Scanner(new File(docFile));
        while(sc.hasNextLine()){
            phrase = sc.nextLine();
            tokenizer = new StringTokenizer(phrase, "|");
            while(tokenizer.hasMoreTokens()){
                ctr++;
                token = tokenizer.nextToken();
                switch(ctr) {
                    //first token is the index number
                    case 1: break;
                    //second token is the drug name
                    case 2: drugName = setDrugName(token);
                        break;

                    //third token is the drug ID
                    case 3: drugID = setDrugID(token);
                        break;
                    //fourth token is the strength
                    case 4: strength = setDrugStrength(token);
                        break;
                    //fifth token is the dosage
                    case 5: dosage = setDrugDosage(token);
                        break;
                }
            }
            Medication obj = new Medication(drugName, drugID);
            //then calculate consumption, given strength and dosage
            consumption = calculateConsumption(strength, dosage);
        }
    }

    public String setDrugName(String str){
        return str;
    }

    public String setDrugID(String str){
        return str;
    }
    public float setDrugStrength(String str){
        return Float.parseFloat(str);
    }

    public float setDrugDosage(String str){
        return Float.parseFloat(str);
    }

    public float calculateConsumption(float strength, float dosage){
        return strength*dosage;
    }
}
