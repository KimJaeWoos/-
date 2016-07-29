package com.jwoos.android.sellbook.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Jwoo on 2016-06-13.
 */
public class RialTextView extends TextView {

    String rawText;

    public RialTextView(Context context) {
        super(context);

    }

    public RialTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RialTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        rawText = text.toString();
        String prezzo = text.toString();
        try {

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###" + "원", symbols);
            prezzo = decimalFormat.format(Integer.parseInt(text.toString()));
        }catch (Exception e){}

        super.setText(prezzo, type);
    }

    @Override
    public CharSequence getText() {

        return rawText;
    }
}