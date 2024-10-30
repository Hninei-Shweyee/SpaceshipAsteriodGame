package com.example.invaders;


import com.example.invaders.controller.playerController;
import com.example.invaders.model.Player;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;
import javafx.embed.swing.JFXPanel;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.example.invaders.SpaceInvaderApp.root;
import static org.junit.jupiter.api.Assertions.*;


public class playerTest {

    playerController playerController;
    Player player;

    @Before
    public void setUp() throws NoSuchMethodException {
        JFXPanel jfxPanel = new JFXPanel();
        Image playerImage = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/player1.png"));
        this.player = new Player(300, 580, playerImage, "player");
        this.playerController = new playerController(player);


    }

    @Test
    public void playerisMovedLeft() throws NoSuchFieldException, IllegalAccessException {
        playerController = new playerController(player);
        Field isMovedLeft= playerController.getClass().getDeclaredField("isMoveLeft");
        isMovedLeft.setAccessible(true);
        playerController.moveLeft();
        assertTrue(isMovedLeft.getBoolean(player), "Player is not moving left");


    }
    @Test
    public void playerisMovedRight() throws NoSuchFieldException, IllegalAccessException {
        playerController = new playerController(player);
        Field isMovedRight= playerController.getClass().getDeclaredField("isMoveRight");
        isMovedRight.setAccessible(true);
        playerController.moveRight();
        assertTrue(isMovedRight.getBoolean(player), "Player is not moving right");


    }

    @Test
    public void playerIsCollidedWithTheWall() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        Field isMovedRight= playerController.getClass().getDeclaredField("isMoveRight");
        isMovedRight.setAccessible(true);
        player.setTranslateX(600);

         assertTrue(isMovedRight.getBoolean(player),"player collided with wall.");

    }
    @Test
    public void playerRespawnAgainAfterDead(){
            root.getChildren().remove(player);
           player.setDead(true);
          boolean isAdded= root.getChildren().add(player);
        assertTrue(isAdded, "player doesn't respawn");
    }

    }

