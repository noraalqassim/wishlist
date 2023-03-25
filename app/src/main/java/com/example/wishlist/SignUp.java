package com.example.wishlist;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wishlist.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

import io.reactivex.rxjava3.annotations.NonNull;

public class SignUp extends AppCompatActivity {
    Button btnSign;
    EditText mail, pass, name;
    TextView tvLogin;
    ImageView profile_image;
    Uri filePath, urlImage, uriPath;
    StorageReference storageReference;
    FirebaseStorage storage;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    ProgressDialog pDialog ;
    public static FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        pDialog = new ProgressDialog(getApplicationContext());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        tvLogin = findViewById(R.id.tvLogin);
        btnSign = findViewById(R.id.btnSaveNewUser);
        btnSign.setEnabled(false);
        profile_image=findViewById(R.id.profile_image);
        sharedPreferences = getSharedPreferences("UT",MODE_PRIVATE);
        myEdit= sharedPreferences.edit();
        tvLogin.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, Login_Activity.class);
            startActivity(intent);
        });
        mail = findViewById(R.id.UserEmail);
        name = findViewById(R.id.UserName);
        pass = findViewById(R.id.UserPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        profile_image.setOnClickListener(view -> {

            Intent Galary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(Galary, 200);
        });



        btnSign.setOnClickListener(view -> {

           if ((name.getText().toString().trim().equals(""))) {
                name.requestFocus();
                name.setError("Please Enter Name ");

            }
           else   if ((mail.getText().toString().trim().equals(""))) {
                mail.requestFocus();
                mail.setError("Please Enter Email ");
            }
            else if ((pass.getText().toString().trim().equals(""))) {
                pass.requestFocus();
                pass.setError("Please Enter Password ");
            }
           else if ((pass.getText().length()<6)) {
               pass.requestFocus();
               pass.setError("password are less than 6 letter");
           }
            else {
                firebaseAuth.createUserWithEmailAndPassword(mail.getText().toString(),
                        pass.getText().toString()).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            AddNewUser();
                            myEdit.putBoolean("login", true);
                            myEdit.putString("email", mail.getText().toString());
                            myEdit.commit();
                            Intent intent =new Intent(SignUp.this,MainActivity.class);
                            startActivity(intent);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
                filePath = data.getData();
                profile_image.setImageURI(filePath);
                uriPath = filePath;
UploadImage();
                btnSign.setEnabled(true);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void UploadImage() {
        if (uriPath != null) {
    Calendar calendar=Calendar.getInstance();
            StorageReference imagePath = storageReference.child("UsersImage").child(calendar.getTime().toString());
            imagePath.putFile(uriPath).addOnSuccessListener(taskSnapshot -> {
                imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        urlImage = uri;

                    }
                });
            }).addOnFailureListener(e ->
                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }
    private void AddNewUser() {
        pDialog.dismiss();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        User user = new User();
        user.setName(name.getText().toString());
        user.setEmail(mail.getText().toString());
        user.setPassword(pass.getText().toString());
        user.setImage(urlImage.toString());
        String token=myRef.push().getKey();
        myEdit.putString("user", token.toString());
myEdit.commit();
        myRef.child(token).setValue(user).addOnSuccessListener(aVoid -> {

        }).addOnFailureListener(e -> {
        });
    }
}

//