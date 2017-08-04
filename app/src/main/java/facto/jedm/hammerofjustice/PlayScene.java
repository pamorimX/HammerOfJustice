package facto.jedm.hammerofjustice;

import android.cg.com.megavirada.AndGraph.AGGameManager;
import android.cg.com.megavirada.AndGraph.AGInputManager;
import android.cg.com.megavirada.AndGraph.AGScene;
import android.cg.com.megavirada.AndGraph.AGScreenManager;
import android.cg.com.megavirada.AndGraph.AGSoundManager;
import android.cg.com.megavirada.AndGraph.AGSprite;
import android.cg.com.megavirada.AndGraph.AGTimer;

import java.util.ArrayList;

public class PlayScene extends AGScene {
    // Cria o Array de sprites do scoreboard
    AGSprite[] scoreboard = new AGSprite[6];

    // Cria o vetor de tiros
    ArrayList<AGSprite> littleHammerVector = null;
    ArrayList<AGSprite> bigHammerVector = null;
    ArrayList<AGSprite> moneyVector = null;
    ArrayList<AGSprite> condemnationVector = null;

    AGSprite[] bandits = new AGSprite[3];

    // Cria o tempo para controle de movimento de Coro
    AGTimer couroTime = null;
    AGTimer hammerTime = null;
    AGTimer moneyTime = null;
    AGTimer ammoTime = null;

    // Efeitos sonoros de eventos
    //int effectMovement = 0;
    int effectDowncastBandit = 0;

    // Controle de saldo
    int score;
    int positiveScoreTime;
    int negativeScoreTime;

    // Controle de munição gasta e recuperada
    int ammoSpent;
    int ammoRecovered;

    // Controles de paralisação
    boolean pause;
    boolean gameOver;

    // Cria sprites de fundo e elementos controláveis
    AGSprite background = null;
    AGSprite couro = null;
    AGSprite arrowLeft = null;
    AGSprite arrowRight = null;
    AGSprite littleHammer = null;
    AGSprite bigHammer = null;
    AGSprite hammerSelectedIndicator = null;
    AGSprite shoot = null;
    AGSprite pauseMenu = null;
    AGSprite resumeGameButton = null;
    AGSprite quitGameButton = null;
    AGSprite gameOverMenu = null;
    AGSprite restartGameButton = null;
    AGSprite goHomeMenuButton = null;
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
        // Parâmetros iniciais de jogo
        score = 100;
        positiveScoreTime = 0;
        negativeScoreTime = 0;

        ammoSpent = 0;
        ammoRecovered = 0;

        pause = false;
        gameOver = false;

        // Vetores de martelo, dinheiro e condenações
        littleHammerVector = new ArrayList<AGSprite>();
        bigHammerVector = new ArrayList<AGSprite>();
        moneyVector = new ArrayList<AGSprite>();
        condemnationVector = new ArrayList<AGSprite>();

        // Carrega a imagem de fundo 100x100 no centro da tela
        background = createSprite(R.drawable.background_play, 1, 1);
        background.setScreenPercent(100, 100);
        background.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        background.vrPosition.setY(AGScreenManager.iScreenHeight / 2);

        topBar = createSprite(R.drawable.bar, 1, 1);
        topBar.setScreenPercent(100, 8);
        topBar.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        topBar.vrPosition.fY = AGScreenManager.iScreenHeight - topBar.getSpriteHeight() / 2;
        topBar.bAutoRender = false;

        emptyAmmoBar = createSprite(R.drawable.empty_ammo_bar, 1, 1);
        emptyAmmoBar.setScreenPercent(100, 2);
        emptyAmmoBar.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        emptyAmmoBar.vrPosition.fY = topBar.vrPosition.fY - topBar.getSpriteHeight() / 2 - emptyAmmoBar.getSpriteHeight() / 2;
        emptyAmmoBar.bAutoRender = false;

        fullAmmoBar = createSprite(R.drawable.full_ammo_bar, 1, 1);
        fullAmmoBar.setScreenPercent(100, 2);
        fullAmmoBar.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        fullAmmoBar.vrPosition.fY = topBar.vrPosition.fY - topBar.getSpriteHeight() / 2 - fullAmmoBar.getSpriteHeight() / 2;
        fullAmmoBar.bAutoRender = false;

        // Barra de controle (inferior)
        bottomBar = createSprite(R.drawable.bar, 1, 1);
        bottomBar.setScreenPercent(100, 8);
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
        littleHammer.setScreenPercent(14, 6);
        littleHammer.vrPosition.setY(bottomBar.getSpriteHeight() / 2);
        littleHammer.bAutoRender = false;

