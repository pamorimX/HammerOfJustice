package facto.jedm.hammerofjustice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {
    // TODO: 05/07/17 Providenciar arte para exibição na animação inicial do jogo
    private static final int TIME_SPLASH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openGameMenu();
            }
        }, TIME_SPLASH);
    }

    private void openGameMenu() {
        startActivity(new Intent(SplashScreen.this, LoadGame.class));
        finish();
    }
}
