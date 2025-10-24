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
import com.app.socialmediacontentmanager.activities.ViewContentActivity;
import com.app.socialmediacontentmanager.models.ContentItem;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ContentItemAdapter extends RecyclerView.Adapter<ContentItemAdapter.ViewHolder> {

    private Context context;
    private List<ContentItem> contentItems;
    private SimpleDateFormat dateFormat;

    // Add this interface
    public interface OnItemClickListener {
        void onItemClick(int position, ContentItem item);
    }

    private OnItemClickListener onItemClickListener;

    // Add this method
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }



    public ContentItemAdapter(Context context, List<ContentItem> contentItems) {
        this.context = context;
        this.contentItems = contentItems;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContentItem item = contentItems.get(position);

        holder.contentTitle.setText(item.getTitle());
        holder.contentDescription.setText(item.getDescription());
        holder.contentDate.setText(dateFormat.format(item.getLastUpdated()));

        if (item.getStatus() == 1) {
            holder.contentStatus.setText("Published");
            holder.contentStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.contentStatus.setText("Draft");
            holder.contentStatus.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        }
        holder.cardView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position, item);
            }
        });

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewContentActivity.class);
            intent.putExtra("CONTENT_ITEM_ID", item.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contentItems.size();
    }

    public void updateData(List<ContentItem> newItems) {
        contentItems.clear();
        contentItems.addAll(newItems);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView contentTitle;
        TextView contentDescription;
        TextView contentDate;
        TextView contentStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            contentTitle = itemView.findViewById(R.id.contentTitle);
            contentDescription = itemView.findViewById(R.id.contentDescription);
            contentDate = itemView.findViewById(R.id.contentDate);
            contentStatus = itemView.findViewById(R.id.contentStatus);
        }
    }
}