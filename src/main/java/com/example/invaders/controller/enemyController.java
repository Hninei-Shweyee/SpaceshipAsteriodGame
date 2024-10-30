package com.example.invaders.controller;

import com.example.invaders.model.Boss;
import com.example.invaders.model.Enemy;
import javafx.animation.*;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.example.invaders.SpaceInvaderApp.bossAnimationTimer;

import java.util.List;
import java.util.Random;

import static com.example.invaders.view.GamePlatform.boss;

public class enemyController {
    static Logger logger = LogManager.getLogger(enemyController.class);

    public static void rotateEnemies(List<Enemy> enemies) {
        Duration rotationDuration = Duration.seconds(2);
        ParallelTransition parallelTransition = new ParallelTransition();

        for (Enemy enemy : enemies) {
            RotateTransition rotateTransition = new RotateTransition(rotationDuration, enemy);
            rotateTransition.setByAngle(360);
            rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
            parallelTransition.getChildren().add(rotateTransition);
        }

        parallelTransition.play();
    }

    public static void moveEnemiesDown(List<Enemy> enemies, double newY) {
        Duration animationDuration = Duration.seconds(1.0);
        ParallelTransition parallelTransition = new ParallelTransition();

        for (Enemy enemy : enemies) {
            TranslateTransition transition = new TranslateTransition(animationDuration, enemy);
            transition.setToY(newY);
            parallelTransition.getChildren().add(transition);
        }

        parallelTransition.play();
    }

    public static void moveBoss() {
        int paneWidth = 600;
        double speed = 5.0;
        double currentX = boss.getTranslateX();
        logger.debug("Boss location :{}", currentX);
        double newLocationX = currentX + speed;
        logger.debug("Boss is located to :{}", newLocationX);
        boss.setTranslateX(newLocationX);

        if (boss.getTranslateX() >= paneWidth - boss.getFitWidth()) {
            boss.setTranslateX(0);
        }

        if (boss.dead) {
            logger.info("Boss is dead!");
            bossAnimationTimer.stop();
        }
    }

    public static void initiateAsteroidMovement(List<Enemy> asteroids, double screenWidth, double screenHeight) {
        Random random = new Random();

        // Delay asteroid appearance by 3 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            ParallelTransition parallelTransition = new ParallelTransition();

            for (Enemy asteroid : asteroids) {
                // Randomly select a side for the asteroid to start from
                int side = random.nextInt(4); // 0 = top, 1 = bottom, 2 = left, 3 = right
                double startX = 0, startY = 0, endX = 0, endY = 0;

                switch (side) {
                    case 0: // Top
                        startX = random.nextDouble() * screenWidth;
                        startY = -asteroid.getFitHeight();
                        endX = random.nextDouble() * screenWidth;
                        endY = screenHeight + asteroid.getFitHeight();
                        break;
                    case 1: // Bottom
                        startX = random.nextDouble() * screenWidth;
                        startY = screenHeight + asteroid.getFitHeight();
                        endX = random.nextDouble() * screenWidth;
                        endY = -asteroid.getFitHeight();
                        break;
                    case 2: // Left
                        startX = -asteroid.getFitWidth();
                        startY = random.nextDouble() * screenHeight;
                        endX = screenWidth + asteroid.getFitWidth();
                        endY = random.nextDouble() * screenHeight;
                        break;
                    case 3: // Right
                        startX = screenWidth + asteroid.getFitWidth();
                        startY = random.nextDouble() * screenHeight;
                        endX = -asteroid.getFitWidth();
                        endY = random.nextDouble() * screenHeight;
                        break;
                }

                asteroid.setTranslateX(startX);
                asteroid.setTranslateY(startY);

                TranslateTransition transition = new TranslateTransition(Duration.seconds(5 + random.nextInt(3)), asteroid);
                transition.setToX(endX);
                transition.setToY(endY);
                transition.setCycleCount(TranslateTransition.INDEFINITE);
                transition.setAutoReverse(true);

                parallelTransition.getChildren().add(transition);
            }

            parallelTransition.play();
        });

        delay.play();
    }
}
