package com.shopfitt.android.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontView extends TextView{
    private static final String TAG = FontView.class.getSimpleName();
    //Cache the font load status to improve performance
    private static Typeface font;

    public FontView(Context context) {
        super(context);
        setFont(context);
    }

    public FontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public FontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context) {
        // prevent exception in Android Studio / ADT interface builder
        if (this.isInEditMode()) {
            return;
        }

        //Check for font is already loaded
        if(font == null) {
            try {
                font = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
                Logger.d(TAG, "Open Sans loaded");
            } catch (RuntimeException e) {
                Logger.e(TAG, "Open Sans not loaded",e);
            }
        }

        //Finally set the font
        setTypeface(font);
    }
}
