package com.prag.stickertabs.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prag.stickertabs.Adapter.StickerRecyclerAdapter;
import com.prag.stickertabs.Add_item_model;
import com.prag.stickertabs.R;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class Category1 extends Fragment {

    Button upload;
    final static int RQS_OPEN_DOCUMENT = 1;
    RecyclerView recyclerView;
    StorageReference storageReference;
    FirebaseStorage storage;
    private CollectionReference noteRef;
    FirebaseFirestore db;
    String name;
    ViewGroup temp;
    StickerRecyclerAdapter stickerRecyclerAdapter;
    public Category1() {

    }

    @Override
    public void onStart() {
        super.onStart();
        stickerRecyclerAdapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        stickerRecyclerAdapter.stopListening();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        temp = container;
        View view = inflater.inflate(R.layout.fragment_category1, container, false);
        upload = view.findViewById(R.id.upload);
        recyclerView = view.findViewById(R.id.sticker_Recycler);

        db = FirebaseFirestore.getInstance();
        noteRef = db.collection("Category 1");
        getAllDocs();

        storage = FirebaseStorage.getInstance("gs://quickselll.appspot.com/");
        storageReference = storage.getReference();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                String strType = "image/*";
                intent.setType(strType);
                startActivityForResult(intent, RQS_OPEN_DOCUMENT);
            }
        });
        return view;
    }

    private void getAllDocs() {
            Query query = noteRef.orderBy("imagepath", Query.Direction.DESCENDING);
            //Query query = noteRef.whereGreaterThanOrEqualTo("product_images","https:");
            FirestoreRecyclerOptions<Add_item_model> options = new FirestoreRecyclerOptions.Builder<Add_item_model>()
                    .setQuery(query, Add_item_model.class)
                    .build();

            stickerRecyclerAdapter = new StickerRecyclerAdapter(options, getContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(temp.getContext(), 3));
            recyclerView.setAdapter(stickerRecyclerAdapter);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
               getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            StorageReference ref = storageReference.child("Stickers").child(filePath.getLastPathSegment());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    Toast.makeText(getContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                                        upload_To_FireStore(String.valueOf(task.getResult()));

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                        }
                    });
        }
    }

    private void FireBase_AddItem(Add_item_model item) {
        db.collection("Category 1").document().set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Successfully added..", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Something Went Wrong..", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void upload_To_FireStore(String s) {
        Add_item_model item = new Add_item_model(name, s);
        FireBase_AddItem(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RQS_OPEN_DOCUMENT) {
                Uri uri = data.getData();
                name = uri.getPath();
                try {
                    uploadImage(uri);
                    getBitmapFromUri(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
