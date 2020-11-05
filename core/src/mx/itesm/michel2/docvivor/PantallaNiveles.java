package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaNiveles extends Pantalla {
    private  final Juego juego;


    private Texture texturaNiveles;
    private Stage escenaNiveles;

    public PantallaNiveles(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        texturaNiveles = new Texture("Fondos/fondo_general.png");
        crearPantallaNiveles();
    }

    private void crearPantallaNiveles() {
        escenaNiveles = new Stage(vista);
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
        escenaNiveles.addActor(btnRegresar);
        //Boton de nivel 1
        Texture texturaNivelUno = new Texture("Botones/btn_lvl1.png");
        TextureRegionDrawable botonNivelUno = new TextureRegionDrawable(new TextureRegion(texturaNivelUno));
        //Aqui seria para el boton inverso
        ImageButton btnNivelUno = new ImageButton(botonNivelUno);
        btnNivelUno.setPosition(0+btnNivelUno.getWidth(),0+btnNivelUno.getHeight(),Align.center);
        btnNivelUno.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Para poder entrar al nivel uno
                juego.setScreen(new PantallaNivelUno(juego));
            }
        });
        escenaNiveles.addActor(btnNivelUno);
        //Boton de nivel 2
        Texture texturaNivelDos = new Texture("Botones/btn_lvl2.png");
        TextureRegionDrawable botonNivelDos = new TextureRegionDrawable(new TextureRegion(texturaNivelDos));
        //Aqui seria para el boton inverso
        ImageButton btnNivelDos = new ImageButton(botonNivelDos);
        btnNivelDos.setPosition(ANCHO/2,ALTO/2,Align.center);
        btnNivelDos.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Para poder entrar al nivel uno
                juego.setScreen(new PantallaNivelDos(juego));
            }
        });
        escenaNiveles.addActor(btnNivelDos);
        //Boton de nivel 3
        Texture texturaNivelTres = new Texture("Botones/btn_lvl3.png");
        TextureRegionDrawable botonNivelTres = new TextureRegionDrawable(new TextureRegion(texturaNivelTres));
        //Aqui seria para el boton inverso
        ImageButton btnNivelTres = new ImageButton(botonNivelTres);
        btnNivelTres.setPosition(ANCHO-btnNivelUno.getWidth(),ALTO-btnNivelUno.getHeight(),Align.center);
        btnNivelTres.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Para poder entrar al nivel uno
                juego.setScreen(new PantallaNivelTres(juego));
            }
        });
        escenaNiveles.addActor(btnNivelTres);

        Gdx.input.setInputProcessor(escenaNiveles);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaNiveles,0,0);
        batch.end();
        escenaNiveles.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaNiveles.dispose();
        batch.dispose();
    }
}
