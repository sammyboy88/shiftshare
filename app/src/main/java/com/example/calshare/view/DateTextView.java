package com.example.calshare.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import org.joda.time.LocalDate;

/**
 * Created by root on 4/10/15.
 */
public class DateTextView extends TextView {

    public DateTextView(Context context) {
        super(context);
    }

    public DateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    private LocalDate localDate;
}
