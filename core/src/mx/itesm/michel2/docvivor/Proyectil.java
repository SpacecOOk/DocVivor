package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Proyectil extends Objeto {

    //Fisica
    public final float VELOCIDAD_X = 800;  //pixeles por segundo

    public Proyectil(Texture textura, float x, float y){
        super(textura, x, y);
    }

    private int orientacion2;

    public void mover(int orientacion){
        if(orientacion == 1) {
            float lapso = Gdx.graphics.getDeltaTime();
            float distancia = VELOCIDAD_X * lapso;
            sprite.setX(sprite.getX() + distancia);
        }else if(orientacion == 0){
            float lapso = Gdx.graphics.getDeltaTime();
            float distancia = -VELOCIDAD_X * lapso;
            sprite.setX(sprite.getX() + distancia);
        }
    }

    public void mover2(){
        if(orientacion2 == 1) {
            float lapso = Gdx.graphics.getDeltaTime();
            float distancia = VELOCIDAD_X * lapso;
            sprite.setX(sprite.getX() + distancia);
            sprite.rotate(10);
        }else if(orientacion2 == 0){
            float lapso = Gdx.graphics.getDeltaTime();
            float distancia = -VELOCIDAD_X * lapso;
            sprite.setX(sprite.getX() + distancia);
            sprite.rotate(10);
        }
    }

    public void setOrientacion2(int orientacion2) {
        this.orientacion2 = orientacion2;
    }
    public int getOrientacion2(){
        return this.orientacion2;
    }
}