package com.shopfitt.android.Utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Hari Haran on 26-Jan-16.
 */
public class Font {
    public static final String ROOT = "",
            FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}
