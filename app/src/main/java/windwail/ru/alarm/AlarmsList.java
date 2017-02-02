package windwail.ru.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import windwail.ru.alarm.entities.AlarmItem;

public class AlarmsList extends AppCompatActivity implements AdapterView.OnItemClickListener
{

    public static final int NEW_ALARM = 1000;

    public static String TAG = "AlarmList";

    AlarmListAdapter adapter;

    ListView listView;

    List<AlarmItem> alarms;

    AlarmManager alarmManager;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms_list);
        alarms = AlarmItem.listAll(AlarmItem.class);
        listView = (ListView) findViewById(R.id.alarms);
        listView.setClickable(true);
        adapter = new AlarmListAdapter(this, R.layout.alarm_item, alarms);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == NEW_ALARM) {
            if (resultCode == RESULT_OK) {
                adapter.updateAlarms();

                long alarm_id = data.getLongExtra("alarm_id",  -1);

                if(alarm_id >= 0) {
                    AlarmItem alarm = AlarmItem.findById(AlarmItem.class, alarm_id);
                    Calendar calendar = Calendar.getInstance();

                    calendar.set(Calendar.MINUTE, alarm.getStartMinute());
                    calendar.set(Calendar.HOUR, alarm.getStartHour());
                    calendar.set(Calendar.SECOND, 0);


                    Intent alarmReceiverIntent = new Intent(this, AlarmReceiver.class);

                    alarmReceiverIntent.putExtra("alarm_id", alarm_id);
                    alarmReceiverIntent.putExtra("alarm_file", alarm.file);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                            alarmReceiverIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

                    Toast.makeText(this, "Будильник '"+alarm.title+"' установлен!", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void onAdd(View v){

        Intent myIntent = new Intent(v.getContext(), AlarmDetails.class);
        startActivityForResult(myIntent, NEW_ALARM);
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
        Intent myIntent = new Intent(view.getContext(), AlarmDetails.class);

        AlarmItem alarm = ((AlarmListAdapter.DataHandler)view.getTag()).alarm;

        myIntent.putExtra("alarm_id",alarm.getId());

        startActivityForResult(myIntent, NEW_ALARM);
    }
}
