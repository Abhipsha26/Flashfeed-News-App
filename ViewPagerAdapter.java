package com.example.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewPagerAdapter extends PagerAdapter {
    // Existing variables
    List<SliderItems> sliderItems;
    LayoutInflater mLayoutInflater;
    Context context;
    ArrayList<String> title;
    ArrayList<String> newslink;
    ArrayList<String> desc;
    ArrayList<String> head;
    VerticalViewerPage verticalViewerPage;
    int newposition;
    float x1, x2;

    // New TTS variables
    private TextToSpeech tts;
    private boolean isTTSReady = false;

    public ViewPagerAdapter(Context context, List<SliderItems> sliderItems, ArrayList<String> title, ArrayList<String> desc,
                            ArrayList<String> newslink, ArrayList<String> head, VerticalViewerPage verticalViewerPage) {
        // Existing initialization
        this.context = context;
        this.sliderItems = sliderItems;
        this.desc = desc;
        this.title = title;
        this.head = head;
        this.newslink = newslink;
        this.verticalViewerPage = verticalViewerPage;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // New TTS initialization
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.getDefault());
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isTTSReady = true;
                }
            }
        });
    }

    // Existing methods remain unchanged below
    @Override
    public int getCount() {
        return sliderItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // Existing view setup
        View itemView = mLayoutInflater.inflate(R.layout.item_container, container, false);
        ImageView imageView = itemView.findViewById(R.id.imageView);
        ImageView imageView2 = itemView.findViewById(R.id.imageView2);
        TextView titles = itemView.findViewById(R.id.headline);
        TextView descrip = itemView.findViewById(R.id.desc);
        TextView heads = itemView.findViewById(R.id.head);

        // New TTS button implementation
        ImageButton ttsButton = itemView.findViewById(R.id.ttsButton);
        ttsButton.setOnClickListener(v -> {
            if (isTTSReady) {
                tts.stop();
                tts.speak(title.get(position), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });

        // Existing binding code continues unchanged
        titles.setText(title.get(position));
        descrip.setText(desc.get(position));
        heads.setText(head.get(position));

        Glide.with(context)
                .load(sliderItems.get(position).getImage())
                .centerCrop()
                .into(imageView);
        Glide.with(context)
                .load(sliderItems.get(position).getImage())
                .override(12, 12)
                .centerCrop()
                .into(imageView2);

        // Existing touch listeners and page change listeners remain unchanged
        verticalViewerPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                newposition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        verticalViewerPage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        if (deltaX > 300) {
                            Intent i = new Intent(context, NewsDetailActivity.class);
                            if (position == 1) {
                                i.putExtra("url", newslink.get(0));
                                context.startActivity(i);
                            } else {
                                i.putExtra("url", newslink.get(newposition));
                                context.startActivity(i);
                            }
                        }
                        break;
                }
                return false;
            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // Existing removal
        container.removeView((LinearLayout) object);

        // New TTS cleanup
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}