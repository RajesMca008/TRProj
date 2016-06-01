package com.tgs.tecipe.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

public class MyLatoRegularText extends TextView {

    public MyLatoRegularText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyLatoRegularText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyLatoRegularText(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"Lato-Regular.ttf");
        setTypeface(tf);
    }

}
