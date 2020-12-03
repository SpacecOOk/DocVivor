package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaInstrucciones extends Pantalla {
    private final Juego juego;


    public PantallaInstrucciones(Juego juego) {
        this.juego=juego;
    }

    private Stage escenaInstrucciones;
    private Texture texturaFondoInstrucciones;
    private Texture jump = new Texture("Instrucciones/jump.png");
    private Texture left = new Texture("Instrucciones/left.png");
    private Texture pause = new Texture("Instrucciones/pause.png");
    private Texture right = new Texture("Instrucciones/right.png");
    private Texture shoot = new Texture("Instrucciones/shoot.png");
    private Texture imgjump = new Texture("Botones/Boton_saltar_negro.png");
    private Texture imgleft = new Texture("Botones/Boton_izq_negro.png");
    private Texture imgpausa = new Texture("Botones/Boton_pausa.png");
    private Texture imgder = new Texture("Botones/Boton_der_negro.png");
    private Texture imgshoot = new Texture("Botones/Boton_disparo.png");

    @Override
    public void show() {
        texturaFondoInstrucciones = new Texture("Fondos/fondo_general.png");
        crearInstrucciones();
        Gdx.input.setCatchKey(Input.Keys.BACK,true);
    }

    private void crearInstrucciones() {
        escenaInstrucciones = new Stage(vista);
        //Boton para regresar
        Texture texturaRegresar = new Texture("Botones/btn_Exit.png");
        TextureRegionDrawable botonRegresarMenu = new TextureRegionDrawable(new TextureRegion(texturaRegresar));
        //Aqui para el boton inverso (click)
        ImageButton btnRegresar = new ImageButton(botonRegresarMenu);
        btnRegresar.setPosition(0+btnRegresar.getWidth(),ALTO-btnRegresar.getHeight(), Align.center);
        btnRegresar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Para poder regresar al menu principal
                juego.setScreen(new PantallaMenu(juego));
            }
        });
        escenaInstrucciones.addActor(btnRegresar);
        Gdx.input.setInputProcessor(escenaInstrucciones);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaFondoInstrucciones,0,0);
        batch.draw(imgpausa,ANCHO/2,25);
        batch.draw(pause,(ANCHO/2)+imgjump.getWidth(),25);
        batch.draw(imgjump,50,0+128);
        batch.draw(jump,50+imgjump.getWidth(),0+128);
        batch.draw(imgder,ANCHO/2,0+256);
        batch.draw(right,(ANCHO/2)+imgjump.getWidth(),0+256);
        batch.draw(imgleft,50,0+384);
        batch.draw(left,50+imgjump.getWidth(),0+384);
        batch.draw(imgshoot,ANCHO/2 ,0+512);
        batch.draw(shoot,(ANCHO/2)+imgjump.getWidth(),0+512);
        batch.end();
        escenaInstrucciones.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaFondoInstrucciones.dispose();
        right.dispose();
        left.dispose();
        jump.dispose();
        shoot.dispose();
        pause.dispose();
        imgjump.dispose();
        imgder.dispose();
        imgleft.dispose();
        imgpausa.dispose();
        imgshoot.dispose();
        batch.dispose();
    }
}
