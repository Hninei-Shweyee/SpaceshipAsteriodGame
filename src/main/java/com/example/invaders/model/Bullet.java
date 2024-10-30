package com.example.invaders.model;

import com.example.invaders.SpaceInvaderApp;
import javafx.scene.image.Image;
import static com.example.invaders.controller.playerController.PLAYER_SHOOT_COOLDOWN;
import static com.example.invaders.controller.playerController.lastPlayerShotTime;

public class Bullet extends Sprite {
    private final double velocityX;
    private final double velocityY;

    // Bullet images
    private static final Image ENEMY_BULLET_IMAGE = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/fireball.png"));
    private static final Image NORMAL_BULLET_IMAGE = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/fireball.png"));
    private static final Image SPECIAL_BULLET_IMAGE = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/bomb.png"));

    // Constants
    private static final double BULLET_SPEED = 5; // Base speed for bullets

    /* Constructs a Bullet with specified position, image, type, angle, and speed. */
    public Bullet(int x, int y, Image image, String type, double angle, double speed) {
        super(x, y, image, type);
        this.velocityX = speed * Math.cos(Math.toRadians(angle-90)); // Ensure angle is in radians
        this.velocityY = speed * Math.sin(Math.toRadians(angle-90)); // Ensure angle is in radians
    }



    /* Updates bullet position based on its velocity. */
    public void update() {
        // Move the bullet according to its calculated velocity
        setTranslateX(getTranslateX() + velocityX);
        setTranslateY(getTranslateY() + velocityY);
    }

    /* Handles shooting a normal bullet from the player's position and rotation. */
    public static void shoot(Sprite shooter) {
        if (shooter.type.equals("player") && isCooldownOver()) {
            double angle = shooter.getRotate(); // Use shooter's current rotation angle
            Bullet bullet = new Bullet(
                    (int) shooter.getTranslateX(),
                    (int) shooter.getTranslateY(),
                    NORMAL_BULLET_IMAGE, // Use the normal bullet image
                    shooter.type + "bullet",
                    (angle),
                    BULLET_SPEED // Use the constant bullet speed
            );
            SpaceInvaderApp.getRoot().getChildren().add(bullet);
            lastPlayerShotTime = System.nanoTime(); // Update the last shot time
        }
    }

    // Handles shooting a special bullet from the player's position and rotation.
    public static void shootSpecial(Sprite shooter) {
        if (shooter.type.equals("player") && isCooldownOver()) {
            double angle = shooter.getRotate(); // Use shooter's current rotation angle
            Bullet bullet = new Bullet(
                    (int) shooter.getTranslateX(),
                    (int) shooter.getTranslateY(),
                    SPECIAL_BULLET_IMAGE, // Use the special bullet image
                    shooter.type + "specialBullet",
                    (angle),
                    BULLET_SPEED // Use the constant bullet speed
            );
            SpaceInvaderApp.getRoot().getChildren().add(bullet);
            lastPlayerShotTime = System.nanoTime(); // Update the last shot time
        }
    }
    /* Checks if the shooting cooldown period has passed. */
    private static boolean isCooldownOver() {
        return System.nanoTime() - lastPlayerShotTime >= PLAYER_SHOOT_COOLDOWN;
    }
}
