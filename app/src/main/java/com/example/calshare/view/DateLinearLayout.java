package com.example.calshare.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import org.joda.time.LocalDate;

/**
 * Created by silkyoak on 12/11/15.
 */
public class DateLinearLayout extends LinearLayout {

    public DateLinearLayout(Context context) {
        super(context);
    }

    public DateLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateLinearLayout(Context context, AttributeSet attrs, int defStyle) {
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
