package com.example.newsapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class VerticalViewerPage extends ViewPager {
    public VerticalViewerPage(@NonNull Context context) {
        super(context);
    }

    public VerticalViewerPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(true,new VerticalPageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);

    }
    private class VerticalPageTransformer implements ViewPager.PageTransformer{

        @Override
        public void transformPage(@NonNull View page, float position) {
            // here we check the position
            if(position<-1){
                //[-infinty,-1]
                //if this page is waay off screen to its left
                page.setAlpha(0);
            }
            else if (position<=0){
                //[-1,0]
                //use default slide transition when moving to the left page
                page.setAlpha(1);
                //counteract the default transition
                page.setTranslationX(page.getWidth()*-position);
                // set y position to swipe infrom top
                float ypostion= position*page.getHeight();
                page.setTranslationY(ypostion);
                page.setScaleX(1);
                page.setScaleY(1);

            }
            else if(position<=1){
                //[0,1]
                //counteract the default slide transition
                page.setTranslationX(page.getWidth()*-position);

                //to scale the page down
                float scale= 0.75f+(1-0.75f)*(1-Math.abs(position));
                page.setScaleY(scale);
                page.setScaleX(scale);
            }
            else{
               // [1,infinity]
                // this page is way off screen to the right
                page.setAlpha(0);

            }

        }
    }
    private MotionEvent swapXYCoordinates(MotionEvent event){
        //now we will swap x and y coordinates
        float width=getWidth();
        float height=getHeight();

        float newX=(event.getY()/height)*width;
        float newY=(event.getX()/width)*height;
        event.setLocation(newX,newY);
        return event;


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted= super.onInterceptTouchEvent(swapXYCoordinates(ev));
        swapXYCoordinates(ev);
        //return touch coordinates to original referenace frame for any child value;
        return intercepted;


    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapXYCoordinates(ev));
    }
}
