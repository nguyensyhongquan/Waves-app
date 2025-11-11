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

import com.example.project.dao.CategoryDao;
import com.example.project.dao.ItemDao;
import com.example.project.models.category;
import com.example.project.models.item;
import com.example.project.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imgPreview;
    private Spinner spCategory;
    private EditText etName, etDescription, etPrice;
    private Button btnSelectImage, btnSave;
    private String encodedImage = null;
    private List<category> categoryList;
    private CategoryDao categoryDao;
    private ItemDao itemDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_item);

        imgPreview = findViewById(R.id.imgPreview);
        spCategory = findViewById(R.id.spCategory);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSave = findViewById(R.id.btnSaveItem);

        categoryDao = new CategoryDao(this);
        itemDao = new ItemDao(this);

        loadCategories();

        btnSelectImage.setOnClickListener(v -> openImageChooser());
        btnSave.setOnClickListener(v -> saveItem());
    }

    private void loadCategories() {
        categoryList = categoryDao.getAll();
        List<String> categoryNames = new ArrayList<>();
        for (category c : categoryList) {
            categoryNames.add(c.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgPreview.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                encodedImage = android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveItem() {
        if (encodedImage == null || etName.getText().toString().isEmpty() || etPrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPos = spCategory.getSelectedItemPosition();
        int categoryId = categoryList.get(selectedPos).getId();

        item i = new item();
        i.setName(etName.getText().toString());
        i.setDescription(etDescription.getText().toString());
        i.setPrice(Double.parseDouble(etPrice.getText().toString()));
        i.setImage(encodedImage);
        i.setCategoryid(categoryId);

        long newId = itemDao.create(i);
        if (newId > 0){
            Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }

        else{
            Toast.makeText(this, "Failed to add item.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
