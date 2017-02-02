package windwail.ru.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by icetsuk on 31.01.17.
 */

public class RingtonePlayingService extends Service {

    MediaPlayer mediaPlayer;

    boolean is_playing = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        boolean alarm = intent.getBooleanExtra("alarm", false);

        Log.e("Finally in service "+alarm, "RingtonePlayingService");

        if(alarm) {

            if(is_playing) {

            } else {
                mediaPlayer  = MediaPlayer.create(this, R.raw.iphone_donald_trump);
                mediaPlayer.start();
                is_playing = true;
            }

        } else {

            if(is_playing) {
                mediaPlayer.stop();
                mediaPlayer.release();
                is_playing = false;
            } else {

            }

        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Toast.makeText(this, "on Destroy called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




}
