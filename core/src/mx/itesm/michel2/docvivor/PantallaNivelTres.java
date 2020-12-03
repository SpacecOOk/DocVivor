package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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


//Pantalla Nivel 3
public class PantallaNivelTres extends Pantalla {
    private final Juego juego;
    public static final float ANCHO_MAPA = 559*32; //CAMBIAR EL ANCHO CUANDO ESTE EL MAPA
    //fondo
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;

    //Manager
    private AssetManager manager;

    //HUD
    private Stage HUD;
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;

    //jugador
    private Texture texturaPersonaje;
    private JugadorPlataformas jugador;
    public static final int TAM_CELDA = 32;
    private Rectangle rectangleJugador;

    //vidas
    private Image imagenVidas;
    private Sprite spriteVidas;
    private TextureRegion[][] texturasFramesVidas;
    private TextureRegion regionVidas;
    private Texture texturaVida = new Texture("vidas.png");

    private Image imagenVidasEnemigos;
    private Sprite spriteVidasEnemigos;
    private TextureRegion[][] texturasFramesVidasEnemigos;
    private TextureRegion regionVidasEnemigo;
    private Texture texturaVidasEnemigo = new Texture("vida_barraEnemigo.png");

    //Proyectil
    private Texture texturaProyectilD;
    private Texture texturaProyectilI;
    private Texture texturaProyectilEnemigo;
    private Proyectil  proyectilJefeFinal;
    private Proyectil proyectil;
    private int orientacion;

    //Sonidos
    private Sound efectoSalto;
    private Sound efectoDisparo;
    private Sound efectoMuerte;
    private Sound efectoMuerteEnemigo;
    private Sound efectoPowerUp;
    private Sound efectoMuerteEnemigoDos;
    private Sound efectoMuerteJefe;

    //Pausa
    private EstadoJuego estadoJuego = PantallaNivelTres.EstadoJuego.JUGANDO;
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

    //Enemigos
    private Texture texturaEnemigoFinal;
    private Texture texturaEnemigoUno;
    private Texture texturaEnemigoDos;
    private Array<EnemigoUnoPlataformas> arrEnemigosUno;
    private Array<EnemigoDosPlataformas> arrEnemigosDos;
    private int[] posicionesEnemigos;
    private int[] posicionesEnemigosDos;
    private JefeFinal enemigoFinal;
    private Rectangle rectangleEnemigo;
    private Rectangle auraEnemigo;

    //Item
    private Texture texturaItemCorazon;
    private Item corazonUno;
    private Item corazonDos;


    public PantallaNivelTres(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        manager = new AssetManager();
        crearPersonaje();
        crearmapa();
        crearHUD();
        crearPausa();
        crearDerrota();
        crearVictoria();
        crearProyectil();
        cargarPreferencias();
        guardarPreferencias();
        crearPosiciones();
        crearPosicionesDos();
        crearEnemigos();
        crearEnemigoFInal();
        crearItem();
        if(juego.efectoSonidoEstado != 1){
            crearSonidos();
        }
        Gdx.input.setInputProcessor(HUD);
        Gdx.input.setCatchKey(Input.Keys.BACK,true);
    }

    private void crearItem() {
        texturaItemCorazon = new Texture("Item_corazon.png");
        corazonUno = new Item(texturaItemCorazon,148*32,12*32,34,42);
        corazonDos = new Item(texturaItemCorazon,516*32,11*32,34,42);
    }

    private void crearEnemigoFInal() {
        texturaEnemigoFinal = new Texture("enemigoFinal.png");
        enemigoFinal = new JefeFinal(texturaEnemigoFinal, 544*32,600,224,224);
        rectangleEnemigo = enemigoFinal.getSprite().getBoundingRectangle().setSize(enemigoFinal.getSprite().getWidth()*.75f,enemigoFinal.getSprite().getHeight()*.75f);
        auraEnemigo = enemigoFinal.getSprite().getBoundingRectangle().setSize(enemigoFinal.getSprite().getWidth()*.90f,enemigoFinal.getSprite().getHeight()*.90f);

    }

    private void crearPosicionesDos() {
        posicionesEnemigosDos = new int[5];
        int x = MathUtils.random(161*32,177*32);
        posicionesEnemigos[0] = x;
        int y = MathUtils.random(195*32,214*32);
        posicionesEnemigos[1] = y;
        int z = MathUtils.random(516*32,540*32); //Verificar por lo del jefe final
        posicionesEnemigos[2] = z;
        int w = MathUtils.random(270*32,276*32);
        posicionesEnemigos[3] = w;
        int t = MathUtils.random(459*32,476*32);
        posicionesEnemigos[4] = t;
    }

    private void crearPosiciones() {
        posicionesEnemigos = new int[10];
        int a = MathUtils.random(70*32,75*32);
        posicionesEnemigos[0] = a;
        int b = MathUtils.random(83*32, 89*32);
        posicionesEnemigos[1] = b;
        int c = MathUtils.random(166*32,177*32);
        posicionesEnemigos[2] = c;
        int d = MathUtils.random(194*32,216*32);
        posicionesEnemigos[3] = d;
        int e = MathUtils.random(234*32,240*32);
        posicionesEnemigos[4] = e;
        int f = MathUtils.random(249*32, 258*32);
        posicionesEnemigos[5] = f;
        int g = MathUtils.random(261*32,278*32);
        posicionesEnemigos[6] = g;
        int h = MathUtils.random(290*32,304*32);
        posicionesEnemigos[7] = h;
        int i = MathUtils.random(459*32,476*32);
        posicionesEnemigos[8] = i;
        int j = MathUtils.random(495*32,533*32); //depende por la posicion del jefe final
        posicionesEnemigos[9] = j;
    }

