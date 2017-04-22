package ddi.hackru.edu.drugtodrugapplication;

/**
 * Created by Rushabh on 4/22/2017.
 */

public class Adversity implements Comparable<Adversity> {
    private Medication consumedMedication;
    private Medication interferingMedication;
    private String description;
    private String severity;
    //default constructor
    //initializes all instance variables to default values
    public Adversity(){
        consumedMedication = null;
        interferingMedication = null;
        description = null;
        severity = null;
    }

    //parameterized constructor

    public Adversity(Medication consumed, Medication target, String description, String severe){
        this.consumedMedication = consumed;
        this.interferingMedication = target;
        this.description = description;
        this.severity = severe;
    }

    //getter method - returns consumed medication
    public Medication getConsumedMedication(){
        return this.consumedMedication;
    }

    //

    public Medication getTargetMedication(){
        return this.interferingMedication;
    }

    public String getDescription(){
        return this.description;
    }

    public String getSeverity()
    {
        return severity;
    }

    public int compareTo(Adversity other){
        int returnVal = this.interferingMedication.getDrugName().compareTo(other.getTargetMedication().getDrugName());
        return returnVal;
    }

}
