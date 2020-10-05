package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class PantallaNivelUno extends Pantalla {
    private final Juego juego;

    private Stage escenaNivelUno;
    private Texture texturaFondoNivelUno;

    //Jugador
    private Texture texturaPersonaje;
    private Jugador jugador;

    //Vidas
    private Texture texturaVidas = new Texture("vidas.png");

    //Enemigos
    private Texture texturaEnemigoUno;
    private Enemigo enemigoUno;

    //Marcador
    private float puntos;

    public PantallaNivelUno(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        texturaFondoNivelUno = new Texture("Fondos/level1_Background.png");
        crearNivelUno();
        crearPersonaje();
        crearEnemigos();
    }

    private void crearEnemigos() {
        texturaEnemigoUno = new Texture("Enemigos/enemigoUno.png");
        enemigoUno = new Enemigo(texturaEnemigoUno,ANCHO-200,133);
    }

    private void crearPersonaje() {
        texturaPersonaje = new Texture("Jugador.png");
        jugador = new Jugador(texturaPersonaje,100,133);
    }

    private void crearNivelUno() {
        escenaNivelUno = new Stage(vista);
        //Boton mover a la derecha
        Texture texturaMoverDerecha = new Texture("Botones/Boton_der_negro.png");
        TextureRegionDrawable botonMoverDerecha = new TextureRegionDrawable(new TextureRegion(texturaMoverDerecha));
        //Aqui para el boton inverso (click)
        ImageButton btnMoverDerecha = new ImageButton(botonMoverDerecha);
        btnMoverDerecha.setPosition(0+(btnMoverDerecha.getWidth()/2)*3,0+btnMoverDerecha.getHeight()/2, Align.center);
        btnMoverDerecha.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cuando le pica se mueve a la derecha, falta implementar el movimiento
                jugador.moverDerecha();
            }
        });
        escenaNivelUno.addActor(btnMoverDerecha);
        //Boton mover izquierda
        Texture texturaMoverIzquierda = new Texture("Botones/Boton_izq_negro.png");
        final TextureRegionDrawable botonMoverIzquierda = new TextureRegionDrawable(new TextureRegion(texturaMoverIzquierda));
        //Aqui para el boton inverso (click)
        final ImageButton btnMoverIzquierda = new ImageButton(botonMoverIzquierda);
        btnMoverIzquierda.setPosition(0+btnMoverIzquierda.getWidth()/2,0+btnMoverIzquierda.getHeight()/2, Align.center);
        btnMoverIzquierda.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cuando le pica se mueve a la izquierda, falta implementar el movimiento
                jugador.moverIzquierda();
            }
        });
        escenaNivelUno.addActor(btnMoverIzquierda);
        //Boton atacar
        Texture texturaAtacar = new Texture("Botones/Boton_disparo.png");
        TextureRegionDrawable botonAtacar = new TextureRegionDrawable(new TextureRegion(texturaAtacar));
        //Aqui para el boton inverso (click)
        ImageButton btnAtacar = new ImageButton(botonAtacar);
        btnAtacar.setPosition(ANCHO-(btnAtacar.getWidth()/2)*3,0+btnAtacar.getHeight()/2, Align.center);
        btnAtacar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cuando le pica se mueve a la izquierda, falta implementar el movimiento

            }
        });
        escenaNivelUno.addActor(btnAtacar);
        //Boton para saltar
        Texture texturaSaltar = new Texture("Botones/Boton_saltar_negro.png");
        TextureRegionDrawable botonSaltar = new TextureRegionDrawable(new TextureRegion(texturaSaltar));
        //Aqui para el boton inverso (click)
        ImageButton btnSaltar = new ImageButton(botonSaltar);
        btnSaltar.setPosition(ANCHO-btnSaltar.getWidth()/2,0+btnSaltar.getHeight()/2, Align.center);
        btnSaltar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cuando le pica se mueve a la izquierda, falta implementar el movimiento

            }
        });
        escenaNivelUno.addActor(btnSaltar);
        //Boton para pausa
        Texture texturaPausa = new Texture("Botones/Boton_pausa.png");
        TextureRegionDrawable botonPausa = new TextureRegionDrawable(new TextureRegion(texturaPausa));
        //Aqui para el boton inverso (click)
        ImageButton btnPausa = new ImageButton(botonPausa);
        btnPausa.setPosition(ANCHO-btnSaltar.getWidth()/2,ALTO-btnPausa.getHeight()/2, Align.center);
        btnPausa.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cuando le pica se pausa el juego, falta implementarlo
                //Crea la mini pantalla de pausa
                juego.pause();
                juego.setScreen(new PantallaPausa(juego));

            }
        });
        escenaNivelUno.addActor(btnPausa);
        Gdx.input.setInputProcessor(escenaNivelUno);

    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaFondoNivelUno,0,0);
        jugador.render(batch);
        enemigoUno.render(batch);
        //Dibujo de vidas, cambiar despues
        batch.draw(texturaVidas,0, ALTO-texturaVidas.getHeight());
        batch.draw(texturaVidas,0+texturaVidas.getWidth(), ALTO-texturaVidas.getHeight());
        batch.draw(texturaVidas,0+2*(texturaVidas.getWidth()),ALTO-texturaVidas.getHeight());

        batch.end();
        escenaNivelUno.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaFondoNivelUno.dispose();
        batch.dispose();
    }
}
