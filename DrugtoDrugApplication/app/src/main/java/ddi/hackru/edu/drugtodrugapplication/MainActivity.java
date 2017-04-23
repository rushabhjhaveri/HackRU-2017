package ddi.hackru.edu.drugtodrugapplication;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    MedicationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MedicationController();
        controller.setOnMedicationChangedListener(new MedicationController.OnMedicationChangedListener() {
            @Override
            public void onMedicationQueried(Medication medication) {
                System.out.println(medication.getDrugName() + ":" + medication.getRXNormID());
            }
        });

        Constants.context = this;

        controller.queryMedicationFromFile("https://i0.wp.com/www.templatesfront.com/wp-content/uploads/2016/05/prescription-template-59641.gif?resize=540%2C419");

    }
}
