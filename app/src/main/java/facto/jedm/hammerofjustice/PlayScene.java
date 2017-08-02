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
    AGTimer ammoTime = null;

    // Cria a variavel para armazenar o cod efeito som
    //int effectMovement = 0;
    int effectDowncastBandit = 0;

    int score = 100;
    int positiveScoreTime = 0;
    int negativeScoreTime = 0;
    int ammoSpent = 0;
    int ammoRecovered = 0;

    boolean pause;
    boolean gameOver;

    // Cria sprites de fundo e elementos controláveis
    AGSprite background = null;
    AGSprite couro = null;
    AGSprite arrowLeft = null;
    AGSprite arrowRight = null;
    AGSprite littleHammer = null;
    AGSprite bigHammer = null;
    AGSprite hammerSelected = null;
    AGSprite shoot = null;
    AGSprite pauseMenu = null;
    AGSprite resume = null;
    AGSprite quit = null;
    AGSprite gameOverMenu = null;
    AGSprite restartGame = null;
    AGSprite goHomeMenu = null;
    AGSprite topBar = null;
    AGSprite bottomBar = null;
    AGSprite emptyAmmoBar = null;
    AGSprite fullAmmoBar = null;

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

        emptyAmmoBar = createSprite(R.drawable.empty_ammo_bar, 1, 1);
        emptyAmmoBar.setScreenPercent(36, 4);
        emptyAmmoBar.vrPosition.fX = AGScreenManager.iScreenWidth - emptyAmmoBar.getSpriteWidth() / 2;
        emptyAmmoBar.vrPosition.fY = topBar.vrPosition.fY;
        emptyAmmoBar.bAutoRender = false;

        fullAmmoBar = createSprite(R.drawable.full_ammo_bar, 1, 1);
        fullAmmoBar.setScreenPercent(36, 2);
        fullAmmoBar.vrPosition.fX = AGScreenManager.iScreenWidth - emptyAmmoBar.getSpriteWidth() / 2;
        fullAmmoBar.vrPosition.fY = topBar.vrPosition.fY;
        fullAmmoBar.bAutoRender = false;

        // Barra de controle (inferior)
        bottomBar = createSprite(R.drawable.top_bar, 1, 1);
        bottomBar.setScreenPercent(100, 10);
        bottomBar.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        bottomBar.vrPosition.fY = bottomBar.getSpriteHeight() / 2;

        // Carrega a imagem de Coro na base da tela
        couro = createSprite(R.drawable.couro, 4, 4);
        couro.setScreenPercent(25, 10);
        couro.addAnimation(1, false, 0);
        couro.addAnimation(30, false, 0, 12);
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

        // Setando controles de paralisação
        pause = false;
        gameOver = false;

        // Setando tempo de repetição das ações Couro, Martelo e Maço de dinheiro
        couroTime = new AGTimer(25);
        hammerTime = new AGTimer(500);
        moneyTime = new AGTimer(500);
        ammoTime = new AGTimer(2000);

        // Criando efeitos sonoros de eventos
        effectDowncastBandit = AGSoundManager.vrSoundEffects.loadSoundEffect("downcast_bandit.mp3");

        // TODO: Corrigir animação michael jackson dos bandidões
        // Carrega os sprites dos bandits
        bandits[0] = createSprite(R.drawable.molusco, 6, 4);
        bandits[0].setScreenPercent(20, 11);
        bandits[0].vrDirection.fX = 1;
        bandits[0].addAnimation(10, true, 0, 20);
        bandits[0].vrPosition.fX = -bandits[0].getSpriteWidth() / 2;
        bandits[0].vrPosition.fY = AGScreenManager.iScreenHeight - topBar.getSpriteHeight() - bandits[0].getSpriteHeight() / 2;

        bandits[1] = createSprite(R.drawable.vampirao, 4, 2);
        bandits[1].setScreenPercent(20, 12);
        bandits[1].iMirror = AGSprite.HORIZONTAL;
        bandits[1].addAnimation(10, true, 0, 7);
        bandits[1].vrDirection.fX = -1;
        bandits[1].vrPosition.fX = AGScreenManager.iScreenWidth + bandits[1].getSpriteWidth() / 2;
        bandits[1].vrPosition.fY = bandits[0].vrPosition.fY - bandits[1].getSpriteHeight();

        bandits[2] = createSprite(R.drawable.toupeirona, 4, 2);
        bandits[2].setScreenPercent(20, 12);
        bandits[2].addAnimation(10, true, 0, 7);
        bandits[2].vrDirection.fX = 1;
        bandits[2].vrPosition.fX = -bandits[2].getSpriteWidth() / 2;
        bandits[2].vrPosition.fY = bandits[1].vrPosition.fY - bandits[2].getSpriteHeight();

        pauseMenu = createSprite(R.drawable.pause_menu, 1, 1);
        pauseMenu.setScreenPercent(76, 40);
        pauseMenu.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        pauseMenu.vrPosition.fY = AGScreenManager.iScreenHeight / 2;
        pauseMenu.bVisible = false;
        pauseMenu.bAutoRender = false;

        resume = createSprite(R.drawable.resume, 1, 1);
        resume.setScreenPercent(76, 9);
        resume.vrPosition.fX = pauseMenu.vrPosition.fX;
        resume.vrPosition.fY = pauseMenu.vrPosition.fY;
        resume.bVisible = false;
        resume.bAutoRender = false;

        quit = createSprite(R.drawable.quit, 1, 1);
        quit.setScreenPercent(76, 9);
        quit.vrPosition.fX = pauseMenu.vrPosition.fX;
        quit.vrPosition.fY = resume.vrPosition.fY - resume.getSpriteHeight() - quit.getSpriteHeight() / 2;
        quit.bVisible = false;
        quit.bAutoRender = false;

        gameOverMenu = createSprite(R.drawable.game_over_menu, 1, 1);
        gameOverMenu.setScreenPercent(76, 40);
        gameOverMenu.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        gameOverMenu.vrPosition.fY = AGScreenManager.iScreenHeight / 2;
        gameOverMenu.bVisible = false;
        gameOverMenu.bAutoRender = false;

        restartGame = createSprite(R.drawable.restart, 1, 1);
        restartGame.setScreenPercent(76, 9);
        restartGame.vrPosition.fX = gameOverMenu.vrPosition.fX;
        restartGame.vrPosition.fY = gameOverMenu.vrPosition.fY;
        restartGame.bVisible = false;
        restartGame.bAutoRender = false;

        goHomeMenu = createSprite(R.drawable.go_home, 1, 1);
        goHomeMenu.setScreenPercent(76, 9);
        goHomeMenu.vrPosition.fX = pauseMenu.vrPosition.fX;
        goHomeMenu.vrPosition.fY = gameOverMenu.vrPosition.fY - restartGame.getSpriteHeight() - goHomeMenu.getSpriteHeight() / 2;
        goHomeMenu.bVisible = false;
        goHomeMenu.bAutoRender = false;
    }

    @Override
    public void render() {
        super.render();

        // Barra superior
        topBar.render();
        emptyAmmoBar.render();
        fullAmmoBar.render();
        for (AGSprite digito : scoreboard) {
            digito.render();
        }

        // Menu de pausa
        pauseMenu.render();
        resume.render();
        quit.render();

        // Menu de Game Over
        gameOverMenu.render();
        restartGame.render();
        goHomeMenu.render();

        // Barra Inferior
        bottomBar.render();
        arrowLeft.render();
        arrowRight.render();
        littleHammer.render();
        bigHammer.render();
        hammerSelected.render();
        shoot.render();
    }

    @Override
    public void restart() {}

    @Override
    public void stop() {}

    @Override
    public void loop() {
        if (AGInputManager.vrTouchEvents.backButtonClicked()) {
            pauseEvent();
        }

        // Jogo rodando
        if (!pause && !gameOver) {
            selectHammer();
            recoverAmmo();
            updateAmmoBar();
            createHammers();
            createMoney();
            verifyHammerBanditsColision();
            verifyMoneyCouroColision();
            updateCouroMovement();
            updateBandits();
            updateCondemnations();
            updateHammers();
            updateMoney();
            updateScoreboard();
        }

        // Jogo pausado
        else if (pause) {
            //for (AGSprite bandit : bandits) {
            //    TODO: Se possível, implementar paralisação de animação dos bandidos
            //    bandit.getCurrentAnimationFrame();
            //}

            if (resume.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                pauseEvent();
            }

            if (quit.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                vrGameManager.setCurrentScene(0);
                return;
            }
        }

        // Game Over
        else  {
            if (restartGame.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                Log.d("TAG", Float.toString(AGInputManager.vrTouchEvents.getLastPosition().getX()));
                Log.d("TAG", Float.toString(AGInputManager.vrTouchEvents.getLastPosition().getY()));
                vrGameManager.setCurrentScene(1);
                return;
            }

            if (goHomeMenu.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                vrGameManager.setCurrentScene(0);
                return;
            }
        }
    }

    public void pauseEvent() {
        pause = !pause;
        switchPauseMenu();
    }

    private void switchPauseMenu() {
        pauseMenu.bVisible = !pauseMenu.bVisible;
        resume.bVisible = !resume.bVisible;
        quit.bVisible = !quit.bVisible;
        return;
    }

    private void showGameOverMenu() {
        gameOverMenu.bVisible = !gameOverMenu.bVisible;
        restartGame.bVisible = !restartGame.bVisible;
        goHomeMenu.bVisible = !goHomeMenu.bVisible;
        return;
    }

    private void selectHammer() {
        if (littleHammer.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            if (littleHammer.vrPosition == hammerSelected.vrPosition) {
                return;
            } else {
                hammerSelected.vrPosition = littleHammer.vrPosition;
            }
        } else if (bigHammer.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            if (bigHammer.vrPosition == hammerSelected.vrPosition) {
                return;
            } else {
                hammerSelected.vrPosition = bigHammer.vrPosition;
            }
        }
    }

    private void recoverAmmo() {
        ammoTime.update();

        if (fullAmmoBar.vrPosition.fX != emptyAmmoBar.vrPosition.fX) {
            if (ammoTime.isTimeEnded()) {
                ammoRecovered += 10;
                ammoTime.restart();
            }
        }
    }

    // Atualiza o status de munição de Couro
    private void updateAmmoBar() {
        if (ammoSpent < 0) {
            ammoSpent++;
            if (fullAmmoBar.collide(emptyAmmoBar))
                fullAmmoBar.vrPosition.fX += 1;
        }

        if (ammoRecovered > 0) {
            ammoRecovered--;
            if (fullAmmoBar.vrPosition.fX != emptyAmmoBar.vrPosition.fX)
                fullAmmoBar.vrPosition.fX -= 1;
        }
    }

    // Coloca um Martelo no vetor de martelos
    private void createHammers() {
        hammerTime.update();

        // Tenta reciclar um Martelo criado anteriormente
        if (shoot.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            if (!hammerTime.isTimeEnded()) {
                return;
            }

            if (!fullAmmoBar.collide(emptyAmmoBar)) {
                return;
            }

            hammerTime.restart();

            ammoSpent -= 100;

            // Anima o lançamento do martelo
            if (couro.getCurrentAnimation().isAnimationEnded())
                if (couro.getCurrentAnimationIndex() == 0)
                    couro.setCurrentAnimation(1);
                couro.getCurrentAnimation().restart();

            for (AGSprite hammer : hammerVector) {

                if (hammer.bRecycled) {
                    hammer.bRecycled = false;
                    hammer.bVisible = true;
                    hammer.vrPosition.fX = couro.vrPosition.fX;
                    hammer.vrPosition.fY = bottomBar.getSpriteHeight() + couro.getSpriteHeight() + hammer.getSpriteHeight() / 2;
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

            if (alignmentFactor > 0.9) {
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
            if (hammer.bRecycled)
                continue;

            for (AGSprite bandit : bandits) {
                if (hammer.collide(bandit)) {
                    positiveScoreTime += 50;
                    createExplosionAnimation(bandit.vrPosition.fX, bandit.vrPosition.fY);
                    hammer.bRecycled = true;
                    hammer.bVisible = false;
                    AGSoundManager.vrSoundEffects.play(effectDowncastBandit);

                    if (bandit.vrDirection.fX == 1) {
                        bandit.vrDirection.fX = -1;
                        bandit.iMirror = AGSprite.NONE;
                        bandit.vrPosition.fX = AGScreenManager.iScreenWidth + bandit.getSpriteWidth() / 2;
                    } else {
                        bandit.vrDirection.fX = 1;
                        bandit.iMirror = AGSprite.HORIZONTAL;
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
                negativeScoreTime -= 10;
                //createExplosionAnimation(couro.vrPosition.fX, couro.vrPosition.fY);
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

    // Verifica se Couro foi derrotado
    private boolean isCouroDefeated() {
        return score == 0 ? true : false;
    }

    // Metodo criado para atualizar quadros do Placar
    private void updateScoreboard() {
        // Soma score
        if (positiveScoreTime > 0) {
            positiveScoreTime--;
            score++;
        }

        // Subtrai score
        if (negativeScoreTime < 0) {
            negativeScoreTime++;
            score--;
        }

        if (isCouroDefeated()) {
            // TODO: implementar imagem gameover sobrepondo o jogo (jogar novamente ou sair)
            gameOver = true;
            showGameOverMenu();
            return;
        }

        if (positiveScoreTime == 0 && negativeScoreTime == 0) {
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
    private void createExplosionAnimation(float x, float y) {
        for (AGSprite comndenation : condemnationVector) {
            if (comndenation.bRecycled) {
                comndenation.bRecycled = false;
                comndenation.getCurrentAnimation().restart();
                comndenation.vrPosition.fX = x;
                comndenation.vrPosition.fY = y;
                return;
            }
        }

        AGSprite newCondemnation = createSprite(R.drawable.comndenation, 8, 6);
        newCondemnation.setScreenPercent(21, 12);
        newCondemnation.addAnimation(30, false, 0, 47);
        newCondemnation.vrPosition.fX = x;
        newCondemnation.vrPosition.fY = y;
        condemnationVector.add(newCondemnation);
    }
}
