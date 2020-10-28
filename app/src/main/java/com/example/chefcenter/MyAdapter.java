package com.example.chefcenter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chefcenter.model.FoodData;

import java.util.ArrayList;
import java.util.List;
import com.example.chefcenter.HomepageActivity;
public class MyAdapter  extends RecyclerView.Adapter<FoodViewHolder>{

    private Context mContext;
    private List<FoodData> myFoodList;
    private int lastPosition = -1;



    public MyAdapter(Context mContext, List<FoodData> myFoodList) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;

    }



    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_row_item,viewGroup,false);

        return new FoodViewHolder(mView);
    }




    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder foodViewHolder, int i) {


        Glide.with(mContext).load(myFoodList.get(i).getItemImage()).into(foodViewHolder.imageView);
        //foodViewHolder.imageView.setImageResource(myFoodList.get(i).getItemImage());
        foodViewHolder.textViewTitle.setText(myFoodList.get(i).getItemName());
        foodViewHolder.textViewDescription.setText(myFoodList.get(i).getItemDescription());
        foodViewHolder.textViewCategory.setText(myFoodList.get(i).getItemCategory());
        foodViewHolder.textViewDuration.setText(myFoodList.get(i).getItemDuration());

        foodViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DescriptionActivity.class);
                intent.putExtra("Image",myFoodList.get(foodViewHolder.getAdapterPosition()).getItemImage());
                intent.putExtra("description",myFoodList.get(foodViewHolder.getAdapterPosition()).getItemDescription());
                intent.putExtra("title",myFoodList.get(foodViewHolder.getAdapterPosition()).getItemName());
                intent.putExtra("duration",myFoodList.get(foodViewHolder.getAdapterPosition()).getItemDuration());
                intent.putExtra("category",myFoodList.get(foodViewHolder.getAdapterPosition()).getItemCategory());
                intent.putExtra("keyValue",myFoodList.get(foodViewHolder.getAdapterPosition()).getKey());


                mContext.startActivity(intent);

            }
        });

        setAnimation(foodViewHolder.itemView,i);
    }

    public void setAnimation(View viewToAnimate, int position){
        if(position>lastPosition){
            ScaleAnimation animation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);

            animation.setDuration(800);
            viewToAnimate.startAnimation(animation);
            lastPosition=position;
        }
    }




    @Override
    public int getItemCount() {
        return myFoodList.size();
    }

    public void filteredList(ArrayList<FoodData> filterList) {
        myFoodList=filterList;
        notifyDataSetChanged();

    }
}

class FoodViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView textViewTitle,textViewDescription,textViewDuration,textViewCategory;
    CardView cardView;


    public FoodViewHolder(View itemView) {
        super(itemView);


        imageView = itemView.findViewById(R.id.imageViewImage);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewDescription = itemView.findViewById(R.id.textViewDescription);
        textViewCategory = itemView.findViewById(R.id.textViewCategory);
        textViewDuration=itemView.findViewById(R.id.textViewDuration);

        cardView=itemView.findViewById(R.id.myCardView);

    }
}
