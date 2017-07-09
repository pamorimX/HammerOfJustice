package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGActivityGame;
import android.os.Bundle;

public class LoadGame extends AGActivityGame {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, true);

        //vrManager.addScene(new MenuScene(this.vrManager));
        vrManager.addScene(new PlayScene(this.vrManager));
        //vrManager.addScene(new CreditsScene(this.vrManager));
    }
}
