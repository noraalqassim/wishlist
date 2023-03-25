package com.example.wishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.Adapters.ProductsAdapter;
import com.example.wishlist.Model.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Fragment_Show_All_Products extends Fragment {
    RecyclerView recview;
    ProductsAdapter adapter;
    private ArrayList<String> listCategory = new ArrayList<>();

    public Fragment_Show_All_Products() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_product, container, false);
       recview = (RecyclerView) view.findViewById(R.id.recview);
       String categoryName = "";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            categoryName = bundle.getString("category", "");
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recview.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>().setQuery(FirebaseDatabase.getInstance().getReference().child("Product").orderByChild("category").equalTo(categoryName), Product.class).build();//استرجاع البيانات من قاعدة البيانات
        adapter = new ProductsAdapter(options);
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
}