        bigHammer = createSprite(R.drawable.big_hammer, 1, 1);
        bigHammer.setScreenPercent(14, 6);
        bigHammer.vrPosition.setY(bottomBar.getSpriteHeight() / 2);
        bigHammer.bAutoRender = false;

        hammerSelectedIndicator = createSprite(R.drawable.hammer_selected_indicator, 1, 1);
        hammerSelectedIndicator.setScreenPercent(15, 8);
        hammerSelectedIndicator.bVisible = true;
        hammerSelectedIndicator.bAutoRender = false;

        // Carrega lançados de martelos
        shoot = createSprite(R.drawable.shoot, 1, 1);
        shoot.setScreenPercent(13, 8);
        shoot.vrPosition.setY(bottomBar.getSpriteHeight() / 2);
        shoot.bAutoRender = false;

        // Calcula o espaço ocupado por todos os sprites da barra de controle (inferior)
        float totalBottomBarSpritesWidth = arrowLeft.getSpriteWidth() + arrowRight.getSpriteWidth() + littleHammer.getSpriteWidth() + bigHammer.getSpriteWidth() + shoot.getSpriteWidth();

        // Calcula espaço livre TOTAL na barra de controle (inferior)
        float freeBottomBarSpace = AGScreenManager.iScreenWidth - totalBottomBarSpritesWidth;

        // Calcula espaço livre entre sprites da barra de controle (inferior)
        float fillSpace = freeBottomBarSpace / 6;

        arrowLeft.vrPosition.setX(fillSpace + arrowLeft.getSpriteWidth() / 2);
        arrowRight.vrPosition.setX(arrowLeft.vrPosition.fX + fillSpace + arrowRight.getSpriteWidth());
        littleHammer.vrPosition.setX(arrowRight.vrPosition.fX + fillSpace + littleHammer.getSpriteWidth());
        bigHammer.vrPosition.setX(littleHammer.vrPosition.fX + fillSpace + bigHammer.getSpriteWidth());
        shoot.vrPosition.setX(bigHammer.vrPosition.fX + fillSpace + shoot.getSpriteWidth());

        // Definindo o martelo selecionado inicialmente
        hammerSelectedIndicator.vrPosition = bigHammer.vrPosition;

        // Posiciona cada dígito à direita do anterior
        // Cria as 10 animações pra cada dígito do placar
        for (int pos = 0; pos < scoreboard.length; pos++) {
            scoreboard[pos] = createSprite(R.drawable.numbers, 10, 1);
            scoreboard[pos].setScreenPercent(9, 6);
            scoreboard[pos].vrPosition.fY = topBar.vrPosition.fY;
            scoreboard[pos].vrPosition.fX = 20 + (pos + 1) * scoreboard[pos].getSpriteWidth();
            scoreboard[pos].bAutoRender = false;
            for (int i = 0; i < 10; i++) {
                scoreboard[pos].addAnimation(1, false, i);
            }
        }

        // Setando tempo de repetição das ações Couro, Martelo e Maço de dinheiro
        couroTime = new AGTimer(25);
        hammerTime = new AGTimer(500);
        moneyTime = new AGTimer(500);
        ammoTime = new AGTimer(2000);

        // Criando efeitos sonoros de eventos
        effectDowncastBandit = AGSoundManager.vrSoundEffects.loadSoundEffect("downcast_bandit.mp3");

        // Carrega os sprites dos bandits
        bandits[0] = createSprite(R.drawable.molusco, 6, 4);
        bandits[0].setScreenPercent(20, 11);
        bandits[0].vrDirection.fX = 1;
        bandits[0].addAnimation(10, true, 0, 20);
        bandits[0].vrPosition.fX = -bandits[0].getSpriteWidth() / 2;
        bandits[0].vrPosition.fY = AGScreenManager.iScreenHeight - topBar.getSpriteHeight() - emptyAmmoBar.getSpriteHeight() - bandits[0].getSpriteHeight() / 2;

        bandits[1] = createSprite(R.drawable.vampirao, 4, 2);
        bandits[1].setScreenPercent(20, 12);
        bandits[1].iMirror = AGSprite.HORIZONTAL;
        bandits[1].addAnimation(10, true, 0, 7);
        bandits[1].vrDirection.fX = -1;
        bandits[1].vrPosition.fX = AGScreenManager.iScreenWidth + bandits[1].getSpriteWidth() / 2;
        //bandits[1].vrPosition.fY = bandits[0].vrPosition.fY - bandits[1].getSpriteHeight();
        bandits[1].vrPosition.fY = bandits[0].vrPosition.fY - bandits[1].getSpriteHeight()/2;

