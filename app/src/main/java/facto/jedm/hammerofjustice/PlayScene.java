package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGGameManager;
import android.cg.com.megavirada.AndGraph.AGInputManager;
import android.cg.com.megavirada.AndGraph.AGScene;
import android.cg.com.megavirada.AndGraph.AGScreenManager;
import android.cg.com.megavirada.AndGraph.AGSoundManager;
import android.cg.com.megavirada.AndGraph.AGSprite;
import android.cg.com.megavirada.AndGraph.AGTimer;
import android.util.Log;

import java.util.ArrayList;

public class PlayScene extends AGScene {
    // Cria o Array de sprites do scoreboard
    AGSprite[] scoreboard = new AGSprite[6];

    // Cria o vetor de tiros
    ArrayList<AGSprite> hammerVector = null;
    ArrayList<AGSprite> moneyVector = null;
    ArrayList<AGSprite> condemnationVector = null;

    AGSprite[] bandits = new AGSprite[3];

    // Cria o tempo para controle de movimento de Coro
    AGTimer couroTime = null;
    AGTimer hammerTime = null;
    AGTimer moneyTime = null;

    // Cria a variavel para armazenar o cod efeito som
    //int effectMovement = 0;
    int effectDowncastBandit = 0;

    int score = 0;
    int scoreTime = 0;

    // Cria sprites de fundo e elementos controláveis
    AGSprite background = null;
    AGSprite couro = null;
    AGSprite arrowLeft = null;
    AGSprite arrowRight = null;
    AGSprite littleHammer = null;
    AGSprite bigHammer = null;
    AGSprite hammerSelected = null;
    AGSprite shoot = null;
    AGSprite paused = null;
    //AGSprite resume = null;
    AGSprite quit = null;
    AGSprite topBar = null;
    AGSprite bottomBar = null;

    float alignmentFactor;

