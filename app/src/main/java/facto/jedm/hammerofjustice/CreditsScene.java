package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGGameManager;
import android.cg.com.megavirada.AndGraph.AGInputManager;
import android.cg.com.megavirada.AndGraph.AGScene;
import android.cg.com.megavirada.AndGraph.AGScreenManager;
import android.cg.com.megavirada.AndGraph.AGSprite;
import android.cg.com.megavirada.AndGraph.AGTouchScreen;

public class CreditsScene extends AGScene {
    private AGSprite background = null;
//    private AGSprite goHomeMenu = null;

    public CreditsScene(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {
        background = createSprite(R.drawable.background_credits, 1, 1);
        background.setScreenPercent(100, 100);
        background.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        background.vrPosition.setY(AGScreenManager.iScreenHeight / 2);

//        goHomeMenu = createSprite(R.drawable.go_home, 1, 1);
//        goHomeMenu.setScreenPercent(76, 9);
//        goHomeMenu.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
//        goHomeMenu.vrPosition.fY = AGScreenManager.iScreenHeight / 2;
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
            return;
        }
    }
}
