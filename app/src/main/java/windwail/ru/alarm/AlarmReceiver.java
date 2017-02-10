package windwail.ru.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;


import org.joda.time.DateTime;

import java.text.DateFormat;
import java.util.List;

import windwail.ru.alarm.entities.AlarmItem;
import windwail.ru.alarm.entities.RepeatData;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by icetsuk on 31.01.17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    AlarmManager alarmManager;
    Intent alarmReceiverIntent;
    PendingIntent pendingIntent;


    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager.WakeLock wakeLock;

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, AlarmsList.APP_TAG);
        wakeLock.acquire();

        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        long alarm_id = intent.getLongExtra("alarm_id", -1);

        FileUtil.logTime();
        FileUtil.log("Сработал будильник: "+alarm_id);

        AlarmItem alarm = AlarmItem.findById(AlarmItem.class, alarm_id);

        if(alarm == null) {
            FileUtil.log("Будильник не найден: "+alarm_id);
            return;
        }

        List<RepeatData> repeats = alarm.getRepeats();
        RepeatData current = null;

        // Ищем первую повторялку с неисчерпанной квотой повторений
        // И проставляем ей +1 повтор
        for(RepeatData r: repeats) {
            if(r.getRepeats() >= r.getRepeatCount()) {
                continue;
            } else {
                current = r;
                current.setRepeats(current.getRepeats() + 1);
                current.save();
                break;
            }
        }

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);

        serviceIntent.putExtra("alarm_stop", false);
        serviceIntent.putExtra("alarm_notify", current.getNotifications());

        serviceIntent.putExtra("alarm_file", current.getFile());
        serviceIntent.putExtra("volume", current.getVolume());

        serviceIntent.putExtra("vibro", current.getVibro());
        serviceIntent.putExtra("vrep", current.getVibroRepeat());
        serviceIntent.putExtra("vint", current.getVibroInterval());
        serviceIntent.putExtra("vlen", current.getVibroLenth());

        context.startService(serviceIntent);



        RepeatData last = repeats.get(repeats.size()-1);
        if(current.getId()== last.getId() && current.getRepeats() >= current.getRepeatCount()) {
            FileUtil.log("Будильник свое отработал: "+alarm_id);
            alarm.setInfo("<ОТРАБОТАЛ>");
            alarm.save();

            Intent alarmReceiverIntent = new Intent(context, AlarmReceiver.class);
            alarmReceiverIntent.setData(Uri.parse("custom://" + alarm.getId()));
            alarmReceiverIntent.setAction(String.valueOf(alarm.getId()));
            alarmReceiverIntent.putExtra("alarm_id", alarm.getId());

            PendingIntent pi =  PendingIntent.getBroadcast(context, alarm.getId().intValue(),
                    alarmReceiverIntent,  PendingIntent.FLAG_NO_CREATE);

            pi.cancel();
            alarmManager.cancel(pi);

            return;
        }


        DateTime calendar = DateTime.now();

        calendar = calendar.withMinuteOfHour(current.getStartMinute());
        calendar = calendar.withHourOfDay(current.getStartHour());
        calendar = calendar.withSecondOfMinute(0);

        int plusMinutes = 0;

        for(RepeatData r: repeats) {
            plusMinutes += r.getRepeats()*r.getRepeatInterval();
        }

        calendar = calendar.plusMinutes(plusMinutes);

        if(calendar.isBefore(DateTime.now())) {
            calendar = calendar.plusDays(1);
        }

        DateFormat df = DateFormat.getDateTimeInstance();
        alarm.setInfo(df.format(calendar.toDate()));
        alarm.save();

        Log.e("ALARM SET:", df.format(calendar.toDate()));
        FileUtil.log("Установлен "+alarm.getId()+" NAME:" + alarm.getTitle() + " TIME:"+df.format(calendar.toDate()));

        alarmReceiverIntent = new Intent(context, AlarmReceiver.class);
        alarmReceiverIntent.setData(Uri.parse("custom://" + alarm.getId()));
        alarmReceiverIntent.setAction(String.valueOf(alarm.getId()));
        alarmReceiverIntent.putExtra("alarm_id", alarm.getId());

        pendingIntent = PendingIntent.getBroadcast(context, alarm.getId().intValue(), alarmReceiverIntent,  PendingIntent.FLAG_CANCEL_CURRENT);

        if(Build.VERSION.SDK_INT < 23){
            if(Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getMillis(), pendingIntent);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getMillis(), pendingIntent);
        }


        if (wakeLock != null) wakeLock.release();
        wakeLock = null;
    }


}
