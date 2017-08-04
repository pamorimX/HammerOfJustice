package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGGameManager;
import android.cg.com.megavirada.AndGraph.AGInputManager;
import android.cg.com.megavirada.AndGraph.AGScene;
import android.cg.com.megavirada.AndGraph.AGScreenManager;
import android.cg.com.megavirada.AndGraph.AGSprite;
import android.util.Log;

public class MenuScene extends AGScene {
    // TODO: 05/07/17 Providenciar background da tela de menu

    private AGSprite background = null;
    private AGSprite playButton = null;
    private AGSprite creditsButton = null;
    private AGSprite instructionsButton = null;
    private AGSprite quitButton = null;

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

        // Criando e definindo botão créditos
        creditsButton = this.createSprite(R.drawable.credits_menu, 1, 1);
        creditsButton.setScreenPercent(64, 10);
        creditsButton.vrPosition.setX(AGScreenManager.iScreenWidth / 2);

        // Criando e definindo botão instruções
        instructionsButton = this.createSprite(R.drawable.instructions_menu, 1, 1);
        instructionsButton.setScreenPercent(64, 10);
        instructionsButton.vrPosition.setX(AGScreenManager.iScreenWidth / 2);

        // Criando e definindo botão quit_menu
        quitButton = this.createSprite(R.drawable.quit_menu, 1, 1);
        quitButton.setScreenPercent(64, 10);
        quitButton.vrPosition.setX(AGScreenManager.iScreenWidth / 2);

        // Calcula o espaço ocupado por todos os sprites dispostos verticalmente
        float buttonsSpace = playButton.getSpriteHeight() + creditsButton.getSpriteHeight()
                + instructionsButton.getSpriteHeight() + quitButton.getSpriteHeight();

        // Calcula espaço livre TOTAL no espaço vertical entre os botões
        float freeButtonsSpace = AGScreenManager.iScreenHeight - buttonsSpace;

        // Calcula espaço livre entre botões dispostos verticalmente
        float spaceBetweenButtons = freeButtonsSpace / 5;

        quitButton.vrPosition.setY(spaceBetweenButtons + quitButton.getSpriteHeight() / 2);
        instructionsButton.vrPosition.setY(quitButton.vrPosition.fY + spaceBetweenButtons + instructionsButton.getSpriteHeight());
        creditsButton.vrPosition.setY(instructionsButton.vrPosition.fY + spaceBetweenButtons + creditsButton.getSpriteHeight());
        playButton.vrPosition.setY(creditsButton.vrPosition.fY + spaceBetweenButtons + playButton.getSpriteHeight());
    }

    @Override
    public void restart() {}

    @Override
    public void stop() {}

    @Override
    public void loop() {
        if (AGInputManager.vrTouchEvents.screenClicked()) {
            if (playButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                vrGameManager.setCurrentScene(1);
            }

            else if (creditsButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                vrGameManager.setCurrentScene(2);
            }

            else if (instructionsButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                vrGameManager.setCurrentScene(3);
            }

            else if (quitButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                vrGameManager.vrActivity.finish();
            }
        }
    }
}
