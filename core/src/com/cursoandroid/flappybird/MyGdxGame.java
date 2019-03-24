package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture[] bird;
    private Texture background;
    private Texture lowPlumb;
    private Texture highPlumb;

    //Atributos de configuração
    private int screenWidth;
    private int screenHeight;
    private float var = 0;
    private float fallSpeed = 0;
    private float initialVerticalPosition;
    private float plumbHorizontalMovePosition;
    private float spaceBetweenPlumbs;


    @Override
    public void create() {
        //Gdx.app.log("Create" , "Initializing game");
        batch = new SpriteBatch();

        bird = new Texture[3];

         lowPlumb = new Texture("cano_baixo.png");
        highPlumb = new Texture("cano_topo.png");


        bird[0] = new Texture("passaro1.png");
        bird[1] = new Texture("passaro2.png");
        bird[2] = new Texture("passaro3.png");


        background = new Texture("fundo.png");
        screenHeight = Gdx.graphics.getHeight();
        screenWidth = Gdx.graphics.getWidth();
        initialVerticalPosition = screenHeight / 2;
        plumbHorizontalMovePosition = screenWidth - 100;
        spaceBetweenPlumbs = 300;

    }

    @Override
    public void render() {
        //Gdx.app.log("Render" , "Rendering game" );

        var += Gdx.graphics.getDeltaTime() * 5;
        fallSpeed++;

        if (var > 2)
            var = 0;

        if (Gdx.input.justTouched()) {
            fallSpeed = -20;
        }

        if (initialVerticalPosition > 0 || fallSpeed < 0)
            initialVerticalPosition -= fallSpeed;

        //Iniciando a exibicao das imagens
        batch.begin();

        batch.draw(background, 0, 0, screenWidth, screenHeight);

        //cano do topo
        batch.draw(highPlumb, plumbHorizontalMovePosition,screenHeight/2 + spaceBetweenPlumbs);

        //cano do bot
        batch.draw(lowPlumb, plumbHorizontalMovePosition, screenHeight / 2 - lowPlumb.getHeight() - spaceBetweenPlumbs /2);

        //alternando as imagens do bird
        batch.draw(bird[(int) var], 30, initialVerticalPosition);

        batch.end();

    }

}
