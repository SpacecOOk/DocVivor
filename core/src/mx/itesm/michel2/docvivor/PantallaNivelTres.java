package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


//Pantalla Nivel 1
public class PantallaNivelTres extends Pantalla {
    private final Juego juego;

    //Fondo
    private Texture texturaFondoNivelTres;

    //Jugador
    private Texture texturaPersonaje;
    private Jugador jugador;
    private Texture texturaPersonajeTraje = new Texture("MovimientosMeleeTraje/Traje_M_I.png");

    //Contador de kills
    private int kills = 0;

    //Proyectil
    private Texture texturaProyectilD;
    private Proyectil proyectil;
    private Texture texturaProyectilI;
    private int orientacion;

    //Items
    private Texture texturaTraje = new Texture("Items/Item_SuperTraje.png");
    private Item traje;

    //Vidas
    private Image imagenVidas;
    private Sprite spriteVidas;
    private TextureRegion[][] texturasFramesVidas;
    private TextureRegion regionVidas ;
    private Texture texturaVida = new Texture("vidas.png");

    //Enemigos
    private Texture texturaEnemigoUno;
    private EnemigoUno enemigo;
    private Array<EnemigoUno> arrEnemigosDerecha;
    private Array<EnemigoUno> arrEnemigosIzquierda;
    private float timerCrearEnemigo;
    private float TIEMPO_CREA_ENEMIGO = 1;
    private float tiempoBase = 1;

    //HUD
    private Stage HUD;
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;

    //Pausa
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO;
    private EscenaPausa escenaPausa;
    private Stage escenaPausaHUD;
    private OrthographicCamera camaraPausaHUD;
    private Viewport vistaPausaHUD;

    //Derrota
    private EscenaDerrota escenaDerrota;
    //private Stage escenaDerrota;
    private OrthographicCamera camaraDerrotaHUD;
    private Viewport vistaDerrotaHUD;

    //Victoria
    private EscenaVictoria escenaVictoria;
    //private Stage escenaVictoria;
    private OrthographicCamera camaraVictoriaHUD;
    private Viewport vistaVictoriaHUD;

    //Musica
    private Sound efectoSalto;
    private Sound efectoDisparo;
    private Sound efectoMuerte;
    private Sound efectoMuerteEnemigo;
    private Sound efectoPowerUp;

    public PantallaNivelTres(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        texturaFondoNivelTres = new Texture("Level3/Level3_Background.png");
        crearHUD();
        crearPausa();
        crearDerrota();
        crearVictoria();
        cargarPreferencias();
        guardarPreferencias();
        crearPersonaje();
        crearEnemigos();
        crearProyectil();
        crearItem();
        if(juego.efectoSonidoEstado != 1){
            crearSonidos();
        }


        Gdx.input.setInputProcessor(HUD);
    }

    private void guardarPreferencias() {
        Preferences prefs = Gdx.app.getPreferences("efectoSonido");
        prefs.putFloat("efectoSonido", juego.efectoSonidoEstado);
        prefs.flush();  // OBLIGATORIO
    }

    private void cargarPreferencias() {
        Preferences prefs = Gdx.app.getPreferences("efectoSonido");
        juego.efectoSonidoEstado = (int)prefs.getFloat("efectoSonido");
    }

    private void crearSonidos() {
        AssetManager manager = new AssetManager();
        //cargamos todos los efectos que necesitaremos
        manager.load("Efectos_de_sonido/moneda.mp3", Sound.class);
        manager.load("Efectos_de_sonido/muerteDoc.mp3", Sound.class); //falta acortar el sonido del la tos
        manager.load("Efectos_de_sonido/saltoDoc.mp3", Sound.class);
        manager.finishLoading();
        //Asignamos los sonidos a las variables
        efectoDisparo = manager.get("Efectos_de_sonido/moneda.mp3");
        efectoMuerte = manager.get("Efectos_de_sonido/muerteDoc.mp3");
        efectoMuerteEnemigo = manager.get("Efectos_de_sonido/moneda.mp3");
        efectoPowerUp = manager.get("Efectos_de_sonido/moneda.mp3");
        efectoSalto = manager.get("Efectos_de_sonido/saltoDoc.mp3");
    }

