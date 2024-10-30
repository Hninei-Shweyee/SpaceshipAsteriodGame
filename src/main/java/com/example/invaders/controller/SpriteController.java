package com.example.invaders.controller;

import com.example.invaders.model.Boss;
import com.example.invaders.model.Bullet;
import com.example.invaders.model.Player;
import com.example.invaders.model.Sprite;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import com.example.invaders.SpaceInvaderApp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.example.invaders.SpaceInvaderApp.*;
import static com.example.invaders.controller.playerController.*;

public class SpriteController {
    static double t = 0;
    static Stage gameOverStage, gameWinStage;
    public static boolean isGameOver = false;
    private static boolean bossSpawned = false; // Track if the boss has spawned
    static Logger logger = LogManager.getLogger(SpriteController.class);

    public static void update() {
        t += 0.016;
        if (!bossSpawned && areAllEnemiesDefeated()) {
            //spawnBoss();
            bossSpawned = true;
        }

        // Copy the list of sprites to avoid concurrent modification issues
        List<Sprite> currentSprites = new ArrayList<>(sprites());

        currentSprites.forEach(s -> {
            switch (s.type) {
                case "enemy":
                    if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        handlePlayerHit(s);
                    }
                    break;
                case "playerbullet":
                    if (s instanceof Bullet) {
                        ((Bullet) s).update();
                    }

                    handleBulletCollision(s);
                    break;
                case "playerspecialBullet":
                    if (s instanceof Bullet) {
                        ((Bullet) s).update();
                    }
                    handleSpecialBulletCollision(s);
                    break;
                case "Boss":
                    handleBossActions(s);
                    break;
            }
        });

        // Remove dead sprites from the root safely
        root.getChildren().removeIf(n -> n instanceof Sprite && ((Sprite) n).dead);

