package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Proyectil extends Objeto {

    //Fisica
    private final float VELOCIDAD_X = Pantalla.ANCHO/3;  //pixeles por segundo

    public Proyectil(Texture textura, float x, float y){
        super(textura, x, y);
    }

    public void moverDerecha(){
        float lapso = Gdx.graphics.getDeltaTime();
        float distancia = VELOCIDAD_X * lapso;
        sprite.setX(sprite.getX() + distancia);
    }
    public void moverIzquierda(){
        float lapso = Gdx.graphics.getDeltaTime();
        float distancia = -VELOCIDAD_X * lapso;
        sprite.setX(sprite.getX() + distancia);
    }

}
