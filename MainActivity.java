package com.example.newsapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    // now we create list of slider items(types)
    List<SliderItems> sliderItems = new ArrayList<>();

    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> newslink = new ArrayList<>();
    ArrayList<String> imagelink = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();
    ArrayList<String> head = new ArrayList<>();

    // database reference
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        final VerticalViewerPage verticalViewerPage = (VerticalViewerPage) findViewById(R.id.VerticalViewPager);

        mRef = FirebaseDatabase.getInstance().getReference("News");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseData", "DataSnapshot children count: " + snapshot.getChildrenCount());

                if (!snapshot.exists()) {
                    Log.e("FirebaseData", "No data found in Firebase!");
                    return;
                }

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d("FirebaseData", "Snapshot Key: " + ds.getKey());

                    String titleItem = ds.child("title").getValue(String.class);
                    String imagelinkItem = ds.child("imagelink").getValue(String.class);

                    Log.d("FirebaseData", "Title: " + titleItem + ", ImageLink: " + imagelinkItem);

                    title.add(titleItem);
                    newslink.add(ds.child("newslink").getValue(String.class));
                    imagelink.add(imagelinkItem);
                    desc.add(ds.child("desc").getValue(String.class));
                    head.add(ds.child("head").getValue(String.class));

                    sliderItems.add(new SliderItems(imagelinkItem));
                }

                Log.d("FirebaseData", "Final Items Loaded: " + sliderItems.size());

                verticalViewerPage.setAdapter(new ViewPagerAdapter(MainActivity.this, sliderItems, title, desc,
                        newslink, head, verticalViewerPage));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}