package com.example.invaders.exception;

import com.example.invaders.SpaceInvaderApp;
import com.example.invaders.model.Player;
import com.example.invaders.model.Sprite;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;



public class exceptionHandle  {



    public void showTalkingDialog(String s, Player player, Pane exceptionpane, String direction,Duration duration) {
        Text dialogText = new Text(s);
        dialogText.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        dialogText.setFill(Color.BLACK);
        Image dialogBackgroundImage = null;
        if (direction.equals("right")) {
            dialogBackgroundImage = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/dialoug.png"));
        } else if (direction.equals("left")) {
            dialogBackgroundImage = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/leftdialoug.png"));
        } else if (direction.equals("default")) {
            dialogBackgroundImage = new Image(SpaceInvaderApp.class.getResourceAsStream("assets/dialoug.png"));
        }

        ImageView dialogBackgroundImgView = new ImageView(dialogBackgroundImage);

        StackPane dialogPane = new StackPane();
        dialogPane.getChildren().addAll(dialogBackgroundImgView, dialogText);

        double dialogX = 0;
        if (direction.equals("right")) {
            dialogX = player.getTranslateX() - player.getWidth() - 60;

        } else if (direction.equals("left")) {
            dialogX = 0;
        }
        else if (direction.equals("default")){
            dialogX = player.getTranslateX() - player.getWidth();


        }

        dialogPane.setTranslateX(dialogX);
        dialogPane.setTranslateY(460);

        exceptionpane.getChildren().add(dialogPane);


        PauseTransition dialogRemoval = new PauseTransition(duration);
        dialogRemoval.setOnFinished(event -> {
            exceptionpane.getChildren().remove(dialogPane);
        });
        dialogRemoval.play();
    }

    }
