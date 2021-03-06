package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EnemigoUnoPlataformas {
    public static final float VELOCIDAD_X = 4;
    public static final float VELOCIDAD_Y = -4f;

    private Sprite sprite;  // Sprite cuando no se mueve

    // Animación
    private Animation animacion;    // Caminando
    private Animation animacionMuerte; 
    private float timerAnimacion;   // tiempo para calcular el frame
    private int ladoQUIETO;
    private int contadorAnimacion;

    //estados
    private estadoMovimiento estadoMov;

    public EnemigoUnoPlataformas(Texture textura) {
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);
        // La divide en frames de 16x32 (ver marioSprite.png)
        TextureRegion[][] texturasFrames = texturaCompleta.split(50,50);
        // Crea la animación con tiempo de 0.25 segundos entre frames.
        animacion = new Animation(0.15f,texturasFrames[0][0],texturasFrames[0][1],texturasFrames[0][2], texturasFrames[0][3], texturasFrames[0][4],texturasFrames[0][5],texturasFrames[0][6],
                texturasFrames[0][7]);
        animacionMuerte = new Animation(0.5f,texturasFrames[0][8],texturasFrames[0][9],texturasFrames[0][10],texturasFrames[0][11],texturasFrames[0][12],texturasFrames[0][13],texturasFrames[0][14],
                texturasFrames[0][15]);
        // Animación infinita
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        animacionMuerte.setPlayMode(Animation.PlayMode.NORMAL);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite cuando para el personaje quieto (idle)
        sprite = new Sprite(texturasFrames[0][0]);    // quieto
        estadoMov = estadoMovimiento.INICIANDO;
    }


    public void render(SpriteBatch batch){
        actualizar();
        switch (estadoMov) {
            case INICIANDO:
                //batch.draw(region, sprite.getX(), sprite.getY());
                //setEstadoMov(estadoMovimiento.QUIETO_IZQUIERDA);
                //break;
            case MOV_DERECHA:
                // Incrementa el timer para calcular el frame que se dibuja
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Obtiene el frame que se debe mostrar (de acuerdo al timer)
                TextureRegion region = (TextureRegion)animacion.getKeyFrame(timerAnimacion);
                // Dirección correcta
                if (estadoMov== estadoMovimiento.MOV_DERECHA) {
                    if (!region.isFlipX()) {
                        region.flip(true,false);
                    }
                } else {
                    if (region.isFlipX()) {
                        region.flip(false,false);
                    }
                }
                // Dibuja el frame en las coordenadas del sprite
                batch.draw(region, sprite.getX(), sprite.getY());
                break;
            case MOV_IZQUIERDA:
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
            case MURIENDO:
                // Incrementa el timer para calcular el frame que se dibuja
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Obtiene el frame que se debe mostrar (de acuerdo al timer)
                TextureRegion region2 = (TextureRegion)animacionMuerte.getKeyFrame(timerAnimacion);
                contadorAnimacion++;
                batch.draw(region2, sprite.getX(), sprite.getY());
                break;
        }
    }

    private void actualizar() {
        actualizarMovimientoHorizontal();
    }


    private void actualizarMovimientoHorizontal() {
        float nuevaX = sprite.getX();
        switch (estadoMov) {
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
                sprite.setX(nuevaX);
                break;
        }
    }

    public void caer() {
        sprite.setY(sprite.getY() + VELOCIDAD_Y);
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

    public enum estadoMovimiento{
        QUIETO,
        QUIETO_IZQUIERDA,
        MOV_DERECHA,
        MOV_IZQUIERDA,
        INICIANDO,
        MURIENDO
    }

    public estadoMovimiento getEstadoMov() {
        return estadoMov;
    }

    public void setEstadoMov(estadoMovimiento estadoMov) {
        this.estadoMov = estadoMov;
    }

    public boolean acaboAnimacion() {
        if(contadorAnimacion<6){
            return false;
        }else{
            return true;
        }
    }
}
