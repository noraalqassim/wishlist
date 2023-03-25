package com.example.wishlist;

import static android.app.Activity.RESULT_OK;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.wishlist.Model.Category;
import com.example.wishlist.ui.CategoryViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.Calendar;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
public class Fragment_Add_Category extends Fragment {

    private CategoryViewModel mViewModel;
    public static Fragment_Add_Category newInstance() {
        return new Fragment_Add_Category();
    }

    FirebaseStorage storage;
    StorageReference storageReference;
    public Button btnSave, chooseImg;
    EditText CategoryName;
    ImageView CategoryImage;
    Uri filePath, urlImage, uriPath;

    SearchView searchView;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
                filePath = data.getData();
                CategoryImage.setImageURI(filePath);
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
                new ViewModelProvider(this).get(CategoryViewModel.class);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        View root;
            root = inflater.inflate(R.layout.fragment_add_category, container, false);
            btnSave = root.findViewById(R.id.btnSaveCategory);
            storageReference = FirebaseStorage.getInstance().getReference();
            CategoryImage = root.findViewById(R.id.imgNewCategory);
            chooseImg = root.findViewById(R.id.chooseImageCategory);
            CategoryName = root.findViewById(R.id.txtCategoryName);
            chooseImg.setOnClickListener(v -> {
                Intent Galary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Galary, 200);
            });
            btnSave.setOnClickListener(view -> {
                String name = CategoryName.getText().toString();
                if (CategoryImage.getDrawable() == null ||urlImage.toString()=="") {
                    Toast.makeText(getContext(),"Please Choose Image", Toast.LENGTH_LONG).show();
                } else if (name.equals("")) {
                    Toast.makeText(getContext(),"Please Insert Name ", Toast.LENGTH_LONG).show();
                } else {
                    AddCategory();
                }
            });




        return root;
    }


    private void AddCategory() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Category");
        Category category = new Category();
        category.setName(CategoryName.getText().toString());
        category.setImage(String.valueOf(urlImage));
        myRef.push().setValue(category).addOnSuccessListener(aVoid -> {
            CategoryName.setText("");
            CategoryImage.setImageURI(null);
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to save Image", Toast.LENGTH_LONG).show());
    }
    private void UploadImage() {
        if (uriPath != null) {
            Calendar calendar = Calendar.getInstance();
            StorageReference imagePath = storageReference.child("CategoryImages").child("category" + calendar.getTimeInMillis());
            imagePath.putFile(uriPath).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getActivity(), " success upload image ", Toast.LENGTH_LONG).show();
                imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        urlImage = uri;
                    }

                });
            }).addOnFailureListener(e -> Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show());
        }
    }
}