    private void crearEnemigos() {
        texturaEnemigoUno = new Texture("Enemigo_LvL2.png");
        texturaEnemigoDos = new Texture("Enemigo3.png");
        arrEnemigosUno = new Array<>();
        arrEnemigosDos = new Array<>();
        for (int i = 0; i < 16; i++) {
            EnemigoUnoPlataformas enemigo = new EnemigoUnoPlataformas(texturaEnemigoUno);
            int x = MathUtils.random(0,posicionesEnemigos.length-1);
            enemigo.getSprite().setPosition(posicionesEnemigos[x],25*32);
            arrEnemigosUno.add(enemigo);
            crearPosiciones();
        }
        for (int i = 0; i < 8; i++) {
            EnemigoDosPlataformas enemigoDos = new EnemigoDosPlataformas(texturaEnemigoDos);
            int x = MathUtils.random(0,posicionesEnemigosDos.length-1);
            enemigoDos.getSprite().setPosition(posicionesEnemigos[x],25*32);
            arrEnemigosDos.add(enemigoDos);
            crearPosicionesDos();
        }
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
        //cargamos todos los efectos que necesitaremos
        manager.load("Efectos_de_sonido/powerup.mp3", Sound.class);
        manager.load("Efectos_de_sonido/muerteDoc.mp3", Sound.class);
        manager.load("Efectos_de_sonido/saltoDoc.mp3", Sound.class);
        manager.load("Efectos_de_sonido/disparo.mp3", Sound.class);
        manager.load("Efectos_de_sonido/monster1.mp3", Sound.class);
        manager.load("Efectos_de_sonido/sonidoMonstruo.mp3",Sound.class);
        manager.load("enemigoFinalMuerte.mp3",Sound.class);
        manager.finishLoading();
        //Asignamos los sonidos a las variables
        efectoDisparo = manager.get("Efectos_de_sonido/disparo.mp3");
        efectoMuerte = manager.get("Efectos_de_sonido/muerteDoc.mp3");
        efectoMuerteEnemigo = manager.get("Efectos_de_sonido/monster1.mp3");
        efectoPowerUp = manager.get("Efectos_de_sonido/powerup.mp3");
        efectoSalto = manager.get("Efectos_de_sonido/saltoDoc.mp3");
        efectoMuerteEnemigoDos = manager.get("Efectos_de_sonido/sonidoMonstruo.mp3");
        efectoMuerteJefe = manager.get("enemigoFinalMuerte.mp3");
    }

    private void crearProyectil() {
        texturaProyectilEnemigo = new Texture("Balas/Bala_roja.png");
        texturaProyectilD = new Texture("Balas/Bala_Jeringa_D.png");
        texturaProyectilI = new Texture("Balas/Bala_Jeringa_I.png");
    }

