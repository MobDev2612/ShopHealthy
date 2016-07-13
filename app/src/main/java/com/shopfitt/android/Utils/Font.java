package com.shopfitt.android.Utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public class Font {
    public static final String ROOT = "",
            FONT_OPEN_SANS = ROOT + "OpenSans-Regular.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                FONT_OPEN_SANS);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
