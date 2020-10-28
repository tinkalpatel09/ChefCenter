package com.example.chefcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DescriptionActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener, ValueEventListener {


    TextView textViewDescription,textViewTitle,textViewDuration,textViewCategory;
    ImageView imageViewFood;
    String key="";
    ImageView imageViewShare;
    String imageUrl="";
    String titleShare,categoryShare;
    String durationShare,descriptionShare;
    DatabaseReference ChefCenterDatabase;
    String uName;
    String pass;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        initiliaze();

    }

    private void initiliaze() {
        textViewDescription=findViewById(R.id.textDescription);
        textViewTitle=findViewById(R.id.textTitle);
        textViewDuration=findViewById(R.id.textDuration);
        textViewCategory=findViewById(R.id.textCategory);
        imageViewFood=findViewById(R.id.imageViewFood);
        imageViewShare=findViewById(R.id.shareButton);




        imageViewShare.setOnClickListener(this);
        imageViewFood.setOnLongClickListener(this);


        Bundle mBundle=getIntent().getExtras();
        if(mBundle!=null){
            titleShare=mBundle.getString("title");
            durationShare=mBundle.getString("duration");
            categoryShare=mBundle.getString("category");
            descriptionShare=mBundle.getString("description");

            textViewTitle.setText(titleShare);
            textViewDescription.setText(descriptionShare);
            textViewDuration.setText(durationShare);
            textViewCategory.setText(categoryShare);
            //imageViewFood.setImageResource(mBundle.getInt("Image"));

            Glide.with(this).load(mBundle.getString("Image")).into(imageViewFood);

            key=mBundle.getString("keyValue");
            imageUrl=mBundle.getString("Image");


        }
    }

    @Override
    public boolean onLongClick(View v) {



        final AlertDialog.Builder alert = new AlertDialog.Builder(DescriptionActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialog,null);

        final EditText editTextUname = (EditText)mView.findViewById(R.id.editTextUsernameDialog);
        final EditText editTextPass = (EditText)mView.findViewById(R.id.editTextPasswordDialog);
        Button btnCancel =(Button)mView.findViewById(R.id.buttonCancelDialog);
        Button btnConfirm =(Button)mView.findViewById(R.id.buttonConfirmDialog);

        alert.setView(mView);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);





        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uName= editTextUname.getText().toString();
                pass = editTextPass.getText().toString();


                if(uName.isEmpty()) {
                    editTextUname.setError("Please enter the Username");
                    editTextUname.requestFocus();
                } else if(pass.isEmpty()) {
                    editTextPass.setError("Please enter the Password");
                    editTextPass.requestFocus();
                }
                else if(!(uName.isEmpty() && pass.isEmpty())) {

                    if(uName.equals("dinkar")){
                        data();

                    }else{
                        alertDialog.dismiss();
                        Toast.makeText(DescriptionActivity.this,"Only Admin are authorized to delete the recipe",Toast.LENGTH_LONG).show();
                    }


                }
                else {
                    Toast.makeText(DescriptionActivity.this,"Error Occurred",Toast.LENGTH_LONG).show();
                }

            }
        });
        alertDialog.show();

        return false;
    }

    public void data(){
        DatabaseReference login = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("login")
                .child(String.valueOf(uName));
        login.addValueEventListener(this);

    }









    @Override
    public void onClick(View v) {
        if (v==imageViewShare){


            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody="Hey, \n\nYour friend have shared a recipe named "+titleShare+" which is a "+categoryShare+
                    " dish.\n\n You can be made in just "+durationShare+". \n\n Here is the full recpie for the dish\n\n"
                    +descriptionShare+ "\n\n\nDownload Chef Center Android app for more recipes";

            shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);


            startActivity(Intent.createChooser(shareIntent,"Share Using"));


        }
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        if(dataSnapshot.exists()){
            String passwordd=dataSnapshot.child("password").getValue().toString();
            name=dataSnapshot.child("name").getValue().toString().toUpperCase();

            if (pass.contentEquals(passwordd)) {


                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipe");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        reference.child(key).removeValue();
                        Toast.makeText(DescriptionActivity.this,"Recipe deleted",Toast.LENGTH_LONG).show();

                        Intent i = new Intent(getApplicationContext(),HomepageActivity.class);
                        i.putExtra("name",name);
                        startActivity(i);
                        finish();
                    }
                });




            }else{
                Toast.makeText(DescriptionActivity.this,"Password doesn't match",Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(DescriptionActivity.this,"User doesn't exist",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
