package windwail.ru.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

/**
 * Created by icetsuk on 31.01.17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long alarm_id = intent.getLongExtra("alarm_id", -1);
        String alarm_file = intent.getStringExtra("alarm_file");


        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("alarm_id", alarm_id);
        serviceIntent.putExtra("alarm", true);
        serviceIntent.putExtra("alarm_file", alarm_file);
        context.startService(serviceIntent);
    }
}
