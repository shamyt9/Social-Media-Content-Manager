package com.app.socialmediacontentmanager.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.socialmediacontentmanager.R;
import com.app.socialmediacontentmanager.adapters.ContentTypeAdapter;
import com.app.socialmediacontentmanager.database.DbOperations;
import com.app.socialmediacontentmanager.models.ContentType;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private TextView categoryTitle;
    private RecyclerView contentTypesRecyclerView;
    private ContentTypeAdapter contentTypeAdapter;
    private DbOperations dbOperations;

    private int categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Get category data from intent
        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        dbOperations = new DbOperations(this);

        initializeViews();
        loadContentTypes();
    }

    private void initializeViews() {
        categoryTitle = findViewById(R.id.categoryTitle);
        contentTypesRecyclerView = findViewById(R.id.contentTypesRecyclerView);

        categoryTitle.setText(categoryName + " Content Types");
        contentTypesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentTypesRecyclerView.setHasFixedSize(true);
    }

    private void loadContentTypes() {
        List<ContentType> contentTypes = dbOperations.getContentTypesByCategory(categoryId);
        contentTypeAdapter = new ContentTypeAdapter(this, contentTypes, categoryName);
        contentTypesRecyclerView.setAdapter(contentTypeAdapter);
    }
}