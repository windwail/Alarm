package windwail.ru.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    TimePicker alarmTimePicker;
    TextView updateText;
    Context contex;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.contex = this;

        // Initialize alarm manager;
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Initialize time picker;
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);

        // Initialize text update box;
        updateText = (TextView) findViewById(R.id.update_text);

        final Calendar calendar = Calendar.getInstance();

        Button alarmOn = (Button) findViewById(R.id.alarm_on);

        // create an intent to the Alarm Receiver class
        final Intent alarmReceiverIntent = new Intent(this.contex, AlarmReceiver.class);

        alarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
                calendar.set(Calendar.HOUR, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.SECOND, 0);

                DateFormat df = DateFormat.getTimeInstance();


                alarmReceiverIntent.putExtra("alarm",true);
                alarmReceiverIntent.putExtra("alarmStr", "alarm on");

                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        alarmReceiverIntent,  PendingIntent.FLAG_UPDATE_CURRENT);


                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                updateText.setText(df.format(calendar.getTime()));
            }
        });

        Button alarmOff = (Button) findViewById(R.id.alarm_off);


        alarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateText.setText(R.string.alarmOff);
                alarmManager.cancel(pendingIntent);

                alarmReceiverIntent.putExtra("alarm",false);
                alarmReceiverIntent.putExtra("alarmStr", "alarm off");

                sendBroadcast(alarmReceiverIntent);
            }
        });


    }


}
