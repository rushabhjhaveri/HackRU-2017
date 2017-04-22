package ddi.hackru.edu.drugtodrugapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MedicationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MedicationController();
        controller.setOnMedicationChangedListener(new MedicationController.OnMedicationChangedListener() {
            @Override
            public void onMedicationChanged(Medication medication, int type)
            {
                switch(type)
                {
                    case MEDICATION_CHANGED:
                        // Change UI
                        break;
                    case ADVERSITY_CHANGED:
                        // Change UI
                        break;
                }
            }
        });
    }
}
