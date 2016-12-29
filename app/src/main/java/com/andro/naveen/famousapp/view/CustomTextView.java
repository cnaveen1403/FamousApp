package com.andro.naveen.famousapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.andro.naveen.famousapp.myapplication.MyApplication;

/**
 * Created by Lenovo on 4/4/2016.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
        setFont();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    private void setFont() {
        setTypeface(MyApplication.Fonts.FontIcon);
    }
}
