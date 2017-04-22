package ddi.hackru.edu.drugtodrugapplication;

import java.util.List;

/**
 * Created by Jasper Bae on 4/22/2017.
 */

public class MedicationController
{

    public interface OnMedicationChangedListener
    {
        public static final int MEDICATION_CHANGED = 0,
                                ADVERSITY_CHANGED = 1;

        void OnMedicationChanged(Medication medication, int type);
    }

    private WebService webService;
    private Medication currentMedication;

    public MedicationController()
    {
        webService = WebService.getInstance();
        webService.setOnRequestAdversitiesRespondedListener(new WebService.OnRequestAdversitiesRespondedListener() {
            @Override
            public void onRequestAdversitiesResponded(List<Adversity> adversities) {

            }
        });
        webService.setOnRequestRxNormIdRespondedListener(new WebService.OnRequestRxNormIdRespondedListener() {
            @Override
            public void onRequestRxNormIdResponded(Medication medication) {

            }
        });
    }

}