    public PlayScene(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {
        // Carrega as imagens na memoria
        //createSprite(R.drawable.hammer, 1, 1).bVisible = false;
        //createSprite(R.drawable.comndenation, 4, 2).bVisible = false;

        hammerVector = new ArrayList<AGSprite>();
        moneyVector = new ArrayList<AGSprite>();
        condemnationVector = new ArrayList<AGSprite>();

        // Carrega a imagem de fundo 100x100 no centro da tela
        background = createSprite(R.drawable.background_play, 1, 1);
        background.setScreenPercent(100, 100);
        background.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        background.vrPosition.setY(AGScreenManager.iScreenHeight / 2);

        topBar = createSprite(R.drawable.top_bar, 1, 1);
        topBar.setScreenPercent(100, 10);
        topBar.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        topBar.vrPosition.fY = AGScreenManager.iScreenHeight - topBar.getSpriteHeight() / 2;
        topBar.bAutoRender = false;

        // Barra de controle (inferior)
        bottomBar = createSprite(R.drawable.top_bar, 1, 1);
        bottomBar.setScreenPercent(100, 10);
        bottomBar.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        bottomBar.vrPosition.fY = bottomBar.getSpriteHeight() / 2;

        // Carrega a imagem de Coro na base da tela
        couro = createSprite(R.drawable.couro, 4, 4);
        couro.setScreenPercent(25, 10);
        couro.addAnimation(1, false, 0);
        couro.addAnimation(30, false, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        couro.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        couro.vrPosition.setY(bottomBar.getSpriteHeight() + (couro.getSpriteHeight() / 2));

        // Carrega setas direcionais
        arrowLeft = createSprite(R.drawable.arrow, 1, 1);
        arrowLeft.setScreenPercent(14, 8);
        arrowLeft.vrPosition.setY(bottomBar.getSpriteHeight() / 2);
        arrowLeft.bAutoRender = false;

        arrowRight = createSprite(R.drawable.arrow, 1, 1);
        arrowRight.setScreenPercent(14, 8);
        arrowRight.vrPosition.setY(bottomBar.getSpriteHeight() / 2);
        arrowRight.fAngle = 180;
        arrowRight.bAutoRender = false;

        // Carrega selecionador de martelos
        littleHammer = createSprite(R.drawable.little_hammer, 1, 1);
        littleHammer.setScreenPercent(13, 8);
        littleHammer.vrPosition.setY(bottomBar.getSpriteHeight() / 2);
        littleHammer.bAutoRender = false;

        bigHammer = createSprite(R.drawable.big_hammer, 1, 1);
        bigHammer.setScreenPercent(13, 8);
        bigHammer.vrPosition.setY(bottomBar.getSpriteHeight() / 2);
        bigHammer.bAutoRender = false;

        hammerSelected = createSprite(R.drawable.selected_hammer, 1, 1);
        hammerSelected.setScreenPercent(18, 10);
        hammerSelected.bVisible = true;
        hammerSelected.bAutoRender = false;

        // Carrega lançados de martelos
        shoot = createSprite(R.drawable.shoot, 1, 1);
        shoot.setScreenPercent(13, 8);
        shoot.vrPosition.setY(bottomBar.getSpriteHeight() / 2);
        shoot.bAutoRender = false;

        // Calcula o espaço ocupado por todos os sprites da barra de controle (inferior)
        float ctrlSpritesWidth = arrowLeft.getSpriteWidth() + arrowRight.getSpriteWidth() + littleHammer.getSpriteWidth() + bigHammer.getSpriteWidth() + shoot.getSpriteWidth();

        // Calcula espaço livre TOTAL na barra de controle (inferior)
        float ctrlFreeSpace = 1080 - ctrlSpritesWidth;

        // Calcula espaço livre entre sprites da barra de controle (inferior)
        float ctrlFillSpace = ctrlFreeSpace / 6;

        arrowLeft.vrPosition.setX(ctrlFillSpace + arrowLeft.getSpriteWidth() / 2);
        arrowRight.vrPosition.setX(arrowLeft.vrPosition.fX + ctrlFillSpace + arrowRight.getSpriteWidth());
        littleHammer.vrPosition.setX(arrowRight.vrPosition.fX + ctrlFillSpace + littleHammer.getSpriteWidth());
        bigHammer.vrPosition.setX(littleHammer.vrPosition.fX + ctrlFillSpace + bigHammer.getSpriteWidth());
        shoot.vrPosition.setX(bigHammer.vrPosition.fX + ctrlFillSpace + shoot.getSpriteWidth());

        hammerSelected.vrPosition = littleHammer.vrPosition;

        // Configura os sprites do placar
        for (int pos = 0; pos < scoreboard.length; pos++) {
            scoreboard[pos] = createSprite(R.drawable.font, 4, 4);
            scoreboard[pos].setScreenPercent(8, 8);
            scoreboard[pos].vrPosition.fY = topBar.vrPosition.fY;
            scoreboard[pos].vrPosition.fX = 20 + (pos + 1) * scoreboard[pos].getSpriteWidth();
            scoreboard[pos].bAutoRender = false;
            for (int i = 0; i < 10; i++) {
                scoreboard[pos].addAnimation(1, false, i);
            }
        }

        // Setando tempo de execucao de movimentos de Couro, Martelo e Maço de dinheiro
        couroTime = new AGTimer(25);
        hammerTime = new AGTimer(500);
        moneyTime = new AGTimer(500);

        // Criando efeitos sonoros para movimento de Couro e Condenação
        //effectMovement = AGSoundManager.vrSoundEffects.loadSoundEffect("toc.wav");
        //effectDowncastBandit = AGSoundManager.vrSoundEffects.loadSoundEffect("downcast_bandit.mp4");

        // TODO: Corrigir animação michael jackson dos bandidões
        // Carrega os sprites dos bandits
        bandits[0] = createSprite(R.drawable.molusco, 6, 4);
        bandits[0].setScreenPercent(20, 11);
        bandits[0].vrDirection.fX = 1;
        bandits[0].addAnimation(10, true, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        bandits[0].vrPosition.fX = -bandits[0].getSpriteWidth() / 2;
        bandits[0].vrPosition.fY = AGScreenManager.iScreenHeight - bandits[0].getSpriteHeight() / 2 - topBar.getSpriteHeight();

        bandits[1] = createSprite(R.drawable.vampirao, 4, 2);
        bandits[1].setScreenPercent(20, 12);
        bandits[1].iMirror = AGSprite.HORIZONTAL;
        bandits[1].addAnimation(10, true, 0, 1, 2, 3, 4, 5, 6, 7);
        bandits[1].vrDirection.fX = -1;
        bandits[1].vrPosition.fX = AGScreenManager.iScreenWidth + bandits[1].getSpriteWidth() / 2;
        bandits[1].vrPosition.fY = bandits[0].vrPosition.fY - bandits[1].getSpriteHeight();

        bandits[2] = createSprite(R.drawable.toupeirona, 4, 2);
        bandits[2].setScreenPercent(20, 12);
        bandits[2].addAnimation(10, true, 0, 1, 2, 3, 4, 5, 6, 7);
        bandits[2].vrDirection.fX = 1;
        //bandits[2].vrPosition.fX = AGScreenManager.iScreenWidth + bandits[2].getSpriteWidth() / 2;
        bandits[2].vrPosition.fX = -bandits[2].getSpriteWidth() / 2;
        bandits[2].vrPosition.fY = bandits[1].vrPosition.fY - bandits[2].getSpriteHeight();

        paused = createSprite(R.drawable.pause_menu, 1, 1);
        paused.setScreenPercent(93, 39);
        paused.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        paused.vrPosition.fY = AGScreenManager.iScreenHeight / 2;
        paused.bVisible = false;
        paused.bAutoRender = false;

//        resume = createSprite(R.drawable.resume, 1, 1);
//        resume.setScreenPercent(53, 7);
//        resume.vrPosition.fX = AGScreenManager.iScreenWidth /  2;
//        resume.vrPosition.fY = paused.getSpriteWidth() / 3;
//        // TODO:    corrigir posicionamento da altura de continuar
//        resume.bVisible = false;
//        resume.bAutoRender = false;

        quit = createSprite(R.drawable.quit, 1, 1);
        quit.setScreenPercent(53, 7);
        quit.vrPosition.fX = AGScreenManager.iScreenWidth /  2;
        quit.vrPosition.fY = paused.vrPosition.fY;
        // TODO:    corrigir posicionamento da altura de continuar
        quit.bVisible = false;
        quit.bAutoRender = false;
    }

    @Override
    public void render() {
        super.render();
        bottomBar.render();
        arrowLeft.render();
        arrowRight.render();
        littleHammer.render();
        bigHammer.render();
        hammerSelected.render();
        shoot.render();

        paused.render();
        //resume.render();
        quit.render();

        topBar.render();
        for (AGSprite digito : scoreboard) {
            digito.render();
        }
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
            paused.bVisible = !paused.bVisible;
            quit.bVisible = !quit.bVisible;
            return;
        }

        if (!paused.bVisible) {
            selectHammer();
            //stopCouroHammerAnimation();
            createHammers();
            createMoney();
            verifyHammerBanditsColision();
            //verifyMoneyCouroColision();
            updateCouroMovement();
            updateBandits();
            updateCondemnations();
            updateHammers();
            updateMoney();
            updateScoreboard();
        }

        if (paused.bVisible) {
            if (quit.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                vrGameManager.setCurrentScene(0);
            }
        }
        //Log.d("X - Position", Float.toString(AGInputManager.vrTouchEvents.getLastPosition().getX()));
        //Log.d("Y - Position", Float.toString(AGInputManager.vrTouchEvents.getLastPosition().getY()));
    }

    private void selectHammer() {
        if (littleHammer.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            if (littleHammer.vrPosition == hammerSelected.vrPosition) {
                return;
            }
            else {
                hammerSelected.vrPosition = littleHammer.vrPosition;
            }
        }

        else if (bigHammer.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            if (bigHammer.vrPosition == hammerSelected.vrPosition) {
                return;
            }
            else {
                hammerSelected.vrPosition = bigHammer.vrPosition;
            }
        }
    }

//    private void stopCouroHammerAnimation() {
//        if (couro.getCurrentAnimationIndex() == 1 && couro.getCurrentAnimation().isAnimationEnded()) {
//            couro.setCurrentAnimation(0);
//        }
//    }

    // Coloca um Martelo no vetor de martelos
    private void createHammers() {
        hammerTime.update();

        // Tenta reciclar um Martelo criado anteriormente
        if (shoot.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            if (!hammerTime.isTimeEnded()) {
                return;
            }

            hammerTime.restart();

            for (AGSprite hammer : hammerVector) {
                if (hammer.bRecycled) {
                    hammer.bRecycled = false;
                    hammer.bVisible = true;
                    hammer.vrPosition.fX = couro.vrPosition.fX;
                    hammer.vrPosition.fY = bottomBar.getSpriteHeight() + couro.getSpriteHeight() + hammer.getSpriteHeight() / 2;
                    //Log.d("Teste", Integer.toString(couro.getCurrentAnimation().getTotalFrames()));
                    //couro.setCurrentAnimation(1);
                    return;
                }
            }

            AGSprite newHammer = createSprite(hammerSelected.vrPosition == littleHammer.vrPosition ? R.drawable.little_hammer : R.drawable.big_hammer, 1, 1);
            newHammer.setScreenPercent(8, 5);
            newHammer.vrPosition.fX = couro.vrPosition.fX;
            newHammer.vrPosition.fY = bottomBar.getSpriteHeight() + couro.getSpriteHeight() + newHammer.getSpriteHeight() / 2;
            hammerVector.add(newHammer);
        }
    }

    // Coloca um maço de dinheiro no vetor de maços
    private void createMoney() {
        moneyTime.update();

        for (AGSprite bandit : bandits) {
            if (bandit.vrPosition.fX <= couro.vrPosition.fX) {
                alignmentFactor = bandit.vrPosition.fX / couro.vrPosition.fX;
            } else {
                alignmentFactor = couro.vrPosition.fX / bandit.vrPosition.fX;
            }

            if (alignmentFactor > 0.8) {
                // Tenta reciclar um Maço de dinheiro criado anteriormente
                if (!moneyTime.isTimeEnded()) {
                    return;
                }

                moneyTime.restart();

                for (AGSprite money : moneyVector) {
                    if (money.bRecycled) {
                        money.bRecycled = false;
                        money.bVisible = true;
                        money.vrPosition.fX = bandit.vrPosition.fX;
                        money.vrPosition.fY = bandit.vrPosition.fY - (bandit.getSpriteHeight() / 2) - (money.getSpriteHeight() / 2);
                        return;
                    }
                }

                AGSprite newMoney = createSprite(R.drawable.pack_of_money, 1, 1);
                newMoney.setScreenPercent(8, 5);
                newMoney.vrPosition.fX = bandit.vrPosition.fX;
                newMoney.vrPosition.fY = bandit.vrPosition.fY - (bandit.getSpriteHeight() / 2) - (newMoney.getSpriteHeight() / 2);
                moneyVector.add(newMoney);
            }
        }
    }

    // Metodo que verifica a colisão entre Martelos e Bandidos
    private void verifyHammerBanditsColision() {
        for (AGSprite hammer : hammerVector) {
            if (hammer.bRecycled) {
                continue;
            }
            for (AGSprite bandit : bandits) {
                if (hammer.collide(bandit)) {
                    scoreTime += 50;
                    createDowncastAnimation(bandit.vrPosition.fX, bandit.vrPosition.fY);
                    hammer.bRecycled = true;
                    hammer.bVisible = false;
                    AGSoundManager.vrSoundEffects.play(effectDowncastBandit);

                    if (bandit.vrDirection.fX == 1) {
                        bandit.vrDirection.fX = -1;
                        //bandit.iMirror = AGSprite.NONE;
                        bandit.vrPosition.fX = AGScreenManager.iScreenWidth + bandit.getSpriteWidth() / 2;
                    } else {
                        bandit.vrDirection.fX = 1;
                        //bandit.iMirror = AGSprite.HORIZONTAL;
                        bandit.vrPosition.fX = -bandit.getSpriteWidth();
                    }
                    break;
                }
            }

        }
    }

    // Método que verifica a colisão entre propina e couro
    private void verifyMoneyCouroColision() {
        for (AGSprite money : moneyVector) {
            if (money.bRecycled) {
                continue;
            }
            if (money.collide(couro)) {
                scoreTime -= 50;
                //createDowncastAnimation(couro.vrPosition.fX, couro.vrPosition.fY);
                money.bRecycled = true;
                money.bVisible = false;
                //AGSoundManager.vrSoundEffects.play(effectDowncastBandit);

                break;
            }
        }
    }

    // Metodo criado para movimentar
    private void updateCouroMovement() {

        couroTime.update();
        if (couroTime.isTimeEnded()) {
            couroTime.restart();
            if (arrowRight.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                if (couro.vrPosition.getX() <= AGScreenManager.iScreenWidth - couro.getSpriteWidth() / 2) {
                    //AGSoundManager.vrSoundEffects.play(effectMovement);
                    couro.vrPosition.setX(couro.vrPosition.getX() + 10);
                }
            } else if (arrowLeft.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                if (couro.vrPosition.getX() > 0 + couro.getSpriteWidth() / 2) {
                    //AGSoundManager.vrSoundEffects.play(effectMovement);
                    couro.vrPosition.setX(couro.vrPosition.getX() - 10);
                }
            }
            if (!AGInputManager.vrTouchEvents.screenDragged()) {
                AGInputManager.vrTouchEvents.fPosX = 0;
                AGInputManager.vrTouchEvents.fPosY = 0;
            }
        }
    }

    // Metodo que atualiza a posicao dos Bandidos
    private void updateBandits() {
        for (AGSprite bandit : bandits) {
            bandit.vrPosition.fX += 5 * bandit.vrDirection.fX;
            if (bandit.vrDirection.fX == 1) {
                // Indo para a direita
                if (bandit.vrPosition.fX >= AGScreenManager.iScreenWidth + bandit.getSpriteWidth() / 2) {
                    bandit.iMirror = AGSprite.NONE;
                    bandit.vrDirection.fX = -1;
                }
            } else {
                // Indo para a esquerda
                if (bandit.vrPosition.fX <= -bandit.getSpriteWidth() / 2) {
                    bandit.iMirror = AGSprite.HORIZONTAL;
                    bandit.vrDirection.fX = 1;
                }
            }
        }
    }

    // Metodo utilizado para reciclar o vetor de Condenação
    private void updateCondemnations() {
        for (AGSprite comndenation : condemnationVector) {
            if (comndenation.getCurrentAnimation().isAnimationEnded()) {
                comndenation.bRecycled = true;
            }
        }
    }

    // metodo para atualizar o movimento dos martelos
    private void updateHammers() {
        for (AGSprite hammer : hammerVector) {
            if (hammer.bRecycled)
                continue;

            hammer.vrPosition.fY += 10;
            hammer.fAngle += 15;

            if (hammer.vrPosition.fY > AGScreenManager.iScreenHeight - topBar.getSpriteHeight() + hammer.getSpriteHeight() / 2) {
                hammer.bRecycled = true;
                hammer.bVisible = false;
            }
        }
    }

    // metodo para atualizar o movimento dos maços de dinheiro
    private void updateMoney() {
        for (AGSprite money : moneyVector) {
            if (money.bRecycled)
                continue;

            money.vrPosition.fY -= 10;
            money.fAngle -= 15;

            if (money.vrPosition.fY < bottomBar.getSpriteHeight() - money.getSpriteHeight() / 2) {
                money.bRecycled = true;
                money.bVisible = false;
            }
        }
    }

    // Metodo criado para atualizar quadros do Placar
    private void updateScoreboard() {
        if (scoreTime > 0) {
            //for (AGSprite digito : scoreboard) {
            //    digito.bVisible = !digito.bVisible;
            //}
            scoreTime--;
            score++;
        } else {
            for (AGSprite digito : scoreboard) {
                digito.bVisible = true;
            }
        }

        scoreboard[5].setCurrentAnimation(score % 10);
        scoreboard[4].setCurrentAnimation((score % 100) / 10);
        scoreboard[3].setCurrentAnimation((score % 1000) / 100);
        scoreboard[2].setCurrentAnimation((score % 10000) / 1000);
        scoreboard[1].setCurrentAnimation((score % 100000) / 10000);
        scoreboard[0].setCurrentAnimation((score % 1000000) / 100000);

    }

    // Metodo utilizado para criar animação ao abater um Bandido
    private void createDowncastAnimation(float x, float y) {
        for (AGSprite comndenation : condemnationVector) {
            if (comndenation.bRecycled) {
                comndenation.bRecycled = false;
                comndenation.getCurrentAnimation().restart();
                comndenation.vrPosition.fX = x;
                comndenation.vrPosition.fY = y;
                return;
            }
        }

        AGSprite newCondemnation = createSprite(R.drawable.comndenation, 4, 2);
        newCondemnation.setScreenPercent(20, 12);
        newCondemnation.addAnimation(10, false, 0, 7);
        newCondemnation.vrPosition.fX = x;
        newCondemnation.vrPosition.fY = y;
        condemnationVector.add(newCondemnation);
    }
}
