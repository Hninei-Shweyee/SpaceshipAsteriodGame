package com.example.invaders.view;

import com.example.invaders.SpaceInvaderApp;
import com.example.invaders.model.Boss;
import com.example.invaders.model.Enemy;
import com.example.invaders.model.Player;
import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.WritableImage;
import java.util.Random;
public class GamePlatform {
    ArrayList<Enemy> enemies = new ArrayList<>();
    public static Boss boss;

    public Region addingBackgroundImage() {
        // Add the background to the root pane
        Region background = new Region();
        Image img = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/mainBackground.jpg"));
        BackgroundImage backgroundImg = new BackgroundImage(new ImagePattern(img).getImage(),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, true, true));
        background.setBackground(new Background(backgroundImg));
        background.setPrefSize(600, 650);  // Set the size to match your scene size
        return background;
    }

    public Player addingPlayer(Pane pane, Image selectedImg) {
        if (selectedImg != null) {
            Player player = new Player(300, 480, selectedImg, "player");
            pane.getChildren().add(player);
            return player;
        } else {//Adding player
            Image playerImage = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/player3.png"));
            Player player = new Player(300, 480, playerImage, "player");
            pane.getChildren().add(player);
            return player;
        }

    }

    public List<Enemy> addingEnemies(Pane pane) {
        // Load the asteroid sprite sheet image
        Image asteroidImageSheet = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/asteriod-removebg-preview.png"));

        // Slicing the image, assuming 5 columns and 2 rows
        int columns = 5;
        int rows = 2;
        int frameWidth = 70;
        int frameHeight = 70;

        List<Image> frames = sliceImage(asteroidImageSheet, columns, rows, frameWidth, frameHeight);

        Random random = new Random();

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                int paneWidth = (int) pane.getWidth() > 0 ? (int) pane.getWidth() : 600;  // Default width if pane width is 0
                int paneHeight = (int) pane.getHeight() > 0 ? (int) pane.getHeight() : 650; // Default height if pane height is 0

                // Set start position within bounds
                int startX = random.nextInt(paneWidth - frameWidth);
                int startY = random.nextInt(paneHeight - frameHeight);

                Enemy enemy;
                if (frames.size() > 1) {
                    enemy = new Enemy(startX, startY, frames.toArray(new Image[0]), "enemy");
                } else {
                    enemy = new Enemy(startX, startY, frames.get(0), "enemy");
                }
                pane.getChildren().add(enemy);
                enemies.add(enemy);

                RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), enemy);
                rotateTransition.setByAngle(360);
                rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
                rotateTransition.play();

                // Restrict translate transition to keep the asteroid within the pane
                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(3 + random.nextInt(3)), enemy);

                // Randomize movement but ensure it stays within bounds
                int moveX = random.nextInt(paneWidth / 2) - paneWidth / 4;
                int moveY = random.nextInt(paneHeight / 2) - paneHeight / 4;

                // Ensure the asteroid stays within bounds of the pane
                translateTransition.setByX(Math.min(moveX, paneWidth - startX - frameWidth));
                translateTransition.setByY(Math.min(moveY, paneHeight - startY - frameHeight));

                translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
                translateTransition.setAutoReverse(true);
                translateTransition.play();
            }
        }
        return enemies;
    }


    private List<Image> sliceImage(Image image, int columns, int rows, int frameWidth, int frameHeight) {
        List<Image> frames = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int x = col * frameWidth;
                int y = row * frameHeight;
                WritableImage frame = new WritableImage(image.getPixelReader(), x, y, frameWidth, frameHeight);
                frames.add(frame);
            }
        }
        return frames;
    }

    public static Timeline BossSpawning(Pane pane) {
        Image bossImg = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/boss.png"));
        boss = new Boss(240, (int) -bossImg.getHeight(), bossImg, "Boss"); // Set the initial Y position to be above the visible area
        pane.getChildren().add(boss);
        Duration duration = Duration.seconds(3);
        int endY = 100;
        KeyValue keyValue = new KeyValue(boss.translateYProperty(), endY);
        KeyFrame keyFrame = new KeyFrame(duration, keyValue);
        Timeline timeline = new Timeline(keyFrame);

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        return timeline;
    }


    public List<Enemy> getEnemies() {
        return this.enemies;
    }



    public void removeBoss(Pane root){
        boss.setDead(true);
        root.getChildren().remove(boss);



    }
}