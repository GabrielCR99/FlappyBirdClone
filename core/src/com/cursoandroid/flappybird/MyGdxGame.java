package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture[] bird;
    private Texture background;
    private Texture lowPlumb;
    private Texture highPlumb;
    private Texture gameOver;
    private Random random;
    private BitmapFont bitmapFont;
    private BitmapFont mensagem;
    private Circle birdCircle;
    private Rectangle topPipe;
    private Rectangle bottomPipe;
    //private ShapeRenderer shapeRenderer;

    //Atributos de configuração
    private int screenWidth;
    private int screenHeight;
    private int gameState = 0; // 0 -> game not started 1 -> game started 2 -> game over
    private int points = 0;
    private int scaleSpeed = 0;

    private float var = 0;
    private float fallSpeed = 0;
    private float initialVerticalPosition;
    private float plumbHorizontalMovePosition;
    private float spaceBetweenPlumbs;
    private float deltaTime;
    private float randomHeightBetweenPlumbs;
    private boolean scoredPoint;


    @Override
    public void create() {
        //Gdx.app.log("Create" , "Initializing game");
        batch = new SpriteBatch();
        random = new Random();
        birdCircle = new Circle();
       /* bottomPipe = new Rectangle();
        topPipe = new Rectangle();
        shapeRenderer = new ShapeRenderer();*/
        bitmapFont = new BitmapFont();
        mensagem = new BitmapFont();

        mensagem.setColor(Color.WHITE);
        mensagem.getData().setScale(3);

        //font color
        bitmapFont.setColor(Color.BLUE);
        bitmapFont.getData().setScale(12);

        bird = new Texture[3];
        bird[0] = new Texture("passaro1.png");
        bird[1] = new Texture("passaro2.png");
        bird[2] = new Texture("passaro3.png");
        gameOver = new Texture("game_over.png");


        background = new Texture("fundo.png");
        lowPlumb = new Texture("cano_baixo.png");
        highPlumb = new Texture("cano_topo.png");

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        initialVerticalPosition = screenHeight / 2;
        plumbHorizontalMovePosition = screenWidth;
        spaceBetweenPlumbs = 200;

    }

    @Override
    public void render() {
        //Gdx.app.log("Render" , "Rendering game" );

        deltaTime = Gdx.graphics.getDeltaTime();

        var += deltaTime * 5;

        if (var > 2)
            var = 0;


        if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else {
            fallSpeed++;
            if (initialVerticalPosition > 0 || fallSpeed < 0)
                initialVerticalPosition -= fallSpeed;

            if (gameState == 1) {

                plumbHorizontalMovePosition -= deltaTime * 500;

                if (Gdx.input.justTouched()) {
                    fallSpeed = -15;
                }

                //verificar se o cano saiu da tela
                if (plumbHorizontalMovePosition < -highPlumb.getWidth()) {
                    plumbHorizontalMovePosition = screenWidth;
                    randomHeightBetweenPlumbs = random.nextInt(400) - 200;
                    scoredPoint = false;

                }

                //verifica pontuacao
                if (plumbHorizontalMovePosition < 120) {
                    if (!scoredPoint) {
                        points++;
                        scoredPoint = true;
                    }
                }

            } else {//game over

                if (Gdx.input.justTouched()) {
                    gameState = 0;
                    points = 0;
                    fallSpeed = 0;
                    initialVerticalPosition = screenHeight / 2;
                    plumbHorizontalMovePosition = screenWidth;
                }

            }

        }


        //Iniciando a exibicao das imagens
        batch.begin();

        batch.draw(background, 0, 0, screenWidth, screenHeight);

        //cano do topo
        batch.draw(highPlumb, plumbHorizontalMovePosition, screenHeight / 2 + spaceBetweenPlumbs + randomHeightBetweenPlumbs);

        //cano do bot
        batch.draw(lowPlumb, plumbHorizontalMovePosition, screenHeight / 2 - lowPlumb.getHeight() - spaceBetweenPlumbs / 2 + randomHeightBetweenPlumbs);

        //alternando as imagens do bird
        batch.draw(bird[(int) var], 120, initialVerticalPosition);

        bitmapFont.draw(batch, String.valueOf(points), screenWidth / 2 - 50, screenHeight - 50);

        if (gameState == 2) {
            batch.draw(gameOver, screenWidth / 2 - gameOver.getWidth() / 2, screenHeight / 2);
            mensagem.draw(batch, "Touch to play again!", screenWidth / 2 - gameOver.getWidth() / 2, screenHeight / 2 - gameOver.getHeight() / 2);
        }

        batch.end();

        birdCircle.set(120 + bird[0].getWidth() / 2,
                initialVerticalPosition + bird[0].getHeight() / 2,
                bird[0].getWidth() / 2
        );

        bottomPipe = new Rectangle(
                plumbHorizontalMovePosition, screenHeight / 2 - lowPlumb.getHeight() - spaceBetweenPlumbs / 2 + randomHeightBetweenPlumbs,
                lowPlumb.getWidth(),
                lowPlumb.getHeight()
        );

        topPipe = new Rectangle(plumbHorizontalMovePosition,
                screenHeight / 2 + spaceBetweenPlumbs + randomHeightBetweenPlumbs,
                highPlumb.getWidth(),
                highPlumb.getHeight()
        );

        //Desenhar formas
       /* shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
        shapeRenderer.rect(bottomPipe.x, bottomPipe.y, bottomPipe.width, bottomPipe.height);
        shapeRenderer.rect(topPipe.x, topPipe.y, topPipe.width, topPipe.height);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.end();*/

        //teste colisao

        if (Intersector.overlaps(birdCircle, bottomPipe) || Intersector.overlaps
                (birdCircle, topPipe) || initialVerticalPosition <= 0 || initialVerticalPosition >= screenHeight) {
            //Gdx.app.log("collision", "Collision detected");
            gameState = 2;
        }

    }

}
