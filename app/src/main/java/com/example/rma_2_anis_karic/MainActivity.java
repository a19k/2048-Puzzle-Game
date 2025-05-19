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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //VIEW ELEMENTS
    private TextView scoreDisplay;
    private TextView hiScoreDisplay;
    private Button undoButton;
    private Button resetButton;
    private Button settingsButton;
    private RecyclerView recyclerGrid;

    //UI LOGIC
    private Manager manager;
    private TileAdapter tileAdapter;
    private GestureDetector gestureDetector;

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

        recyclerGrid.setLayoutManager(new GridLayoutManager(this,4));

        tileAdapter = new TileAdapter();
        recyclerGrid.setAdapter(tileAdapter);

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
        manager.getTiles().observe(this, new Observer<List<Tile>>() {
            @Override
            public void onChanged(List<Tile> tiles) {
                Log.d("MainActivity", "Submitting list to adapter. Size: " + (tiles == null ? "null" : tiles.size()));
                tileAdapter.submitList(tiles);
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
        recyclerGrid.setOnTouchListener(new View.OnTouchListener() {
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
        recyclerGrid = (RecyclerView) findViewById(R.id.recyclerGrid);

    }


    //SETUP / UPDATE


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