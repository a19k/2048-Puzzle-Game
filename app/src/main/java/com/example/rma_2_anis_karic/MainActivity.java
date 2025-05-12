package com.example.rma_2_anis_karic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
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

public class MainActivity extends AppCompatActivity {

    private TextView scoreDisplay;
    private TextView hiScoreDisplay;
    private Button undoButton;
    private Button resetButton;
    private Button settingsButton;
    private GridLayout viewGrid;

    private final TextView [][] gridTiles = new TextView[4][4];

    private final Board board = new Board(4);
    private LayoutInflater inflater;


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

        inflater = LayoutInflater.from(this);

        setUpViewReferences();
        board.addNewTiles();
        initBoard();


    }

    private void setUpViewReferences(){
        scoreDisplay = (TextView) findViewById(R.id.score);
        hiScoreDisplay = (TextView) findViewById(R.id.hiscore);
        undoButton = (Button) findViewById(R.id.undo);
        resetButton = (Button) findViewById(R.id.reset);
        settingsButton = (Button) findViewById(R.id.settings);
        viewGrid = (GridLayout) findViewById(R.id.grid);
    }

    private void initBoard(){
        int dimension = board.getDimension();
        for (int row = 0; row < dimension; row++)
            for (int col = 0; col < dimension; col++) {
                View newTile = inflater.inflate(R.layout.tile,viewGrid,false);
                TextView tileText = newTile.findViewById(R.id.tileText);

                if (board.getGrid()[row][col] != 0) {
                    tileText.setText(String.valueOf(board.getGrid()[row][col]));
                    tileText.setBackgroundColor(Color.parseColor("#0288D1"));
                }
                else {
                    tileText.setText("");
                    tileText.setBackgroundColor(Color.WHITE);
                }

                GridLayout.LayoutParams parameters = new GridLayout.LayoutParams();
                parameters.rowSpec = GridLayout.spec(row);
                parameters.columnSpec = GridLayout.spec(col);

                int dpValue = 5; // margin in dips
                float d = this.getResources().getDisplayMetrics().density;
                int margin = (int)(dpValue * d); // margin in pixels
                parameters.setMargins(margin,margin,margin,margin);

                newTile.setLayoutParams(parameters);

                viewGrid.addView(newTile);
                gridTiles[row][col] = tileText;
            }
    }

}