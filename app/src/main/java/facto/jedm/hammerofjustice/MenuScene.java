package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGGameManager;
import android.cg.com.megavirada.AndGraph.AGInputManager;
import android.cg.com.megavirada.AndGraph.AGScene;
import android.cg.com.megavirada.AndGraph.AGScreenManager;
import android.cg.com.megavirada.AndGraph.AGSprite;

public class MenuScene extends AGScene {
    // TODO: 05/07/17 Providenciar background da tela de menu

    AGSprite background = null;
    AGSprite playButton = null;
    AGSprite creditsButton = null;
    AGSprite quitButton = null;

    public MenuScene(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {
        // Carregando trilha sonora e mantendo-a em repetição
        //AGSoundManager.vrMusic.loadMusic("abutre.mp4", true);
        //AGSoundManager.vrMusic.play();

        // Carregando plano de fundo
        // setSceneBackgroundColor("#000000");
        background = createSprite(R.drawable.background_menu, 1, 1);
        background.setScreenPercent(100, 100);
        background.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        background.vrPosition.setY(AGScreenManager.iScreenHeight / 2);

        // Criando e definindo botão jogar
        playButton = this.createSprite(R.drawable.play_menu, 1, 1);
        playButton.setScreenPercent(64, 10);
        playButton.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        playButton.vrPosition.setY(AGScreenManager.iScreenHeight / ((float) 4 / 3));

        // Criando e definindo botão créditos
        creditsButton = this.createSprite(R.drawable.credits_menu, 1, 1);
        creditsButton.setScreenPercent(64, 10);
        creditsButton.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        creditsButton.vrPosition.setY(AGScreenManager.iScreenHeight / 2);

        // Criando e definindo botão quit_menu
        quitButton = this.createSprite(R.drawable.quit_menu, 1, 1);
        quitButton.setScreenPercent(64, 10);
        quitButton.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        quitButton.vrPosition.setY(AGScreenManager.iScreenHeight / 4);
    }

    @Override
    public void restart() {}

    @Override
    public void stop() {}

    @Override
    public void loop() {
        if (playButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            vrGameManager.setCurrentScene(1);
            return;
        }

        if (creditsButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            vrGameManager.setCurrentScene(2);
            return;
        }

        if (quitButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            vrGameManager.vrActivity.finish();
        }
    }
}