    private void crearItem() {
        //int posicion = MathUtils.random(0,texturaFondoNivelUno.getWidth()-texturaTraje.getWidth());
        traje = new Item(texturaTraje,1590,133,48,49);
    }

    private void crearVictoria() {
        camaraVictoriaHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraVictoriaHUD.position.set(ANCHO/2,ALTO/2,0);
        camaraVictoriaHUD.update();
        vistaVictoriaHUD = new StretchViewport(ANCHO,ALTO,camaraVictoriaHUD);
    }

    private void crearDerrota() {
        camaraDerrotaHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraDerrotaHUD.position.set(ANCHO/2,ALTO/2,0);
        camaraDerrotaHUD.update();
        vistaDerrotaHUD = new StretchViewport(ANCHO,ALTO,camaraDerrotaHUD);
    }

    private void crearProyectil() {
        texturaProyectilD = new Texture("Balas/Bala_Jeringa_D.png");
        texturaProyectilI = new Texture("Balas/Bala_Jeringa_I.png");
    }

    private void crearPausa() {
        camaraPausaHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraPausaHUD.position.set(ANCHO/2,ALTO/2,0);
        camaraPausaHUD.update();
        vistaPausaHUD = new StretchViewport(ANCHO,ALTO,camaraPausaHUD);
    }


    private void crearEnemigos() {
        texturaEnemigoUno = new Texture("Enemigos/Enemigo_1.png");
        arrEnemigosDerecha = new Array<>();
        arrEnemigosIzquierda = new Array<>();
    }

    private void crearPersonaje() {
        texturaPersonaje = new Texture("MovimientosMelee/Doctor_M_I.png");
        jugador = new Jugador(texturaPersonaje,texturaFondoNivelTres.getWidth()/2,133);
    }

    private void crearHUD() {
        camaraHUD = new OrthographicCamera(ANCHO,ALTO);
        // Ajustar para el fondo
        camaraHUD.position.set(ANCHO/3,ALTO/2,0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO,ALTO,camaraHUD);
        HUD = new Stage(vistaHUD);
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
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(jugador.getEstado() == EstadoJugador.SALTANDO) {//Cuando camina a la derecha
                    jugador.setEstadoCaminando(EstadoCaminando.DERECHA);
                }else{
                    jugador.setEstadoCaminando(EstadoCaminando.DERECHA);
                    jugador.setEstado(EstadoJugador.CAMINANDO);
                }
                //jugador.setEstado(EstadoJugador.CAMINANDO);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (jugador.getEstado() == EstadoJugador.SALTANDO){
                    jugador.setEstadoCaminando(EstadoCaminando.QUIETO_DERECHA); //Revisar los quietos y donde van
                }else{
                    jugador.setEstadoCaminando(EstadoCaminando.QUIETO_DERECHA);          //Cuando deja de presionar el boton
                    jugador.setEstado(EstadoJugador.QUIETO);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
        //Le movi aqui
        HUD.addActor(btnMoverDerecha);

        //Boton mover izquierda
        Texture texturaMoverIzquierda = new Texture("Botones/Boton_izq_negro.png");
        final TextureRegionDrawable botonMoverIzquierda = new TextureRegionDrawable(new TextureRegion(texturaMoverIzquierda));
        //Aqui para el boton inverso (click)
        final ImageButton btnMoverIzquierda = new ImageButton(botonMoverIzquierda);
        btnMoverIzquierda.setPosition(0+btnMoverIzquierda.getWidth()/2,0+btnMoverIzquierda.getHeight()/2, Align.center);
        btnMoverIzquierda.addListener(new ClickListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(jugador.getEstado() == EstadoJugador.SALTANDO) {             //Cuando camina a la izquierda
                    jugador.setEstadoCaminando(EstadoCaminando.IZQUIERDA);
                }else{
                    jugador.setEstadoCaminando(EstadoCaminando.IZQUIERDA);
                    jugador.setEstado(EstadoJugador.CAMINANDO);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (jugador.getEstado() == EstadoJugador.SALTANDO){
                    jugador.setEstadoCaminando(EstadoCaminando.QUIETO_IZQUIERDA); //Revisar los quietos y donde van
                }else{
                    jugador.setEstadoCaminando(EstadoCaminando.QUIETO_IZQUIERDA);          //Cuando deja de presionar el boton
                    jugador.setEstado(EstadoJugador.QUIETO);
                }

                super.touchUp(event, x, y, pointer, button);
            }
        });

