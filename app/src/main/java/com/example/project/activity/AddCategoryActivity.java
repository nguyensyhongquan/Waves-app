package com.example.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.dao.CategoryDao;
import com.example.project.models.category;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddCategoryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private EditText etName, etDesc;
    private ImageView imgPreview;
    private Button btnChooseImage, btnSave;
    private String encodedImage = null;
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

        btnChooseImage.setOnClickListener(v -> openImageChooser());
        btnSave.setOnClickListener(v -> saveCategory());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgPreview.setImageBitmap(bitmap);

                // nén ảnh thành PNG rồi mã hóa Base64
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveCategory() {
        String name = etName.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();

        if (name.isEmpty() || encodedImage == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        category c = new category();
        c.setName(name);
        c.setDescription(desc);
        c.setImage(encodedImage); // lưu Base64 thay vì URI

        dao.create(c);
        Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
