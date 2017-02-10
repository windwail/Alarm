package windwail.ru.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by icetsuk on 31.01.17.
 */

public class RingtonePlayingService extends Service {

    ArrayList<MediaPlayer> players = new ArrayList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean alarm_stop = intent.getBooleanExtra("alarm_stop", false);
        boolean alarm_notify = intent.getBooleanExtra("alarm_notify", false);

        boolean vibro = intent.getBooleanExtra("vibro", false);
        int vrep = intent.getIntExtra("vrep", 0);
        int vlen = intent.getIntExtra("vlen", 0);
        int vint = intent.getIntExtra("vint", 0);

        String alarm_file = intent.getStringExtra("alarm_file");
        int volume = intent.getIntExtra("volume", 10);

        if(alarm_stop) {
            for(MediaPlayer p: players) {
                p.stop();
                p.release();
                players.remove(p);
            }

            return START_NOT_STICKY;
        }

        if(alarm_notify) {
            showNotiyfy();
        }



        if(alarm_file == null) {
            return START_NOT_STICKY;
        }

        if(alarm_file!=null && !alarm_file.isEmpty()) {
            final MediaPlayer mediaPlayer = MediaPlayer.create(this, Uri.parse(alarm_file));
            float fv = ((float) volume) / 10f;
            mediaPlayer.setVolume(fv, fv);
            players.add(mediaPlayer);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    players.remove(mediaPlayer);
                }
            });

        }

        if(vibro) {
            onVibroTest(vrep, vlen, vint);
        }



        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        players.clear();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showNotiyfy() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.alarm)
                        .setContentTitle("My Notification Title")
                        .setContentText("Something interesting happened");
        int NOTIFICATION_ID = 12345;

        Intent targetIntent = new Intent(this, AlarmsList.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void onVibroTest(int vrep, int vlen, int vint) {

        Vibrator vibrate = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);

        for(int i=0; i<vrep; i++) {
            vibrate.vibrate(vlen);
            try {
                Thread.sleep(vint+vlen);
            } catch (InterruptedException e) {
            }
        }

    }


}
