package com.example.rma_2_anis_karic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class TileView extends FrameLayout { // View object representing the tile.xml layout
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
