package facto.jedm.hammerofjustice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {
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
        Intent intent = new Intent(SplashScreen.this, LoadGame.class);
        startActivity(intent);
        finish();
    }
}
