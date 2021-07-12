package com.example.myapplicationpln.fragment;

import android.view.MotionEvent;

public interface IOnFocusListenable {
    public void onWindowFocusChanged(boolean hasFocus);

    boolean onTouchEvent(MotionEvent event);
}
