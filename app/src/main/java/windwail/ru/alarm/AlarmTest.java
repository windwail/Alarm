package windwail.ru.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

public class AlarmTest extends AppCompatActivity {

    public static final String TAG = AlarmTest.class.getName();
    AlarmManager alarmManager;
    Context contex;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_test);

        this.contex = this;

        // Initialize alarm manager;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);

        DateFormat df = DateFormat.getDateTimeInstance();
        Log.e("Some", df.format(calendar.getTime()));

        // create an intent to the Alarm Receiver class
        final Intent alarmReceiverIntent = new Intent(this.contex, AlarmReceiver.class);

        alarmReceiverIntent.putExtra("alarm", true);
        alarmReceiverIntent.putExtra("alarmStr", "alarm on");

        pendingIntent = PendingIntent.getBroadcast(this, 0,
                alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
