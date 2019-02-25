package igotchiew.com.hungrybox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    //declare variables
    private TextView scoreTextView;
    private TextView startTextView;
    private ImageView box;
    private ImageView red;
    private ImageView green;
    private ImageView black;

    //size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    //position
    private int boxY;
    private int redX;
    private int redY;
    private int greenX;
    private int greenY;
    private int blackX;
    private int blackY;


    private int score = 0;

    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private Sound sound;

    //check status
    private boolean action_flg = false;
    private boolean start_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        sound = new Sound(this);

        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        startTextView = (TextView) findViewById(R.id.startTextView);
        box = (ImageView) findViewById(R.id.box);
        red = (ImageView) findViewById(R.id.red);
        green = (ImageView) findViewById(R.id.green);
        black = (ImageView) findViewById(R.id.black);


        //get screen size
        WindowManager wm = getWindowManager();
        Display dis = wm.getDefaultDisplay();
        Point size = new Point();
        dis.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //onCreate off screen
        red.setX(-100);
        red.setY(-100);
        green.setX(-100);
        green.setY(-100);
        black.setX(-100);
        black.setY(-100);

        scoreTextView.setText("Score: 0");

    }

    public void changePos() {

        hitCheck();

        //red
        redX -= 20;
        if(redX < 0) {
            redX = screenWidth + 100;
            redY = (int) Math.floor(Math.random() * (frameHeight - red.getHeight()));
        }
        red.setX(redX);
        red.setY(redY);

        //green
        greenX -=16;
        if(greenX < 0) {
            greenX = screenWidth + 5000;
            greenY = (int) (Math.random() * (frameHeight - green.getHeight()));
        }

        green.setX(greenX);
        green.setY(greenY);


        //black
        blackX -= 45;
        if(blackX < 0) {
            blackX = screenWidth + 3000;
            blackY = (int) (Math.random() * (frameHeight - black.getHeight()));
        }

        black.setX(blackX);
        black.setY(blackY);


        //Move box
        if(action_flg == true) {
            //touching
            boxY -= 20;
        }else {
            //release
            boxY += 20;
        }

        //check box position
        if(boxY < 0) boxY = 0;
        if(boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        box.setY(boxY);

        scoreTextView.setText("Score: " + score);
    }

    public void hitCheck() {
        //red
        //if center of ball is in box, counts as a hit points++
        int redCenterX = redX + red.getWidth()/2;
        int redCenterY = redY + red.getHeight()/2;

        //0 <= redCenterX <= boxWidth
        //boxY <= redCenterY <= boxY + boxHeight
        if(0 <= redCenterX && redCenterX <= boxSize && boxY <= redCenterY && redCenterY <= boxY + boxSize) {
            score += 10;
            redX = -10;
            sound.playHitSoundRed();
        }

        //green
        int greenCenterX = greenX + green.getWidth()/2;
        int greenCenterY = greenY + green.getHeight()/2;

        if(0 <= greenCenterX && greenCenterX <= boxSize && boxY <= greenCenterY && greenCenterY <= boxY + boxSize) {
            score += 30;
            greenX = -10;
            sound.playHitSoundGreen();
        }

        //black
        int blackCenterX = blackX + black.getWidth()/2;
        int blackCenterY = blackY + black.getHeight();

        if(0 <= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY && blackCenterY <= boxY + boxSize) {
            //stop timer
            timer.cancel();
            timer = null;
            sound.playOverSound();

            //show results, Results Activity
            Intent intent =  new Intent(getApplicationContext(), ResultsActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);

        }

    }

    public boolean onTouchEvent(MotionEvent me) {

        if(start_flg == false) {

            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int)box.getY();

            boxSize = box.getHeight();

            startTextView.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);

        }else{
            if(me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            }else if(me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }


        return true;
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
