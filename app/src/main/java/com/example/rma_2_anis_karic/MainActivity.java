package com.example.rma_2_anis_karic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //VIEW ELEMENTS
    private TextView scoreDisplay;
    private TextView hiScoreDisplay;
    private Button undoButton;
    private Button resetButton;
    private RecyclerView recyclerGrid;

    private int primaryColor;
    private int paleColor;
    private int maxTileOnBoard;

    private Button gameTitle;
    private TextView scoreLabel;
    private TextView hiScoreLabel;
    private View divider;

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

        setPrimaryColor(getColor(R.color.purple));
        setPaleColor(getColor(R.color.purple_pale));
        applyPrimaryColor();


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

                int currentMax = 0;
                for (Tile tile : tiles){
                    if (tile.getValue() > currentMax) currentMax = tile.getValue();
                }
                maxTileOnBoard = currentMax;

                switch (currentMax){
                    case 2:setPrimaryColor(getColor(R.color.purple));
                        setPaleColor(getColor(R.color.purple_pale));
                        break;
                    case 4:setPrimaryColor(getColor(R.color.bluedark));
                        setPaleColor(getColor(R.color.bluedark_pale));
                        break;
                    case 8:setPrimaryColor(getColor(R.color.blue));
                        setPaleColor(getColor(R.color.blue_pale));
                        break;
                    case 16:setPrimaryColor(getColor(R.color.teal));
                        setPaleColor(getColor(R.color.teal_pale));
                        break;
                    case 32:setPrimaryColor(getColor(R.color.green));
                        setPaleColor(getColor(R.color.green_pale));
                        break;
                    case 64:setPrimaryColor(getColor(R.color.lime));
                        setPaleColor(getColor(R.color.lime_pale));
                        break;
                    case 128:setPrimaryColor(getColor(R.color.piss));
                        setPaleColor(getColor(R.color.piss_pale));
                        break;
                    case 256:setPrimaryColor(getColor(R.color.yellow));
                        setPaleColor(getColor(R.color.yellow_pale));
                        break;
                    case 512:setPrimaryColor(getColor(R.color.orange));
                        setPaleColor(getColor(R.color.orange_pale));
                        break;
                    case 1024:setPrimaryColor(getColor(R.color.pink));
                        setPaleColor(getColor(R.color.pink_pale));
                        break;
                    case 2048:setPrimaryColor(getColor(R.color.red));
                        setPaleColor(getColor(R.color.red_pale));
                        break;
                    default:setPrimaryColor(getColor(R.color.text_dark));
                    setPaleColor(getColor(R.color.white));
                }
                applyPrimaryColor();
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
        recyclerGrid = (RecyclerView) findViewById(R.id.recyclerGrid);

        gameTitle = (Button) findViewById(R.id.gameTitle);
        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        hiScoreLabel = (TextView) findViewById(R.id.hiscoreLabel);
        divider = findViewById(R.id.divider);

        recyclerGrid.setLayoutManager(new GridLayoutManager(this,4));

        tileAdapter = new TileAdapter();
        recyclerGrid.setAdapter(tileAdapter);
    }


    //COLORING

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }
    public void setPaleColor(int paleColor) {
        this.paleColor = paleColor;
    }

    private void applyPrimaryColor(){
        gameTitle.setTextColor(primaryColor);
        gameTitle.setBackgroundColor(paleColor);
        divider.setBackgroundColor(primaryColor);
        scoreLabel.setTextColor(primaryColor);
        scoreDisplay.setTextColor(primaryColor);
        hiScoreLabel.setTextColor(primaryColor);
        hiScoreDisplay.setTextColor(primaryColor);
        recyclerGrid.setBackgroundColor(paleColor);
        undoButton.setBackgroundColor(primaryColor);
        resetButton.setBackgroundColor(primaryColor);
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