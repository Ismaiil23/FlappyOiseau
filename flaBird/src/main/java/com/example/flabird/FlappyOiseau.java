package com.example.flabird;


import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;

import static javax.swing.text.StyleConstants.setBackground;

public class FlappyOiseau extends Application {
    // Constantes
    private static final int LARGEUR = 450;
    private static final int HAUTEUR = 700;
    private static final int LARGEUROBSTACLE = 60;
    private static final int espaceInterObstacle = 200;
    private static final int TAILLEOISEAU = 50;

    private static final double HAUTEURSAUTBASE = 10;
    private static final double VITESSEBASE = 5;

    private static final String ambianceSoundPath = "C:\\Users\\mkism\\Documents\\jeuFlappy\\FlappyOiseau\\flaBird\\sprites\\ambiance.mp3";

    private static final Media ambianceSound = new Media(new File(ambianceSoundPath).toURI().toString());

    private static final String obstacleSoundPath = "C:\\Users\\mkism\\Documents\\jeuFlappy\\FlappyOiseau\\flaBird\\sprites\\franchissementobstacle.mp3";

    private static final Media obstacleSound = new Media(new File(obstacleSoundPath).toURI().toString());

    private static final String jumpSoundPath = "C:\\Users\\mkism\\Documents\\jeuFlappy\\FlappyOiseau\\flaBird\\sprites\\saut.mp3";

    private static final Media jumpSound = new Media(new File(jumpSoundPath).toURI().toString());

    private static final String gameOverSoundPath = "C:\\Users\\mkism\\Documents\\jeuFlappy\\FlappyOiseau\\flaBird\\sprites\\gameOver.mp3";

    private static final Media gameOverSound = new Media(new File(gameOverSoundPath).toURI().toString());

    // Création de l'objet MediaPlayer pour le son d'ambiance
    private static final MediaPlayer ambiancePlayer = new MediaPlayer(ambianceSound);

    // Création de l'objet MediaPlayer pour le son de passage d'obstacle
    private static final MediaPlayer obstaclePlayer = new MediaPlayer(obstacleSound);

    // Création de l'objet MediaPlayer pour le son de saut
    private static final MediaPlayer jumpPlayer = new MediaPlayer(jumpSound);

    // Création de l'objet MediaPlayer pour le son de gameOver
    private static final MediaPlayer gameOverPlayer= new MediaPlayer(gameOverSound);



    // Variables du jeu


    AnimationTimer boucleJeu;
    private double vitesse = VITESSEBASE;

    private double hauteurSaut = HAUTEURSAUTBASE;
    private int score = 0;
    private double positionOiseauY = HAUTEUR / 2;
    private double oiseauVitesseY = 0;
    private double obstacleX = LARGEUR;
    private double obstacleY = 0;
    private int viesRestantes = 1;
    private int highestScore=0;
    boolean isPlaying = false;
    private Group root;


    // Création des boutons
    private final Button restartButton = new Button("Recommencer");

    // Création des textes
    private final Text loose = new Text("Game Over");
    private Text pauseText;
    Text title = new Text("Flappy Oiseau");
    Text scoreText = new Text("Score: 0");
    Text startText = new Text("Start");
    private Text record;

    // Création des rectangles

    private Rectangle oiseau;
    private Rectangle obstacleTop;
    private Rectangle obstacleBottom;
    private Rectangle pauseButton ;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Group();
        Scene scene = new Scene(root, LARGEUR, HAUTEUR);
        primaryStage.setScene(scene);


       /* // Ajout des boutons à la scène
        root.getChildren().add(boutonSaut);
        root.getChildren().add(boutonPause);

        // Positionnement des boutons sur l'écran
        boutonSaut.setLayoutX(LARGEUR / 2 - boutonSaut.getWidth() / 2);
        boutonSaut.setLayoutY(HAUTEUR - boutonSaut.getHeight() - 10);
        boutonPause.setLayoutX(10);
        boutonPause.setLayoutY(10);*/

        // Creation de l'oiseau
        oiseau = new Rectangle(TAILLEOISEAU, TAILLEOISEAU);
        oiseau.setX(30);
        oiseau.setY(HAUTEUR/2-oiseau.getHeight());
        root.getChildren().add(oiseau);
        String cheminImage ="C:/Users/mkism/IdeaProjects/flaBird/sprites/oiseau.png";
        Image oiseauImage = new Image(cheminImage);
        ImageView oisImgView = new ImageView(oiseauImage);
        oiseau.setFill(new ImagePattern((oisImgView.getImage())));



