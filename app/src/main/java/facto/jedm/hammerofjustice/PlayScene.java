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

    AGSprite[] vultures = new AGSprite[3];

    // Cria o tempo para controle de movimento de Coro
    AGTimer couroTime = null;
    AGTimer hammerTime = null;
    AGTimer moneyTime = null;

    // Cria a variavel para armazenar o cod efeito som
    int effectMovement = 0;
    int effectDowncastVulture = 0;
    int score = 0;
    int scoreTime = 0;

    // Cria sprites de fundo e elementos controláveis
    //AGSprite background = null;
    AGSprite couro = null;
    AGSprite arrow_left = null;
    AGSprite arrow_right = null;
    AGSprite little_hammer = null;
    AGSprite big_hammer = null;
    AGSprite shoot = null;
    AGSprite paused = null;
    AGSprite topBar = null;
    AGSprite bottomBar = null;

    float alignmentFactor;

    public PlayScene(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {
        // Carrega as imagens na memoria
        createSprite(R.drawable.hammer, 1, 1).bVisible = false;
        createSprite(R.drawable.comndenation, 4, 2).bVisible = false;

        hammerVector = new ArrayList<AGSprite>();
        moneyVector = new ArrayList<AGSprite>();
        condemnationVector = new ArrayList<AGSprite>();

        // Seta a cor do fundo
        setSceneBackgroundColor("#FFFFFF");

        // Carrega a imagem de fundo 100x100 no centro da tela
        //background = createSprite(R.drawable.background_play, 1, 1);
        //background.setScreenPercent(100, 100);
        //background.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        //background.vrPosition.setY(AGScreenManager.iScreenHeight / 2);

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
        couro.addAnimation(10, false, 0);
        //couro.addAnimation(30, true, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        //couro.addAnimation(10, true, 0, 2, 4, 6, 8, 10, 12);
        couro.vrPosition.setX(AGScreenManager.iScreenWidth / 2);
        couro.vrPosition.setY(bottomBar.getSpriteHeight() + (couro.getSpriteHeight() / 2));

        // Carrega setas direcionais
        arrow_left = createSprite(R.drawable.arrow, 1, 1);
        arrow_left.setScreenPercent(10, 8);
        arrow_left.vrPosition.setY(bottomBar.getSpriteHeight() / 2);

        arrow_right = createSprite(R.drawable.arrow, 1, 1);
        arrow_right.setScreenPercent(10, 8);
        arrow_right.vrPosition.setY(bottomBar.getSpriteHeight() / 2);
        arrow_right.fAngle = 180;

        // Carrega selecionador de martelos
        little_hammer = createSprite(R.drawable.little_hammer, 1, 1);
        little_hammer.setScreenPercent(13, 8);
        little_hammer.vrPosition.setY(bottomBar.getSpriteHeight() / 2);

        big_hammer = createSprite(R.drawable.big_hammer, 1, 1);
        big_hammer.setScreenPercent(13, 8);
        big_hammer.vrPosition.setY(bottomBar.getSpriteHeight() / 2);

        // Carrega lançados de martelos
        shoot = createSprite(R.drawable.shoot, 1, 1);
        shoot.setScreenPercent(13, 8);
        shoot.vrPosition.setY(bottomBar.getSpriteHeight() / 2);

        // Calcula o espaço ocupado por todos os sprites da barra de controle (inferior)
        float ctrlSpritesWidth = arrow_left.getSpriteWidth() + arrow_right.getSpriteWidth() + little_hammer.getSpriteWidth() + big_hammer.getSpriteWidth() + shoot.getSpriteWidth();

        // Calcula espaço livre TOTAL na barra de controle (inferior)
        float ctrlFreeSpace = 1080 - ctrlSpritesWidth;

        // Calcula espaço livre entre sprites da barra de controle (inferior)
        float ctrlFillSpace = ctrlFreeSpace / 6;

        arrow_left.vrPosition.setX(ctrlFillSpace + arrow_left.getSpriteWidth() / 2);
        arrow_right.vrPosition.setX(arrow_left.vrPosition.fX + ctrlFillSpace + arrow_right.getSpriteWidth());
        little_hammer.vrPosition.setX(arrow_right.vrPosition.fX + ctrlFillSpace + little_hammer.getSpriteWidth());
        big_hammer.vrPosition.setX(little_hammer.vrPosition.fX + ctrlFillSpace + big_hammer.getSpriteWidth());
        shoot.vrPosition.setX(big_hammer.vrPosition.fX + ctrlFillSpace + shoot.getSpriteWidth());

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
        couroTime = new AGTimer(50);
        hammerTime = new AGTimer(500);
        moneyTime = new AGTimer(500);

        // Criando efeitos sonoros para movimento de Couro e Condenação
        effectMovement = AGSoundManager.vrSoundEffects.loadSoundEffect("toc.wav");
        effectDowncastVulture = AGSoundManager.vrSoundEffects.loadSoundEffect("downcast_vulture.mp4");

        // Carrega os sprites dos vultures
        vultures[0] = createSprite(R.drawable.molusco, 1, 4);
        vultures[0].setScreenPercent(20, 12);
        vultures[0].vrDirection.fX = 1;
        vultures[0].addAnimation(10, true, 0, 1, 2);
        vultures[0].vrPosition.fX = -vultures[0].getSpriteWidth() / 2;
        vultures[0].vrPosition.fY = AGScreenManager.iScreenHeight - vultures[0].getSpriteHeight() / 2 - topBar.getSpriteHeight();

        vultures[1] = createSprite(R.drawable.vampirao, 4, 2);
        vultures[1].setScreenPercent(20, 12);
        vultures[1].iMirror = AGSprite.HORIZONTAL;
        vultures[1].addAnimation(10, true, 0, 1, 2, 3, 4, 5, 6, 7);
        vultures[1].vrDirection.fX = -1;
        vultures[1].vrPosition.fX = AGScreenManager.iScreenWidth + vultures[1].getSpriteWidth() / 2;
        vultures[1].vrPosition.fY = vultures[0].vrPosition.fY - vultures[1].getSpriteHeight();

        vultures[2] = createSprite(R.drawable.toupeirona, 4, 2);
        vultures[2].setScreenPercent(20, 12);
        vultures[2].addAnimation(10, true, 0, 1, 2, 3, 4, 5, 6, 7);
        vultures[2].vrDirection.fX = 1;
        //vultures[2].vrPosition.fX = AGScreenManager.iScreenWidth + vultures[2].getSpriteWidth() / 2;
        vultures[2].vrPosition.fX = -vultures[2].getSpriteWidth() / 2;
        vultures[2].vrPosition.fY = vultures[1].vrPosition.fY - vultures[2].getSpriteHeight();

        paused = createSprite(R.drawable.paused, 1, 1);
        paused.setScreenPercent(31, 7);
        paused.vrPosition.fX = AGScreenManager.iScreenWidth / 2;
        paused.vrPosition.fY = AGScreenManager.iScreenHeight / 2;
        paused.bVisible = false;
    }

    @Override
    public void render() {
        super.render();
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
            vrGameManager.setCurrentScene(0);
            paused.bVisible = !paused.bVisible;
            return;
        }

        if (paused.bVisible == false) {
            createHammers();
            createMoney();
            verifyHammerVulturesColision();
            verifyMoneyCouroColision();
            updateCouroMovement();
            updateVultures();
            updateCondemnations();
            updateHammers();
            updateMoneys();
            updateScoreboard();
        }
    }

    // Coloca um Martelo no vetor de martelos
    private void createHammers() {
        hammerTime.update();

        // Tenta reciclar um Martelo criado anteriormente
        //if (AGInputManager.vrTouchEvents.screenClicked()) {
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
                    return;
                }
            }

            AGSprite newHammer = createSprite(R.drawable.hammer, 1, 1);
            newHammer.setScreenPercent(8, 5);
            newHammer.vrPosition.fX = couro.vrPosition.fX;
            newHammer.vrPosition.fY = bottomBar.getSpriteHeight() + couro.getSpriteHeight() + newHammer.getSpriteHeight() / 2;
            hammerVector.add(newHammer);
        }
    }

    // Coloca um maço de dinheiro no vetor de maços
    private void createMoney() {
        moneyTime.update();

        for (AGSprite vulture : vultures) {
            if (vulture.vrPosition.fX <= couro.vrPosition.fX) {
                alignmentFactor = vulture.vrPosition.fX / couro.vrPosition.fX;
            } else {
                alignmentFactor = couro.vrPosition.fX / vulture.vrPosition.fX;
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
                        money.vrPosition.fX = vulture.vrPosition.fX;
                        money.vrPosition.fY = vulture.vrPosition.fY - (vulture.getSpriteHeight() / 2) - (money.getSpriteHeight() / 2);
                        return;
                    }
                }

                AGSprite newMoney = createSprite(R.drawable.pack_of_money, 1, 1);
                newMoney.setScreenPercent(8, 5);
                newMoney.vrPosition.fX = vulture.vrPosition.fX;
                newMoney.vrPosition.fY = vulture.vrPosition.fY - (vulture.getSpriteHeight() / 2) - (newMoney.getSpriteHeight() / 2);
                moneyVector.add(newMoney);
            }
        }
    }

    // Metodo que verifica a colisão entre Martelos e Abutres
    private void verifyHammerVulturesColision() {
        for (AGSprite hammer : hammerVector) {
            if (hammer.bRecycled) {
                continue;
            }
            for (AGSprite vulture : vultures) {
                if (hammer.collide(vulture)) {
                    scoreTime += 50;
                    createDowncastAnimation(vulture.vrPosition.fX, vulture.vrPosition.fY);
                    hammer.bRecycled = true;
                    hammer.bVisible = false;
                    AGSoundManager.vrSoundEffects.play(effectDowncastVulture);

                    if (vulture.vrDirection.fX == 1) {
                        vulture.vrDirection.fX = -1;
                        //vulture.iMirror = AGSprite.NONE;
                        vulture.vrPosition.fX = AGScreenManager.iScreenWidth + vulture.getSpriteWidth() / 2;
                    } else {
                        vulture.vrDirection.fX = 1;
                        //vulture.iMirror = AGSprite.HORIZONTAL;
                        vulture.vrPosition.fX = -vulture.getSpriteWidth();
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
                //AGSoundManager.vrSoundEffects.play(effectDowncastVulture);

                break;
            }
        }
    }

    // Metodo criado para movimentar
    private void updateCouroMovement() {

        couroTime.update();
        if (couroTime.isTimeEnded()) {
            couroTime.restart();
            //if (AGInputManager.vrAccelerometer.getAccelX() > 2.0f) {
            if (arrow_right.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                if (couro.vrPosition.getX() <= AGScreenManager.iScreenWidth - couro.getSpriteWidth() / 2) {
                    AGSoundManager.vrSoundEffects.play(effectMovement);
                    couro.vrPosition.setX(couro.vrPosition.getX() + 10);
                }
                //} else if (AGInputManager.vrAccelerometer.getAccelX() < -2.0f) {
            } else if (arrow_left.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                if (couro.vrPosition.getX() > 0 + couro.getSpriteWidth() / 2) {
                    AGSoundManager.vrSoundEffects.play(effectMovement);
                    couro.vrPosition.setX(couro.vrPosition.getX() - 10);
                }
            }
            //Log.d("TAG", Float.toString(AGInputManager.vrTouchEvents.getLastPosition().getX()));
            //Log.d("TAG", Float.toString(AGInputManager.vrTouchEvents.getLastPosition().getY()));
        }
    }

    // Metodo que atualiza a posicao dos Abutres
    private void updateVultures() {
        for (AGSprite vulture : vultures) {
            vulture.vrPosition.fX += 5 * vulture.vrDirection.fX;
            if (vulture.vrDirection.fX == 1) {
                // Indo para a direita
                if (vulture.vrPosition.fX > AGScreenManager.iScreenWidth + vulture.getSpriteWidth() / 2) {
                    vulture.iMirror = AGSprite.NONE;
                    vulture.vrDirection.fX = -1;
                }
            } else {
                // Indo para a esquerda
                if (vulture.vrPosition.fX <= -vulture.getSpriteWidth() / 2) {
                    vulture.iMirror = AGSprite.HORIZONTAL;
                    vulture.vrDirection.fX = 1;
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
            hammer.vrPosition.fY += 10;
            hammer.fAngle += 15;

            if (hammer.vrPosition.fY > AGScreenManager.iScreenHeight + hammer.getSpriteHeight() / 2) {
                hammer.bRecycled = true;
                hammer.bVisible = false;
            }
        }
    }

    // metodo para atualizar o movimento dos maços de dinheiro
    private void updateMoneys() {
        for (AGSprite money : moneyVector) {
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

    // Metodo utilizado para criar animação ao abater um Abutre
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
