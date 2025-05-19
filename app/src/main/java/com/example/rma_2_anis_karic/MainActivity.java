package com.example.rma_2_anis_karic;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //VIEW ELEMENTS
    private TextView scoreDisplay;
    private TextView hiScoreDisplay;
    private Button undoButton;
    private Button resetButton;
    private Button settingsButton;
    private GridLayout viewGrid;

    //UI LOGIC
    private Manager manager;
    private GestureDetector gestureDetector;
    private List<List<TextView>> gridTiles;

    private static final String TAG = "grid/gridTiles";//DEBUG TAG


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        setUpViewReferences();

        //VIEWMODEL
        manager = new ViewModelProvider(this).get(Manager.class);

        //GESTURE DETECTOR
        gestureDetector = new GestureDetector(this, new SwipeListener() {
            @Override
            protected void onSwipeRight() {
                manager.swipeRightHandler();
            }
            @Override
            protected void onSwipeLeft() {
                manager.swipeLeftHandler();
            }
            @Override
            protected void onSwipeUp() {
                manager.swipeUpHandler();
            }
            @Override
            protected void onSwipeDown() {
                manager.swipeDownHandler();
            }
        });

        //LIVEDATA OBSERVERS
        manager.getGrid().observe(this, new Observer<List<List<Integer>>>() {
            @Override
            public void onChanged(List<List<Integer>> grid) {
                updateGrid(grid);
            }
        });
        manager.getScore().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer newValue) {
                scoreDisplay.setText(String.valueOf(newValue));
            }
        });
        manager.getHiScore().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer newValue) {
                hiScoreDisplay.setText(String.valueOf(newValue));
            }
        });


        //VIEW LISTENERS
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.newGame();
            }
        });
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.undo();
            }
        });
        viewGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    //SETUP
    private void setUpViewReferences() {
        scoreDisplay = (TextView) findViewById(R.id.score);
        hiScoreDisplay = (TextView) findViewById(R.id.hiscore);
        undoButton = (Button) findViewById(R.id.undo);
        resetButton = (Button) findViewById(R.id.reset);
        settingsButton = (Button) findViewById(R.id.settings);
        viewGrid = (GridLayout) findViewById(R.id.grid);

    }
    private void initBoard(List<List<Integer>> grid) {
        gridTiles = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(this);

        //in each row
        for (int row = 0; row < 4; row++) {

            //initialize a row for the gridTiles
            //gridTiles saves references to TextViews in tiles that are about to be created
            List<TextView> gridTilesRow = new ArrayList<>();
            gridTiles.add(gridTilesRow);

            //for each element/column
            for (int col = 0; col < 4; col++) {

                //inflate a new tile
                View newTile = inflater.inflate(R.layout.tile, viewGrid, false);
                //and take a reference to its TextView
                TextView tileText = newTile.findViewById(R.id.tileText);

                //assign value and color to new tile
                if (grid.get(row).get(col) != 0) {
                    tileText.setText(String.valueOf(grid.get(row).get(col)));
                    setTileColor(tileText, grid.get(row).get(col));
                } else {
                    tileText.setText("");
                    tileText.setBackgroundColor(Color.WHITE);
                }

                //specify its position on grid
                GridLayout.LayoutParams parameters = new GridLayout.LayoutParams();
                parameters.rowSpec = GridLayout.spec(row);
                parameters.columnSpec = GridLayout.spec(col);

                //set margins
                int dpValue = 5; // margin in dips
                float d = this.getResources().getDisplayMetrics().density;
                int margin = (int) (dpValue * d); // margin in pixels
                parameters.setMargins(margin, margin, margin, margin);

                //apply position and margins
                newTile.setLayoutParams(parameters);

                //add the new tile to the grid
                viewGrid.addView(newTile);
                //add the reference to its TextView to gridTiles (in the new row)
                gridTilesRow.add(tileText);
            }
        }
    }

    //SETUP / UPDATE
    private void updateGrid(List<List<Integer>> grid) {

        if (gridTiles == null || gridTiles.size() != grid.size() || gridTiles.get(0).size() != grid.get(0).size()) {
            initBoard(grid);
            return;
        }

        //in each row
        for (int row = 0; row < 4; row++) {
            //for each element/column
            for (int col = 0; col < 4; col++) {
                int tileValue = grid.get(row).get(col);
                TextView tileText = gridTiles.get(row).get(col);

                if (tileValue == 0)
                    tileText.setText("");
                else {
                    tileText.setText(String.valueOf(tileValue));
                }

                setTileColor(tileText, tileValue);
            }
        }
    }

    //HELPER
    private void setTileColor(TextView tileText, int tileValue) {
        switch (tileValue) {
            case 0:
                tileText.setBackgroundColor(Color.WHITE);
                break;
            case 2:
                tileText.setBackgroundColor(Color.parseColor("#388E3C"));
                break;
            case 4:
                tileText.setBackgroundColor(Color.parseColor("#00796B"));
                break;
            case 8:
                tileText.setBackgroundColor(Color.BLUE);
                break;
            case 16:
                tileText.setBackgroundColor(Color.CYAN);
                break;
            case 32:
                tileText.setBackgroundColor(Color.parseColor("#0288D1"));
                break;
            case 64:
                tileText.setBackgroundColor(Color.MAGENTA);
                break;
            case 128:
                tileText.setBackgroundColor(Color.LTGRAY);
                break;
            case 256:
                tileText.setBackgroundColor(Color.parseColor("#C2185B"));
                break;
            case 512:
                tileText.setBackgroundColor(Color.parseColor("#E64A19"));
                break;
            case 1024:
                tileText.setBackgroundColor(Color.YELLOW);
                break;
            case 2048:
                tileText.setBackgroundColor(Color.RED);
                break;
            default:
                tileText.setBackgroundColor(Color.BLACK);
                tileText.setTextColor(Color.RED);
        }
    }
    private List<List<Integer>> getState(){
        List<List<Integer>> state = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                String item = gridTiles.get(i).get(j).getText().toString();
                if (item.equals(""))
                    row.add(0);
                else
                    row.add(Integer.parseInt(item));
            }
            state.add(row);
        }
        return state;
    }

    //ANIMATION
    private void animateMove(TextView tile, float diffX, float diffY) {
        tile.animate().
                translationXBy(diffX).
                translationYBy(diffY).
                setDuration(150).
                start();

    }
    private void animateAppearance(TextView tile){
        tile.setScaleX(0f);
        tile.setScaleY(0f);
        tile.animate().
                scaleX(1f).
                scaleY(1f).
                setDuration(150).
                setInterpolator(new OvershootInterpolator()).
                start();
    }
    private void animateMerge(TextView tile){
        tile.animate().
                scaleX(1.2f).
                scaleY(1.2f).
                setDuration(150).
                withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        tile.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
                    }
                })
                .start();
    }

}