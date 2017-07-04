package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGActivityGame;
import android.os.Bundle;

public class LoadGame extends AGActivityGame {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, false);

        MenuScene menuScene = new MenuScene(this.vrManager);
        PlayScene playScene = new PlayScene(this.vrManager);
        CreditsScene creditsScene = new CreditsScene(this.vrManager);

        vrManager.addScene(menuScene);
        vrManager.addScene(playScene);
        vrManager.addScene(creditsScene);
    }
}
