package com.app.socialmediacontentmanager.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.app.socialmediacontentmanager.R;
import com.app.socialmediacontentmanager.database.DbOperations;
import com.app.socialmediacontentmanager.models.ContentItem;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ViewContentActivity extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvTags, tvStatus, tvCreatedDate, tvUpdatedDate;
    private MaterialButton btnEdit, btnDelete;
    private DbOperations dbOperations;

    private ContentItem contentItem;
    private int contentItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_content);

        contentItemId = getIntent().getIntExtra("CONTENT_ITEM_ID", -1);
        dbOperations = new DbOperations(this);

        initializeViews();
        loadContentItem();
        setupListeners();
    }

    private void initializeViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvTags = findViewById(R.id.tvTags);
        tvStatus = findViewById(R.id.tvStatus);
        tvCreatedDate = findViewById(R.id.tvCreatedDate);
        tvUpdatedDate = findViewById(R.id.tvUpdatedDate);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void loadContentItem() {
        contentItem = dbOperations.getContentItemById(contentItemId);
        if (contentItem != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

            tvTitle.setText(contentItem.getTitle());
            tvDescription.setText(contentItem.getDescription());
            tvTags.setText(contentItem.getTags() != null ? contentItem.getTags() : "No tags");

            if (contentItem.getStatus() == 1) {
                tvStatus.setText("Published");
                tvStatus.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvStatus.setText("Draft");
                tvStatus.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
            }

            tvCreatedDate.setText(dateFormat.format(contentItem.getCreationDate()));
            tvUpdatedDate.setText(dateFormat.format(contentItem.getLastUpdated()));
        }
    }

    private void setupListeners() {
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ViewContentActivity.this, AddEditContentActivity.class);
            intent.putExtra("CONTENT_ITEM_ID", contentItemId);
            intent.putExtra("CONTENT_TYPE_ID", contentItem.getTypeId());
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Content")
                .setMessage("Are you sure you want to delete this content idea?")
                .setPositiveButton("Delete", (dialog, which) -> deleteContentItem())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteContentItem() {
        int rowsAffected = dbOperations.deleteContentItem(contentItemId);
        if (rowsAffected > 0) {
            Toast.makeText(this, "Content deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete content", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            shareContent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareContent() {
        String shareText = "Content Idea: " + contentItem.getTitle() + "\n\n" +
                contentItem.getDescription() + "\n\n" +
                "Tags: " + (contentItem.getTags() != null ? contentItem.getTags() : "None");

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Content Idea: " + contentItem.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh content if it was edited
        loadContentItem();
    }
}