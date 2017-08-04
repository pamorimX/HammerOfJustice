package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGActivityGame;
import android.os.Bundle;

public class LoadScenes extends AGActivityGame {
    private MenuScene menuScene = null;
    private PlayScene playScene = null;
    private CreditsScene creditsScene = null;
    private InstructionsScene instructionsScene = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init(this, true);

        menuScene = new MenuScene(this.vrManager);
        playScene = new PlayScene(this.vrManager);
        creditsScene = new CreditsScene(this.vrManager);
        instructionsScene = new InstructionsScene(this.vrManager);

        vrManager.addScene(menuScene);
        vrManager.addScene(playScene);
        vrManager.addScene(creditsScene);
        vrManager.addScene(instructionsScene);
    }
}
