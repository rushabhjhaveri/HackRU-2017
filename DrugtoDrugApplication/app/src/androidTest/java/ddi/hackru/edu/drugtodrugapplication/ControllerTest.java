package ddi.hackru.edu.drugtodrugapplication;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Rushabh on 4/22/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ControllerTest
{

    @Test
    public void useMedicationController() throws Exception
    {
        MedicationController controller = new MedicationController();
        controller.setOnMedicationChangedListener(new MedicationController.OnMedicationChangedListener() {
            @Override
            public void onMedicationQueried(Medication medication) {
                Assert.assertEquals("Advil", medication.getDrugName());
                Assert.assertEquals("153165", medication.getRXNormID());
            }
        });
        controller.queryMedication("Advil");
    }

}
