package com.app.socialmediacontentmanager.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.app.socialmediacontentmanager.R;
import com.app.socialmediacontentmanager.database.DbOperations;
import com.app.socialmediacontentmanager.models.ContentItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Date;

public class AddEditContentActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etDescription, etTags;
    private MaterialButton btnSaveDraft, btnPublish;
    private DbOperations dbOperations;

    private int contentTypeId;
    private int contentItemId = -1; // -1 means new item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_content);

        contentTypeId = getIntent().getIntExtra("CONTENT_TYPE_ID", -1);
        contentItemId = getIntent().getIntExtra("CONTENT_ITEM_ID", -1);

        dbOperations = new DbOperations(this);

        initializeViews();
        setupListeners();

        if (contentItemId != -1) {
            // Edit mode - load existing content
            loadContentItem();
        }
    }

    private void initializeViews() {
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etTags = findViewById(R.id.etTags);
        btnSaveDraft = findViewById(R.id.btnSaveDraft);
        btnPublish = findViewById(R.id.btnPublish);
    }

    private void setupListeners() {
        btnSaveDraft.setOnClickListener(v -> saveContent(0)); // 0 = draft
        btnPublish.setOnClickListener(v -> saveContent(1));   // 1 = published
    }

    private void loadContentItem() {
        ContentItem item = dbOperations.getContentItemById(contentItemId);
        if (item != null) {
            etTitle.setText(item.getTitle());
            etDescription.setText(item.getDescription());
            etTags.setText(item.getTags());
        }
    }

    private void saveContent(int status) {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String tags = etTags.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Title is required");
            return;
        }

        ContentItem contentItem = new ContentItem();
        contentItem.setTitle(title);
        contentItem.setDescription(description);
        contentItem.setTags(tags);
        contentItem.setStatus(status);
        contentItem.setTypeId(contentTypeId);

        if (contentItemId != -1) {
            // Update existing item
            contentItem.setId(contentItemId);
            int rowsAffected = dbOperations.updateContentItem(contentItem);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Content updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update content", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add new item
            long newId = dbOperations.addContentItem(contentItem);
            if (newId != -1) {
                Toast.makeText(this, "Content saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save content", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}