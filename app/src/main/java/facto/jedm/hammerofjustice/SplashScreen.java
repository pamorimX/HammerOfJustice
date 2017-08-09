package facto.jedm.hammerofjustice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static final int TIME_SPLASH = 3000;

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
        startActivity(new Intent(SplashScreen.this, LoadScenes.class));
        finish();
    }
}