        // Creation des obstacles (haut et bas)
        obstacleTop = new Rectangle(LARGEUROBSTACLE, 0, LARGEUROBSTACLE, espaceInterObstacle / 2);
        obstacleBottom = new Rectangle(LARGEUROBSTACLE, espaceInterObstacle / 2 + espaceInterObstacle, LARGEUROBSTACLE, HAUTEUR - espaceInterObstacle / 2 - espaceInterObstacle);
        obstacleBottom.setX(200);
        obstacleTop.setX(200);
        obstacleTop.setFill(Color.SANDYBROWN);
        obstacleBottom.setFill(Color.DARKGOLDENROD);
        root.getChildren().add(obstacleTop);
        root.getChildren().add(obstacleBottom);


        //region Text

        loose.setFill(Color.BLACK);
        loose.setFont(Font.font(30));
        loose.setX(LARGEUR / 2 - loose.getLayoutBounds().getWidth() / 2);
        loose.setY(HAUTEUR / 2 - loose.getLayoutBounds().getHeight() / 2);

        restartButton.setLayoutX(loose.getX() - restartButton.getWidth());
        restartButton.setLayoutY(loose.getY() + HAUTEUR/15);
        restartButton.setStyle("-fx-background-color: #DCEDC8; -fx-text-fill: red; -fx-font-size: 18px;");

        title.setFill(Color.BLACK);
        title.setFont(Font.font(32));
        title.setX(LARGEUR / 2 - title.getLayoutBounds().getWidth() / 2);
        title.setY(HAUTEUR / 2 - 100);
        root.getChildren().add(title);

        final Button startButton = new Button("Start");
        startButton.setLayoutX(loose.getX() - startButton.getWidth());
        startButton.setLayoutY(loose.getY() + HAUTEUR/15);
        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 40px;");
        root.getChildren().add(startButton);

        scoreText.setFill(Color.BLACK);
        scoreText.setFont(Font.font(18));
        scoreText.setX(10);
        scoreText.setY(30);
        root.getChildren().add(scoreText);

        record = new Text("Record: "+ plusHautScore());
        record.setFill(Color.BLUE);
        record.setFont(Font.font(18));
        record.setX(10);
        record.setY(50);
        root.getChildren().add(record);


        //endregion

        //Son d'ambiance
        ambiancePlayer.setCycleCount(MediaPlayer.INDEFINITE);
        ambiancePlayer.play();


        startGame(title, startButton, startText);

        restartGame(restartButton);