    private void crearPersonaje() {
        texturaPersonaje = new Texture("Level2/AssetsPersonajes/Doctor2_moviendose.png");
        jugador = new JugadorPlataformas(texturaPersonaje,56,55);
        jugador.getSprite().setPosition(100,300);//100 300
        rectangleJugador = jugador.getSprite().getBoundingRectangle().setSize(texturaPersonaje.getWidth()*.28f,texturaPersonaje.getHeight()*.8f);

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

    private void crearPausa() {
        camaraPausaHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraPausaHUD.position.set(ANCHO/2,ALTO/2,0);
        camaraPausaHUD.update();
        vistaPausaHUD = new StretchViewport(ANCHO,ALTO,camaraPausaHUD);
    }

    private void crearHUD() {
        camaraHUD = new OrthographicCamera(ANCHO, ALTO);
        // Ajustar para el fondo
        camaraHUD.position.set(ANCHO / 3, ALTO / 2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);
        HUD = new Stage(vistaHUD);
        //Boton mover a la derecha
        Texture texturaMoverDerecha = new Texture("Botones/Boton_der_blanco.png");
        TextureRegionDrawable botonMoverDerecha = new TextureRegionDrawable(new TextureRegion(texturaMoverDerecha));

        //Aqui para el boton inverso (click)
        ImageButton btnMoverDerecha = new ImageButton(botonMoverDerecha);
        btnMoverDerecha.setPosition(0 + (btnMoverDerecha.getWidth() / 2) * 3, 0 + btnMoverDerecha.getHeight() / 2, Align.center);
        btnMoverDerecha.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.MOV_DERECHA);

                //jugador.setEstado(EstadoJugador.CAMINANDO);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        //Le movi aqui
        HUD.addActor(btnMoverDerecha);

        //Boton mover izquierda
        Texture texturaMoverIzquierda = new Texture("Botones/Boton_izq_blanco.png");
        final TextureRegionDrawable botonMoverIzquierda = new TextureRegionDrawable(new TextureRegion(texturaMoverIzquierda));
        //Aqui para el boton inverso (click)
        final ImageButton btnMoverIzquierda = new ImageButton(botonMoverIzquierda);
        btnMoverIzquierda.setPosition(0 + btnMoverIzquierda.getWidth() / 2, 0 + btnMoverIzquierda.getHeight() / 2, Align.center);
        btnMoverIzquierda.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.SUBIENDO || jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.BAJANDO){
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.MOV_IZQUIERDA);
                }else{
                    jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.EN_PISO);
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.MOV_IZQUIERDA);
                }


                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.SUBIENDO || jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.BAJANDO){
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO_IZQUIERDA);
                }else{
                    jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.EN_PISO);
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO_IZQUIERDA);
                }

                super.touchUp(event, x, y, pointer, button);
            }
        });

        //Le movi aqui
        HUD.addActor(btnMoverIzquierda);

        //Boton atacar
        Texture texturaAtacar = new Texture("Botones/Boton_disparo_blanco.png");
        TextureRegionDrawable botonAtacar = new TextureRegionDrawable(new TextureRegion(texturaAtacar));
        //Aqui para el boton inverso (click)
        ImageButton btnAtacar = new ImageButton(botonAtacar);
        btnAtacar.setPosition(ANCHO - (btnAtacar.getWidth() / 2) * 3, 0 + btnAtacar.getHeight() / 2, Align.center);
        btnAtacar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                    if (proyectil == null) { //si no existe la creo, sino no la crea
                        if (jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.MOV_DERECHA || jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.QUIETO) {
                            proyectil = new Proyectil(texturaProyectilD, jugador.getX() + jugador.getSprite().getWidth() / 2,
                                    jugador.getY() + jugador.getSprite().getHeight() * 0.3f);
                            orientacion = 1;
                        } else if (jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.MOV_IZQUIERDA || jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.QUIETO_IZQUIERDA) {
                            proyectil = new Proyectil(texturaProyectilI, jugador.getX() + jugador.getSprite().getWidth() / 2,
                                    jugador.getY() + jugador.getSprite().getHeight() * 0.3f);
                            orientacion = 0;
                        }
                    }
                }
        });

        //Le movi aqui
        HUD.addActor(btnAtacar);

        //Boton para saltar
        Texture texturaSaltar = new Texture("Botones/Boton_saltar_blanco.png");
        TextureRegionDrawable botonSaltar = new TextureRegionDrawable(new TextureRegion(texturaSaltar));
        //Aqui para el boton inverso (click)
        ImageButton btnSaltar = new ImageButton(botonSaltar);
        btnSaltar.setPosition(ANCHO - btnSaltar.getWidth() / 2, 0 + btnSaltar.getHeight() / 2, Align.center);
        btnSaltar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Salta a la izquierda/derecha
                jugador.saltar();
                if (juego.efectoSonidoEstado != 1) {
                    efectoSalto.play();
                }
            }
        });

        //Le movi aqui
        HUD.addActor(btnSaltar);

        //Boton para pausa
        Texture texturaPausa = new Texture("Botones/Boton_pausa_blanco.png");
        TextureRegionDrawable botonPausa = new TextureRegionDrawable(new TextureRegion(texturaPausa));
        //Aqui para el boton inverso (click)
        ImageButton btnPausa = new ImageButton(botonPausa);
        btnPausa.setPosition(ANCHO - btnSaltar.getWidth()/2 - 30, ALTO - btnPausa.getHeight()/2 - 15, Align.center);
        btnPausa.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //Cuando le pica se pausa el juego, falta implementarlo
                //Crea la mini pantalla de pausa
                if (estadoJuego == PantallaNivelTres.EstadoJuego.JUGANDO) {
                    estadoJuego = PantallaNivelTres.EstadoJuego.PAUSA;
                }
                if (escenaPausa == null) {
                    escenaPausa = new PantallaNivelTres.EscenaPausa(vistaPausaHUD, batch); //La vista HUD no se mueve
                }
                Gdx.input.setInputProcessor(escenaPausa);
            }
        });

        //Le movi aqui
        HUD.addActor(btnPausa);
        //hola

        regionVidas = new TextureRegion(texturaVida);
        texturasFramesVidas = regionVidas.split(34, 34);
        spriteVidas = new Sprite(texturasFramesVidas[0][1]);
        imagenVidas = new Image(spriteVidas);
        imagenVidas.setPosition(50, 650);

        HUD.addActor(imagenVidas);

        regionVidasEnemigo = new TextureRegion(texturaVidasEnemigo);
        texturasFramesVidasEnemigos = regionVidasEnemigo.split(304, 60);
        spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][0]);
        imagenVidasEnemigos = new Image(spriteVidasEnemigos);
        imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);

        //HUD.addActor(imagenVidasEnemigos);*/

    }


    private void crearmapa() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("Lvl3_mapaV1.1.tmx", TiledMap.class);
        manager.finishLoading();
        mapa = manager.get("Lvl3_mapaV1.1.tmx");
        rendererMapa = new OrthogonalTiledMapRenderer(mapa);
    }

    @Override
    public void render(float delta) {
        if(estadoJuego == PantallaNivelTres.EstadoJuego.JUGANDO) {
            int numAleatorio = MathUtils.random(0,10);
            if(numAleatorio>=4 && proyectilJefeFinal==null){
                generarProyectil();
            }
            actualizar();
            borrarPantalla(0, 0, 0.5f);
            batch.setProjectionMatrix(camara.combined);
            rendererMapa.setView(camara);
            rendererMapa.render();
            batch.begin();
            jugador.render(batch);
            corazonUno.render(batch);
            corazonDos.render(batch);
            if(jugador.getX() >= 491*32) {
                enemigoFinal.render(batch);
                HUD.addActor(imagenVidasEnemigos);
            }
            dibujarEnemigos();
            if (proyectil != null) {
                proyectil.render(batch);
            }
            if (proyectilJefeFinal != null) {
                proyectilJefeFinal.render(batch);
            }
            batch.end();
        }

        //************ HUD ***************
        batch.setProjectionMatrix(camaraHUD.combined);
        if(estadoJuego == EstadoJuego.JUGANDO && jugador.getX()>=491*32){
            HUD.addActor(imagenVidasEnemigos);
        }
        HUD.draw();


        //*************Pausa*****************
        batch.setProjectionMatrix(camaraPausaHUD.combined);
        if (estadoJuego == EstadoJuego.PAUSA) {
            escenaPausa.draw();
        }

        //************Derrota**************
        batch.setProjectionMatrix(camaraDerrotaHUD.combined);
        if (estadoJuego == EstadoJuego.DERROTA) {
            escenaDerrota.draw();
        }

        //************Victoria**********
        batch.setProjectionMatrix(camaraVictoriaHUD.combined);
        if (estadoJuego == EstadoJuego.VICTORIA) {
            escenaVictoria.draw();
        }
    }

    private void generarProyectil() {
        proyectilJefeFinal = new Proyectil(texturaProyectilEnemigo,enemigoFinal.getX()+50,enemigoFinal.getY()+100);
    }

    private void verificarColisiones() {
        verificarCaida();
        verificarColisionEnemigos();
        verificarColisionesEnemigosDos();
        verificarColisionesItems();
        verificarColisionEnemigoFinal();
        
        if(proyectil != null){
            for (int j = arrEnemigosUno.size-1; j >= 0 ; j--) {
                EnemigoUnoPlataformas enemigo = arrEnemigosUno.get(j);
                if (proyectil.sprite.getBoundingRectangle().overlaps(enemigo.getSprite().getBoundingRectangle())) {
                    //Si hay colisión
                    if (juego.efectoSonidoEstado != 1){
                        efectoMuerteEnemigo.play();
                    }
                    arrEnemigosUno.removeIndex(j);
                    proyectil = null;
                    break;
                }
            }
        }
        if(proyectil != null){
            for (int j = arrEnemigosDos.size-1; j >= 0 ; j--) {
                EnemigoDosPlataformas enemigo = arrEnemigosDos.get(j);
                if (proyectil.sprite.getBoundingRectangle().overlaps(enemigo.getSprite().getBoundingRectangle())) {
                    //Si hay colisión
                    if (juego.efectoSonidoEstado != 1){
                        efectoMuerteEnemigoDos.play();
                    }
                    arrEnemigosDos.removeIndex(j);
                    proyectil = null;
                    break;
                }
            }
        }
        if(proyectil != null){
                if (proyectil.sprite.getBoundingRectangle().overlaps(rectangleEnemigo)) {
                    //Si hay colisión
                    if (juego.efectoSonidoEstado != 1){
                        efectoMuerteEnemigoDos.play();
                    }
                    enemigoFinal.setVidas(enemigoFinal.getVidas()-1);
                    proyectil = null;
                }
        }
        if(proyectilJefeFinal != null){
            if(proyectilJefeFinal.sprite.getBoundingRectangle().overlaps(rectangleJugador)){
                int vidasJugador = jugador.getVidas();
                if(vidasJugador-1>0){
                vidasJugador--;
                jugador.setVidas(vidasJugador);
                proyectilJefeFinal = null;
                }else{
                    if (juego.efectoSonidoEstado != 1){
                        efectoMuerte.play();
                    }
                    jugador.setVidas(jugador.getVidas()-1);
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
                    jugador.getSprite().setY(-100); //Lo manda a lo alto
                    estadoJuego = PantallaNivelTres.EstadoJuego.DERROTA;
                    escenaDerrota = new PantallaNivelTres.EscenaDerrota(vistaDerrotaHUD,batch);
                    Gdx.input.setInputProcessor(escenaDerrota);
                }
            }
        }
        //}
    }

    private void verificarColisionEnemigoFinal() {
        if(estadoJuego == EstadoJuego.JUGANDO && rectangleJugador.overlaps(auraEnemigo)){
            if (juego.efectoSonidoEstado != 1){
                efectoMuerte.play();
            }
            //jugador.setVidas(jugador.getVidas()-1);
            jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
            jugador.getSprite().setY(-100); //Lo manda a lo alto
            estadoJuego = PantallaNivelTres.EstadoJuego.DERROTA;
            escenaDerrota = new PantallaNivelTres.EscenaDerrota(vistaDerrotaHUD,batch);
            Gdx.input.setInputProcessor(escenaDerrota);
        }
    }

    private void verificarColisionesItems() {
        if(estadoJuego == EstadoJuego.JUGANDO){
            if(rectangleJugador.overlaps(corazonUno.sprite.getBoundingRectangle())){
                if(jugador.getVidas()+1 <=4){
                    jugador.setVidas(jugador.getVidas()+1);
                    corazonUno.sprite.setPosition(corazonUno.sprite.getX(),-300);
                    if(juego.efectoSonidoEstado != 1){
                        efectoPowerUp.play();
                    }
                }
            }else if( rectangleJugador.overlaps(corazonDos.sprite.getBoundingRectangle())){
                if(jugador.getVidas()+1 <=4){
                    jugador.setVidas(jugador.getVidas()+1);
                    corazonDos.sprite.setPosition(corazonDos.sprite.getX(),-300);
                    if(juego.efectoSonidoEstado != 1){
                        efectoPowerUp.play();
                    }
                }
            }
        }
    }

    private void verificarColisionesEnemigosDos() {
        for (int i = arrEnemigosDos.size-1; i >=0; i--) {
            EnemigoDosPlataformas enemigo = arrEnemigosDos.get(i);
            if(rectangleJugador.overlaps(enemigo.getSprite().getBoundingRectangle())){
                if(jugador.getVidas() - 1 > 0){
                    arrEnemigosDos.removeIndex(i);
                    jugador.setVidas(jugador.getVidas()-1);
                    //hola
                }else {
                    if (juego.efectoSonidoEstado != 1){
                        efectoMuerte.play();
                    }
                    jugador.setVidas(jugador.getVidas()-1);
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
                    jugador.getSprite().setY(-100); //Lo manda abajo de la pantalla
                    estadoJuego = PantallaNivelTres.EstadoJuego.DERROTA;
                    escenaDerrota = new PantallaNivelTres.EscenaDerrota(vistaDerrotaHUD,batch);
                    Gdx.input.setInputProcessor(escenaDerrota);
                }break;
            }
        }
    }

    private void verificarColisionEnemigos() {
        for (int i = arrEnemigosUno.size-1; i >= 0; i--) {
            EnemigoUnoPlataformas enemigo = arrEnemigosUno.get(i);
            if(rectangleJugador.overlaps(enemigo.getSprite().getBoundingRectangle())){
                if(jugador.getVidas() - 1 > 0){
                    arrEnemigosUno.removeIndex(i);
                    jugador.setVidas(jugador.getVidas()-1);
                }else {
                    if (juego.efectoSonidoEstado != 1){
                        efectoMuerte.play();
                    }
                    jugador.setVidas(jugador.getVidas()-1);
                    jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
                    jugador.getSprite().setY(-100); //Lo manda a lo alto
                    estadoJuego = PantallaNivelTres.EstadoJuego.DERROTA;
                    escenaDerrota = new PantallaNivelTres.EscenaDerrota(vistaDerrotaHUD,batch);
                    Gdx.input.setInputProcessor(escenaDerrota);
                }break;
            }
        }
    }

    private void verificarCaida() {
        if(jugador.getY() < 5 && estadoJuego!= PantallaNivelTres.EstadoJuego.DERROTA){
            if (juego.efectoSonidoEstado != 1){
                efectoMuerte.play();
            }
            jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
            //jugador(ANCHO); //Lo manda a lo alto
            estadoJuego = PantallaNivelTres.EstadoJuego.DERROTA;
            jugador.getSprite().setY(-100);
            escenaDerrota = new PantallaNivelTres.EscenaDerrota(vistaDerrotaHUD,batch);
            Gdx.input.setInputProcessor(escenaDerrota);
        }
    }

    private void dibujarEnemigos() {
        for (EnemigoUnoPlataformas enemigo : arrEnemigosUno) {
            enemigo.render(batch);
        }
        for (EnemigoDosPlataformas enemigoDos : arrEnemigosDos) {
            enemigoDos.render(batch);
        }
    }

    private void actualizar() {
        rectangleJugador.setPosition(jugador.getX(),jugador.getY());
        rectangleEnemigo.setPosition(enemigoFinal.getX(), enemigoFinal.getY());
        auraEnemigo.setPosition(enemigoFinal.getX(), enemigoFinal.getY());
        actualizarEnemigoFinal();
        actualizarCamara();
        actualizarProyectil();
        moverPersonaje();
        moverEnemigos();
        moverEnemigoDos();
        actualizarVidas();
        if(jugador.getX() >= 491*32) {
            actualizarVidasEnemigo();
        }
        verificarColisiones();
        comprobarVictoria(); //cambiar la victoria
    }

    private void actualizarVidasEnemigo() {
        switch (enemigoFinal.getVidas()) {
            case 19:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][0]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 18:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][1]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 17:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][2]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 16:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][3]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 15:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][4]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 14:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][5]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 13:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][6]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 12:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][7]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 11:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][8]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 10:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][9]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 9:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][10]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 8:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][11]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 7:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][12]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 6:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][13]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 5:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][14]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 4:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][15]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 3:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][16]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 2:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][17]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 1:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][18]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
            case 0:
                spriteVidasEnemigos = new Sprite(texturasFramesVidasEnemigos[0][19]);
                imagenVidasEnemigos = new Image(spriteVidasEnemigos);
                imagenVidasEnemigos.setPosition((ANCHO / 2)-spriteVidasEnemigos.getWidth()/2, 44);
                HUD.addActor(imagenVidasEnemigos);
                break;
        }
    }

    private void actualizarEnemigoFinal() {
        switch(enemigoFinal.getEstadoMov()){
            case INICIANDO:
                enemigoFinal.setEstadoMov(JefeFinal.estadoMovimiento.MOV_ARRIBA);
                break;
            case MOV_ARRIBA:
                if(enemigoFinal.getY()>= ALTO-texturaEnemigoFinal.getHeight()){
                    enemigoFinal.setEstadoMov(JefeFinal.estadoMovimiento.MOV_ABAJO);
                }
                break;
            case MOV_ABAJO:
                if(enemigoFinal.getY()<32*5){
                    enemigoFinal.setEstadoMov(JefeFinal.estadoMovimiento.MOV_ARRIBA);
                }
                break;
        }
        int random = MathUtils.random(1,10);
        if (random > 5 && enemigoFinal.getX() <555*32){
            enemigoFinal.moverDerecha();
        }
        if (random <= 5 && enemigoFinal.getX() > 533*32){
            enemigoFinal.moverIzquierda();
        }
    }

    private void comprobarVictoria() {
        if(estadoJuego == EstadoJuego.JUGANDO && enemigoFinal.getVidas() == 0){
            jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
            if (juego.efectoSonidoEstado != 1){
                efectoMuerteJefe.play();
            }
            jugador.getSprite().setY(-100); //Lo manda a lo alto
            enemigoFinal.getSprite().setY(-300);
            estadoJuego = PantallaNivelTres.EstadoJuego.VICTORIA;
            escenaVictoria = new PantallaNivelTres.EscenaVictoria(vistaVictoriaHUD,batch);
            Gdx.input.setInputProcessor(escenaVictoria);
        }
    }

    private void moverEnemigoDos() {
            for (int i = arrEnemigosDos.size-1 ; i >= 0; i--) {
                switch (arrEnemigosDos.get(i).getEstadoMov()) {
                    case INICIANDO:     // Mueve el personaje en Y hasta que se encuentre sobre un bloque
                        // Los bloques en el mapa son de 32x32
                        // Calcula la celda donde estaría después de moverlo
                        int celdaX = (int) (arrEnemigosDos.get(i).getX() / TAM_CELDA);
                        int celdaY = (int) ((arrEnemigosDos.get(i).getY() + arrEnemigosDos.get(i).VELOCIDAD_Y) / TAM_CELDA);
                        // Recuperamos la celda en esta posición
                        // La capa 0 son las plataformas
                        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get("Plataformas");
                        TiledMapTileLayer.Cell celda = capa.getCell(celdaX, celdaY);
                        // probar si la celda está ocupada
                        if (celda == null) {
                            //****revisar esta condicion, para ver si si se caen********
                            arrEnemigosDos.get(i).caer();
                            if (arrEnemigosDos.get(i).getY() < 5) {
                                arrEnemigosDos.removeIndex(i);
                                break;
                            }
                        } else {
                            arrEnemigosDos.get(i).setEstadoMov(EnemigoDosPlataformas.estadoMovimiento.QUIETO_IZQUIERDA);
                        }
                        break;
                    case MOV_DERECHA:
                        //********REVISAR CONDICIONES MOVIMIENTOS****************
                        probarChoqueParedesEnemigosDos();// Se mueve horizontal
                        if (arrEnemigosDos.get(i).getX() > 559 * 32) {
                            arrEnemigosDos.removeIndex(i);
                            break;
                        }
                        break;
                    case MOV_IZQUIERDA:
                        probarChoqueParedesEnemigosDos();      // Prueba si debe moverse
                        if (arrEnemigosDos.get(i).getX() < 5) {
                            arrEnemigosDos.removeIndex(i);
                            break;
                        }
                        break;
                    case QUIETO_IZQUIERDA:
                        if (jugador.getX() + ANCHO / 2 >= arrEnemigosDos.get(i).getX()) {
                            arrEnemigosDos.get(i).setEstadoMov(EnemigoDosPlataformas.estadoMovimiento.MOV_IZQUIERDA);
                        }
                        break;
                    case QUIETO:
                        if (jugador.getX() - ANCHO / 2 <= arrEnemigosDos.get(i).getX()) {
                            arrEnemigosDos.get(i).setEstadoMov(EnemigoDosPlataformas.estadoMovimiento.MOV_DERECHA);
                        }
                        break;
                }
            }
    }

    private void probarChoqueParedesEnemigosDos() {
        for (int i = 0; i <= arrEnemigosDos.size-1; i++) {
            // Quitar porque este método sólo se llama cuando se está moviendo
            float px = arrEnemigosDos.get(i).getX();    // Posición actual
            // Posición después de actualizar
            px = arrEnemigosDos.get(i).getEstadoMov() == EnemigoDosPlataformas.estadoMovimiento.MOV_DERECHA ? px + EnemigoUnoPlataformas.VELOCIDAD_X :
                    px - EnemigoUnoPlataformas.VELOCIDAD_X;
            int celdaX = (int) (px / TAM_CELDA);   // Casilla del personaje en X
            int celdaY = MathUtils.roundPositive(arrEnemigosDos.get(i).getY() / TAM_CELDA); // Casilla del personaje en Y
            TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get("Plataformas");
            if (arrEnemigosDos.get(i).getEstadoMov() == EnemigoDosPlataformas.estadoMovimiento.MOV_DERECHA) {
                celdaX++;   // Casilla del lado derecho
            }
            TiledMapTileLayer.Cell celdaDerecha = capaPlataforma.getCell(celdaX+2, celdaY);
            TiledMapTileLayer.Cell celdaIzquierda = capaPlataforma.getCell(celdaX, celdaY);
            TiledMapTileLayer.Cell celdaAbajoDerecha = capaPlataforma.getCell(celdaX+1, celdaY-1);
            TiledMapTileLayer.Cell celdaAbajoIzquierda = capaPlataforma.getCell(celdaX-1, celdaY-1);
            if(celdaDerecha != null && arrEnemigosDos.get(i).getEstadoMov() == EnemigoDosPlataformas.estadoMovimiento.MOV_DERECHA){
                arrEnemigosDos.get(i).setEstadoMov(EnemigoDosPlataformas.estadoMovimiento.MOV_IZQUIERDA);
            }
            if(celdaIzquierda != null && arrEnemigosDos.get(i).getEstadoMov() == EnemigoDosPlataformas.estadoMovimiento.MOV_IZQUIERDA){
                arrEnemigosDos.get(i).setEstadoMov(EnemigoDosPlataformas.estadoMovimiento.MOV_DERECHA);
            }
            if(celdaAbajoDerecha == null && celdaDerecha == null && arrEnemigosDos.get(i).getEstadoMov() == EnemigoDosPlataformas.estadoMovimiento.MOV_DERECHA){
                arrEnemigosDos.get(i).setEstadoMov(EnemigoDosPlataformas.estadoMovimiento.MOV_IZQUIERDA);
            }
            if(celdaAbajoIzquierda == null && celdaIzquierda == null && arrEnemigosDos.get(i).getEstadoMov() == EnemigoDosPlataformas.estadoMovimiento.MOV_IZQUIERDA){
                arrEnemigosDos.get(i).setEstadoMov(EnemigoDosPlataformas.estadoMovimiento.MOV_DERECHA);
            }
            if(celdaAbajoDerecha == null && celdaAbajoIzquierda == null){
                arrEnemigosDos.get(i).caer();
            }
        }
    }

    private void actualizarVidas() { //Verificar bien el funcionamiento
        switch (jugador.getVidas()){
            case 4:
                spriteVidas = new Sprite(texturasFramesVidas[0][0]);
                imagenVidas = new Image(spriteVidas);
                imagenVidas.setPosition(50,650);
                HUD.addActor(imagenVidas);
                break;
            case 3:
                spriteVidas = new Sprite(texturasFramesVidas[0][1]);
                imagenVidas = new Image(spriteVidas);
                imagenVidas.setPosition(50,650);
                HUD.addActor(imagenVidas);
                break;
            case 2:
                spriteVidas = new Sprite(texturasFramesVidas[0][2]);
                imagenVidas = new Image(spriteVidas);
                imagenVidas.setPosition(50,650);
                HUD.addActor(imagenVidas);
                break;
            case 1:
                spriteVidas = new Sprite(texturasFramesVidas[0][3]);
                imagenVidas = new Image(spriteVidas);
                imagenVidas.setPosition(50,650);
                HUD.addActor(imagenVidas);
                break;
            case 0:
                spriteVidas = new Sprite(texturasFramesVidas[0][4]);
                imagenVidas = new Image(spriteVidas);
                imagenVidas.setPosition(50,650);
                HUD.addActor(imagenVidas);
                break;
        }
    }

    private void moverEnemigos() {
        for (int i = arrEnemigosUno.size-1; i >= 0; i--) {
                switch (arrEnemigosUno.get(i).getEstadoMov()) {
                    case INICIANDO:     // Mueve el personaje en Y hasta que se encuentre sobre un bloque
                        // Los bloques en el mapa son de 32x32
                        // Calcula la celda donde estaría después de moverlo
                        int celdaX = (int) (arrEnemigosUno.get(i).getX() / TAM_CELDA);
                        int celdaY = (int) ((arrEnemigosUno.get(i).getY() + arrEnemigosUno.get(i).VELOCIDAD_Y) / TAM_CELDA);
                        // Recuperamos la celda en esta posición
                        // La capa 0 son las plataformas
                        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get("Plataformas");
                        TiledMapTileLayer.Cell celda = capa.getCell(celdaX, celdaY);
                        // probar si la celda está ocupada
                        if (celda == null) {
                            //****revisar esta condicion, para ver si si se caen********
                            arrEnemigosUno.get(i).caer();
                            if (arrEnemigosUno.get(i).getY() < 5) {
                                arrEnemigosUno.removeIndex(i);
                                break;
                            }
                        } else {
                            arrEnemigosUno.get(i).setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.QUIETO_IZQUIERDA);
                        }
                        break;
                    case MOV_DERECHA:
                        //********REVISAR CONDICIONES MOVIMIENTOS****************
                        probarChoqueParedesEnemigos();// Se mueve horizontal
                        if (arrEnemigosUno.get(i).getX() > 559 * 32) {
                            arrEnemigosUno.removeIndex(i);
                            break;
                        }
                        break;
                    case MOV_IZQUIERDA:
                        probarChoqueParedesEnemigos();      // Prueba si debe moverse
                        if (arrEnemigosUno.get(i).getX() < 5) {
                            arrEnemigosUno.removeIndex(i);
                            break;
                        }
                        break;
                    case QUIETO_IZQUIERDA:
                        if (jugador.getX() + ANCHO / 2 >= arrEnemigosUno.get(i).getX()) {
                            arrEnemigosUno.get(i).setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.MOV_IZQUIERDA);
                        }
                        break;
                    case QUIETO:
                        if (jugador.getX() - ANCHO / 2 <= arrEnemigosUno.get(i).getX()) {
                            arrEnemigosUno.get(i).setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA);
                        }
                        break;
                }
        }
    }

    private void probarChoqueParedesEnemigos() {
        for (int i = 0; i<=arrEnemigosUno.size-1; i++) {
            EnemigoUnoPlataformas.estadoMovimiento estado = arrEnemigosUno.get(i).getEstadoMov();
            // Quitar porque este método sólo se llama cuando se está moviendo
            float px = arrEnemigosUno.get(i).getX();    // Posición actual
            // Posición después de actualizar
            px = arrEnemigosUno.get(i).getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA ? px + EnemigoUnoPlataformas.VELOCIDAD_X :
                    px - EnemigoUnoPlataformas.VELOCIDAD_X;
            int celdaX = (int) (px / TAM_CELDA);   // Casilla del personaje en X
            int celdaY = MathUtils.roundPositive(arrEnemigosUno.get(i).getY() / TAM_CELDA); // Casilla del personaje en Y
            TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get("Plataformas");
            if (arrEnemigosUno.get(i).getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA) {
                celdaX++;   // Casilla del lado derecho
            }
            TiledMapTileLayer.Cell celdaDerecha = capaPlataforma.getCell(celdaX+2, celdaY);
            TiledMapTileLayer.Cell celdaIzquierda = capaPlataforma.getCell(celdaX, celdaY);
            TiledMapTileLayer.Cell celdaAbajoDerecha = capaPlataforma.getCell(celdaX+1, celdaY-1);
            TiledMapTileLayer.Cell celdaAbajoIzquierda = capaPlataforma.getCell(celdaX-1, celdaY-1);
            TiledMapTileLayer.Cell celdaArribaDerecha = capaPlataforma.getCell(celdaX+1, celdaY+1);
            TiledMapTileLayer.Cell celdaArribaIzquierda = capaPlataforma.getCell(celdaX-1, celdaY+1);
            /*Gdx.app.log("el enemigo en las celdas"+celdaX," y la "+celdaY);
            Gdx.app.log("celda derecha " +celdaDerecha,"");
            Gdx.app.log("celda izquierda " + celdaIzquierda,"");
            Gdx.app.log("celda arriba derecha " + celdaArribaDerecha," celda arriba izquierda " + celdaAbajoIzquierda );
            Gdx.app.log("celda abajo derecha " + celdaAbajoDerecha," celda abajo izquierda " + celdaAbajoIzquierda);*/
            if((celdaDerecha != null || celdaArribaDerecha !=null) && arrEnemigosUno.get(i).getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA){
                arrEnemigosUno.get(i).setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.MOV_IZQUIERDA);
            }
            if((celdaIzquierda != null || celdaArribaIzquierda != null) && arrEnemigosUno.get(i).getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_IZQUIERDA){
                arrEnemigosUno.get(i).setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA);
            }
            if(celdaAbajoDerecha == null && celdaDerecha == null && arrEnemigosUno.get(i).getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA){
                arrEnemigosUno.get(i).setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.MOV_IZQUIERDA);
            }
            if(celdaAbajoIzquierda == null && celdaIzquierda == null && arrEnemigosUno.get(i).getEstadoMov() == EnemigoUnoPlataformas.estadoMovimiento.MOV_IZQUIERDA){
                arrEnemigosUno.get(i).setEstadoMov(EnemigoUnoPlataformas.estadoMovimiento.MOV_DERECHA);
            }
            if(celdaAbajoDerecha == null && celdaAbajoIzquierda == null){
                arrEnemigosUno.get(i).caer();
            }
        }
    }

    private void moverPersonaje() {
        // Prueba caída libre inicial o movimiento horizontal
        switch (jugador.getEstadoMovimiento()) {
            case INICIANDO:     // Mueve el personaje en Y hasta que se encuentre sobre un bloque
                // Los bloques en el mapa son de 32x32
                // Calcula la celda donde estaría después de moverlo
                int celdaX = (int) (jugador.getX() / TAM_CELDA);
                int celdaY = (int) ((jugador.getY() + jugador.VELOCIDAD_Y) / TAM_CELDA);
                // Recuperamos la celda en esta posición
                // La capa 0 son las plataformas
                TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get("Plataformas");
                TiledMapTileLayer.Cell celda = capa.getCell(celdaX, celdaY);
                // probar si la celda está ocupada
                if (celda == null) {
                    // Celda vacía, entonces el personaje puede avanzar
                    jugador.caer();
                }
                break;
            case MOV_DERECHA:// Se mueve horizontal
                probarChoqueParedes();      // Prueba si debe moverse
                break;
            case MOV_IZQUIERDA:
                probarChoqueParedes(); // Prueba si debe moverse
                break;
        }
        switch (jugador.getEstadoSalto()){
            case EN_PISO:
                //probarChoqueParedes();
                break;
            case SUBIENDO:
                probarChoqueParedes();
                break;
            case BAJANDO:
                probarChoqueParedes();
                break;
            case CAIDA_LIBRE:
                probarChoqueParedes();
                break;
        }
        // Prueba si debe caer por llegar a un espacio vacío
        if (jugador.getEstadoMovimiento() != JugadorPlataformas.EstadoMovimiento.INICIANDO
                && (jugador.getEstadoSalto() != JugadorPlataformas.EstadoSalto.SUBIENDO)) {
            // Calcula la celda donde estaría después de moverlo
            int celdaX = (int) (jugador.getX() / TAM_CELDA);
            int celdaY = (int) ((jugador.getY() + jugador.VELOCIDAD_Y) / TAM_CELDA);
            // Recuperamos la celda en esta posición
            // La capa 0 son las plataformas
            TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get("Plataformas");
            TiledMapTileLayer.Cell celdaAbajoIzq = capa.getCell(celdaX, celdaY);
            TiledMapTileLayer.Cell celdaAbajoDer = capa.getCell(celdaX+1, celdaY);
            TiledMapTileLayer.Cell celdaDerecha = capa.getCell(celdaX + 2, celdaY);
            TiledMapTileLayer.Cell celdaIzquierda = capa.getCell(celdaX +1 , celdaY); // revisar la hitbox de las jeringas
            // probar si la celda está ocupada
            if (celdaAbajoIzq == null && (celdaDerecha == null || celdaIzquierda == null) && celdaAbajoDer == null) {
                // Celda vacía, entonces el personaje puede avanzar
                //jugador.caer();
                jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.CAIDA_LIBRE);
            } else {
                // Dejarlo sobre la celda que lo detiene
                jugador.setPosicion(jugador.getX(), (celdaY + 1) * TAM_CELDA);
                jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.EN_PISO);
            }
        }
    }

    private void probarChoqueParedes() {
        JugadorPlataformas.EstadoMovimiento estado = jugador.getEstadoMovimiento();
        JugadorPlataformas.EstadoSalto estadoSalto = jugador.getEstadoSalto();
        // Si el jugador no esta en movimiento no es necesario llamar a este metodo
        float px = jugador.getX();    // Posición actual
        // Posición después de actualizar
        px = jugador.getEstadoMovimiento()==JugadorPlataformas.EstadoMovimiento.MOV_DERECHA? px+JugadorPlataformas.VELOCIDAD_X:
                px-JugadorPlataformas.VELOCIDAD_X;
        int celdaX = (int)(px/TAM_CELDA);   // Casilla del personaje en X
        int celdaY = (MathUtils.roundPositive(jugador.getY()/TAM_CELDA));
        TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get("Plataformas");
        TiledMapTileLayer.Cell celdaDerecha = capaPlataforma.getCell(celdaX+2, celdaY);
        TiledMapTileLayer.Cell celdaIzquierda = capaPlataforma.getCell(celdaX, celdaY);
        TiledMapTileLayer.Cell celdaAbajoDer = capaPlataforma.getCell(celdaX+1, celdaY-1);
        TiledMapTileLayer.Cell celdaAbajoIzq = capaPlataforma.getCell(celdaX, celdaY-1);
        TiledMapTileLayer.Cell celdaArribaDer = capaPlataforma.getCell(celdaX+1, celdaY+1);
        TiledMapTileLayer.Cell celdaArribaIzq = capaPlataforma.getCell(celdaX+1, celdaY+1);
        if(celdaDerecha != null && jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.MOV_DERECHA){
            jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO);
        }
        if(celdaIzquierda != null && jugador.getEstadoMovimiento() == JugadorPlataformas.EstadoMovimiento.MOV_IZQUIERDA){
            jugador.setEstadoMovimiento(JugadorPlataformas.EstadoMovimiento.QUIETO_IZQUIERDA);
        }
        if(celdaArribaDer != null && celdaArribaIzq != null){
            jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.BAJANDO);
        }
        if(celdaAbajoDer == null && celdaAbajoIzq == null && estadoSalto == JugadorPlataformas.EstadoSalto.BAJANDO){
            jugador.caer();
        }
        if(celdaAbajoDer == null && celdaAbajoIzq == null && jugador.getEstadoSalto() == JugadorPlataformas.EstadoSalto.EN_PISO){
            jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.CAIDA_LIBRE);
        }
        if(celdaAbajoDer != null && celdaAbajoIzq != null && (estadoSalto == JugadorPlataformas.EstadoSalto.BAJANDO || estadoSalto ==  JugadorPlataformas.EstadoSalto.CAIDA_LIBRE)){
            jugador.setEstadoSalto(JugadorPlataformas.EstadoSalto.EN_PISO);
        }
    }

    private void actualizarProyectil(){ //Va a pasar a traves de las celdas
        if(proyectil != null) {
            proyectil.mover(orientacion);
            float px = proyectil.sprite.getX();    // Posición actual
            // Posición después de actualizar
            px = orientacion==1? px+proyectil.VELOCIDAD_X:
                    px-proyectil.VELOCIDAD_X;
            int celdaX = (int) (proyectil.sprite.getX() / TAM_CELDA);
            int celdaY = (int)proyectil.sprite.getY()/TAM_CELDA;
            TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get("Plataformas");
            TiledMapTileLayer.Cell celdaDerecha = capaPlataforma.getCell(celdaX+1, celdaY+1);
            TiledMapTileLayer.Cell celdaIzquierda = capaPlataforma.getCell(celdaX, celdaY+1); //verificar el signo por la orientacion
            if (proyectil.sprite.getX() > jugador.getX() + ANCHO/2 ||proyectil.sprite.getX() < jugador.getX() - ANCHO/2) {
                proyectil = null;
            }
            if(celdaDerecha != null ){
                proyectil = null;
            }
            if (celdaIzquierda != null){
                proyectil = null;
            }

        }
        if(proyectilJefeFinal != null) {
            proyectilJefeFinal.mover2();
            float px = proyectilJefeFinal.sprite.getX();    // Posición actual
            // Posición después de actualizar
            px = orientacion==1? px+proyectilJefeFinal.VELOCIDAD_X:
                    px-proyectilJefeFinal.VELOCIDAD_X;
            int celdaX = (int) (proyectilJefeFinal.sprite.getX() / TAM_CELDA);
            int celdaY = (int)proyectilJefeFinal.sprite.getY()/TAM_CELDA;
            TiledMapTileLayer capaPlataforma = (TiledMapTileLayer) mapa.getLayers().get("Plataformas");
            TiledMapTileLayer.Cell celdaDerecha = capaPlataforma.getCell(celdaX+1, celdaY+1);
            TiledMapTileLayer.Cell celdaIzquierda = capaPlataforma.getCell(celdaX, celdaY+1); //verificar el signo por la orientacion
            TiledMapTileLayer.Cell celdaIzquierdaAbajo = capaPlataforma.getCell(celdaX,celdaY);
            if (proyectilJefeFinal.sprite.getX() <= 491*32 ) {
                proyectilJefeFinal = null;
            }
            if(celdaDerecha != null ){
                proyectilJefeFinal = null;
            }
            if (celdaIzquierda != null || celdaIzquierdaAbajo != null ){
                proyectilJefeFinal = null;
            }

        }
    }

    private void actualizarCamara() {
        float xCamara = camara.position.x;
        float yCamara = camara.position.y;
        if (jugador.getX() < ANCHO/2){
            xCamara = ANCHO/2;
        }else if (jugador.getX()+ANCHO/2 > 559*32){ //checar la condicion del mapa
            xCamara = ANCHO_MAPA - ANCHO/2; //checar para llegar al limite del mapa
        }else {
            xCamara = jugador.getX();
        }
        if (jugador.getY() < ALTO / 2){
            yCamara = ALTO/2;
        }else if(jugador.getY() + ALTO >= 32*30){
            yCamara = jugador.getY();
        }else{
            yCamara = ALTO/2;
        }
        camara.position.x = xCamara;
        camara.position.y = yCamara;
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
        mapa.dispose();
        texturaVida.dispose();
        texturaVidasEnemigo.dispose();
        texturaItemCorazon.dispose();
        texturaEnemigoFinal.dispose();
        texturaEnemigoDos.dispose();
        texturaEnemigoUno.dispose();
        texturaPersonaje.dispose();
        texturaProyectilI.dispose();
        texturaProyectilD.dispose();
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
            imgFondoPausa.setPosition(ANCHO / 2 - texturaFondoPausa.getWidth() / 2,
                    ALTO / 2 - texturaFondoPausa.getHeight() / 2);
            this.addActor(imgFondoPausa);

            Texture texturaBtnRegresar = new Texture("Botones/Btn_resume.png");
            TextureRegionDrawable botonRegresar = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
            //Aqui para el boton inverso (click)
            ImageButton btnRegresar = new ImageButton(botonRegresar);
            btnRegresar.setPosition(ANCHO / 2, ALTO * 0.6f, Align.center);
            btnRegresar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para regresar al juego
                    estadoJuego = PantallaNivelTres.EstadoJuego.JUGANDO;
                    Gdx.input.setInputProcessor(HUD);
                }
            });
            this.addActor(btnRegresar);
            Texture texturaBtnMenu = new Texture("Botones/btn_Exit.png");
            TextureRegionDrawable botonMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));
            //Aqui para el boton inverso (click)
            ImageButton btnMenu = new ImageButton(botonMenu);
            btnMenu.setPosition(ANCHO / 2, ALTO * 0.3f, Align.center);
            btnMenu.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para regresar al juego
                    estadoJuego = PantallaNivelTres.EstadoJuego.JUGANDO;
                    juego.setScreen(new PantallaNiveles(juego));
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
            btnRetry.setPosition(ANCHO*0.35f, ALTO*0.3f, Align.center);
            btnRetry.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    //Para reintentar el nivel
                    juego.setScreen(new PantallaNivelTres(juego));

                }
            });
            this.addActor(btnRetry);

            Texture texturaBtnNiveles = new Texture("Botones/btn_Exit.png");
            TextureRegionDrawable botonNiveles = new TextureRegionDrawable(new TextureRegion(texturaBtnNiveles));
            //Aqui para el boton inverso (click)
            ImageButton btnNiveles = new ImageButton(botonNiveles);
            btnNiveles.setPosition(ANCHO*0.65f, ALTO*0.3f, Align.center);
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

            Texture texturaFondoVictoria = new Texture("Fondos/Win.png");
            Image imgFondoVictoria = new Image(texturaFondoVictoria);
            imgFondoVictoria.setPosition(ANCHO / 2 - texturaFondoVictoria.getWidth() / 2,
                    ALTO / 2 - texturaFondoVictoria.getHeight() / 2);
            this.addActor(imgFondoVictoria);

            Texture texturaBtnNiveles = new Texture("Botones/btn_Exit.png");
            TextureRegionDrawable botonNiveles = new TextureRegionDrawable(new TextureRegion(texturaBtnNiveles));
            //Aqui para el boton inverso (click)
            ImageButton btnNiveles = new ImageButton(botonNiveles);
            btnNiveles.setPosition(ANCHO /2, ALTO * 0.2f, Align.center);
            btnNiveles.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    juego.setScreen(new PantallaMenu(juego));
                }
            });
            this.addActor(btnNiveles);
        }
    }
}
