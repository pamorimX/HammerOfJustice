package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGGameManager;
import android.cg.com.megavirada.AndGraph.AGInputManager;
import android.cg.com.megavirada.AndGraph.AGScene;
import android.cg.com.megavirada.AndGraph.AGScreenManager;
import android.cg.com.megavirada.AndGraph.AGSprite;
import android.cg.com.megavirada.AndGraph.AGTouchScreen;

public class CreditsScene extends AGScene {

    public CreditsScene(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {
        setSceneBackgroundColor(0.43f, 0.48f, 0.9f);
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
            vrGameManager.setCurrentScene(0);
        }
    }
}
