package com.example.rma_2_anis_karic;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    private final AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
    private final SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int soundClick = soundPool.load(this , R.raw.click, 1);

        CardView funnyTile = (CardView) findViewById(R.id.funnyTile);
        View mainView = findViewById(android.R.id.content);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mainView.setOnTouchListener(null);
                    soundPool.play(soundClick, 1f, 1f, 0, 0, 1);
                    funnyTile.animate().
                            scaleX(1.1f).
                            scaleY(1.1f).
                            translationXBy(dpToPx(110)).
                            translationZ(1f).
                            setDuration(200).
                            withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    funnyTile.animate().
                                            setStartDelay(250).
                                            scaleX(1f).
                                            scaleY(1f).
                                            setDuration(200).setInterpolator(new DecelerateInterpolator()).
                                            withEndAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    startMainActivity();
                                                }
                                            })
                                            .start();
                                }
                            })
                            .start();

                    return true;
                }
                return false;
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public int dpToPx(float dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

}