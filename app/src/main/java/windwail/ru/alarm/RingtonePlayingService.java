package windwail.ru.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
        long alarm_id = intent.getLongExtra("alarm_id", -1);
        String alarm_file = intent.getStringExtra("alarm_file");

        Log.e("Finally in service "+alarm, "RingtonePlayingService");

        if(alarm) {

            if(is_playing) {
                // Останавливаем то, что играло.
                mediaPlayer.stop();
                mediaPlayer.release();
                is_playing = false;
            } else {
                mediaPlayer  = MediaPlayer.create(this, Uri.parse(alarm_file));
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        is_playing = false;
                        mediaPlayer.release();
                    }
                });
                is_playing = true;
            }

        } else {

            if(is_playing) {
                mediaPlayer.stop();
                mediaPlayer.release();
                is_playing = false;
            } else {
                // Ничего и так не играет.
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
