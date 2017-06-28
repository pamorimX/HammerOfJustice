package facto.jedm.hammerofjustice;

import android.os.Bundle;

import android.cg.com.megavirada.AndGraph.AGActivityGame;

public class MenuScene extends AGActivityGame {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_menu_scene);
        init(this, false);

        CreditsScene creditsScene = new CreditsScene(this.vrManager);
        PlayScene playScene = new PlayScene(this.vrManager);

        vrManager.addScene(creditsScene);
        vrManager.addScene(playScene);
    }
}
