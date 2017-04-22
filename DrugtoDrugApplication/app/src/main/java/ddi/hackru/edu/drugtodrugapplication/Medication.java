package ddi.hackru.edu.drugtodrugapplication;
import java.io.*;
import java.util.*;
/**
 * Created by Rushabh on 4/22/2017.
 */

public class Medication {

    //instance variable declarations

    private String drugName;
    private String rxNormID;
    private List<Adversity> adversityList;

    //default constructor
    //initializes instance mem
    Medication(){
        drugName = null;
        rxNormID = null;
    }

    //parametrized constructor
    Medication(String drugName, String rxNormID){
        this.drugName = drugName;
        this.rxNormID = rxNormID;
    }

    //setter method - sets AdversityList to pre-made, sorted adversity list
    public void setAdversityList(List<Adversity> adversityList) {
        this.adversityList = adversityList;
    }

    public String getDrugName(){
        return this.drugName;
    }

    public String getRXNormID(){
        return this.rxNormID;
    }

    public List<Adversity> getAdversityList(){
        return this.adversityList;
    }
}
