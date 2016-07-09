package com.shopfitt.android.Utils;

import android.content.Context;
import android.graphics.Typeface;

public class Font {
    public static final String ROOT = "",
            FONT_OPEN_SANS = ROOT + "OpenSans-Regular.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}
