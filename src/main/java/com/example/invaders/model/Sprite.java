package com.example.invaders.model;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;import javafx.animation.AnimationTimer;

public  class Sprite extends ImageView {
    public boolean dead = false;

    public final String type;
    private int frameIndex = 0;
    private Image[] frames; // Array of frames for animated objects
    private long lastFrameTime;
    private boolean isAnimated = false;

    public Sprite(int x, int y, Image image,String type){
        super(image);
        this.type = type;
        setTranslateX(x);
        setTranslateY(y);
    }
    //second constructor
    public Sprite(int x, int y, Image[] frames,String type){
        this(x,y,frames[0],type);
        this.frames = frames;
        isAnimated = true;
        startAnimation();
    }

    private void startAnimation(){
        if (isAnimated) {
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (now - lastFrameTime > 200_000_000) {
                        frameIndex = (frameIndex + 1) % frames.length;
                        setImage(frames[frameIndex]);
                        lastFrameTime = now;
                    }
                }
            };
            timer.start();
        }
    }

    public void moveLeft(){
        setTranslateX(getTranslateX() - 5);
    }

    public void moveRight(){

        setTranslateX(getTranslateX() + 5);
    }

    public void moveUp(){
        setTranslateY(getTranslateY() - 5);
    }

    public void moveDown(){
        setTranslateY(getTranslateY() + 5);
    }

    public void moveForward(double speed) {
        double angleRad = Math.toRadians(getRotate());
        setTranslateX(getTranslateX() + Math.cos(angleRad) * speed);
        setTranslateY(getTranslateY() + Math.sin(angleRad) * speed);
    }

    public void setRotation(double angle){
        setRotate(angle);
    }

    public boolean isDead() {

        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public String getType() {
        return type;
    }


}

