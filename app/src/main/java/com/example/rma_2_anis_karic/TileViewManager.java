package com.example.rma_2_anis_karic;

import android.content.Context;
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
    private final FrameLayout grid; //view container, passed from mainactivity
    private final Context context; // context for layout inflation, mainactivity
    private final int tileSize; // size of tile, calculated per device
    private final int tileMargin; // size of tile margin, calculated per device
    private final HashMap<Long, View> activeViews = new HashMap<>(); // active views on grid, for easier data manipulation

    public TileViewManager(FrameLayout grid, Context context, int tileSize, int tileMargin) {
        this.grid = grid;
        this.context = context;
        this.tileSize = tileSize;
        this.tileMargin = tileMargin;
    }

    //UPDATE CALL, once after every move
    public void updateGrid(HashMap<Long, Tile> newState){
        List<Tile> newTiles = new ArrayList<>(); //new tiles found in newState
        List<Tile> emptyTiles = new ArrayList<>();// zero value, empty tiles in newState

        //remove tiles not supposed to be on grid(UI)
        for (Long id : activeViews.keySet())
            if (!newState.containsKey(id)){
                View view = activeViews.get(id);
                grid.removeView(view);
            }

        //and remove them from activeViews
        activeViews.keySet().retainAll(newState.keySet());

                                    //classify tiles
        for (Tile tile : newState.values()){
            View view = activeViews.get(tile.getId());

            if (view == null){
                newTiles.add(tile);                 //postpone new tile rendering
            }
            else if (tile.getValue() == 0) {
                emptyTiles.add(tile);               //postpone empty tile rendering
            }
            else {                                  //non-empty older tiles get render and animation priority because they slide
                updateTileView(view,tile);
                animateMove(view,tile);
            }
        }

        //create and animate new tiles
        for (Tile tile : newTiles){
            View view = createTileView(tile);
            grid.addView(view);
            activeViews.put(tile.getId(),view);
            if (tile.getValue() > 0) animateSpawn(view);
        }

        //reposition old empty tiles, without animation (instantly)
        for (Tile tile : emptyTiles){
            View view = activeViews.get(tile.getId());
            grid.removeView(view);
            activeViews.remove(tile.getId());
            view = createTileView(tile);
            grid.addView(view);
            activeViews.put(tile.getId(),view);
            //here views are deleted and then recreated
            //when old view is reused, for some reason the layout is disrupted, causing the empty tiles to be offset to top-left
            //possibly because the FrameLayout(grid or container)' s padding is being ignored when resetting the x and y coordinates of the view
        }

        //sterilize the containers for next time
        newTiles.clear();
        emptyTiles.clear();
    }

    private View createTileView(Tile tile) {
        View view = LayoutInflater.from(context).inflate(R.layout.tile, grid, false);
        updateTileView(view, tile);

        //define view dimensions, position
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(tileSize, tileSize);
        view.setLayoutParams(layoutParams);
        view.setX(tile.getCol() * (tileSize + tileMargin));
        view.setY(tile.getRow() * (tileSize + tileMargin));
        view.setTag(tile.getId());

        //non-empty tiles should always be above
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

    //get color based on tile value
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
        //target position
        float targetX = tile.getCol() * (tileSize + tileMargin);
        float targetY = tile.getRow() * (tileSize + tileMargin);

        //move view to target position with temporary elevation so it stays above other tiles while moving
        view.animate().translationX(targetX).translationY(targetY).setDuration(150).withStartAction(new Runnable() {
            @Override
            public void run() {
                view.setElevation(2f);
            }
        }).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.setElevation(1f);
            }
        }).start();;
    }
}
