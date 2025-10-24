package com.app.socialmediacontentmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.app.socialmediacontentmanager.R;
import com.app.socialmediacontentmanager.activities.ContentListActivity;
import com.app.socialmediacontentmanager.database.DbOperations;
import com.app.socialmediacontentmanager.models.ContentType;
import java.util.List;

public class ContentTypeAdapter extends RecyclerView.Adapter<ContentTypeAdapter.ViewHolder> {

    private Context context;
    private List<ContentType> contentTypes;
    private DbOperations dbOperations;
    private String categoryName;

    public ContentTypeAdapter(Context context, List<ContentType> contentTypes, String categoryName) {
        this.context = context;
        this.contentTypes = contentTypes;
        this.dbOperations = new DbOperations(context);
        this.categoryName = categoryName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_content_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContentType contentType = contentTypes.get(position);

        holder.contentTypeName.setText(contentType.getName());

        // Get count of items for this content type
        int itemsCount = dbOperations.getContentItemsCountByType(contentType.getId());
        holder.itemsCount.setText(itemsCount + " ideas");

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContentListActivity.class);
            intent.putExtra("CONTENT_TYPE_ID", contentType.getId());
            intent.putExtra("CONTENT_TYPE_NAME", contentType.getName());
            intent.putExtra("CATEGORY_NAME", categoryName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contentTypes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView contentTypeName;
        TextView itemsCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            contentTypeName = itemView.findViewById(R.id.contentTypeName);
            itemsCount = itemView.findViewById(R.id.itemsCount);
        }
    }
}