        bandits[2] = createSprite(R.drawable.toupeirona, 4, 2);
        bandits[2].setScreenPercent(20, 12);
        bandits[2].addAnimation(10, true, 0, 7);
        bandits[2].vrDirection.fX = 1;
        bandits[2].vrPosition.fX = -bandits[2].getSpriteWidth() / 2;
        //bandits[2].vrPosition.fY = bandits[1].vrPosition.fY - bandits[2].getSpriteHeight();
        bandits[2].vrPosition.fY = bandits[1].vrPosition.fY - bandits[2].getSpriteHeight()/2;

        pauseMenu = createSprite(R.drawable.pause_menu, 1, 1);
        pauseMenu.setScreenPercent(76, 40);
        pauseMenu.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        pauseMenu.vrPosition.fY = AGScreenManager.iScreenHeight / 2;
        pauseMenu.bVisible = false;
        pauseMenu.bAutoRender = false;

        resumeGameButton = createSprite(R.drawable.resume_game, 1, 1);
        resumeGameButton.setScreenPercent(76, 9);
        resumeGameButton.vrPosition.fX = pauseMenu.vrPosition.fX;
        resumeGameButton.vrPosition.fY = pauseMenu.vrPosition.fY;
        resumeGameButton.bVisible = false;
        resumeGameButton.bAutoRender = false;

        quitGameButton = createSprite(R.drawable.quit_game, 1, 1);
        quitGameButton.setScreenPercent(76, 9);
        quitGameButton.vrPosition.fX = pauseMenu.vrPosition.fX;
        quitGameButton.vrPosition.fY = resumeGameButton.vrPosition.fY - resumeGameButton.getSpriteHeight() - quitGameButton.getSpriteHeight() / 2;
        quitGameButton.bVisible = false;
        quitGameButton.bAutoRender = false;

        gameOverMenu = createSprite(R.drawable.game_over_menu, 1, 1);
        gameOverMenu.setScreenPercent(76, 40);
        gameOverMenu.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        gameOverMenu.vrPosition.fY = AGScreenManager.iScreenHeight / 2;
        gameOverMenu.bVisible = false;
        gameOverMenu.bAutoRender = false;

        restartGameButton = createSprite(R.drawable.restart_game, 1, 1);
        restartGameButton.setScreenPercent(76, 9);
        restartGameButton.vrPosition.fX = gameOverMenu.vrPosition.fX;
        restartGameButton.vrPosition.fY = gameOverMenu.vrPosition.fY;
        restartGameButton.bVisible = false;
        restartGameButton.bAutoRender = false;

        goHomeMenuButton = createSprite(R.drawable.go_home, 1, 1);
        goHomeMenuButton.setScreenPercent(76, 9);
        goHomeMenuButton.vrPosition.fX = pauseMenu.vrPosition.fX;
        goHomeMenuButton.vrPosition.fY = gameOverMenu.vrPosition.fY - restartGameButton.getSpriteHeight() - goHomeMenuButton.getSpriteHeight() / 2;
        goHomeMenuButton.bVisible = false;
        goHomeMenuButton.bAutoRender = false;
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
        resumeGameButton.render();
        quitGameButton.render();

        // Menu de Game Over
        gameOverMenu.render();
        restartGameButton.render();
        goHomeMenuButton.render();

