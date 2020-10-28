package com.example.chefcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chefcenter.model.FoodData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoast.StyleableToast;
import com.tapadoo.alerter.Alert;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {

    RecyclerView recyclerViewHome;
    List<FoodData> myFoodList;
    FoodData mFoodData;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    ProgressDialog progressDialog;
    EditText editTextSearch;
    SwipeRefreshLayout swipeRefreshLayout;
    String uName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initialize();
        String name = getIntent().getStringExtra("name");
        //Toast t = Toast.makeText(getApplicationContext(),"Welcome "+name,Toast.LENGTH_LONG);
        //t.setGravity(Gravity.CENTER,0,200);
        //t.show();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("\n\nLoading Items......");


        Alerter.create(this)
                .setTitle("\n\nWelcome")
                .setText("\n"+name+"\n")
                .setIcon(R.drawable.user)
                .setBackgroundColorRes(R.color.coloruser)
                .setDuration(1500)
                .enableSwipeToDismiss()
                .enableProgress(true)
                .setProgressColorRes(R.color.colorAccent)
                .show();

    }




    private void initialize() {
        editTextSearch = findViewById(R.id.searchText);
        swipeRefreshLayout=findViewById(R.id.refreshLayout);





        recyclerViewHome = findViewById(R.id.recyclerViewHome);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomepageActivity.this, 1);
        recyclerViewHome.setLayoutManager(gridLayoutManager);




       /* mFoodData = new FoodData(R.drawable.food6,"Pasta","30min","Afgani","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        myFoodList.add(mFoodData);
        mFoodData = new FoodData(R.drawable.food7,"Burger","15min","Canadian","Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source.");
        myFoodList.add(mFoodData);
        mFoodData = new FoodData(R.drawable.food8,"Samosa","1 hour","Indian","Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source.");
        myFoodList.add(mFoodData);
        mFoodData = new FoodData(R.drawable.food9,"Pasta","30min","Afgani","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry’s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        myFoodList.add(mFoodData);
        mFoodData = new FoodData(R.drawable.food10,"Burger","15min","Canadian","Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source.");
        myFoodList.add(mFoodData);
        mFoodData = new FoodData(R.drawable.food11,"Samosa","1 hour","Indian","Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source.");
        myFoodList.add(mFoodData);
*/
        myFoodList = new ArrayList<>();

        final MyAdapter myAdapter = new MyAdapter(HomepageActivity.this, myFoodList);
        recyclerViewHome.setAdapter(myAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");

        //progressDialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myFoodList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {


                    FoodData foodData = itemSnapshot.getValue(FoodData.class);
                    foodData.setKey(itemSnapshot.getKey());
                    myFoodList.add(foodData);


                }
                myAdapter.notifyDataSetChanged();
                //progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //progressDialog.dismiss();
            }
        });


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

            private void filter(String text) {
                ArrayList<FoodData> filterList = new ArrayList<>();
                for (FoodData item : myFoodList) {
                    if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                        filterList.add(item);
                    }
                }
                myAdapter.filteredList(filterList);
            }

        });


















        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                String searchText=editTextSearch.getText().toString();





                if (!(searchText.isEmpty())){
                    editTextSearch.setText(null);
                    myAdapter.notifyDataSetChanged();
                    recyclerViewHome.setAdapter(myAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                }else {
                    myAdapter.notifyDataSetChanged();
                    recyclerViewHome.setAdapter(myAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });


    }




    public void onBackPressed() {

        AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
        alertdialog.setTitle("Warning");
        alertdialog.setMessage("Are you sure you Want to Logout ???\n\n");
        alertdialog.setPositiveButton("yes", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                HomepageActivity.super.onBackPressed();

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


    public void btnUpload(View view) {
        startActivity(new Intent(this,Upload_Recipe.class));
    }
}
