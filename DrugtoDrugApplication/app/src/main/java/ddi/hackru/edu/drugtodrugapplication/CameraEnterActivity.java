package ddi.hackru.edu.drugtodrugapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraEnterActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_enter);

        Constants.context = this;

        btnSelect = (Button) findViewById(R.id.btnSelectPhoto);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        ivImage = (ImageView) findViewById(R.id.ivImage);

    }

    private void selectImage(){



        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CameraEnterActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean result = Utility.checkPermission(CameraEnterActivity.this);

                if(items[which].equals("Take Photo")){
                    userChoosenTask = "Take Photo";

                    if(result){
                        cameraIntent();
                    }

                }
                else if(items[which].equals("Cancel")){
                    dialog.dismiss();
                }

            }
        });

        builder.show();


    }

    public void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    public void galleryIntent(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"),SELECT_FILE);


    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults){


        switch(requestCode){


            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(userChoosenTask.equals("Take Photo")){
                        cameraIntent();
                    }
                    else if(userChoosenTask.equals("Choose From Library")){
                        galleryIntent();
                    }

                }
                else{

                }

                break;
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){


        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == SELECT_FILE){
                onSelectFromGalleryResult(data);
            }
            else if(requestCode == REQUEST_CAMERA){
                onCaptureImageResult(data);
            }
        }

    }

    private void onSelectFromGalleryResult(Intent data){
        Intent dataIntent = new Intent();
        dataIntent.putExtra("pathtopicture", "content:/" + data.getData().getPath());

        setResult(Activity.RESULT_OK, dataIntent);
        finish();
//
//        Bitmap bm = null;
//        if(data != null){
//            try{
//                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
//
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//        ivImage.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data){

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");

        FileOutputStream fo;

        try{

            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();;
        }

        Intent dataIntent = new Intent();
        //dataIntent.putExtra("pathtopicture", "file://" + destination.getAbsolutePath());
        dataIntent.putExtra("picture", bytes.toByteArray());

        setResult(Activity.RESULT_OK, dataIntent);
        finish();
//        ivImage.setImageBitmap(thumbnail);

    }
}
