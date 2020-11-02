package mx.itesm.michel2.docvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Item extends Objeto{
    private Animation<TextureRegion> animacion;
    private float timerAnimacion;


    public Item(Texture textura,float x, float y,int widthTextura,int HeightTextura){
        TextureRegion region = new TextureRegion(textura);
        TextureRegion[][] texturasFrame = region.split(widthTextura,HeightTextura);

        //ANIMACION
        TextureRegion[] arrFrames = {texturasFrame[0][0],texturasFrame[0][1],texturasFrame[0][2],texturasFrame[0][3],texturasFrame[0][4],texturasFrame[0][5],texturasFrame[0][6],texturasFrame[0][7],texturasFrame[0][8],texturasFrame[0][9]};
        animacion = new Animation<TextureRegion>(0.1f,arrFrames);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;

        sprite = new Sprite(texturasFrame[0][0]);
        sprite.setPosition(x,y);
    }
    public void render(SpriteBatch batch){
        float delta = Gdx.graphics.getDeltaTime();
        timerAnimacion += delta;
        TextureRegion frame = animacion.getKeyFrame(timerAnimacion);
        batch.draw(frame,sprite.getX(),sprite.getY());
    }

}
