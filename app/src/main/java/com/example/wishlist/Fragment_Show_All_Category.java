package com.example.wishlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wishlist.Adapters.CategoryAdapter;
import com.example.wishlist.Model.Category;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import io.reactivex.rxjava3.annotations.NonNull;


public class Fragment_Show_All_Category extends Fragment {
    RecyclerView recview;
    CategoryAdapter adapter;
    SearchView searchView;
    public Fragment_Show_All_Category() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment__show__all__category, container, false);
       recview = (RecyclerView) view.findViewById(R.id.recviewcategory);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
       recview.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>().
                        setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Category"), Category.class).build();
       adapter = new CategoryAdapter(options);
       recview.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
       adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }

    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);
    }

    private void  process_serch(String s){

    }



}