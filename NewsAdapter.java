package com.example.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<NewsItem> newsList;
    private RequestManager glide;

    public NewsAdapter(List<NewsItem> newsList, RequestManager glide) {
        this.newsList = newsList;
        this.glide = glide;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsList.get(position);

        // Set title with fallback
        String title = newsItem.getTitle();
        holder.titleTextView.setText(title != null ? title : "");

        // Set description with fallback
        String description = newsItem.getDesc();
        holder.descriptionTextView.setText(description != null ? description : "");

        // Load image using Glide with error handling
        String imageUrl = newsItem.getImagelink();
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            glide.load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(holder.newsImageView);
        } else {
            holder.newsImageView.setImageResource(R.drawable.placeholder);
        }

        // Set click listener to open news in browser
        String newsUrl = newsItem.getNewslink();
        if (newsUrl != null && !newsUrl.trim().isEmpty()) {
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl));
                v.getContext().startActivity(intent);
            });
        } else {
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImageView;
        TextView titleTextView;
        TextView descriptionTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImageView = itemView.findViewById(R.id.newsImage);
            titleTextView = itemView.findViewById(R.id.newsTitle);
            descriptionTextView = itemView.findViewById(R.id.newsDescription);
        }
    }
}