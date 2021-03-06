package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class JugadorPlataformas {
    public static final float VELOCIDAD_YCAIDA = -4.5f;
    public static final float VELOCIDAD_Y = -2.4f;   // Velocidad de caída
    public static final float VELOCIDAD_X = 10;     // Velocidad horizontal

    private Sprite sprite;  // Sprite cuando no se mueve

    //VIDAS
    private int vidas;

    //Parametros
    private int ancho;
    private int alto;

    // Animación
    private Animation animacion;    // Caminando
    private float timerAnimacion;   // tiempo para calcular el frame

    // Estados del personaje
    private EstadoMovimiento estadoMovimiento;
    private EstadoSalto estadoSalto;
    public int ladoQUIETO;

    // SALTO del personaje
    private static final float V0 = 65;     // Velocidad inicial del salto
    private static final float G = 9.81f;   //Checar valor cuando esten los enemigos
    private static final float G_2 = G/2;   // Gravedad
    private float yInicial;         // 'y' donde inicia el salto
    private float tiempoVuelo;       // Tiempo que estará en el aire
    private float tiempoSalto;      // Tiempo actual de vuelo



    /*
        Constructor del personaje, recibe una imagen con varios frames, (ver imagen marioSprite.png)
         */
    public JugadorPlataformas(Texture textura,int ancho, int alto) {
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);
        // La divide en frames de 16x32 (ver marioSprite.png)
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(ancho,alto);//CAMBIAR LOS PERSONAJES
        // Crea la animación con tiempo de 0.25 segundos entre frames.
        animacion = new Animation(0.1f,texturaPersonaje[0][2],
                texturaPersonaje[0][0]);
        // Animación infinita
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturaPersonaje[0][1]);    // quieto
        estadoMovimiento = EstadoMovimiento.INICIANDO;
        estadoSalto = EstadoSalto.EN_PISO;
        vidas = 3;
    }

    // Dibuja el personaje
    public void render(SpriteBatch batch) {
        actualizar();
        // Dibuja el personaje dependiendo del estadoMovimiento
        switch (estadoMovimiento) {
            case INICIANDO:
                setEstadoMovimiento(EstadoMovimiento.QUIETO);
                break;
            case MOV_DERECHA:
            case MOV_IZQUIERDA:
                // Incrementa el timer para calcular el frame que se dibuja
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Obtiene el frame que se debe mostrar (de acuerdo al timer)
                TextureRegion region = (TextureRegion)animacion.getKeyFrame(timerAnimacion);
                // Dirección correcta
                if (estadoMovimiento== EstadoMovimiento.MOV_IZQUIERDA) {
                    if (!region.isFlipX()) {
                        region.flip(true,false);
                    }
                } else {
                    if (region.isFlipX()) {
                        region.flip(true,false);
                    }
                }
                // Dibuja el frame en las coordenadas del sprite
                batch.draw(region, sprite.getX(), sprite.getY());
                break;

            case QUIETO:
                if(ladoQUIETO==1){
                    ladoQUIETO=0;
                    sprite.flip(true,false);
                }
                sprite.draw(batch);
                break;
            case QUIETO_IZQUIERDA:
                if(ladoQUIETO==0){
                    ladoQUIETO=1;
                    sprite.flip(true,false);
                }
                sprite.draw(batch);
                break;
        }
        switch (estadoSalto){
            case EN_PISO:
                break;
            case SUBIENDO:
                actualizarSalto();
                break;
            case BAJANDO:
                caer();
                break;
            case CAIDA_LIBRE:
                caidaLibre();
                break;
        }
    }

    public void caidaLibre() {
        sprite.setY(sprite.getY() + VELOCIDAD_YCAIDA);
    }

    // Actualiza el sprite, de acuerdo al estadoMovimiento
    public void actualizar() {
        // Ejecutar movimiento horizontal
        float nuevaX = sprite.getX();
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                // Prueba que no salga del mundo
                nuevaX += VELOCIDAD_X;
                if (nuevaX<=PantallaNivelDos.ANCHO_MAPA-sprite.getWidth()) {
                    sprite.setX(nuevaX);
                }
                break;
            case MOV_IZQUIERDA:
                // Prueba que no salga del mundo
                nuevaX -= VELOCIDAD_X;
                if (nuevaX>=0) {
                    sprite.setX(nuevaX);
                }
                break;
        }
    }

    // Avanza en su caída
    public void caer() {
        sprite.setY(sprite.getY() + VELOCIDAD_Y);
    }

    // Actualiza la posición en 'y', está saltando
    public void actualizarSalto() {
        if(estadoSalto!=EstadoSalto.EN_PISO || estadoSalto!=EstadoSalto.CAIDA_LIBRE){//POSIBLE ERROR A FUTURO
        // Ejecutar movimiento vertical
        float y = V0 * tiempoSalto - G_2 * tiempoSalto * tiempoSalto;  // Desplazamiento desde que inició el salto
        if (tiempoSalto > tiempoVuelo / 2) { // Llegó a la altura máxima?
            // Inicia caída
            estadoSalto = EstadoSalto.BAJANDO;
        }
        tiempoSalto += 10 * Gdx.graphics.getDeltaTime();  // Actualiza tiempo
        sprite.setY(yInicial + y);    // Actualiza posición
        /*if (y < 0) {
            // Regresó al piso
            sprite.setY(yInicial);  // Lo deja donde inició el salto
            estadoSalto = EstadoSalto.EN_PISO;  // Ya no está saltando
            }*/
        }
    }

    // Accesor de la variable sprite
    public Sprite getSprite() {
        return sprite;
    }

    // Accesores para la posición
    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public void setPosicion(float x, int y) {
        sprite.setPosition(x,y);
    }

    // Accesor del estadoMovimiento
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    // Modificador del estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    public void setEstadoSalto(EstadoSalto estadoSalto) {
        this.estadoSalto = estadoSalto;
    }

    // Inicia el salto
    public void saltar() {
        if (estadoSalto == EstadoSalto.EN_PISO) {
            tiempoSalto = 0;
            yInicial = sprite.getY();
            estadoSalto = EstadoSalto.SUBIENDO;
            tiempoVuelo = 2 * V0 / G;
        }
    }

    public EstadoSalto getEstadoSalto() {
        return estadoSalto;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }
    public enum EstadoMovimiento {
        INICIANDO,
        QUIETO,
        QUIETO_IZQUIERDA,
        MOV_IZQUIERDA,
        MOV_DERECHA
    }

    public enum EstadoSalto {
        EN_PISO,
        SUBIENDO,
        BAJANDO,
        CAIDA_LIBRE // Cayó de una orilla
    }
}
