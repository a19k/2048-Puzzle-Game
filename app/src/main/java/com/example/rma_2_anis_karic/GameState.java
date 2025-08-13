package com.example.rma_2_anis_karic;

import java.util.List;

public class GameState {
    private List<List<Tile>> currentState;
    private List<List<Tile>> previousState;
    private int currentScore;
    private int previousScore;
    private int currentHiscore;
    private int previousHiscore;

    public GameState(List<List<Tile>> currentState, List<List<Tile>> previousState, int currentScore, int previousScore, int currentHiscore, int previousHiscore) {
        this.currentState = currentState;
        this.previousState = previousState;
        this.currentScore = currentScore;
        this.previousScore = previousScore;
        this.currentHiscore = currentHiscore;
        this.previousHiscore = previousHiscore;
    }

    public List<List<Tile>> getCurrentState() {
        return currentState;
    }

    public void setCurrentState(List<List<Tile>> currentState) {
        this.currentState = currentState;
    }

    public List<List<Tile>> getPreviousState() {
        return previousState;
    }

    public void setPreviousState(List<List<Tile>> previousState) {
        this.previousState = previousState;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getPreviousScore() {
        return previousScore;
    }

    public void setPreviousScore(int previousScore) {
        this.previousScore = previousScore;
    }

    public int getCurrentHiscore() {
        return currentHiscore;
    }

    public void setCurrentHiscore(int currentHiscore) {
        this.currentHiscore = currentHiscore;
    }

    public int getPreviousHiscore() {
        return previousHiscore;
    }

    public void setPreviousHiscore(int previousHiscore) {
        this.previousHiscore = previousHiscore;
    }

}
