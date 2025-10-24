package com.app.socialmediacontentmanager.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.socialmediacontentmanager.R;
import com.app.socialmediacontentmanager.adapters.CategoryAdapter;
import com.app.socialmediacontentmanager.database.DbOperations;
import com.app.socialmediacontentmanager.models.Category;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView categoriesRecyclerView;
    private DbOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Activity started");

        try {
            initializeDatabase();
            initializeViews();
            loadCategories();
            Toast.makeText(this, "App loaded successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeDatabase() {
        try {
            dbOperations = new DbOperations(this);
            Log.d(TAG, "Database initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Database initialization failed: " + e.getMessage(), e);
            throw new RuntimeException("Database error");
        }
    }

    private void initializeViews() {
        try {
            categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
            if (categoriesRecyclerView == null) {
                throw new RuntimeException("RecyclerView not found");
            }

            categoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            categoriesRecyclerView.setHasFixedSize(true);
            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "View initialization failed: " + e.getMessage(), e);
            throw new RuntimeException("View initialization error");
        }
    }

    private void loadCategories() {
        try {
            List<Category> categories = dbOperations.getAllCategories();
            Log.d(TAG, "Loaded " + categories.size() + " categories");

            CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories);
            categoriesRecyclerView.setAdapter(categoryAdapter);
            Log.d(TAG, "Adapter set successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error loading categories: " + e.getMessage(), e);
            Toast.makeText(this, "Error loading content", Toast.LENGTH_SHORT).show();
        }
    }
}