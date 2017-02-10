package windwail.ru.alarm;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.angads25.filepicker.view.FilePickerDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import windwail.ru.alarm.entities.AlarmConfig;
import windwail.ru.alarm.entities.AlarmItem;
import windwail.ru.alarm.entities.RepeatData;

public class AlarmsList extends AppCompatActivity implements AdapterView.OnItemClickListener
{

    public static final int NEW_ALARM = 1000;

    public static String TAG = "AlarmList";
    public static String APP_TAG = "Alarm";

    AlarmListAdapter adapter;

    ListView listView;

    List<AlarmItem> alarms;

    AlarmManager alarmManager;

    Intent alarmReceiverIntent;

    PendingIntent pendingIntent;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms_list);


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.VIBRATE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();

        try {
            alarms = AlarmItem.listAll(AlarmItem.class);
        } catch (Exception ex) {
            alarms = new ArrayList<>();
        }


        listView = (ListView) findViewById(R.id.alarms);
        listView.setClickable(true);
        adapter = new AlarmListAdapter(this, R.layout.alarm_item, alarms);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if(!AlarmConfig.getInstance().getRulesAccepted()) {
            new AlertDialog.Builder(this)
                    .setTitle("Caution/Предупреждение")
                    .setMessage(
                            "Эта программа была написана автором для себя, используйте ее на свой страх и риск. Автор снимает с себя ответственность за проблемы связанные с использованием данной программы.\n" +
                                    "This application is distributes as is. It is written for myself and i'm not responsible of problems you'll face using this app. ")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AlarmConfig instance = AlarmConfig.getInstance();
                            instance.setRulesAccepted(true);
                            instance.save();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            AlarmsList.this.finish();
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == NEW_ALARM) {
            if (resultCode == RESULT_OK) {
                Log.e("ALARM", "SET");

                long alarm_id = data.getLongExtra("alarm_id",  -1);

                if(alarm_id >= 0) {
                    AlarmItem alarm = AlarmItem.findById(AlarmItem.class, alarm_id);

                    // Сбрасываем счетчики срабатывания.
                    for(RepeatData r: alarm.getRepeats()) {
                        r.setRepeats(0);
                        r.save();
                    }

                    RepeatData repeat = alarm.getRepeats().get(0);

                    DateTime calendar = DateTime.now();

                    calendar = calendar.withMinuteOfHour(repeat.getStartMinute());
                    calendar = calendar.withHourOfDay(repeat.getStartHour());
                    calendar = calendar.withSecondOfMinute(0);

                    if(calendar.isBefore(DateTime.now())) {
                        calendar = calendar.plusDays(1);
                    }

                    repeat.save();


                    DateFormat df = DateFormat.getDateTimeInstance();
                    alarm.setInfo(df.format(calendar.toDate()));
                    alarm.save();

                    Log.e("ALARM SET:", df.format(calendar.toDate()));
                    FileUtil.log("Утсановлен будильник "+alarm.getId()+" Repeat:" + repeat.getId() + " TIME:"+df.format(calendar.toDate()));

                    alarmReceiverIntent = new Intent(this, AlarmReceiver.class);
                    alarmReceiverIntent.setData(Uri.parse("custom://" + alarm.getId()));
                    alarmReceiverIntent.setAction(String.valueOf(alarm.getId()));
                    alarmReceiverIntent.putExtra("alarm_id", alarm.getId());

                    pendingIntent = PendingIntent.getBroadcast(this, alarm.getId().intValue(),
                            alarmReceiverIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

                    if(Build.VERSION.SDK_INT < 23){
                        if(Build.VERSION.SDK_INT >= 19) {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getMillis(), pendingIntent);
                        } else {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getMillis(), pendingIntent);
                        }
                    } else {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getMillis(), pendingIntent);
                    }

                    Toast.makeText(this, "Будильник установлен! "+df.format(calendar.toDate()), Toast.LENGTH_SHORT).show();

                    adapter.add(alarm);
                    adapter.notifyDataSetChanged();

                }

            } else if (resultCode == RESULT_CANCELED){

                Log.e("ALARM", "SAVE");

                if(data == null) {
                    return;
                }

                adapter.notifyDataSetChanged();

            }
        }
    }

    public void onRefresh(View v) {
        adapter.updateAll();
        adapter.notifyDataSetChanged();
    }

    public void onAdd(View v){

        Log.e("CREATING NEW", "CREATING NEW");

        AlarmItem alarm = new AlarmItem();
        alarm.save();

        alarm.setTitle("Будильник "+alarm.getId());

        RepeatData repeat = new RepeatData(alarm);
        repeat.setAlarm(alarm);
        repeat.save();

        DateTime dt = DateTime.now();
        repeat.setStartHour(dt.getHourOfDay());
        repeat.setStartMinute(dt.getMinuteOfHour());
        repeat.save();
        alarm.save();

        adapter.updateAll();
        adapter.notifyDataSetChanged();

        //Intent myIntent = new Intent(v.getContext(), AlarmDetails.class);
        //startActivityForResult(myIntent, NEW_ALARM);
    }

    public void onStopAlarm(View v) {
        Intent serviceIntent = new Intent(this, RingtonePlayingService.class);
        serviceIntent.putExtra("alarm_stop", true);
        startService(serviceIntent);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent myIntent = new Intent(view.getContext(), TabbedAlarmDetails.class);

        AlarmItem alarm = adapter.get(position);

        myIntent.putExtra("alarm_id",alarm.getId());

        startActivityForResult(myIntent, NEW_ALARM);
    }

}
