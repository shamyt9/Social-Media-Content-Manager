package com.app.socialmediacontentmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.app.socialmediacontentmanager.R;
import com.app.socialmediacontentmanager.activities.CategoryActivity;
import com.app.socialmediacontentmanager.models.Category;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.categoryName.setText(category.getName());

        // Set platform icons (you'll need to add these drawables to your project)
        int iconResId = getIconResource(category.getName());
        holder.categoryIcon.setImageResource(iconResId);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryActivity.class);
            intent.putExtra("CATEGORY_ID", category.getId());
            intent.putExtra("CATEGORY_NAME", category.getName());
            context.startActivity(intent);
        });
    }

    private int getIconResource(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case "youtube": return R.drawable.ic_youtube;
            case "instagram": return R.drawable.ic_instagram;
            case "twitter": return R.drawable.ic_twitter;
            case "facebook": return R.drawable.ic_facebook;
            case "telegram": return R.drawable.ic_telegram;
            case "snapchat": return R.drawable.ic_snapchat;
            default: return R.drawable.ic_default;
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView categoryIcon;
        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}