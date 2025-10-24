package com.app.socialmediacontentmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.socialmediacontentmanager.R;
import com.app.socialmediacontentmanager.adapters.ContentItemAdapter;
import com.app.socialmediacontentmanager.database.DbOperations;
import com.app.socialmediacontentmanager.models.ContentItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ContentListActivity extends AppCompatActivity {

    private static final String TAG = "ContentListActivity";
    private Toolbar toolbar;
    private RecyclerView contentItemsRecyclerView;
    private FloatingActionButton fabAddContent;
    private TextView tvEmptyState;
    private ContentItemAdapter contentItemAdapter;
    private DbOperations dbOperations;

    private int contentTypeId;
    private String contentTypeName;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_list);

        Log.d(TAG, "ContentListActivity started");

        // Get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            contentTypeId = intent.getIntExtra("CONTENT_TYPE_ID", -1);
            contentTypeName = intent.getStringExtra("CONTENT_TYPE_NAME");
            categoryName = intent.getStringExtra("CATEGORY_NAME");
            Log.d(TAG, "Received: typeId=" + contentTypeId + ", typeName=" + contentTypeName + ", category=" + categoryName);
        }

        dbOperations = new DbOperations(this);

        initializeViews();
        setupToolbar();
        loadContentItems();
        setupFab();
    }

    private void initializeViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            contentItemsRecyclerView = findViewById(R.id.contentItemsRecyclerView);
            fabAddContent = findViewById(R.id.fabAddContent);
            tvEmptyState = findViewById(R.id.tvEmptyState);

            if (contentItemsRecyclerView == null) {
                Log.e(TAG, "RecyclerView not found!");
                return;
            }

            contentItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            contentItemsRecyclerView.setHasFixedSize(true);

        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage(), e);
        }
    }

    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(contentTypeName);
                getSupportActionBar().setSubtitle(categoryName);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up toolbar: " + e.getMessage(), e);
        }
    }

    private void loadContentItems() {
        try {
            if (contentTypeId == -1) {
                Log.e(TAG, "Invalid content type ID");
                return;
            }

            List<ContentItem> contentItems = dbOperations.getContentItemsByType(contentTypeId);
            Log.d(TAG, "Loaded " + contentItems.size() + " items for type ID: " + contentTypeId);

            if (contentItems.isEmpty()) {
                showEmptyState();
            } else {
                showContentList();
                contentItemAdapter = new ContentItemAdapter(this, contentItems);
                contentItemsRecyclerView.setAdapter(contentItemAdapter);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading content items: " + e.getMessage(), e);
            showEmptyState();
        }
    }

    private void showEmptyState() {
        if (contentItemsRecyclerView != null) contentItemsRecyclerView.setVisibility(View.GONE);
        if (tvEmptyState != null) {
            tvEmptyState.setVisibility(View.VISIBLE);
            tvEmptyState.setText("No ideas yet!\nClick the + button to add your first idea.");
        }
    }

    private void showContentList() {
        if (tvEmptyState != null) tvEmptyState.setVisibility(View.GONE);
        if (contentItemsRecyclerView != null) contentItemsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setupFab() {
        if (fabAddContent != null) {
            fabAddContent.setOnClickListener(v -> {
                Intent addIntent = new Intent(ContentListActivity.this, AddEditContentActivity.class);
                addIntent.putExtra("CONTENT_TYPE_ID", contentTypeId);
                addIntent.putExtra("CONTENT_TYPE_NAME", contentTypeName);
                startActivity(addIntent);
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list when returning from Add/Edit activity
        loadContentItems();
    }
}