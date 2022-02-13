package com.example.gappa;

import static android.provider.MediaStore.Images.Media.getBitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.gappa.Models.Users;
import com.example.gappa.databinding.ActivitySettingBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    StorageReference storageReference;
    Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth  = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.backArrowSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = binding.statusEdittext.getText().toString();
                String username = binding.usernameEditText.getText().toString();

                HashMap<String , Object> obj = new HashMap<>();
                obj.put("userName",username);
                obj.put("status",status);

                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);
                Toast.makeText(SettingActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
            }
        });
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                       /*Picasso.get().load(users.getProfilepic())
                              .placeholder(R.drawable.o2)
                             .into(binding.profileImage);*/
                        retriveImage();










                        binding.usernameEditText.setText(users.getUserName());
                        binding.statusEdittext.setText(users.getStatus());





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                /*
                Intent i = new Intent();
                i.setType("images/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                i,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);

                //startActivityForResult(i,33);
                */
                SelectImage();
                uploadImage();
            }
        });
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null)
        {
            File = data.getData();
            binding.profileImage.setImageURI(File);



            final StorageReference reference = storage.getReference().child("profile_images")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(File).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profilePics").setValue(uri.toString());

                            Toast.makeText(SettingActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }

    }
    */
private void SelectImage()
{

    // Defining Implicit Intent to mobile gallery
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(
            Intent.createChooser(
                    intent,
                    "Select Image from here..."),
            PICK_IMAGE_REQUEST);
}

    // Override onActivityResult method
    @Override

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            /*
            try {

                // Setting image on image view using Bitmap
               Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                binding.profileImage.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
            */
        }
    }

    private void retriveImage()
    {

        storageReference = storage.getReference();
        storageReference=FirebaseStorage.getInstance().getReference().child("profile_images")
                .child(FirebaseAuth.getInstance().getUid());


        try{
           File localfile = File.createTempFile("tempfile",".jpg");

        storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
            {

                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                binding.profileImage.setImageBitmap(bitmap);
            }

        });

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

        private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading




            // Defining the child of storageReference
            /*StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());*/
            final StorageReference ref = storage.getReference().child("profile_images")
                    .child(FirebaseAuth.getInstance().getUid());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog

                                    Toast.makeText(SettingActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded

                            Toast.makeText(SettingActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

        }
    }




}