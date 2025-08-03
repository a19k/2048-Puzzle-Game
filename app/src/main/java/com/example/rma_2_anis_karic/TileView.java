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
        init(context);
    }
    public TileView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.tile,this,true);
        tileCard = findViewById(R.id.tileCard);
        tileText = findViewById(R.id.tileText);
    }

    public void setValue(int value, Context context){
        if (value == 0)
            tileText.setText("");
        else
            tileText.setText(String.valueOf(value));

        tileCard.setCardBackgroundColor(getTileColor(value, context));
    }

    public static int getTileColor(int tileValue, Context context) {
        int colorResId;
        switch (tileValue) {
            case 0:    colorResId = R.color.white; break;
            case 2:    colorResId = R.color.purple; break;
            case 4:    colorResId = R.color.bluedark; break;
            case 8:    colorResId = R.color.blue; break;
            case 16:   colorResId = R.color.teal; break;
            case 32:   colorResId = R.color.green; break;
            case 64:   colorResId = R.color.lime; break;
            case 128:  colorResId = R.color.piss; break;
            case 256:  colorResId = R.color.yellow; break;
            case 512:  colorResId = R.color.orange; break;
            case 1024: colorResId = R.color.pink; break;
            case 2048: colorResId = R.color.red; break;
            default:   colorResId = R.color.white;
        }
        return ContextCompat.getColor(context, colorResId);
    }

    @Override
    public String toString() {
        return "TileView{ Value :" + tileText.getText() + '}';
    }
}
