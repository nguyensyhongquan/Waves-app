package com.example.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.dao.CategoryDao;
import com.example.project.models.category;

public class AddCategoryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private EditText etName, etDesc;
    private ImageView imgPreview;
    private Button btnChooseImage, btnSave;
    private Uri imageUri;
    private CategoryDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_create_category);

        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        imgPreview = findViewById(R.id.imgPreview);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSave = findViewById(R.id.btnSave);
        dao = new CategoryDao(this);

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String desc = etDesc.getText().toString();
            if (name.isEmpty() || imageUri == null) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            category c = new category();
            c.setName(name);
            c.setDescription(desc);
            c.setImage(imageUri.toString());
            dao.create(c);

            Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imgPreview.setImageURI(imageUri);
        }
    }
}
