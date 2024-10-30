package com.example.invaders.controller;

import com.example.invaders.exception.exceptionHandle;
import com.example.invaders.model.Bullet;
import com.example.invaders.model.Player;

import java.util.ArrayList;

import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.example.invaders.SpaceInvaderApp.*;

public class playerController {
    static Player player;
    static ArrayList<Integer> previousScore = new ArrayList<>();
    public static boolean isMoveLeft = false;
    public static boolean isMoveRight = false;
    public static boolean isMoveUp = false;
    public static boolean isMoveDown = false;
    public static long lastPlayerShotTime = 0;

    public static final long PLAYER_SHOOT_COOLDOWN = 500_000_000; // 0.5 seconds (adjust as needed)
    static Logger logger = LogManager.getLogger(playerController.class);
    static exceptionHandle exception = new exceptionHandle();

    public playerController(Player player) {
        this.player = player;
    }

    // Move the player left
    public void moveLeft() {
        isMoveLeft = true;
        isMoveRight = false;
        isMoveUp = false;
        isMoveDown = false;

        if (isMoveLeft) {
            player.setTranslateX(player.getTranslateX() - 5);
        }
    }

    // Move the player right
    public void moveRight() {
        isMoveRight = true;
        isMoveLeft = false;
        isMoveUp = false;
        isMoveDown = false;

        if (isMoveRight) {
            player.setTranslateX(player.getTranslateX() + 5);
        }
    }

    // Move the player up
    public void moveUp() {
        isMoveUp = true;
        isMoveLeft = false;
        isMoveRight = false;
        isMoveDown = false;

        if (isMoveUp) {
            player.setTranslateY(player.getTranslateY() - 5);
        }
    }

    // Move the player down
    public void moveDown() {
        isMoveDown = true;
        isMoveLeft = false;
        isMoveRight = false;
        isMoveUp = false;

        if (isMoveDown) {
            player.setTranslateY(player.getTranslateY() + 5);
        }
    }

    // Rotate the player 360 degrees
    // Rotate the player clockwise
    public void rotateClockwise() {
        player.setRotate((player.getRotate() + 10) % 360); // Increase the angle by 10 degrees
    }

    // Rotate the player anticlockwise
    public void rotateAnticlockwise() {
        player.setRotate((player.getRotate() - 10 + 360) % 360); // Decrease the angle by 10 degrees, ensuring it's within 0-360
    }

    // Check if the player has collided with the screen boundaries
    public static Boolean checkCollision() {
        if (player.getTranslateX() >= 560) {
            player.setTranslateX(560);
            exception.showTalkingDialog("I've hit \n the right wall", player, exceptionPane, "right", Duration.millis(600));
            logger.debug("Dialog textbox is shown");
            isMoveRight = false;
            return false;
        } else if (player.getTranslateX() <= 0) {
            player.setTranslateX(0);
            exception.showTalkingDialog("I've hit \n the left wall", player, exceptionPane, "left", Duration.millis(600));
           logger.debug("Dialog textbox is shown");
            isMoveLeft = false;
            return false;
        }

        // Add checks for top and bottom boundaries
        if (player.getTranslateY() >= 580) {
            player.setTranslateY(580);
            exception.showTalkingDialog("I've hit \n the bottom wall", player, exceptionPane, "bottom", Duration.millis(600));
            logger.debug("Dialog textbox is shown");
            isMoveDown = false;
            return false;
        } else if (player.getTranslateY() <= 0) {
            player.setTranslateY(0);
            exception.showTalkingDialog("I've hit \n the top wall", player, exceptionPane, "top", Duration.millis(600));
            logger.debug("Dialog textbox is shown");
            isMoveUp = false;
            return false;
        }
        return true;
    }

    // Player shoots a normal bullet
    public void shoot() {
        Bullet.shoot(player);
    }

    // Player shoots a special bullet
    public void shootSpecial() {
        Bullet.shootSpecial(player);
    }

    // Respawn the player
    public static void respawn() {
        root.getChildren().remove(player);
        player.setTranslateX(300);
        player.setTranslateY(580);
        player.setDead(false);

        if (player.getCurrentChance() <= 3) {
            player.setChances(3 - player.getCurrentChance());
            player.setCurrentChance(player.getCurrentChance() + 1);
        }

        if (player.getHealth() >= 0) {
            player.setHealth(100 / player.getCurrentChance());
        }
        isMoveLeft = false;
        isMoveRight = false;
        isMoveUp = false;
        isMoveDown = false;

        root.getChildren().add(player);
        logger.warn("Player respawned!");
    }

    // Show the previous score
    public static int ShowPreviousScore() {
        return previousScore.get(previousScore.size() - 2);
    }
}
