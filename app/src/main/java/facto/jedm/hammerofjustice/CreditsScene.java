package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGGameManager;
import android.cg.com.megavirada.AndGraph.AGInputManager;
import android.cg.com.megavirada.AndGraph.AGScene;
import android.cg.com.megavirada.AndGraph.AGScreenManager;
import android.cg.com.megavirada.AndGraph.AGSoundManager;
import android.cg.com.megavirada.AndGraph.AGSprite;
import android.cg.com.megavirada.AndGraph.AGTouchScreen;

public class CreditsScene extends AGScene {
    AGSprite background = null;
    int effectButtonClick = 0;

    public CreditsScene(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {
        background = createSprite(R.drawable.background_credits, 1, 1);
        background.setScreenPercent(100, 100);
        background.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        background.vrPosition.setY(AGScreenManager.iScreenHeight / 2);
        effectButtonClick = AGSoundManager.vrSoundEffects.loadSoundEffect("button_click.wav");
    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void loop() {
        if (AGInputManager.vrTouchEvents.backButtonClicked()) {
            AGSoundManager.vrSoundEffects.play(effectButtonClick);
            vrGameManager.setCurrentScene(0);
        }
    }
}