        // Evènement saut grâce à l'espace et evenement PAUSE grâce à échap
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.SPACE) {
                    isPlaying=true;
                    if(isPlaying==true){
                        oiseauVitesseY = -hauteurSaut;

                        sonPlayer(jumpPlayer);
                    }
                }else if (event.getCode() == KeyCode.ESCAPE) {
                    isPlaying = !isPlaying;

                    sonPlayer(obstaclePlayer);
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

                        // Désactiver les événements de la souris
                        scene.setOnMouseClicked(null);
                    } else {
                        // Supprimer le bouton PAUSE et le texte
                        root.getChildren().remove(pauseButton);
                        root.getChildren().remove(pauseText);
                    }
                }
            }
        });

             // Boucle de jeu
             boucleJeu = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if(isPlaying) {
                        update();

                        collision();

                    }
                }
            };
            boucleJeu.start();

        // Affichage
        primaryStage.show();
    }

    private void collisionObstacle() {
        Bounds birdBounds = oiseau.getBoundsInParent();
        if (obstacleX + LARGEUROBSTACLE > 0 && obstacleX <= LARGEUR) {
            // Verifier si l'oiseau touche obstacle ou non
            if (birdBounds.intersects(obstacleTop.getBoundsInParent()) || birdBounds.intersects(obstacleBottom.getBoundsInParent())) {
                // Decrémenter viesRestantes et afficher un message de fin de jeu
                if(viesRestantes>=1){
                    perdreVie();
                }
                else if (viesRestantes == 0) {
                    GameOver();
                }
            }
        }
    }

    private void collision() {
        collisionBord();
        collisionObstacle();
    }

        private void collisionBord() {
        // Verifie collision avec haut et bas de l'écran
        if (positionOiseauY + TAILLEOISEAU / 2 > HAUTEUR) {
            // Collision avec le bas
            if(viesRestantes>=1){
                perdreVie();
            }
            else if (viesRestantes < 1) {
                GameOver();

            }
        } else if (positionOiseauY+TAILLEOISEAU/2 < 0) {
            // Collision avec le haut
            if(viesRestantes>=1){
                perdreVie();
            }
            else if (viesRestantes < 1) {
                GameOver();

            }
        }
    }


    private void startGame(Text title, Button startButton, Text startText) {
        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                root.getChildren().remove(title);
                root.getChildren().remove(startButton);
                root.getChildren().remove(startText);
            }
        });
    }

    private void restartGame(Button restartButton) {
        restartButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                root.getChildren().remove(restartButton);
                root.getChildren().remove(loose);
                resetGame();
            }
        });
    }

    private void sonPlayer(MediaPlayer media){
        media.seek(Duration.ZERO);
        media.play();
    }
    private void perdreVie() {
        viesRestantes--;
    }

    private void GameOver() {
        root.getChildren().add(loose);
        root.getChildren().add(restartButton);
        ambiancePlayer.stop();
        sonPlayer(gameOverPlayer);
        isPlaying = false;
    }

    private void update() {
        // Faire tomber l'oiseau et déplacer les obstacles vers la gauche
        positionOiseauY += oiseauVitesseY;
        obstacleX -= vitesse;

        // Si l'obstacle est hors de l'ecran
        if (obstacleX + LARGEUROBSTACLE < 0) {
            sonPlayer(obstaclePlayer);

            obstacleX = LARGEUR;
            obstacleY = 0;

            if(score>=10 && score<20){
                //sonPlayer(sonStressant);
                obstacleTop.setFill(Color.DARKGOLDENROD);
                obstacleBottom.setFill(Color.SANDYBROWN);
            }

            randomObstacle();


            // MAJ score
            score++;

            if(score>5 && vitesse<=7.5){
                vitesse+=0.1;
            }
        }


        // MAJ valeurs position
        oiseau.setY(positionOiseauY);
        obstacleTop.setX(obstacleX);
        obstacleBottom.setX(obstacleX);
        // MAJ valeurs score et record
        scoreText.setText("Score: " + score);
        record.setText("Record: " + plusHautScore());
        // MAJ vitesse oiseau en Y
        oiseauVitesseY += 0.5;
    }

    private void randomObstacle() {
        // Taille du trou dans l'obstacle
        var tailleTrou = oiseau.getHeight()*5;

        //Génération aléatoire apparition des obstacles
        Random rand= new Random();
        int rnd=rand.nextInt(2 - 1 + 1) + 1;
        if(rnd==1){
            int rndHeightTop = rand.nextInt((HAUTEUR-HAUTEUR/3) - 1 + 1) + 1;
            obstacleTop.setHeight(rndHeightTop);
            double complementBottom=(HAUTEUR-obstacleTop.getHeight())- tailleTrou;
            obstacleBottom.setHeight(complementBottom);

            obstacleTop.setY(obstacleY);
            obstacleBottom.setY(HAUTEUR- obstacleBottom.getHeight());
            obstacleTop.setX(obstacleX);
            obstacleBottom.setX(obstacleX);
        }else{
            int rndHeightBottom = rand.nextInt((HAUTEUR-HAUTEUR/3) - 1 + 1) + 1;
            obstacleBottom.setHeight(rndHeightBottom);
            double complementTop=(HAUTEUR-obstacleBottom.getHeight())- tailleTrou;
            obstacleTop.setHeight(complementTop);

            obstacleTop.setY(obstacleY);
            obstacleBottom.setY(HAUTEUR- obstacleBottom.getHeight());
            obstacleTop.setX(obstacleX);
            obstacleBottom.setX(obstacleX);
        }
    }

    private void resetGame() {
        isPlaying = false;
        oiseau.setX(30);
        oiseau.setY(HAUTEUR/2-oiseau.getHeight());
        randomObstacle();
        obstacleBottom.setX(250);
        obstacleTop.setX(250);
        vitesse = VITESSEBASE;
        hauteurSaut = HAUTEURSAUTBASE;
        score = 0;
        positionOiseauY = HAUTEUR / 2;
        oiseauVitesseY = 0;
        obstacleX = LARGEUR;
        obstacleY = 0;
        viesRestantes = 1;
    }


    private int plusHautScore(){
        if(highestScore<score)
            highestScore=score;
        return highestScore;
    }

}
