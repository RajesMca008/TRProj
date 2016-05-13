package com.tgs.tecipe.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

public class MyTextUnique extends TextView {

    public MyTextUnique(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextUnique(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextUnique(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"Roboto-Light.ttf");
        setTypeface(tf);
    }

}
