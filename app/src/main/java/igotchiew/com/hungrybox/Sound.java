package igotchiew.com.hungrybox;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Sound {

    private static SoundPool soundPool;
    private static int redHitSound;
    private static int greenHitSound;
    private static int overSound;

    public Sound(Context context) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        redHitSound = soundPool.load(context, R.raw.oh, 1);
        greenHitSound = soundPool.load(context, R.raw.uh, 1);
        overSound = soundPool.load(context, R.raw.playerexplode, 1);

    }

    public void playHitSoundRed() {

        soundPool.play(redHitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playHitSoundGreen() {

        soundPool.play(greenHitSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }

    public void playOverSound() {

        soundPool.play(overSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }
}
