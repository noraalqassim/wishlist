package com.example.wishlist.Adapters;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.wishlist.Model.Category;
import com.example.wishlist.Model.Product;
import com.example.wishlist.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class ProductsAdapter extends FirebaseRecyclerAdapter<Product, ProductsAdapter.myviewholder> {
    private ArrayList<String> listCategory=new ArrayList<>();

public static SharedPreferences sharedPreferences;
    DatabaseReference CategoryDatabaseReference= FirebaseDatabase.getInstance().getReference();
    public ProductsAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder,int position, @NonNull Product model) {
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>().setQuery(FirebaseDatabase.getInstance().getReference().child("Category"), Category.class).build();
        holder.NameProduct.setText(model.getName());
        holder.PriceProduct.setText(model.getPrice());
        holder.UserName.setText(model.getUser());

        Glide.with(holder.ImgProduct.getContext()).load(model.getImage()).into(holder.ImgProduct);
        holder.itemView.setOnClickListener(view -> {

            {
                AlertDialog.Builder alert=new AlertDialog.Builder(holder.ImgProduct.getContext());
                alert.setTitle("Do you want update or delete ");
                alert.setPositiveButton("Update  ", (dialogInterface, i) -> {
                    final DialogPlus dialogPlus = DialogPlus.newDialog(holder.ImgProduct.getContext())
                            .setContentHolder(new ViewHolder(R.layout.fragment_edit_product))
                            .setExpanded(true, 1420)
                            .create();
                    View myview = dialogPlus.getHolderView();
                    final ImageView img = myview.findViewById(R.id.editProImage);
                    final EditText name = myview.findViewById(R.id.editProName);
                    final EditText details = myview.findViewById(R.id.editProPrice);
                    final Spinner cate=myview.findViewById(R.id.edit_Pro_Category_spinner);
                    Button submit = myview.findViewById(R.id.Pro_edit_submit);
                    Glide.with(holder.ImgProduct.getContext()).load(model.getImage()).into(img);
                    name.setText(model.getName());
                    details.setText(model.getPrice());
                    dialogPlus.show();
                    CategoryDatabaseReference.child("Category").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listCategory.clear();
                            for(DataSnapshot item:snapshot.getChildren() ){
                                listCategory.add(item.child("name").getValue().toString());
                            }
                            ArrayAdapter<String> adapter=new ArrayAdapter<>(view.getContext(),R.layout.style_spinner,listCategory);
                            cate.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    submit.setOnClickListener(view1 -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name.getText().toString());
                        map.put("price", details.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("Product")
                                .child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialogPlus.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });
                    });
                });
                alert.setNegativeButton("Delete", (dialogInterface, i) -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.ImgProduct.getContext());
                    builder.setTitle("Delete Product");
                    builder.setMessage("Are you sure to delete ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference().child("Product")
                                    .child(getRef(position).getKey()).removeValue();//لحذف المنتج من قاعدة البيانات
                        }
                    });
                    builder.setNegativeButton("No", (dialogInterface1, i1) -> {
                    });
                    builder.show();
                });
                alert.show();

            }
        });
    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_card, parent, false);
        return new myviewholder(view);
    }
    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView ImgProduct;
        TextView NameProduct, PriceProduct,UserName;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            ImgProduct = itemView.findViewById(R.id.productimg);
            NameProduct = itemView.findViewById(R.id.productname);
            PriceProduct = itemView.findViewById(R.id.productprice);
            UserName = itemView.findViewById(R.id.productUser);
        }
    }
}