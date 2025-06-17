package com.example.newsapp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsService {
    private DatabaseReference newsRef;
    private List<NewsItem> newsList;
    private NewsUpdateListener listener;

    public interface NewsUpdateListener {
        void onNewsUpdated(List<NewsItem> newsList);

        void onError(String error);
    }

    public NewsService() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        newsRef = database.getReference("News");
        newsList = new ArrayList<>();
    }

    public void setNewsUpdateListener(NewsUpdateListener listener) {
        this.listener = listener;
    }

    public void startListening() {
        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NewsItem newsItem = snapshot.getValue(NewsItem.class);
                    if (newsItem != null) {
                        newsList.add(newsItem);
                    }
                }
                if (listener != null) {
                    listener.onNewsUpdated(newsList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listener != null) {
                    listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void stopListening() {
        newsRef.removeEventListener((ValueEventListener) this);
    }
}