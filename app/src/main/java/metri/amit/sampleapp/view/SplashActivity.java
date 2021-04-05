package metri.amit.sampleapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by amitmetri on 05,April,2021
 * Splash activity has been added to improve app launch experience.
 * Splash activity will render the UI directly from the theme
 * so that there is not time gap before rendering layout and no blank white screen appearance.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        * Navigate to MainActivity post 1 second delay
        * */
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }, 1000);
    }
}