        //Le movi aqui
        HUD.addActor(btnMoverIzquierda);

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
                //Cuando le pica para atacar
                if (proyectil==null){ //si no existe la creo, sino no la crea
                    if(jugador.getEstadoCaminando() == EstadoCaminando.DERECHA || jugador.getEstadoCaminando() == EstadoCaminando.QUIETO_DERECHA){
                        proyectil = new Proyectil(texturaProyectilD,jugador.sprite.getX()+jugador.sprite.getWidth()/2,
                                jugador.sprite.getY()+jugador.sprite.getHeight()*0.3f);
                        orientacion=1;
                    }else if(jugador.getEstadoCaminando() == EstadoCaminando.IZQUIERDA || jugador.getEstadoCaminando() == EstadoCaminando.QUIETO_IZQUIERDA){
                        proyectil = new Proyectil(texturaProyectilI,jugador.sprite.getX()+jugador.sprite.getWidth()/2,
                                jugador.sprite.getY()+jugador.sprite.getHeight()*0.3f);
                        orientacion=0;
                    }
                }
                if (juego.efectoSonidoEstado != 1){
                    efectoDisparo.play();
                }
            } //Jeringa
        });

        //Le movi aqui
        HUD.addActor(btnAtacar);

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
                //Salta a la izquierda/derecha
                if(jugador.getEstado() != EstadoJugador.SALTANDO){
                    jugador.saltar();
                }
                if (juego.efectoSonidoEstado != 1){
                    efectoSalto.play();
                }
            }
        });

        //Le movi aqui
        HUD.addActor(btnSaltar);

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
                if (estadoJuego == EstadoJuego.JUGANDO){
                    estadoJuego = EstadoJuego.PAUSA;
                }
                if(escenaPausa == null){
                    escenaPausa = new EscenaPausa(vistaPausaHUD,batch); //La vista HUD no se mueve
                }
                Gdx.input.setInputProcessor(escenaPausa);
            }
        });

        //Le movi aqui
        HUD.addActor(btnPausa);

        regionVidas = new TextureRegion(texturaVida);
        texturasFramesVidas = regionVidas.split(34,34);
        spriteVidas = new Sprite(texturasFramesVidas[0][1]);
        imagenVidas = new Image(spriteVidas);
        imagenVidas.setPosition(50,650);

        HUD.addActor(imagenVidas);
    }

    @Override
    public void render(float delta) {
        actualizar();

        borrarPantalla(0,0,0.5f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        batch.draw(texturaFondoNivelTres,0,0);
        jugador.render(batch);
        dibujarEnemigosDerecha();
        dibujarEnemigosIzquierda();
        traje.render(batch);

        if(proyectil != null){
            proyectil.render(batch);
        }

        batch.end();

        //************ HUD ***************
        batch.setProjectionMatrix(camaraHUD.combined);
        HUD.draw();

        //*************Pausa*****************
        batch.setProjectionMatrix(camaraPausaHUD.combined);
        if(estadoJuego == EstadoJuego.PAUSA){
            escenaPausa.draw();
        }

        //************Derrota**************
        batch.setProjectionMatrix(camaraDerrotaHUD.combined);
        if (estadoJuego == EstadoJuego.DERROTA){
            escenaDerrota.draw();
        }

        //************Victoria**********
        batch.setProjectionMatrix(camaraVictoriaHUD.combined);
        if(estadoJuego == EstadoJuego.VICTORIA){
            escenaVictoria.draw();
        }


    }

    private void dibujarEnemigosIzquierda() {
        for (EnemigoUno enemigo : arrEnemigosIzquierda) {
            enemigo.render(batch);
            if(estadoJuego == EstadoJuego.JUGANDO){
                enemigo.setEstado(EstadoEnemigo.CAMINANDO);
                enemigo.setEstadoCaminando(EstadoEnemigoCaminando.DERECHA);
            }else{
                enemigo.setEstado(EstadoEnemigo.QUIETO);
                enemigo.setEstadoCaminando(EstadoEnemigoCaminando.QUIETO);
            }
        }
    }

    private void dibujarEnemigosDerecha() {
        for (EnemigoUno enemigo : arrEnemigosDerecha) {
            enemigo.render(batch);
            if(estadoJuego == EstadoJuego.JUGANDO){
                enemigo.setEstado(EstadoEnemigo.CAMINANDO);
                enemigo.setEstadoCaminando(EstadoEnemigoCaminando.IZQUIERDA);
            }else{
                enemigo.setEstado(EstadoEnemigo.QUIETO);
                enemigo.setEstadoCaminando(EstadoEnemigoCaminando.QUIETO);
            }
        }
    }

    private void actualizar() {
        actualizarCamara();
        actualizarEnemigosDerecha();
        actualizarEnemigosIzquierda();
        actualizarProyectil();
        actualizarVidas();

        //**********Colisiones*************
        verificarColisionesEnemigosDerecha();
        verificarColisionesEnemigosIzquierda();
        if(kills>=3 && estadoJuego != EstadoJuego.VICTORIA) {
            jugador.setEstado(EstadoJugador.QUIETO);
            estadoJuego = EstadoJuego.VICTORIA;
            escenaVictoria = new EscenaVictoria(vistaVictoriaHUD, batch);
            Gdx.input.setInputProcessor(escenaVictoria);
        }else{
            verificarColisionesProyectilDerecha();
            verificarColisionesProyectilIzquierda();
        }

        //************Items***************
        verificarColisionTraje();

    }

    private void verificarColisionTraje() {
        if (traje.sprite.getBoundingRectangle().overlaps(jugador.sprite.getBoundingRectangle())){
            if (juego.efectoSonidoEstado != 1){
                efectoPowerUp.play();
            }
            int x = (int)jugador.sprite.getX();
            traje.sprite.setY(ALTO);
            jugador = new Jugador(texturaPersonajeTraje,x,133);
            jugador.setVidas(4);
        }
    }

    private void verificarColisionesProyectilIzquierda() {
        if(estadoJuego == EstadoJuego.JUGANDO && kills < 3){
            if(proyectil != null){
                for (int j = arrEnemigosIzquierda.size-1; j >= 0 ; j--) {
                    EnemigoUno enemigo = arrEnemigosIzquierda.get(j);
                    if (proyectil.sprite.getBoundingRectangle().overlaps(enemigo.sprite.getBoundingRectangle())) {
                        //Si hay colisión
                        if (juego.efectoSonidoEstado != 1){
                            efectoMuerteEnemigo.play();
                        }
                        arrEnemigosIzquierda.removeIndex(j);
                        proyectil = null;
                        kills +=1;
                        break;
                    }
                }
            }
        }
    }

    private void verificarColisionesProyectilDerecha() {
        if(estadoJuego == EstadoJuego.JUGANDO && kills < 3){
            if(proyectil != null){
                for (int j = arrEnemigosDerecha.size-1; j >= 0 ; j--) {
                    EnemigoUno enemigo = arrEnemigosDerecha.get(j);
                    if (proyectil.sprite.getBoundingRectangle().overlaps(enemigo.sprite.getBoundingRectangle())) {
                        //Si hay colisión
                        if (juego.efectoSonidoEstado != 0){
                            efectoMuerteEnemigo.play();
                        }
                        arrEnemigosDerecha.removeIndex(j);
                        proyectil = null;
                        kills +=1;
                        break;
                    }
                }
            }
        }
    }

    private void actualizarVidas() {
        if(jugador.getVidas() == 4){
            spriteVidas = new Sprite(texturasFramesVidas[0][0]);
            imagenVidas = new Image(spriteVidas);
            imagenVidas.setPosition(50,650);
            HUD.addActor(imagenVidas);
        }else if(jugador.getVidas() == 3){
            spriteVidas = new Sprite(texturasFramesVidas[0][1]);
            imagenVidas = new Image(spriteVidas);
            imagenVidas.setPosition(50,650);
            HUD.addActor(imagenVidas);
        }else if(jugador.getVidas() == 2){
            spriteVidas = new Sprite(texturasFramesVidas[0][2]);
            imagenVidas = new Image(spriteVidas);
            imagenVidas.setPosition(50,650);
            HUD.addActor(imagenVidas);
        }else if(jugador.getVidas() == 1){
            spriteVidas = new Sprite(texturasFramesVidas[0][3]);
            imagenVidas = new Image(spriteVidas);
            imagenVidas.setPosition(50,650);
            HUD.addActor(imagenVidas);
        }else{
            spriteVidas = new Sprite(texturasFramesVidas[0][4]);
            imagenVidas = new Image(spriteVidas);
            imagenVidas.setPosition(50,650);
            HUD.addActor(imagenVidas);
        }

    }

    private void verificarColisionesEnemigosIzquierda() {
        for(int i =arrEnemigosIzquierda.size-1; i>=0; i--){
            EnemigoUno enemigoUno= arrEnemigosIzquierda.get(i);
            if (jugador.sprite.getBoundingRectangle().overlaps(enemigoUno.sprite.getBoundingRectangle())){
                //PERDIO
                if(jugador.getVidas()-1>0){
                    arrEnemigosIzquierda.removeIndex(i);
                    jugador.setVidas(jugador.getVidas()-1);
                }else{     //Murio
                    if (juego.efectoSonidoEstado != 1){
                        efectoMuerte.play();
                    }
                    jugador.setVidas(jugador.getVidas()-1);
                    jugador.setEstado(EstadoJugador.QUIETO);
                    jugador.sprite.setY(ANCHO); //Lo manda a lo alto
                    estadoJuego = EstadoJuego.DERROTA;
                    escenaDerrota = new EscenaDerrota(vistaDerrotaHUD,batch);
                    Gdx.input.setInputProcessor(escenaDerrota);
                }break;
            }
        }
    }

    private void verificarColisionesEnemigosDerecha() {
        for(int i =arrEnemigosDerecha.size-1; i>=0; i--){
            EnemigoUno enemigoUno= arrEnemigosDerecha.get(i);
            if (jugador.sprite.getBoundingRectangle().overlaps(enemigoUno.sprite.getBoundingRectangle())){
                //PERDIO
                if(jugador.getVidas()-1>0){
                    arrEnemigosDerecha.removeIndex(i);
                    jugador.setVidas(jugador.getVidas()-1);
                }else{     //Murio
                    jugador.setVidas(jugador.getVidas()-1);
                    jugador.setEstado(EstadoJugador.QUIETO);
                    jugador.sprite.setY(ANCHO); //Lo manda a lo alto
                    estadoJuego = EstadoJuego.DERROTA;
                    escenaDerrota = new EscenaDerrota(vistaDerrotaHUD,batch);
                    Gdx.input.setInputProcessor(escenaDerrota);
                }break;
            }
        }
    }

    private void actualizarEnemigosIzquierda() {        //Los que van para el lado izquierdo
        timerCrearEnemigo += Gdx.graphics.getDeltaTime();
        if(estadoJuego == EstadoJuego.JUGANDO){
            if (timerCrearEnemigo >= TIEMPO_CREA_ENEMIGO){
                timerCrearEnemigo = 0;
                TIEMPO_CREA_ENEMIGO = 2 + MathUtils.random()*2;
                EnemigoUno enemigo = new EnemigoUno(texturaEnemigoUno,0,133);
                arrEnemigosIzquierda.add(enemigo);
            }
            for (int i = arrEnemigosIzquierda.size-1; i >= 0; i--) {
                EnemigoUno enemigo = arrEnemigosIzquierda.get(i);
                if (enemigo.sprite.getX()> jugador.sprite.getX() + ANCHO/2 + enemigo.sprite.getWidth() ){
                    arrEnemigosIzquierda.removeIndex(i);
                }
            }
        }
    }

    private void actualizarProyectil() {    //aqui esta el problema
        if(proyectil != null) {
            proyectil.mover(orientacion);
            if (proyectil.sprite.getX() > jugador.sprite.getX() + ANCHO / 2) {
                proyectil = null;
            } else if (proyectil.sprite.getX() < jugador.sprite.getX() - ANCHO / 2) {
                proyectil = null;
            }
        }
    }

    private void actualizarEnemigosDerecha() {            //Los que van para el lado derecho
        timerCrearEnemigo += Gdx.graphics.getDeltaTime();
        if(estadoJuego == EstadoJuego.JUGANDO){
            if (timerCrearEnemigo >= TIEMPO_CREA_ENEMIGO){
                timerCrearEnemigo = 0;
                TIEMPO_CREA_ENEMIGO = 2 + MathUtils.random()*2;
                EnemigoUno enemigo = new EnemigoUno(texturaEnemigoUno,texturaFondoNivelTres.getWidth(),133);
                arrEnemigosDerecha.add(enemigo);
            }
            for (int i = arrEnemigosDerecha.size-1; i >= 0; i--) {
                EnemigoUno enemigo = arrEnemigosDerecha.get(i);
                if (enemigo.sprite.getX()< jugador.sprite.getX() - ANCHO/2 -enemigo.sprite.getWidth() ){
                    arrEnemigosDerecha.removeIndex(i);
                }
            }
        }
    }

    private void actualizarCamara() {
        float xCamara = camara.position.x;
        if (jugador.sprite.getX() < ANCHO/2){
            xCamara = ANCHO/2;
        }else if (jugador.sprite.getX() > texturaFondoNivelTres.getWidth()-ANCHO/2){
            xCamara = texturaFondoNivelTres.getWidth() - ANCHO/2; //checar para llegar al limite del mapa
        }else {
            xCamara = jugador.sprite.getX();
        }
        camara.position.x = xCamara;
        camara.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaFondoNivelTres.dispose();
        batch.dispose();
    }

    private enum EstadoJuego {
        JUGANDO,
        PAUSA,
        DERROTA,
        VICTORIA
    }

    private class EscenaPausa extends Stage {

        public EscenaPausa(Viewport vista, SpriteBatch batch) {
            super(vista, batch);

            Texture texturaFondoPausa = new Texture("Fondos/fondoPausa.png");
            Image imgFondoPausa = new Image(texturaFondoPausa);
            imgFondoPausa.setPosition(ANCHO/2 - texturaFondoPausa.getWidth()/2,
                    ALTO/2 - texturaFondoPausa.getHeight()/2);
            this.addActor(imgFondoPausa);

            Texture texturaBtnRegresar = new Texture("Botones/Btn_resume.png");
            TextureRegionDrawable botonRegresar = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
            //Aqui para el boton inverso (click)
            ImageButton btnRegresar = new ImageButton(botonRegresar);
            btnRegresar.setPosition(ANCHO*0.4f, ALTO*0.6f, Align.center);
            btnRegresar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para regresar al juego
                    estadoJuego = EstadoJuego.JUGANDO;
                    Gdx.input.setInputProcessor(HUD);
                }
            });
            this.addActor(btnRegresar);
            Texture texturaBtnMenu = new Texture("Botones/btn_Exit.png");
            TextureRegionDrawable botonMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));
            //Aqui para el boton inverso (click)
            ImageButton btnMenu = new ImageButton(botonMenu);
            btnMenu.setPosition(ANCHO*0.65f, ALTO*0.6f, Align.center);
            btnMenu.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para regresar al juego
                    estadoJuego = EstadoJuego.JUGANDO;
                    juego.setScreen(new PantallaMenu(juego));
                }
            });
            this.addActor(btnMenu);
        }
    }

    private class EscenaDerrota extends Stage {
        public EscenaDerrota(Viewport vista, SpriteBatch batch) {
            super(vista, batch);

            Texture texturaFondoPausa = new Texture("Fondos/GameOver.png");
            Image imgFondoPausa = new Image(texturaFondoPausa);
            imgFondoPausa.setPosition(ANCHO/2 - texturaFondoPausa.getWidth()/2,
                    ALTO/2 - texturaFondoPausa.getHeight()/2);
            this.addActor(imgFondoPausa);

            Texture texturaBtnRetry = new Texture("Botones/btn_jugar.png");
            TextureRegionDrawable botonRetry = new TextureRegionDrawable(new TextureRegion(texturaBtnRetry));
            //Aqui para el boton inverso (click)
            ImageButton btnRetry = new ImageButton(botonRetry);
            btnRetry.setPosition(ANCHO*0.35f, ALTO*0.2f, Align.center);
            btnRetry.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para reintentar el nivel
                    juego.setScreen(new PantallaNivelUno(juego));

                }
            });
            this.addActor(btnRetry);

            Texture texturaBtnNiveles = new Texture("Botones/btn_Exit.png");
            TextureRegionDrawable botonNiveles = new TextureRegionDrawable(new TextureRegion(texturaBtnNiveles));
            //Aqui para el boton inverso (click)
            ImageButton btnNiveles = new ImageButton(botonNiveles);
            btnNiveles.setPosition(ANCHO*0.65f, ALTO*0.2f, Align.center);
            btnNiveles.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para regresar al los niveles
                    borrarPantalla();
                    juego.setScreen(new PantallaNiveles(juego));
                }
            });
            this.addActor(btnNiveles);
        }
    }

    private class EscenaVictoria extends Stage {
        public EscenaVictoria(Viewport vista, SpriteBatch batch) {
            super(vista, batch);

            Texture texturaFondoVictoria = new Texture("Fondos/Victory.png");
            Image imgFondoVictoria = new Image(texturaFondoVictoria);
            imgFondoVictoria.setPosition(ANCHO/2 - texturaFondoVictoria.getWidth()/2,
                    ALTO/2 - texturaFondoVictoria.getHeight()/2);
            this.addActor(imgFondoVictoria);

            Texture texturaBtnSeguir = new Texture("Botones/btn_jugar.png");
            TextureRegionDrawable botonSeguir = new TextureRegionDrawable(new TextureRegion(texturaBtnSeguir));
            //Aqui para el boton inverso (click)
            ImageButton btnSeguir = new ImageButton(botonSeguir);
            btnSeguir.setPosition(ANCHO*0.35f, ALTO*0.2f, Align.center);
            btnSeguir.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para ir a los creditos
                    juego.setScreen(new PantallaAcercaDe(juego));
                }
            });
            this.addActor(btnSeguir);

            Texture texturaBtnNiveles = new Texture("Botones/btn_Exit.png");
            TextureRegionDrawable botonNiveles = new TextureRegionDrawable(new TextureRegion(texturaBtnNiveles));
            //Aqui para el boton inverso (click)
            ImageButton btnNiveles = new ImageButton(botonNiveles);
            btnNiveles.setPosition(ANCHO*0.65f, ALTO*0.2f, Align.center);
            btnNiveles.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    juego.setScreen(new PantallaNiveles(juego));
                }
            });
            this.addActor(btnNiveles);
        }
    }
}
