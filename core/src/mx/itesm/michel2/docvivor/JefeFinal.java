package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class JefeFinal {
    public static final float VELOCIDAD_Y = 4;
    private Sprite sprite;

    //ANIMACION
    private Animation animacion;
    private float timerAnimacion;

    //estados
    private estadoMovimiento estadoMov;


    public JefeFinal(Texture textura, float x, float y, int width, int height){
        TextureRegion texturaCompleta = new TextureRegion(textura);
        TextureRegion[][] texutrasFrames = texturaCompleta.split(width,height);
        animacion = new Animation(0.15f, texutrasFrames[0][0]);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;
        sprite = new Sprite(texutrasFrames[0][0]);
        estadoMov = estadoMovimiento.INICIANDO;
    }
    //RENDER Y ACTUALIZAR
    public void render(SpriteBatch batch){
        actualizar();
        switch(estadoMov){
            case QUIETO:
                sprite.draw(batch);
                break;
            case INICIANDO:
            case MOV_ARRIBA:
                timerAnimacion += Gdx.graphics.getDeltaTime();
                TextureRegion region = (TextureRegion)animacion.getKeyFrame(timerAnimacion);
                batch.draw(region, sprite.getX(), sprite.getY());
                break;
            case MOV_ABAJO:
                break;
        }
    }

    private void actualizar() { actualizarMovimientoHorizontal();}

    private void actualizarMovimientoHorizontal() {
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
    //ACCESORES DE POSICION
    public float getY(){return sprite.getY();}
    public float getX(){return sprite.getX();}
    public void setPosition(float x,int y){sprite.setPosition(x,y);}
}
