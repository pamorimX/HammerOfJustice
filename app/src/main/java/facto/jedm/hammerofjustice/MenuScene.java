package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGGameManager;
import android.cg.com.megavirada.AndGraph.AGInputManager;
import android.cg.com.megavirada.AndGraph.AGScene;
import android.cg.com.megavirada.AndGraph.AGScreenManager;
import android.cg.com.megavirada.AndGraph.AGSoundManager;
import android.cg.com.megavirada.AndGraph.AGSprite;
import android.util.Log;

public class MenuScene extends AGScene {
    // TODO: 05/07/17 Providenciar background da tela de menu

    AGSprite background = null;
    AGSprite playButton = null;
    AGSprite creditsButton = null;
    AGSprite exitButton = null;
    int codigoEfeito = 0;

    public MenuScene(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {
        Log.i("TAG", Integer.toString(AGScreenManager.iScreenWidth));
        Log.i("TAG", Integer.toString(AGScreenManager.iScreenHeight));

        codigoEfeito = AGSoundManager.vrSoundEffects.loadSoundEffect("abutre.mp4");

        // Carregando plano de fundo
        // setSceneBackgroundColor("#000000");
        background = createSprite(R.drawable.background_menu, 1, 1);
        background.setScreenPercent(100, 100);
        background.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        background.vrPosition.setY(AGScreenManager.iScreenHeight / 2);

        // Criando e definindo botão jogar
        playButton = this.createSprite(R.drawable.play, 1, 1);
        playButton.setScreenPercent(33, 11);
        playButton.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        playButton.vrPosition.setY(AGScreenManager.iScreenHeight / ((float) 4 / 3));

        // Criando e definindo botão créditos
        creditsButton = this.createSprite(R.drawable.credits, 1, 1);
        creditsButton.setScreenPercent(37, 10);
        creditsButton.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        creditsButton.vrPosition.setY(AGScreenManager.iScreenHeight / 2);

        // Criando e definindo botão exit
        exitButton = this.createSprite(R.drawable.exit, 1, 1);
        exitButton.setScreenPercent(33, 26);
        exitButton.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        exitButton.vrPosition.setY(AGScreenManager.iScreenHeight / 4);

        // Carregando trilha sonora e mantendo-a em repetição
        // AGSoundManager.vrMusic.loadMusic("abutre.mp4", true);
        //AGSoundManager.vrMusic.play();
    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void loop() {
        if (playButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            vrGameManager.setCurrentScene(1);
            //Log.i("TAG", Float.toString(AGInputManager.vrTouchEvents.getLastPosition().getX()));
            //Log.i("TAG", Float.toString(AGInputManager.vrTouchEvents.getLastPosition().getY()));
            return;
        }

        if (creditsButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            vrGameManager.setCurrentScene(2);
            //Log.i("TAG", Float.toString(AGInputManager.vrTouchEvents.getLastPosition().getX()));
            //Log.i("TAG", Float.toString(AGInputManager.vrTouchEvents.getLastPosition().getY()));
            return;
        }

        if (exitButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            vrGameManager.vrActivity.finish();
        }
    }
}
