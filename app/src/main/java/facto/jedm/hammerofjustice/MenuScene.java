package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGGameManager;
import android.cg.com.megavirada.AndGraph.AGInputManager;
import android.cg.com.megavirada.AndGraph.AGScene;
import android.cg.com.megavirada.AndGraph.AGScreenManager;
import android.cg.com.megavirada.AndGraph.AGSoundManager;
import android.cg.com.megavirada.AndGraph.AGSprite;
import android.util.Log;

public class MenuScene extends AGScene {

    private AGSprite playButton = null;
    private AGSprite creditsButton = null;
    private AGSprite instructionsButton = null;
    private AGSprite quitButton = null;

    int effectButtonClick = 0;

    public MenuScene(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {
        // Carregando trilha sonora e mantendo-a em repetição
        AGSoundManager.vrMusic.loadMusic("menu_sound_track.mp3", true);
        AGSoundManager.vrMusic.setVolume(0.08f, 0.08f);
        effectButtonClick = AGSoundManager.vrSoundEffects.loadSoundEffect("button_click.wav");

        // Carregando plano de fundo
        setSceneBackgroundColor(0.43f, 0.48f, 0.9f);

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
    public void render() {
        super.render();
        AGSoundManager.vrMusic.play();
    }

    @Override
    public void restart() {}

    @Override
    public void stop() {}

    @Override
    public void loop() {
        if (AGInputManager.vrTouchEvents.screenClicked()) {
            if (playButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                AGSoundManager.vrSoundEffects.play(effectButtonClick);
                vrGameManager.setCurrentScene(1);
            }

            else if (creditsButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                AGSoundManager.vrSoundEffects.play(effectButtonClick);
                vrGameManager.setCurrentScene(2);
            }

            else if (instructionsButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                AGSoundManager.vrSoundEffects.play(effectButtonClick);
                vrGameManager.setCurrentScene(3);
            }

            else if (quitButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                AGSoundManager.vrSoundEffects.play(effectButtonClick);
                vrGameManager.vrActivity.finish();
            }
        }
    }
}
