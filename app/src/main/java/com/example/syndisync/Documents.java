package com.example.syndisync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Documents extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 1;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DocumentAdapter documentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        Button uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
            }
        });

        // Initialize RecyclerView
        RecyclerView documentsRecyclerView = findViewById(R.id.documentsRecyclerView);
        documentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Set the adapter for the RecyclerView
        // Initialize the adapter with an empty list of documents
        documentAdapter = new DocumentAdapter(new ArrayList<>());
        documentsRecyclerView.setAdapter(documentAdapter);

        // Fetch the list of files from Firebase Storage
        fetchDocuments();
    }

    private void fetchDocuments() {
        storageRef.child("documents").listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference fileRef : listResult.getItems()) {
                            // Get the download URL for each file
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Document document = new Document(fileRef.getName(), uri.toString());
                                    documentAdapter.addDocument(document);
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Documents.this, "Failed to fetch documents", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            StorageReference fileRef = storageRef.child("documents/" + fileUri.getLastPathSegment());

            // Print the file URI and the storage reference path
            Log.d("UploadInfo", "File URI: " + fileUri.toString());
            Log.d("UploadInfo", "Storage Reference Path: " + fileRef.getPath());

            UploadTask uploadTask = fileRef.putFile(fileUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Documents.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();

                    // Update the RecyclerView
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Document document = new Document(fileUri.getLastPathSegment(), uri.toString());
                            documentAdapter.addDocument(document);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Documents.this, "Failed to upload file", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}