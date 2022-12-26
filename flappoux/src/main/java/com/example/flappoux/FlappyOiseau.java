package com.example.flappoux;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.util.Random;

public class FlappyOiseau extends Application {
    // Constantes

    private static final int LARGEUR = 800;
    private static final int HAUTEUR = 600;
    private static final int LARGEUROBSTACLE = 50;
    private static final int espaceInterObstacle = 200;
    private static final int VITESSE = 5;
    private static final int TAILLEOISEAU = 30;
    private static final int GRAVITE = 10;

    // Variables du jeu
    private int score = 0;
    private double positionOiseauY = HAUTEUR / 2;
    private double oiseauVitesseY = 0;
    private double obstacleX = LARGEUR;
    private double obstacleY = 0;
    private int viesRestantes = 3;
    boolean isPlaying = false;
    private Group root;
    private Rectangle oiseau;
    private Rectangle obstacleTop;
    private Rectangle obstacleBottom;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Group();
        Scene scene = new Scene(root, LARGEUR, HAUTEUR);
        primaryStage.setScene(scene);

        // Creation de l'oiseau
        oiseau = new Rectangle(TAILLEOISEAU, TAILLEOISEAU, Color.YELLOW);
        root.getChildren().add(oiseau);

        // Creation des obstacles (haut et bas)
        obstacleTop = new Rectangle(LARGEUROBSTACLE, 0, LARGEUROBSTACLE, espaceInterObstacle / 2);
        obstacleBottom = new Rectangle(LARGEUROBSTACLE, espaceInterObstacle / 2 + espaceInterObstacle, LARGEUROBSTACLE, HAUTEUR - espaceInterObstacle / 2 - espaceInterObstacle);
        root.getChildren().add(obstacleTop);
        root.getChildren().add(obstacleBottom);

        //region Text
        Text vies = new Text("Vies : " + viesRestantes);
        vies.setFill(Color.BLACK);
        vies.setFont(Font.font(18));
        vies.setX(10);
        vies.setY(30);
        root.getChildren().add(vies);

