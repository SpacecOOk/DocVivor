package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class JefeFinal {
    public static final float VELOCIDAD_Y = 4;
    public static final float VELOCIDAD_X = 4;
    private Sprite sprite;

    //ANIMACION
    private Animation animacion;
    private float timerAnimacion;

    //estados
    private estadoMovimiento estadoMov;

    //vidas
    private int vidas;


    public JefeFinal(Texture textura, float x, float y, int width, int height){
        TextureRegion texturaCompleta = new TextureRegion(textura);
        TextureRegion[][] texutrasFrames = texturaCompleta.split(width,height);
        animacion = new Animation(0.15f, texutrasFrames[0][0],texutrasFrames[0][1],texutrasFrames[0][2],texutrasFrames[0][3],
                texutrasFrames[0][4],texutrasFrames[0][5],texutrasFrames[0][6]);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;
        sprite = new Sprite(texutrasFrames[0][0]);
        sprite.setPosition(x,y);
        estadoMov = estadoMovimiento.INICIANDO;
        vidas = 15;
    }
    //RENDER Y ACTUALIZAR
    public void render(SpriteBatch batch){
        actualizar();
        switch(estadoMov){
            case QUIETO:
                //sprite.draw(batch);
                break;
            case INICIANDO:
            case MOV_ARRIBA:
                timerAnimacion += Gdx.graphics.getDeltaTime();
                TextureRegion region = (TextureRegion)animacion.getKeyFrame(timerAnimacion);
                batch.draw(region, sprite.getX(), sprite.getY());
                break;
            case MOV_ABAJO:
                timerAnimacion += Gdx.graphics.getDeltaTime();
                region = (TextureRegion)animacion.getKeyFrame(timerAnimacion);
                batch.draw(region, sprite.getX(), sprite.getY());
                break;
        }
    }

    private void actualizar() {
        actualizarMovimientoVertical();
    }

    private void actualizarMovimientoVertical() {
        float nuevaY = sprite.getY();
        switch (estadoMov){
            case MOV_ABAJO:
                nuevaY -= VELOCIDAD_Y;
                sprite.setY(nuevaY);
                break;
            case MOV_ARRIBA:
                nuevaY += VELOCIDAD_Y;
                sprite.setY(nuevaY);
                break;

        }
    }

    public void moverDerecha(){
        float nuevaX = sprite.getX();
        nuevaX += VELOCIDAD_X;
        sprite.setX(nuevaX);
    }

    public void moverIzquierda(){
        float nuevaX = sprite.getX();
        nuevaX -= VELOCIDAD_X;
        sprite.setX(nuevaX);
    }

    //ESTADOS
    public estadoMovimiento getEstadoMov() {
        return estadoMov;
    }

    public void setEstadoMov(estadoMovimiento estadoMov) {
        this.estadoMov = estadoMov;
    }

    public enum estadoMovimiento{
        QUIETO,
        MOV_ARRIBA,
        MOV_ABAJO,
        INICIANDO,
        MURIENDO
    }

    public Sprite getSprite(){return sprite;}
    //ACCESORES DE POSICION
    public float getY(){return sprite.getY();}
    public float getX(){return sprite.getX();}
    public void setPosition(float x,int y){sprite.setPosition(x,y);}

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }
}
