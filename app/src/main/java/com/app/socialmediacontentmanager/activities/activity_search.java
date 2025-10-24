package com.app.socialmediacontentmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.socialmediacontentmanager.R;
import com.app.socialmediacontentmanager.adapters.ContentItemAdapter;
import com.app.socialmediacontentmanager.database.DbOperations;
import com.app.socialmediacontentmanager.models.ContentItem;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class activity_search extends AppCompatActivity {

    private TextInputEditText etSearch;
    private RecyclerView searchResultsRecyclerView;
    private TextView tvNoResults;
    private ContentItemAdapter contentItemAdapter;
    private DbOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbOperations = new DbOperations(this);

        initializeViews();
        setupSearchListener();
    }

    private void initializeViews() {
        etSearch = findViewById(R.id.etSearch);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        tvNoResults = findViewById(R.id.tvNoResults);

        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setHasFixedSize(true);

        // Show all content initially
        performSearch("");
    }

    private void setupSearchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch(String query) {
        List<ContentItem> searchResults = dbOperations.searchContentItems(query);

        if (searchResults.isEmpty()) {
            tvNoResults.setVisibility(View.VISIBLE);
            searchResultsRecyclerView.setVisibility(View.GONE);
        } else {
            tvNoResults.setVisibility(View.GONE);
            searchResultsRecyclerView.setVisibility(View.VISIBLE);

            contentItemAdapter = new ContentItemAdapter(this, searchResults);
            searchResultsRecyclerView.setAdapter(contentItemAdapter);

            contentItemAdapter.setOnItemClickListener((position, item) -> {
                Intent intent = new Intent(activity_search.this, ViewContentActivity.class);
                intent.putExtra("CONTENT_ITEM_ID", item.getId());
                startActivity(intent);
            });
        }
    }
}