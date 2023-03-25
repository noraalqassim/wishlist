package com.example.wishlist;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.wishlist.Model.Product;
import com.example.wishlist.ui.ProductViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

public class FragmentAddProduct extends Fragment {

    private ProductViewModel mViewModel;
    private ArrayList<String> listCategory = new ArrayList<>();
    DatabaseReference CategoryDatabaseReference = FirebaseDatabase.getInstance().getReference();
    private Spinner spinnerCategory;
    String  Cat;
    public static SharedPreferences sh;

    public static FragmentAddProduct newInstance() {
        return new FragmentAddProduct();
    }

    FirebaseStorage storage;
    StorageReference storageReference;
    public Button btnSave, chooseImg;
    EditText ProductName, PriceProduct,StoreName,productDetails;
    ImageView ProductImage;
    SearchView searchView;
    Uri filePath, urlImage, uriPath;
TextView appbar;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
                filePath = data.getData();
                ProductImage.setImageURI(filePath);
                uriPath = filePath;
                UploadImage();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(ProductViewModel.class);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
         sh = getActivity().getSharedPreferences("UT",MODE_PRIVATE);


        int  type = sh.getInt("type", 0);
        View root;

            root = inflater.inflate(R.layout.fragment_add_product, container, false);
            btnSave = root.findViewById(R.id.btnSave);
            storageReference = FirebaseStorage.getInstance().getReference();
            ProductImage = root.findViewById(R.id.imgNewProduct);
            chooseImg = root.findViewById(R.id.chooseImage);
            ProductName = (EditText) root.findViewById(R.id.txtProductName);
            PriceProduct = (EditText) root.findViewById(R.id.txtProduct_Price);
            spinnerCategory = root.findViewById(R.id.Category_spinner);
            productDetails=root.findViewById(R.id.txtProduct_details);
            showCategoryData();
            chooseImg.setOnClickListener(v -> {
                Intent Galary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Galary, 200);
            });
        btnSave.setOnClickListener(view -> {
            String name = ProductName.getText().toString();
            if (ProductImage.getDrawable() == null) {
                Toast.makeText(getContext(),"Please Select Image", Toast.LENGTH_LONG).show();
            } else if (name.equals("")) {
                Toast.makeText(getContext(),"Please Enter Name ", Toast.LENGTH_LONG).show();
            } else {
                try{
                    AddProduct();
                }catch (Exception ex){
                    Toast.makeText(getContext(),ex.toString(),Toast.LENGTH_LONG).show();
                }

            }
        });


        return root;
    }

    private void AddProduct() {
        Cat = spinnerCategory.getSelectedItem().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
       String user=sh.getString("email","null");
        DatabaseReference myRef = database.getReference("Product");
        Product product = new Product();
        product.setName(ProductName.getText().toString());
        product.setPrice(PriceProduct.getText().toString());
        product.setImage(String.valueOf(urlImage));
        product.setDetails(productDetails.getText().toString());
        product.setUser(user);
        product.setCategory(Cat);
        myRef.push().setValue(product).addOnSuccessListener(aVoid -> {
            ProductName.setText("");
            PriceProduct.setText("");
            ProductImage.setImageURI(null);
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Field To save ", Toast.LENGTH_LONG).show());
    }
    private void UploadImage() {
        if (uriPath != null) {
            Calendar calendar = Calendar.getInstance();
            StorageReference imagePath = storageReference.child("ProductImages").child("Product" + calendar.getTimeInMillis());
            imagePath.putFile(uriPath).addOnSuccessListener(taskSnapshot -> imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    urlImage = uri;
                }
            })).addOnFailureListener(e -> {
            });
        }
    }

    private void showCategoryData() {
        CategoryDatabaseReference.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCategory.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    listCategory.add(item.child("name").getValue().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.style_spinner, listCategory);
                spinnerCategory.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
