package com.example.chefcenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tapadoo.alerter.Alerter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {


    EditText editTextusername,editTextpassword;
    Button btnlogin;
    TextView textViewsignupText;
    DatabaseReference ChefCenterDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiliaze();
       



    }

    private void initiliaze() {
        editTextusername = findViewById(R.id.editTextUsername);
        editTextpassword = findViewById(R.id.editTextPassword);
        textViewsignupText = findViewById(R.id.textViewSignUpText);
        textViewsignupText.setOnClickListener(this);
        btnlogin = findViewById(R.id.buttonLogin);
        btnlogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == btnlogin) {
            String username = editTextusername.getText().toString();
            String password = editTextpassword.getText().toString();
            if (username.isEmpty()) {
                editTextusername.setError("Please enter the Username");
                editTextusername.requestFocus();
            } else if (password.isEmpty()) {
                editTextpassword.setError("Please enter the Password");
                editTextpassword.requestFocus();
            }
            else if (username.isEmpty() && password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter the value in fields", Toast.LENGTH_LONG).show();
                editTextusername.requestFocus();
            }

            else if (!(username.isEmpty() && password.isEmpty())) {
                DatabaseReference login = FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("login")
                        .child(String.valueOf(username));
                login.addValueEventListener(this);

            }
            else {
                Toast.makeText(MainActivity.this,"Error Occurred! Try again",Toast.LENGTH_LONG).show();
            }

        }
        else if (v == textViewsignupText) {
            Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
            startActivityForResult(intent,1);
        }
        else{
            Toast.makeText(MainActivity.this,"Please Choose an option",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode==RESULT_OK){

            String uname = (String)data.getStringExtra("username");
            String pass = (String)data.getStringExtra("pass");
            editTextusername.setText(uname);
            editTextpassword.setText(pass);
            }
    }




    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()){
            String pass=dataSnapshot.child("password").getValue().toString();
            String name=dataSnapshot.child("name").getValue().toString().toUpperCase();


            String password = editTextpassword.getText().toString();
            String username = editTextusername.getText().toString();

            if (!(username.isEmpty() && password.isEmpty())) {

                try {
                    if (pass.contentEquals(password)) {
                        /*Toast.makeText(MainActivity.this, "Welcome "+name, Toast.LENGTH_LONG).show();*/
                        Intent home = new Intent(MainActivity.this, HomepageActivity.class);
                        editTextusername.setText(null);
                        editTextpassword.setText(null);
                        home.putExtra("name",name);

                        startActivity(home);




                    } else {
                        Toast.makeText(MainActivity.this, "Login Credentials doesn't match", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();

                }
            }
            else {
                Toast.makeText(MainActivity.this, "Please enter the both credentials to login", Toast.LENGTH_LONG).show();
            }

        }
        else{
            Toast.makeText(MainActivity.this,"Login Credentials doesn't match",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(MainActivity.this,"Username Doesn't exist",Toast.LENGTH_LONG).show();

    }



    public void onBackPressed() {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
        alertdialog.setTitle("Warning");
        alertdialog.setMessage("Are you sure you Want to Exit ???\n\n");
        alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        });

        alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert=alertdialog.create();
        alertdialog.show();

    }
}


