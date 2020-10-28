package com.example.chefcenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chefcenter.model.Login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText editTextusername, editTextpassword, editTextName;
    Button btnsignup;
    TextView textViewLoginText;
    DatabaseReference ChefCenterDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initiliaze();

    }

    private void initiliaze() {
        editTextpassword = findViewById(R.id.editTextPassword);
        editTextusername = findViewById(R.id.editTextUsername);
        editTextName = findViewById(R.id.editTextName);
        btnsignup = findViewById(R.id.buttonSignup);
        btnsignup.setOnClickListener(this);

        textViewLoginText = findViewById(R.id.textViewLoginText);
        textViewLoginText.setOnClickListener(this);

        ChefCenterDatabase = FirebaseDatabase.getInstance().getReference("login");
    }

    @Override
    public void onClick(View v) {
        try {
            if (v == btnsignup) {
                String username = editTextusername.getText().toString();
                String name = editTextName.getText().toString();
                String password = editTextpassword.getText().toString();

                if (name.isEmpty()) {
                    editTextName.setError("Please enter the name");
                    editTextName.requestFocus();
                } else if (password.isEmpty()) {
                    editTextpassword.setError("Please enter the Password");
                    editTextpassword.requestFocus();
                }if (username.isEmpty()) {
                    editTextusername.setError("Please enter the Username");
                    editTextusername.requestFocus();
                }else if (username.isEmpty() && password.isEmpty() && name.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter the value in fields", Toast.LENGTH_LONG).show();
                    editTextName.requestFocus();
                } else if (!(username.isEmpty() && password.isEmpty() && name.isEmpty())) {

                        if(username.length()<4){
                            editTextusername.setError("Username should be at least 4 character long");
                            editTextusername.setText(null);
                            editTextusername.requestFocus();

                        }
                    else if(password.length()<6){
                            editTextpassword.setError("Password should be at least 6 character long");
                            editTextpassword.setText(null);
                            editTextusername.requestFocus();
                        }
                    else {


                            DatabaseReference login = FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("login")
                                    .child(String.valueOf(username));
                            login.addValueEventListener(this);




                        }
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Error Occurred! Try again",Toast.LENGTH_LONG).show();
                }
            }
            else if(v == textViewLoginText) {
                finish();
                }



        }
        catch (Exception e) {
            Toast.makeText(RegisterActivity.this, "Please Choose an option", Toast.LENGTH_LONG).show();
        }


    }



    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        String username = editTextusername.getText().toString();
        String name = editTextName.getText().toString();
        String password = editTextpassword.getText().toString();

        if (!dataSnapshot.exists()) {

            Login login = new Login(name, password, username);
            ChefCenterDatabase.child(String.valueOf(username)).setValue(login);
            Toast.makeText(RegisterActivity.this, "You are registered successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.putExtra("username",editTextusername.getText().toString());
            intent.putExtra("pass",editTextpassword.getText().toString());
            setResult(RESULT_OK, intent);
            finish();



        } else {
            editTextusername.setError("Username already in use");
            editTextusername.setText(null);
            editTextusername.requestFocus();

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
