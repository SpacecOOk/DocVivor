package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaAcercaDe extends Pantalla {
    private final Juego juego;

    private Texture texturaAcerca;

    private Stage escenaAcerca;

    public PantallaAcercaDe(Juego juego){
        this.juego=juego;
    }

    //Texturas
    private Texture Roberto = new Texture("PantallaAcercaDe/RobertoCastro.jpeg");
    private Texture Diego = new Texture("PantallaAcercaDe/DiegoElizalde.jpeg");
    private Texture Bruno = new Texture("PantallaAcercaDe/BrunoJimenez.jpeg");
    private Texture Mich = new Texture("PantallaAcercaDe/MichelDionne.jpeg");
    private Texture name_Diego = new Texture("PantallaAcercaDe/nombre_Diego.png");
    private Texture name_Roberto = new Texture("PantallaAcercaDe/nombre_Roberto.png");
    private Texture name_Bruno = new Texture("PantallaAcercaDe/nombre_Bruno.png");
    private Texture name_Mich = new Texture("PantallaAcercaDe/nombre_Mich.png");


    @Override
    public void show() {
        texturaAcerca = new Texture("Fondos/fondo_general.png");
        crearAcerca();
    }

    private void crearAcerca() {
        escenaAcerca = new Stage(vista);
        //Boton para regresar
        Texture texturaRegresar = new Texture("Botones/btn_Exit.png");
        TextureRegionDrawable botonRegresarMenu = new TextureRegionDrawable(new TextureRegion(texturaRegresar));
        //Aqui para el boton inverso (click)
        ImageButton btnRegresar = new ImageButton(botonRegresarMenu);
        btnRegresar.setPosition(ANCHO/2,ALTO-btnRegresar.getHeight(), Align.center);
        btnRegresar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Para poder regresar al menu principal
                juego.setScreen(new PantallaMenu(juego));
            }
        });
        escenaAcerca.addActor(btnRegresar);
        Gdx.input.setInputProcessor(escenaAcerca);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaAcerca,0,0);
        batch.draw(Bruno,128,ALTO/6);
        batch.draw(Diego,ANCHO*0.65f,ALTO/6);
        batch.draw(Roberto,128,ALTO*0.6f);
        batch.draw(Mich,ANCHO*0.65f,ALTO*0.6f);
        batch.draw(name_Diego,138+ANCHO*0.65f,ALTO/6);
        batch.draw(name_Mich,138 + ANCHO*0.65f,ALTO*0.6f);
        batch.draw(name_Bruno,128 + 138,ALTO/6);
        batch.draw(name_Roberto,128 + 138,ALTO*0.6f);
        batch.end();
        escenaAcerca.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaAcerca.dispose();
        batch.dispose();
    }
}
