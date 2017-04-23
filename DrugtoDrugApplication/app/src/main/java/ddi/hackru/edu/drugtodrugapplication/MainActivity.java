package ddi.hackru.edu.drugtodrugapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_CODE_CAMERA = 0;

    MedicationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new MedicationController();
        controller.setOnMedicationChangedListener(new MedicationController.OnMedicationChangedListener() {
            @Override
            public void onMedicationQueried(Medication medication) {
                // view.find... listview
                if(medication != null) {
                    List<Adversity> adversityList = medication.getAdversityList();

                    ArrayList<String> adversitySList = new ArrayList<String>(adversityList.size());
                    for (Adversity a : adversityList) {
                        adversitySList.add(a.toString());
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("medication", medication.getDrugName());
                    bundle.putStringArrayList("adversityList", adversitySList);

                    Intent intent = new Intent(MainActivity.this, QuerriesListView.class);
                    intent.putExtra("transfer", bundle);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(MainActivity.this, "Could not find medication!", Toast.LENGTH_LONG).show();
                }
            }


        });
    }

    public void searchForResults(View view){
        EditText edit = (EditText) findViewById(R.id.editText);
        String content = edit.getText().toString();

        controller.queryMedication(content);
        edit.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_CAMERA)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                byte[] filepath = data.getByteArrayExtra("picture");
                Byte[] B = new Byte[filepath.length];
                for(int i = 0; i < B.length; i++)
                {
                    B[i] = filepath[i];
                }

                controller.queryMedicationFromFile(B);
            }
        }
    }

    public void buttonCameraInput(View view){
        Intent intent = new Intent(this, CameraEnterActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

}
