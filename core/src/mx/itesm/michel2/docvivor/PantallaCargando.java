package mx.itesm.michel2.docvivor;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PantallaCargando extends Pantalla
{
    // Animación cargando (espera...)
    private Sprite spriteCargando;
    public static final float TIEMPO_ENTRE_FRAMES = 0.05f;
    private float timerAnimacion = TIEMPO_ENTRE_FRAMES;
    private Texture texturaCargando;

    //fondo
    private Texture texturaFondo;

    // AssetManager
    private AssetManager manager;

    // Referencia al juego
    private Juego juego;    // Para hacer setScreen

    // Identifica las pantallas del juego
    private Pantallas siguientePantalla;

    // % de carga
    private int avance;


    public PantallaCargando(Juego juego, Pantallas siguientePantalla) {
        this.juego = juego;
        this.siguientePantalla = siguientePantalla;
    }

    @Override
    public void show() {
        texturaCargando = new Texture("loading.png");
        texturaFondo = new Texture("Fondos/fondo_general.png");
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(ANCHO/2 - spriteCargando.getWidth()/2,
                ALTO/2 - spriteCargando.getHeight()/2);
        cargarRecursos(siguientePantalla);
        Gdx.input.setCatchKey(Input.Keys.BACK,true);
    }

    private void cargarRecursos(Pantallas siguientePantalla) {
        manager = juego.getManager();
        avance = 0; // % de carga
        switch (siguientePantalla) {
            case MENU:
                cargarRecursosMenu();
                break;
            case NIVEL1:
                cargarRecursosNivel1();
            break;
            case NIVEL2:
                cargarRecursosNivel2();
             break;
            case NIVEL3:
                cargarRecursosNivel3();
             break;

            /*case REVISARENUMPANTALLAS:
                cargarRecursosNOMBREDELAPANTALLA();
                break;
             */
            // Agregar otras pantallas
        }
    }

    private void cargarRecursosNivel3() {
        manager.load("vidas.png",Texture.class);
        manager.load("Item_corazon.png",Texture.class);
        manager.load("enemigoFinal.png",Texture.class);
        manager.load("Enemigo_LvL2.png",Texture.class);
        manager.load("Enemigo3.png",Texture.class);
        manager.load("Balas/Bala_roja.png",Texture.class);
        manager.load("Balas/Bala_Jeringa_D.png",Texture.class);
        manager.load("Balas/Bala_Jeringa_I.png",Texture.class);
        manager.load("Level2/AssetsPersonajes/Doctor2_moviendose.png",Texture.class);
        manager.load("Botones/Boton_der_blanco.png",Texture.class);
        manager.load("Botones/Boton_izq_blanco.png",Texture.class);
        manager.load("Botones/Boton_disparo_blanco.png",Texture.class);
        manager.load("Botones/Boton_saltar_blanco.png",Texture.class);
        manager.load("Botones/Boton_saltar_blanco.png",Texture.class);
        manager.load("Botones/Boton_pausa_blanco.png",Texture.class);
        manager.load("Fondos/fondoPausa.png",Texture.class);
        manager.load("Botones/Btn_resume.png",Texture.class);
        manager.load("Botones/btn_Exit.png",Texture.class);
        manager.load("Fondos/GameOver.png",Texture.class);
        manager.load("Botones/btn_jugar.png",Texture.class);
        manager.load("Botones/btn_Exit.png",Texture.class);
        manager.load("Fondos/Win.png",Texture.class);
        manager.load("Botones/btn_Exit.png",Texture.class);
    }

    private void cargarRecursosNivel2() {
        //manager.load("Level2/AssetsPersonajes/personajeArma1.png",Texture.class);
        manager.load("vidas.png",Texture.class);
        manager.load("Items/metralleta.png",Texture.class);
        //manager.load("Level2/AssetsPersonajes/Enemigo_LvL2.png",Texture.class);
        manager.load("Balas/Bala_Jeringa_D.png",Texture.class);
        manager.load("Balas/Bala_Jeringa_I.png",Texture.class);
        manager.load("Level2/AssetsPersonajes/Doctor2_moviendose.png",Texture.class);
        manager.load("Botones/Boton_der_negro.png",Texture.class);
        manager.load("Botones/Boton_izq_negro.png",Texture.class);
        manager.load("Botones/Boton_disparo.png",Texture.class);
        manager.load("Botones/Boton_saltar_negro.png",Texture.class);
        manager.load("Botones/Boton_pausa.png",Texture.class);
        manager.load("Fondos/fondoPausa.png",Texture.class);
        manager.load("Botones/Btn_resume.png",Texture.class);
        manager.load("Botones/btn_Exit.png",Texture.class);
        manager.load("Fondos/GameOver.png",Texture.class);
        manager.load("Botones/btn_jugar.png",Texture.class);
        manager.load("Botones/btn_Exit.png",Texture.class);
        manager.load("Fondos/Victory.png",Texture.class);
        manager.load("Botones/btn_jugar.png",Texture.class);
        manager.load("Botones/btn_Exit.png",Texture.class);
    }

    private void cargarRecursosNivel1() {
        manager.load("MovimientosMeleeTraje/Traje_M_I.png",Texture.class);
        manager.load("Items/Item_SuperTraje.png",Texture.class);
        manager.load("vidas.png",Texture.class);
        manager.load("Level1/Level1_Background.png",Texture.class);
        manager.load("Balas/Bala_Jeringa_D.png",Texture.class);
        manager.load("Balas/Bala_Jeringa_I.png",Texture.class);
        manager.load("Enemigos/Enemigo_1.png",Texture.class);
        manager.load("MovimientosMelee/Doctor_M_I.png",Texture.class);
        manager.load("Botones/Boton_der_negro.png",Texture.class);
        manager.load("Botones/Boton_izq_negro.png",Texture.class);
        manager.load("Botones/Boton_disparo.png",Texture.class);
        manager.load("Botones/Boton_saltar_negro.png",Texture.class);
        manager.load("Botones/Boton_pausa.png",Texture.class);
        manager.load("Fondos/fondoPausa.png",Texture.class);
        manager.load("Botones/Btn_resume.png",Texture.class);
        manager.load("Botones/btn_Exit.png",Texture.class);
        manager.load("Fondos/GameOver.png",Texture.class);
        manager.load("Botones/btn_jugar.png",Texture.class);
        manager.load("Botones/btn_Exit.png",Texture.class);
        manager.load("Fondos/Victory.png",Texture.class);
        manager.load("Botones/btn_jugar.png",Texture.class);
        manager.load("Botones/btn_Exit.png",Texture.class);
    }

    private void cargarRecursosMenu() {
        manager.load("Fondos/fondo_general.png",Texture.class);
        manager.load("titulo.png",Texture.class);
        manager.load("Botones/btn_jugar.png",Texture.class);
        manager.load("Botones/btn_Configuracion.png",Texture.class);
        manager.load("Botones/btn_Acerca.png",Texture.class);
        manager.load("Botones/btn_Help.png",Texture.class);
    }



    @Override
    public void render(float delta) {
        borrarPantalla(0.1f, 0.5f, 0.1f);

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo,0,0);
        spriteCargando.draw(batch);
        batch.end();

        // Actualizar
        timerAnimacion -= delta;
        if (timerAnimacion<=0) {
            timerAnimacion = TIEMPO_ENTRE_FRAMES;
            spriteCargando.rotate(20);
        }

        // Actualizar carga
        actualizarCarga();
    }

    private void actualizarCarga() {
        // ¿Cómo va la carga de recursos?
        if (manager.update()) {
            // Terminó!
            switch (siguientePantalla) {
                case MENU:
                    juego.setScreen(new PantallaMenu(juego));
                    break;
                case NIVEL1:
                    juego.setScreen(new PantallaNivelUno(juego));
                    break;
                case NIVEL2:
                    juego.setScreen(new PantallaNivelDos(juego));
                    break;
                case NIVEL3:
                    juego.setScreen(new PantallaNivelTres(juego));
                    break;
                // Agregar las otras pantallas
            }
        }

        avance = (int)(manager.getProgress()*100);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaCargando.dispose();
        texturaFondo.dispose();
    }
}
