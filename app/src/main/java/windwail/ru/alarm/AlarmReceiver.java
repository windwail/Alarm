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

import windwail.ru.alarm.entities.AlarmItem;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by icetsuk on 31.01.17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    AlarmManager alarmManager;
    Intent alarmReceiverIntent;
    PendingIntent pendingIntent;



    public AlarmReceiver() {

    }

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
        String alarm_file = intent.getStringExtra("alarm_file");
        int volume = intent.getIntExtra("volume", 10);

        FileUtil.logTime();
        FileUtil.log("Alarm received: "+alarm_id);

        AlarmItem alarm = AlarmItem.findById(AlarmItem.class, alarm_id);

        if(alarm == null) {
            return;
        }

        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        serviceIntent.putExtra("alarm_id", alarm_id);
        serviceIntent.putExtra("alarm", true);
        serviceIntent.putExtra("alarm_file", alarm_file);
        serviceIntent.putExtra("alarm_notify", alarm.notifications);
        serviceIntent.putExtra("volume", volume);

        serviceIntent.putExtra("vibro", intent.getBooleanExtra("vibro", false));
        serviceIntent.putExtra("vrep", intent.getIntExtra("vrep", 0));
        serviceIntent.putExtra("vint", intent.getIntExtra("vint", 0));
        serviceIntent.putExtra("vlen", intent.getIntExtra("vlen", 0));

        context.startService(serviceIntent);
        alarm.repeats += 1;
        alarm.save();

        int[] repeats = new int[]{
                alarm.getRepeatCount1(),
                alarm.getRepeatCount2(),
                alarm.getRepeatCount3(),
                alarm.getRepeatCount4()
        };

        int[] intervals  = new int[]{
                alarm.getRepeatInterval1(),
                alarm.getRepeatInterval2(),
                alarm.getRepeatInterval3(),
                alarm.getRepeatInterval4()
        };

        int[] volumes  = new int[]{
                alarm.getVolume1(),
                alarm.getVolume2(),
                alarm.getVolume3(),
                alarm.getVolume4()
        };

        boolean[] vibros = new boolean[] {
                alarm.getVibro1(),
                alarm.getVibro2(),
                alarm.getVibro3(),
                alarm.getVibro4(),
        };

        int[] vlens = new int[] {
                alarm.getVibroLenth1(),
                alarm.getVibroLenth2(),
                alarm.getVibroLenth3(),
                alarm.getVibroLenth4()
        };

        int[] vreps = new int[] {
                alarm.getVibroRepeat1(),
                alarm.getVibroRepeat2(),
                alarm.getVibroRepeat3(),
                alarm.getVibroRepeat4()
        };

        int[] vints = new int[] {
                alarm.getVibroInterval1(),
                alarm.getVibroInterval2(),
                alarm.getVibroInterval3(),
                alarm.getVibroInterval4()
        };

        int barr_max =  0;
        int barr_min =  0;
        int barr_minutes = 0;
        for(int i = 0; i< repeats.length; i++) {

            barr_max += repeats[i];

            Log.e("BARR MAX:", ""+barr_max);
            Log.e("BARR MIN:", ""+barr_min);
            Log.e("REPEATS:", ""+alarm.repeats);
            Log.e("I:", ""+i);

            if(alarm.repeats <= barr_max) {


                int vol = 10;
                boolean vibro = false;
                int vlen = 0;
                int vrep = 0;
                int vint = 0;
                boolean set_next = true;
                if(alarm.repeats == barr_max && i+1<repeats.length) {
                      vol = volumes[i+1] ;
                      set_next = repeats[i+1] > 0;

                        vlen = vlens[i+1];
                        vrep = vreps[i+1];
                        vint = vints[i+1];
                        vibro = vibros[i+1];
                } else {
                    vol = volumes[i];

                    vlen = vlens[i];
                    vrep = vreps[i];
                    vint = vints[i];
                    vibro = vibros[i];
                }

                if(set_next) {
                    Log.e("SET","SET");
                    setAlarm(context, alarmManager, alarm, barr_minutes + (intervals[i] * (alarm.repeats - barr_min)), vol, vibro, vlen, vrep, vint);
                } else {
                    Log.e("LAST","LAST");
                }
                break;
            }

            barr_minutes += intervals[i]*repeats[i];
            barr_min += repeats[i];
            Log.e("PASSED","PASSED");
        }

        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }

    private void setAlarm(Context context, AlarmManager alarmManager, AlarmItem alarm, int minutes, int volume, boolean vibro, int vlen, int vrep, int vint) {
        DateTime calendar = DateTime.now();

        calendar = calendar.withMinuteOfHour(alarm.getStartMinute());
        calendar = calendar.withHourOfDay(alarm.getStartHour());
        calendar = calendar.withSecondOfMinute(0);
        calendar = calendar.withYear(alarm.year);
        calendar = calendar.withMonthOfYear(alarm.month);
        calendar = calendar.withDayOfMonth(alarm.day);

        calendar = calendar.plusMinutes(minutes);

        DateFormat df = DateFormat.getDateTimeInstance();
        alarm.next = df.format(calendar.toDate());

        alarm.save();

        Log.e("ALARM SET:", df.format(calendar.toDate()));
        FileUtil.log("ALARM "+alarm.getId()+" NAME:" + alarm.title + " TIME:"+df.format(calendar.toDate()));

        alarmReceiverIntent = new Intent(context, AlarmReceiver.class);

        alarmReceiverIntent.putExtra("alarm_id", alarm.getId());
        alarmReceiverIntent.putExtra("alarm_file", alarm.file);
        alarmReceiverIntent.putExtra("volume",  volume);
        alarmReceiverIntent.putExtra("vibro",  vibro);
        alarmReceiverIntent.putExtra("vlen",  vlen);
        alarmReceiverIntent.putExtra("vrep",  vrep);
        alarmReceiverIntent.putExtra("vint",  vint);
        alarmReceiverIntent.putExtra("alarm_notify", alarm.notifications );

        pendingIntent = PendingIntent.getBroadcast(context, 0,
                alarmReceiverIntent,  PendingIntent.FLAG_CANCEL_CURRENT);

        if(Build.VERSION.SDK_INT < 23){
            if(Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getMillis(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getMillis(), pendingIntent);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getMillis(), pendingIntent);
        }

        Log.e("ALARM SET:", ""+df.format(calendar.toDate())+" VOLUME"+volume);
        //Toast.makeText(context, "ALARM SET:"+df.format(calendar.toDate())+" VOLUME"+volume, Toast.LENGTH_LONG).show();
    }
}
