package com.example.wishlist.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wishlist.Fragment_Show_All_Products;
import com.example.wishlist.Model.Category;
import com.example.wishlist.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.rxjava3.annotations.NonNull;

public class CategoryAdapter extends FirebaseRecyclerAdapter<Category, CategoryAdapter.myviewholder> {

    public static SharedPreferences sharedPreferences;
    public CategoryAdapter(@NonNull FirebaseRecyclerOptions<Category> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder,int position, @NonNull Category model) {
        holder.NameCategory.setText(model.getName());
        Glide.with(holder.ImgCategory.getContext()).load(model.getImage()).into(holder.ImgCategory);
        sharedPreferences = holder.ImgCategory.getContext().getSharedPreferences("UT", Context.MODE_PRIVATE);
        holder.ImgCategory.setOnClickListener(view -> {
            Fragment_Show_All_Products fragment = new Fragment_Show_All_Products();
            FragmentManager fragmentManager = ((FragmentActivity) view.getContext()).getSupportFragmentManager(); // instantiate your view context
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("category",model.getName());
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragmentContainer, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category_card, parent, false);
        return new myviewholder(view);
    }
    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView ImgCategory;
        TextView NameCategory;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            ImgCategory = itemView.findViewById(R.id.categoryimg);
            NameCategory = itemView.findViewById(R.id.categoryname);

        }
    }
}