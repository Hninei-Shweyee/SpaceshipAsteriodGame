package com.example.invaders.model;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends Sprite{

    private ImageView imageView;
    private int health;
    int score;
    int  chances;
    int currentChance;
    double width;
    public Player(int x, int y, Image image, String type) {

        super(x, y, image, type);
        health = 100;
        chances=3;
        currentChance=1;
        score = 0;
        width = image.getWidth();
    }


    public int getChances() {
        return chances;
    }

    public void setChances(int chances) {
        this.chances = chances;
    }

    public int getCurrentChance() {
        return currentChance;
    }

    public void setCurrentChance(int currentChance) {
        this.currentChance = currentChance;
    }



    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }



    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void increaseScore(int points) {

        score+= points;

    }

    public double getWidth() {
        return width;
    }
}
