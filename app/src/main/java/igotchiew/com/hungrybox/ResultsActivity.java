package igotchiew.com.hungrybox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_results);

        TextView scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        TextView highScoreTextView = (TextView) findViewById(R.id.highScoreTextView);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreTextView.setText(score + " ");

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH SCORE", 0);

        if (score > highScore) {
            highScoreTextView.setText("High Score: " + score);

            //save high score
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH SCORE", score);
            editor.commit();
        } else {
            highScoreTextView.setText("High Score: " + highScore);
        }
    }

    public void startGame(View view) {
        startActivity(new Intent(getApplicationContext(), StartActivity.class));

    }

    //disable back button
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN) {
            switch(event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return  true;
            }
        }

        return super.dispatchKeyEvent(event);
    }
}

