package com.example.rma_2_anis_karic;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileViewManager {
    private final FrameLayout grid;
    private final Context context;
    private final int tileSize;
    private final int tileMargin;
    private final HashMap<Long, View> activeViews = new HashMap<>();

    public TileViewManager(FrameLayout grid, Context context, int tileSize, int tileMargin) {
        this.grid = grid;
        this.context = context;
        this.tileSize = tileSize;
        this.tileMargin = tileMargin;
    }

    public void updateGrid(HashMap<Long, Tile> newState){
        List<Tile> newTiles = new ArrayList<>();
        List<Tile> emptyTiles = new ArrayList<>();

        for (Long id : activeViews.keySet())
            if (!newState.containsKey(id)){
                View view = activeViews.get(id);
                grid.removeView(view);
            }

        activeViews.keySet().retainAll(newState.keySet());

        for (Tile tile : newState.values()){
            View view = activeViews.get(tile.getId());

            Log.d("tvman", String.valueOf(view));
            if (view == null){
                newTiles.add(tile);
            } else if (tile.getValue() == 0) {
                emptyTiles.add(tile);
            } else {
                updateTileView(view,tile);
                animateMove(view,tile);
            }
        }

        for (Tile tile : newTiles){
            View view = createTileView(tile);
            grid.addView(view);
            activeViews.put(tile.getId(),view);
            if (tile.getValue() > 0) animateSpawn(view);
        }

        for (Tile tile : emptyTiles){
            View view = activeViews.get(tile.getId());
            grid.removeView(view);
            activeViews.remove(tile.getId());
            view = createTileView(tile);
            grid.addView(view);
        }

        newTiles.clear();
        emptyTiles.clear();
    }

    private View createTileView(Tile tile) {
        View view = LayoutInflater.from(context).inflate(R.layout.tile, grid, false);
        updateTileView(view, tile);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(tileSize, tileSize);
        view.setLayoutParams(layoutParams);
        view.setX(tile.getCol() * (tileSize + tileMargin));
        view.setY(tile.getRow() * (tileSize + tileMargin));
        view.setTag(tile.getId());

        if (tile.getValue() != 0) view.setElevation(1f);

        return view;
    }
    private void updateTileView(View view, Tile tile) {
        CardView card = view.findViewById(R.id.tileCard);
        TextView text = view.findViewById(R.id.tileText);

        if (tile.getValue() == 0)
            text.setText("");
        else
            text.setText(String.valueOf(tile.getValue()));

        card.setCardBackgroundColor(getTileColor(tile.getValue(), context));
    }
    private int getTileColor(int tileValue, Context context) {
        int colorResId;
        switch (tileValue) {
            case 0:
                colorResId = R.color.white;
                break;
            case 2:
                colorResId = R.color.purple;
                break;
            case 4:
                colorResId = R.color.bluedark;
                break;
            case 8:
                colorResId = R.color.blue;
                break;
            case 16:
                colorResId = R.color.teal;
                break;
            case 32:
                colorResId = R.color.green;
                break;
            case 64:
                colorResId = R.color.lime;
                break;
            case 128:
                colorResId = R.color.piss;
                break;
            case 256:
                colorResId = R.color.yellow;
                break;
            case 512:
                colorResId = R.color.orange;
                break;
            case 1024:
                colorResId = R.color.pink;
                break;
            case 2048:
                colorResId = R.color.red;
                break;
            default:
                colorResId = R.color.white;
        }
        return ContextCompat.getColor(context, colorResId);
    }

    private void animateSpawn(View view){
        view.setScaleX(0f);
        view.setScaleY(0f);
        view.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
    }
    private void animateMove(View view, Tile tile){
        float targetX = tile.getCol() * (tileSize + tileMargin);
        float targetY = tile.getRow() * (tileSize + tileMargin);

        view.setElevation(2f);

        view.animate().translationX(targetX).translationY(targetY).setDuration(150).start();
    }
}
