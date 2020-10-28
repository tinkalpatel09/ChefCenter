package com.example.chefcenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chefcenter.model.FoodData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class Upload_Recipe extends AppCompatActivity implements View.OnClickListener {


    EditText editTextName,editTextDuration,editTextCatgory,editTextDescription;
    ImageView imageViewUploadImage;
    Button btnUploadImage,btnUploadRecipe;
    Uri uri;
    String imageurl;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__recipe);
        initiliaze();
    }

    private void initiliaze() {
        editTextName=findViewById(R.id.editTextuploadRecipeName);
        editTextDuration=findViewById(R.id.editTextuploadRecipeDuration);
        editTextCatgory=findViewById(R.id.editTextuploadRecipeCategory);
        editTextDescription=findViewById(R.id.editTextuploadRecipeDescription);
        imageViewUploadImage=findViewById(R.id.imageuploadFoodImage);
        btnUploadImage=findViewById(R.id.btnSelectImage);
        btnUploadRecipe=findViewById(R.id.btnUploadRecipe);

        btnUploadRecipe.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Recipe Uploading.....");
    }



    public void uploadImage(){



        progressDialog.show();

        StorageReference storageReference= FirebaseStorage.getInstance()
                .getReference().child("RecipeImage").child(uri.getLastPathSegment());

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlmage =uriTask.getResult();
                imageurl= urlmage.toString();
                uploadRecipe();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }

    public void uploadRecipe(){




        FoodData foodData = new FoodData(editTextCatgory.getText().toString(),editTextDescription.getText().toString()
                ,editTextDuration.getText().toString(),imageurl,editTextName.getText().toString());

        String myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());
        FirebaseDatabase.getInstance().getReference("Recipe")
                .child(myCurrentDateTime).setValue(foodData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(Upload_Recipe.this,"Recpie uploaded",Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_Recipe.this,"Failed to upload",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==btnUploadImage){
            Intent photopicker = new Intent(Intent.ACTION_PICK);
            photopicker.setType("image/*");
            startActivityForResult(photopicker,1);

        }else if(v==btnUploadRecipe){
            try {
                String category = editTextCatgory.getText().toString();
                String duration = editTextDuration.getText().toString();
                String name = editTextName.getText().toString();
                String desc = editTextDescription.getText().toString();


                if (name.isEmpty()) {
                    editTextName.setError("Please enter the name");
                    editTextName.requestFocus();
                }else if (duration.isEmpty()) {
                    editTextDuration.setError("Please enter the Duration");
                    editTextDuration.requestFocus();
                }
                else if (category.isEmpty()) {
                    editTextCatgory.setError("Please enter the Category");
                    editTextCatgory.requestFocus();
                } else if (desc.isEmpty()) {
                    editTextDescription.setError("Please write the Description");
                    editTextDescription.requestFocus();
                }else {

                    uploadImage();
                }
            } catch (Exception e) {
                Toast.makeText(this,"Error occurred, Try again",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }

        }else{
            Toast.makeText(this,"Error occurred, Try again",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== RESULT_OK){
            uri=data.getData();
            imageViewUploadImage.setImageURI(uri);

        }
        else {
            Toast.makeText(this,"You haven't choose an image",Toast.LENGTH_LONG).show();
        }
    }


}
