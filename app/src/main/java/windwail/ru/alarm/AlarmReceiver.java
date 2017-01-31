package windwail.ru.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by icetsuk on 31.01.17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver!", "Yay");

        boolean alarm = intent.getBooleanExtra("alarm",false);

        Log.e("ALARM: "+alarm, "AlarmReciever");

        Intent sreviceIntent = new Intent(context, RingtonePlayingService.class);
        sreviceIntent.putExtra("alarm", alarm);

        context.startService(sreviceIntent);
    }
}