        Text title = new Text("Flappy Oiseau");
        title.setFill(Color.BLACK);
        title.setFont(Font.font(32));
        title.setX(LARGEUR / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setY(HAUTEUR / 2 - 100);
        root.getChildren().add(title);

        Rectangle startButton = new Rectangle(LARGEUR / 2 - 50, HAUTEUR / 2, 100, 50);
        startButton.setFill(Color.LIGHTGREEN);
        root.getChildren().add(startButton);

        Text startText = new Text("Start");
        startText.setFill(Color.BLACK);
        startText.setFont(Font.font(18));
        startText.setX(LARGEUR / 2 - startText.getLayoutBounds().getWidth() / 2);
        startText.setY(startButton.getY() + startButton.getHeight()/2);
        root.getChildren().add(startText);

        Text scoreText = new Text("Score: 0");
        scoreText.setFill(Color.BLACK);
        scoreText.setFont(Font.font(18));
        scoreText.setX(10);
        scoreText.setY(50);
        root.getChildren().add(scoreText);
        //endregion


        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isPlaying = true;
                root.getChildren().remove(title);
                root.getChildren().remove(startButton);
                root.getChildren().remove(startText);
            }
        });



        // Evènement saut grâce à l'espace et evenement PAUSE grace à escape
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE) {
                    if(isPlaying==true){
                    oiseauVitesseY = -GRAVITE;}
                }else if (event.getCode() == KeyCode.ESCAPE) {
                    isPlaying = !isPlaying;
                    /*Text pauseText = null;
                    Rectangle pauseButton = null;

                    if(isPlaying==false) {
                        pauseButton = new Rectangle(LARGEUR / 2 - 50, HAUTEUR / 2, 100, 50);
                        pauseButton.setFill(Color.LIGHTBLUE);
                        root.getChildren().add(pauseButton);

                        pauseText = new Text("PAUSE");
                        pauseText.setFill(Color.BLACK);
                        pauseText.setFont(Font.font(18));
                        pauseText.setX(LARGEUR / 2 - pauseText.getLayoutBounds().getWidth() / 2);
                        pauseText.setY(pauseButton.getY() + pauseButton.getHeight() / 2);
                        root.getChildren().add(pauseText);

                    }else {
                        root.getChildren().remove(pauseText);
                        root.getChildren().remove(pauseButton);
                    }
                */
                }
            }
        });

        // Boucle de jeu
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(isPlaying)
                {
                    update();

                    Bounds birdBounds = oiseau.getBoundsInParent();
                    // Verifier si l'oiseau touche obstacle ou non
                    if (obstacleX + LARGEUROBSTACLE > 0 && obstacleX <= LARGEUR) {
                        if (birdBounds.intersects(obstacleTop.getBoundsInParent()) || birdBounds.intersects(obstacleBottom.getBoundsInParent())) {                           // Decrementer viesRestantes et afficher un message de fin de jeu
                            if(viesRestantes>=1){
                                perdreVie();
                            }
                            else if (viesRestantes == 0) {
                              GameOver();
                            }

                        }
                    }

                    // MAJ valeurs position
                    oiseau.setY(positionOiseauY);
                    obstacleTop.setX(obstacleX);
                    obstacleBottom.setX(obstacleX);
                    // MAJ valeurs score et vies
                    vies.setText("Vies : " + viesRestantes);
                    scoreText.setText("Score: " + score);

                    // Verifie collision avec haut et bas de l'écran
                    if (positionOiseauY + TAILLEOISEAU / 2 > HAUTEUR) {
                        // Collision avec le bas
                        if(viesRestantes>=1){
                           perdreVie();
                        }
                        else if (viesRestantes == 0) {
                            GameOver();

                        }
                    } else if (positionOiseauY+TAILLEOISEAU/2 < 0) {
                        // Collision avec le haut
                        if(viesRestantes>=1){
                            perdreVie();
                        }
                        else if (viesRestantes == 0) {
                            GameOver();

                        }
                    }
                }
            }
        };
        timer.start();

        // Affichage
        primaryStage.show();
    }

    private void perdreVie() {
        viesRestantes--;

        score = 0;
        positionOiseauY = HAUTEUR / 2;
        oiseauVitesseY = 0;
        obstacleX = LARGEUR;
        obstacleY = 0;
    }

    private void GameOver() {
        Text loose = new Text("Game Over");
        loose.setFill(Color.BLACK);
        loose.setFont(Font.font(18));
        loose.setX(LARGEUR / 2 - loose.getLayoutBounds().getWidth() / 2);
        loose.setY(HAUTEUR / 2 - loose.getLayoutBounds().getHeight() / 2);
        root.getChildren().add(loose);
        isPlaying = false;
    }

    private void update() {
        // Faire tomber l'oiseau et déplacer les obstacles vers la gauche
        positionOiseauY += oiseauVitesseY;
        obstacleX -= VITESSE;

        // Si l'obstacle est hors de l'ecran
        if (obstacleX + LARGEUROBSTACLE < 0) {

            obstacleX = LARGEUR;
            obstacleY = 0;

            // Taille du trou dans l'obstacle
            var tailleTrou = oiseau.getHeight()*6;

            Random rand= new Random();
            int rnd=rand.nextInt(2 - 1 + 1) + 1;
            if(rnd==1){
                int rndHeightTop = rand.nextInt((HAUTEUR-HAUTEUR/3) - 1 + 1) + 1;
                obstacleTop.setHeight(rndHeightTop);
                double complementBottom=(HAUTEUR-obstacleTop.getHeight())-tailleTrou;
                obstacleBottom.setHeight(complementBottom);

                obstacleTop.setY(obstacleY);
                obstacleBottom.setY(HAUTEUR- obstacleBottom.getHeight());
                obstacleTop.setX(obstacleX);
                obstacleBottom.setX(obstacleX);
            }else{
                int rndHeightBottom = rand.nextInt((HAUTEUR-HAUTEUR/3) - 1 + 1) + 1;
                obstacleBottom.setHeight(rndHeightBottom);
                double complementTop=(HAUTEUR-obstacleBottom.getHeight())-tailleTrou;
                obstacleTop.setHeight(complementTop);

                obstacleTop.setY(obstacleY);
                obstacleBottom.setY(HAUTEUR- obstacleBottom.getHeight());
                obstacleTop.setX(obstacleX);
                obstacleBottom.setX(obstacleX);
            }

            //MAJ score
            score++;
        }
        // MAJ vitesse oiseau en Y
        oiseauVitesseY += 0.5;
    }
}