        // Barra Inferior
        bottomBar.render();
        arrowLeft.render();
        arrowRight.render();
        littleHammer.render();
        bigHammer.render();
        hammerSelectedIndicator.render();
        shoot.render();
    }

    @Override
    public void restart() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void loop() {
        if (AGInputManager.vrTouchEvents.backButtonClicked() && !gameOver) {
            pauseEvent();
        }

        // Jogo pausado
        if (pause) {
            if (resumeGameButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                pauseEvent();
            } else if (quitGameButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                vrGameManager.setCurrentScene(0);
            }
        }

        // Game Over
        else if (gameOver) {
            if (restartGameButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                init();
            } else if (goHomeMenuButton.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                vrGameManager.setCurrentScene(0);
            }
        }

        // Jogo rodando
        else if (!pause && !gameOver) {
            selectHammer();
            recoverAmmo();
            updateAmmoBar();
            createHammer();
            createMoney();
            verifyHammerBanditsCollision();
            verifyMoneyCouroCollision();
            updateCouroMovement();
            updateBandits();
            updateCondemnations();
            updateHammers();
            updateMoney();
            updateScoreboard();
        }
    }

    // Seta a flag de pausa e chama o menu de pausa
    private void pauseEvent() {
        pause = !pause;
        switchPauseMenu();
    }

    // Alterna o menu de pausa de acordo com o estado de execução do jogo
    private void switchPauseMenu() {
        pauseMenu.bVisible = !pauseMenu.bVisible;
        resumeGameButton.bVisible = !resumeGameButton.bVisible;
        quitGameButton.bVisible = !quitGameButton.bVisible;
    }

    // Exibe o menu de game over
    private void switchGameOverMenu() {
        gameOver = !gameOver;
        gameOverMenu.bVisible = !gameOverMenu.bVisible;
        restartGameButton.bVisible = !restartGameButton.bVisible;
        goHomeMenuButton.bVisible = !goHomeMenuButton.bVisible;
    }

    // Seleciona o tipo de martelo a ser disparado
    private void selectHammer() {
        if (littleHammer.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            if (littleHammer.vrPosition == hammerSelectedIndicator.vrPosition) {
                return;
            } else {
                hammerSelectedIndicator.vrPosition = littleHammer.vrPosition;
            }
        } else if (bigHammer.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            if (bigHammer.vrPosition == hammerSelectedIndicator.vrPosition) {
                return;
            } else {
                hammerSelectedIndicator.vrPosition = bigHammer.vrPosition;
            }
        }
    }

    private String selectedHammer() {
        return hammerSelectedIndicator.vrPosition == littleHammer.vrPosition ? "little" : "big";
    }

    // Recupera munição a cada x milisegundos (ammoTime)
    private void recoverAmmo() {
        ammoTime.update();

        if (fullAmmoBar.vrPosition.fX != emptyAmmoBar.vrPosition.fX) {
            if (ammoTime.isTimeEnded()) {
                ammoRecovered += 20;
                ammoTime.restart();
            }
        }
    }

    // Atualiza o status de munição de Couro
    private void updateAmmoBar() {
        if (ammoSpent < 0) {
            ammoSpent += 15;
            if (fullAmmoBar.collide(emptyAmmoBar)) {
                fullAmmoBar.vrPosition.fX += 15;
            }
            if (!fullAmmoBar.collide(emptyAmmoBar)) {
                ammoSpent = 0;
                fullAmmoBar.vrPosition.fX = emptyAmmoBar.vrPosition.fX + emptyAmmoBar.getSpriteWidth();
            }
        }

        if (ammoRecovered > 0) {
            ammoRecovered -= 15;
            if (fullAmmoBar.vrPosition.fX != emptyAmmoBar.vrPosition.fX) {
                fullAmmoBar.vrPosition.fX -= 15;
            }
            if (fullAmmoBar.vrPosition.fX < emptyAmmoBar.vrPosition.fX) {
                ammoRecovered = 0;
                fullAmmoBar.vrPosition.fX = emptyAmmoBar.vrPosition.fX;
            }
        }
    }

    private void createHammer() {
        hammerTime.update();

        // Tenta reciclar um Martelo criado anteriormente
        if (shoot.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
            // Impede que seja criado outro martelo antes do intervalo (500ms) entre um e outro
            if (!hammerTime.isTimeEnded()) {
                return;
            }

            // Impede de atirar quando esvaziar a ammoBar
            if (!fullAmmoBar.collide(emptyAmmoBar)) {
                return;
            }

            hammerTime.restart();

            // Atualizando gasto com munição de acordo com o martelo atirado
            if (selectedHammer() == "little") {
                ammoSpent -= 150;
            } else {
                ammoSpent -= 100;
            }

            // Anima o lançamento do martelinho
            if (couro.getCurrentAnimationIndex() == 0)
                couro.setCurrentAnimation(1);
            couro.getCurrentAnimation().restart();

            for (AGSprite hammer : (selectedHammer() == "little" ? littleHammerVector : bigHammerVector)) {

                if (hammer.bRecycled) {
                    hammer.bRecycled = false;
                    hammer.bVisible = true;
                    hammer.vrPosition.fX = couro.vrPosition.fX;
                    hammer.vrPosition.fY = bottomBar.getSpriteHeight() + couro.getSpriteHeight() + hammer.getSpriteHeight() / 2;
                    return;
                }
            }

            // Construindo o martelo de acordo com o tamanho selecionado
            AGSprite newHammer;
            if (selectedHammer() == "little") {
                newHammer = createSprite(R.drawable.little_hammer, 1, 1);
                newHammer.setScreenPercent(7, 3);
            } else {
                newHammer = createSprite(R.drawable.big_hammer, 1, 1);
                newHammer.setScreenPercent(8, 5);
            }

            newHammer.vrPosition.fX = couro.vrPosition.fX;
            newHammer.vrPosition.fY = bottomBar.getSpriteHeight() + couro.getSpriteHeight() + newHammer.getSpriteHeight() / 2;
            (selectedHammer() == "little" ? littleHammerVector : bigHammerVector).add(newHammer);
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
                newMoney.setScreenPercent(6, 2);
                newMoney.vrPosition.fX = bandit.vrPosition.fX;
                newMoney.vrPosition.fY = bandit.vrPosition.fY - (bandit.getSpriteHeight() / 2) - (newMoney.getSpriteHeight() / 2);
                moneyVector.add(newMoney);
            }
        }
    }

    // Metodo que verifica a colisão entre Martelos e Bandidos
    private void verifyHammerBanditsCollision() {
        // Recicla martelinho
        for (AGSprite hammer : littleHammerVector) {
            if (hammer.bRecycled)
                continue;
            recycleHammerAndBandit(hammer);
        }
        // Recicla martelão
        for (AGSprite hammer : bigHammerVector) {
            if (hammer.bRecycled)
                continue;
            recycleHammerAndBandit(hammer);
        }
    }

    private void recycleHammerAndBandit(AGSprite hammer) {
        for (AGSprite bandit : bandits) {
            if (hammer.collide(bandit)) {
                // TODO: implementar pontuação exata para cada tipo de martelinho
                positiveScoreTime += 50;
                createExplosionAnimation(bandit.vrPosition.fX, bandit.vrPosition.fY);
                hammer.bRecycled = true;
                hammer.bVisible = false;
                AGSoundManager.vrSoundEffects.play(effectDowncastBandit);

                if (bandit.vrDirection.fX == 1) {
                    bandit.vrDirection.fX = -1;
                    bandit.iMirror = AGSprite.HORIZONTAL;
                    bandit.vrPosition.fX = AGScreenManager.iScreenWidth + bandit.getSpriteWidth() / 2;
                } else {
                    bandit.vrDirection.fX = 1;
                    bandit.iMirror = AGSprite.NONE;
                    bandit.vrPosition.fX = -bandit.getSpriteWidth();
                }
                break;
            }
        }
    }

    // Método que verifica a colisão entre propina e couro
    private void verifyMoneyCouroCollision() {
        for (AGSprite money : moneyVector) {
            if (money.bRecycled) {
                continue;
            }
            if (money.collide(couro)) {
                negativeScoreTime -= 50;
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
                    couro.vrPosition.setX(couro.vrPosition.getX() + 10);
                }
            } else if (arrowLeft.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                if (couro.vrPosition.getX() > 0 + couro.getSpriteWidth() / 2) {
                    couro.vrPosition.setX(couro.vrPosition.getX() - 10);
                }
            }
            if (!AGInputManager.vrTouchEvents.screenDragged() && !AGInputManager.vrTouchEvents.screenDown()) {
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
                    bandit.vrDirection.fX = -1;
                    bandit.iMirror = AGSprite.HORIZONTAL;
                }
            } else {
                // Indo para a esquerda
                if (bandit.vrPosition.fX <= -bandit.getSpriteWidth() / 2) {
                    bandit.vrDirection.fX = 1;
                    bandit.iMirror = AGSprite.NONE;
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
        for (AGSprite hammer : littleHammerVector) {
            if (hammer.bRecycled)
                continue;
            teste(hammer);
        }

        for (AGSprite hammer : bigHammerVector) {
            if (hammer.bRecycled)
                continue;
            teste(hammer);
        }
    }

    private void teste(AGSprite hammer) {
        hammer.vrPosition.fY += 10;
        hammer.fAngle += 15;

        if (hammer.vrPosition.fY > AGScreenManager.iScreenHeight - topBar.getSpriteHeight() + hammer.getSpriteHeight() / 2) {
            hammer.bRecycled = true;
            hammer.bVisible = false;
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

        if (isCouroDefeated()) {
            switchGameOverMenu();
        }
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
