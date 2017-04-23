package ddi.hackru.edu.drugtodrugapplication;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Jasper Bae on 4/22/2017.
 */

@RunWith(AndroidJUnit4.class)
public class PhotoRecognizationTest
{

    @Test
    public void photorecognition() throws Exception
    {
        MedicationController controller = new MedicationController();
        controller.queryMedicationFromFile("data/test_pescription.bmp");
    }

}
