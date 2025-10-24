package com.app.socialmediacontentmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.socialmediacontentmanager.R;
import com.app.socialmediacontentmanager.activities.ContentListActivity;
import com.app.socialmediacontentmanager.activities.ViewContentActivity;
import com.app.socialmediacontentmanager.adapters.ContentItemAdapter;
import com.app.socialmediacontentmanager.database.DbOperations;
import com.app.socialmediacontentmanager.models.ContentItem;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recentIdeasRecyclerView;
    private TextView tvEmptyState;
    private ContentItemAdapter contentItemAdapter;
    private DbOperations dbOperations;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbOperations = new DbOperations(requireContext());

        recentIdeasRecyclerView = view.findViewById(R.id.recentIdeasRecyclerView);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        recentIdeasRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentIdeasRecyclerView.setHasFixedSize(true);

        loadRecentIdeas();
    }

    private void loadRecentIdeas() {
        // Get all content items sorted by last updated date
        List<ContentItem> recentItems = dbOperations.searchContentItems("");

        if (recentItems.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            recentIdeasRecyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            recentIdeasRecyclerView.setVisibility(View.VISIBLE);

            contentItemAdapter = new ContentItemAdapter(getContext(), recentItems);
            recentIdeasRecyclerView.setAdapter(contentItemAdapter);

            // Set click listener for items
            contentItemAdapter.setOnItemClickListener((position, item) -> {
                // You might want to create a different activity for viewing from home
                Intent intent = new Intent(getActivity(), ViewContentActivity.class);
                intent.putExtra("CONTENT_ITEM_ID", item.getId());
                startActivity(intent);
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecentIdeas();
    }
}