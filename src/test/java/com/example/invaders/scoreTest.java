package com.example.invaders;

import com.example.invaders.controller.SpriteController;
import com.example.invaders.model.Bullet;
import com.example.invaders.model.Enemy;
import com.example.invaders.model.Player;
import com.example.invaders.model.Sprite;
import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class scoreTest {
    private Player player;
    private Enemy enemy;
    private Bullet n,s;

    @Before
    public void setUp(){

        Image playerImage = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/player3.png"));
        player = new Player(300, 580, playerImage, "player");
        Image enemyImage = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/boss.png"));
        enemy=new Enemy(0,0, enemyImage, "enemy");
        Image normalBullet=new Image(SpaceInvaderApp.class.getResourceAsStream("assets/fireball.png"));
        n=new Bullet(0,0,normalBullet,"bullet",0,0);
        Image specialBullet=new Image(SpaceInvaderApp.class.getResourceAsStream("assets/fireball.png"));
        s=new Bullet(0,0,specialBullet,"specialBullet",0,0);

    }

    @Test
    public void ScoreIncreasedInFiveHittingWithNormalShot(){
        int initialScore=player.getScore();
        if(n.getType().equals("bullet")){
            enemy.setDead(true);
            SpriteController.update();
            player.increaseScore(5);
        }
        int expectedScore=initialScore+5;
        assertEquals(expectedScore,player.getScore());
    }

    @Test
    public void ScoreIncreasedInTenHittingWithSpecialShot(){
        int initialScore=player.getScore();
        if(s.getType().equals("specialBullet")){
            enemy.setDead(true);
            SpriteController.update();
            player.increaseScore(10);
        }
        int expectedScore=initialScore+10;
        assertEquals(expectedScore,player.getScore());
    }

    @Test
    public void NoScoreIncrementWhenNoTargetHit(){
        int initialScore=player.getScore();
        SpriteController.update();
        int newScore=player.getScore();
        assertEquals("No Hit No Score Increment",initialScore,newScore);
    }
}