        if (t > 2) {
            t = 0;
        }
    }

    private static boolean areAllEnemiesDefeated() {
        // Check if no enemies are left
        return sprites().stream().noneMatch(s -> s.type.equals("enemy"));
    }

    // Cooldown flag to prevent multiple hits in quick succession
    //private static boolean playerHitCooldown = false;

    private static void handlePlayerHit(Sprite s) {
        if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
            logger.error("Enemy bullet shot to player");
            logger.debug("Before Dead Health :" + player.getHealth());


            Thread explosionSoundThread = new Thread(() -> {
                SpaceInvaderApp.playEffectSound(new Media(SpaceInvaderApp.class.getResource("/sounds/explosion.wav").toExternalForm()));

            });
            explosionSoundThread.start();


            player.dead = true;
            s.dead = true;
            player.setDead(true);
            s.setDead(true);
            platform.getEnemies().remove(s);

            if (player.getCurrentChance() < 4) {
                root.getChildren().removeIf(n -> {
                    Sprite one = (Sprite) n;
                    return one.dead;
                });

                //javafx animation delay ,PAUSE
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> {
                    respawn();
                    logger.debug("After dead, Health: " + player.getHealth());
                    logger.debug("After dead , Current life: " + player.getCurrentChance());
                });
                delay.play();
            } else if (player.getCurrentChance() == 4) {
                player.setHealth(0);
                Thread gameOverSoundThread = new Thread(() -> {
                    SpaceInvaderApp.playEffectSound(new Media(SpaceInvaderApp.class.getResource("/sounds/gameover.m4a").toExternalForm()));

                });
                isGameOver = true;
                gameOverSoundThread.start();
                previousScore.add(player.getScore());
                logger.info("Previous added as {}", player.getScore());
                logger.warn("player has no life!");
                logger.error("player is dead!");


                //GameOverScreen
                showGameOverScreen();
            } else if (player.getCurrentChance() >= 4) {
                logger.error("player has already dead!");
            }
            showExplosion(player);


        }
    }

    private static void handleBulletCollision(Sprite s) {
        sprites().forEach(sprite -> {
            if (sprite.type.equals("enemy")) {
                if (s.getBoundsInParent().intersects(sprite.getBoundsInParent())) {
                    sprite.dead = true;
                    s.dead = true;
                    sprite.setDead(true);
                    int pointEarned = 5;
                    player.increaseScore(pointEarned);
                    logger.debug("Score:", player.getScore());
                    platform.getEnemies().remove(sprite);
                    showExplosion(sprite);
                }
            } else if (sprite.type.equals("Boss")) {
                if (s.getBoundsInParent().intersects(sprite.getBoundsInParent())) {
                    //sprite.dead = true;
                    //s.dead = false;
                    //sprite.setDead(false);
                    //platform.removeBoss(root);
                    //bossAnimationTimer.stop();
                    //player.increaseScore(100);
                    //logger.debug("Score:", player.getScore());
                    //showExplosion(sprite);
                    //showGameWinScreen();
                }

            }
        });


    }

    private static void handleSpecialBulletCollision(Sprite s) {
        sprites().forEach(sprite -> {
            if (sprite.type.equals("enemy")) {
                if (s.getBoundsInParent().intersects(sprite.getBoundsInParent())) {
                    sprite.dead = true;
                    s.dead = true;
                    sprite.setDead(true);
                    int pointEarned = 10;
                    player.increaseScore(pointEarned);
                    logger.debug("Score:", player.getScore());
                    platform.getEnemies().remove(sprite);
                    showExplosion(sprite);
                }
            } else if (sprite.type.equals("Boss")) {
                if (s.getBoundsInParent().intersects(sprite.getBoundsInParent())) {
                    sprite.dead = true;
                    s.dead = true;
                    sprite.setDead(true);
                    platform.removeBoss(root);
                    player.increaseScore(10);
                    logger.debug("Score:", player.getScore());
                    showExplosion(sprite);
                    showGameWinScreen();
                }

            }
        });



    }

    private static void handleEnemyActions(Sprite enemy) {
        if (t > 2 && Math.random() < 0.2) {
            // enemy shooting logic
        }
    }

    private static void handleBossActions(Sprite boss) {
        if (t > 1.8 && Math.random() < 0.2) {
            // boss shooting logic
        }
    }

    private static void showGameOverScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SpriteController.class.getResource("/game-over.fxml"));
            Scene gameOverMenuScene = new Scene(fxmlLoader.load(), 300, 250);
            gameOverStage = new Stage();
            gameOverStage.initStyle(StageStyle.UTILITY);
            gameOverStage.initModality(Modality.WINDOW_MODAL);
            gameOverStage.setTitle("Game Over");
            gameOverStage.setResizable(false);
            gameOverStage.setScene(gameOverMenuScene);
            gameOverStage.show();
            gameOverStage.centerOnScreen();
            SpaceInvaderApp.stopAnimation();

            Text scoreVar = (Text) gameOverMenuScene.lookup("#scoreVar");
            scoreVar.setText(String.valueOf(player.getScore()));
        } catch (Exception e) {
            logger.error("Failed to show Game Over screen", e);
        }
    }

    private static void showGameWinScreen() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SpriteController.class.getResource("/game-win.fxml"));
            Scene gameWinMenuScene = new Scene(fxmlLoader.load(), 250, 250);
            gameWinStage = new Stage();
            gameWinStage.initStyle(StageStyle.UTILITY);
            gameWinStage.initModality(Modality.WINDOW_MODAL);
            gameWinStage.setTitle("Game Win");
            gameWinStage.setResizable(false);
            gameWinStage.setScene(gameWinMenuScene);
            SpaceInvaderApp.stopAnimation();
            SpaceInvaderApp.playbackgroundSoundOff();
            SpaceInvaderApp.playEffectSound(new Media(SpaceInvaderApp.class.getResource("/sounds/completion.wav").toExternalForm()));
            gameWinStage.show();
            gameWinStage.centerOnScreen();

            Text scoreVar = (Text) gameWinMenuScene.lookup("#scoreVar");
            scoreVar.setText(String.valueOf(player.getScore()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeGameOverScreen() {
        if (gameOverStage != null) {
            gameOverStage.close();
        }
    }

    public static void RestartGame(Stage window, Player player, Boolean musicOff) {
        if (window != null) {
            window.close();
        }
        isGameOver = false;
        bossSpawned = true; // Reset boss spawn status on game restart
        root.getChildren().removeIf(node -> node instanceof Boss);
        SpaceInvaderApp.stopAnimation();
        respawn();
        SpaceInvaderApp.startGame(window, player, musicOff);

        player.setCurrentChance(1);
        player.setHealth(100);
        player.setScore(0);
        player.setChances(3);
    }

    private static void showExplosion(Sprite target) {
        try {
            Image explosion_img = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/explo1.png"));
            Sprite explosion = new Sprite(0, 0, explosion_img, "explosion");
            explosion.setTranslateX(target.getTranslateX() - 50);
            explosion.setTranslateY(target.getTranslateY() - 50);
            root.getChildren().add(explosion);

            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(500),
                            event -> root.getChildren().remove(explosion)
                    )
            );

            timeline.setCycleCount(1);
            timeline.play();
        } catch (Exception e) {
            logger.error("Failed to show explosion", e);
        }
    }
}
