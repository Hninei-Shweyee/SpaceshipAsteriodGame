package com.example.invaders;

import com.example.invaders.model.Boss;
import com.example.invaders.model.Enemy;
import com.example.invaders.view.GamePlatform;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;


import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;


public class enemyTestCase {

    @Test
    public void BossComingWhenAllEnemiesDie() throws Exception {
        Platform.startup(() -> {
            SpaceInvaderApp app = new SpaceInvaderApp();
            SpaceInvaderApp.playbackgroundSound(new Media(SpaceInvaderApp.class.getResource("/sounds/aggressivebackground.mp3")
                    .toExternalForm()));
            try {
                app.start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            List<Enemy> enemies = SpaceInvaderApp.sprites().stream()
                    .filter(sprite -> sprite instanceof Enemy)
                    .map(sprite -> (Enemy) sprite)
                    .collect(Collectors.toList());

            SpaceInvaderApp.getRoot().getChildren().remove(enemies);
            GamePlatform.BossSpawning(SpaceInvaderApp.getRoot());
            Boss boss = GamePlatform.boss;
            assertNotNull(boss);
        });



    }


}
