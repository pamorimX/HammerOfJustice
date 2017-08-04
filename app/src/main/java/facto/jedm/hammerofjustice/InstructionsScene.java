package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGGameManager;
import android.cg.com.megavirada.AndGraph.AGInputManager;
import android.cg.com.megavirada.AndGraph.AGScene;


public class InstructionsScene extends AGScene {

    public InstructionsScene(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {
        //setSceneBackgroundColor(0.49f, 0.44f, 0.89f);
        setSceneBackgroundColor(0.43, 0.48, 0.9);
    }

    @Override
    public void restart() {}

    @Override
    public void stop() {}

    @Override
    public void loop() {
        if (AGInputManager.vrTouchEvents.backButtonClicked()) {
            vrGameManager.setCurrentScene(0);
        }
    }
}
