package com.example.rma_2_anis_karic;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class TileView extends FrameLayout {
    private CardView tileCard;
    private TextView tileText;

    public TileView(@NonNull Context context) {
        super(context);
    }
    public TileView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public String toString() {
        return "TileView{ Value :" + tileText.getText() + '}';
    }
}
