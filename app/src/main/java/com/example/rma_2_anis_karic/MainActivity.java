package com.example.rma_2_anis_karic;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView scoreDisplay;
    private TextView hiScoreDisplay;
    private Button undoButton;
    private Button resetButton;
    private Button settingsButton;
    private GridLayout viewGrid;

    private Manager manager;

    private List<List<TextView>> gridTiles;

    private static final String TAG = "grid/gridTiles";


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
        manager = new ViewModelProvider(this).get(Manager.class);

        manager.getGrid().observe(this, new Observer<List<List<Integer>>>() {
            @Override
            public void onChanged(List<List<Integer>> grid) {
                initBoard(grid);
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
    }

    private void setUpViewReferences(){
        scoreDisplay = (TextView) findViewById(R.id.score);
        hiScoreDisplay = (TextView) findViewById(R.id.hiscore);
        undoButton = (Button) findViewById(R.id.undo);
        resetButton = (Button) findViewById(R.id.reset);
        settingsButton = (Button) findViewById(R.id.settings);
        viewGrid = (GridLayout) findViewById(R.id.grid);

    }

    private void initBoard(List<List<Integer>> grid){
        gridTiles = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(this);

        int dimension = grid.size();

        //in each row
        for (int row = 0; row < dimension; row++) {

            //initialize a row for the gridTiles
            //gridTiles saves references to TextViews in tiles that are about to be created
            List<TextView> gridTilesRow = new ArrayList<>();
            gridTiles.add(gridTilesRow);

            //for each element/column
            for (int col = 0; col < dimension; col++) {

                //inflate a new tile
                View newTile = inflater.inflate(R.layout.tile,viewGrid,false);
                //and take a reference to its TextView
                TextView tileText = newTile.findViewById(R.id.tileText);

                //assign value and color to new tile
                if (grid.get(row).get(col) != 0) {
                    tileText.setText(String.valueOf(grid.get(row).get(col)));
                    setTileColor(tileText,grid.get(row).get(col));
                }
                else {
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
                int margin = (int)(dpValue * d); // margin in pixels
                parameters.setMargins(margin,margin,margin,margin);

                //apply position and margins
                newTile.setLayoutParams(parameters);

                //add the new tile to the grid
                viewGrid.addView(newTile);
                //add the reference to its TextView to gridTiles (in the new row)
                gridTilesRow.add(tileText);
            }
        }
    }

    private void updateGrid(List<List<Integer>> grid){

        if(gridTiles == null || gridTiles.size() != grid.size() || gridTiles.get(0).size() != grid.get(0).size()){
            if (gridTiles == null) Log.e(TAG,"gridTiles is empty.");

            Log.d(TAG,"gridTiles.size() : " + String.valueOf(gridTiles.size()) + "  ,   grid.size() : " + grid.size());
            Log.d(TAG,"gridTiles.size() : " + String.valueOf(gridTiles.get(0).size()) + "  ,   grid.size() : " + grid.get(0).size());

            initBoard(grid);
            return;
        }

        //in each row
        for (int row = 0; row < grid.size(); row++) {
            //for each element/column
            for (int col = 0; col < grid.get(0).size(); col++) {
                int tileValue = grid.get(row).get(col);
                TextView tileText = gridTiles.get(row).get(col);

                if (tileValue == 0)
                    tileText.setText("");
                else {
                    tileText.setText(String.valueOf(tileValue));
                }

                setTileColor(tileText,tileValue);

            }
        }
    }

    private void setTileColor(TextView tileText, int tileValue){
        switch (tileValue){
            case 0: tileText.setBackgroundColor(Color.WHITE);
            break;
            case 2: tileText.setBackgroundColor(Color.parseColor("#0288D1"));
            break;
        }
    }
}