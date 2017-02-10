package windwail.ru.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import windwail.ru.alarm.entities.AlarmConfig;
import windwail.ru.alarm.entities.AlarmItem;
import windwail.ru.alarm.entities.RepeatData;


public class AlarmDetails extends AppCompatActivity {

    public static final int TIME_DIALOG_ID = 1;
    public static final int FILE_CODE = 2;
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
    private static String TAG = "AlarmDetails";

    private FilePickerDialog dialog;

    private AlarmItem alarm;

    private  RepeatData repeat;

    private Button playButton;

    private EditText audioFile;
    private EditText alarmName;

    private MaterialNumberPicker alarmHour, alarmMinute;

    private MediaPlayer mediaPlayer;

    private SeekBar volume1;

    private CheckBox vibro1;

    private TextView vibroLenth1;

    private TextView vibroInterval1;

    private TextView vibroRepeat1;

    private CheckBox notify;

    private boolean isPlaying;

    private PendingIntent displayIntent;

    private Button setAlarmButton ;

    private ListView repeatsListView;

    private RepeatsListAdapter repeatsListAdapter;

    private void setRepeatSectionsFromAlarm() {
        /*
        sectionStart1.setText(pad(alarm.getStartHour())+":"+pad(alarm.getStartMinute()));

        sectionCount1.setText(""+alarm.getRepeatCount1());

        sectionInterval1.setText(""+alarm.getRepeatInterval1());
        */

    }

    private void setStartSections() {

        /*
        alarm.setRepeatCount1(Integer.parseInt(sectionCount1.getText().toString()));
        alarm.setRepeatCount2(Integer.parseInt(sectionCount2.getText().toString()));
        alarm.setRepeatCount3(Integer.parseInt(sectionCount3.getText().toString()));
        alarm.setRepeatCount4(Integer.parseInt(sectionCount4.getText().toString()));

        alarm.setRepeatInterval1(Integer.parseInt(sectionInterval1.getText().toString()));
        alarm.setRepeatInterval2(Integer.parseInt(sectionInterval2.getText().toString()));
        alarm.setRepeatInterval3(Integer.parseInt(sectionInterval3.getText().toString()));
        alarm.setRepeatInterval4(Integer.parseInt(sectionInterval4.getText().toString()));

        DateTime dt = (new DateTime()).withTime(alarm.getStartHour(), alarm.getStartMinute(), 0, 0);

        dt = dt.plusMinutes(alarm.getRepeatCount1()*alarm.getRepeatInterval1());

        alarm.setRepeatStartHour2(dt.getHourOfDay());
        alarm.setRepeatStartMinute2(dt.getMinuteOfHour());
        sectionStart2.setText(pad(dt.getHourOfDay())+":"+pad(dt.getMinuteOfHour()));

        dt = dt.plusMinutes(alarm.getRepeatCount2()*alarm.getRepeatInterval2());

        alarm.setRepeatStartHour3(dt.getHourOfDay());
        alarm.setRepeatStartMinute3(dt.getMinuteOfHour());
        sectionStart3.setText(pad(dt.getHourOfDay())+":"+pad(dt.getMinuteOfHour()));

        dt = dt.plusMinutes(alarm.getRepeatCount3()*alarm.getRepeatInterval3());

        alarm.setRepeatStartHour4(dt.getHourOfDay());
        alarm.setRepeatStartMinute4(dt.getMinuteOfHour());
        sectionStart4.setText(pad(dt.getHourOfDay())+":"+pad(dt.getMinuteOfHour()));
        */

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_details);

        audioFile = (EditText) findViewById(R.id.fileName);
        playButton = (Button) findViewById(R.id.playButton);
        alarmName = (EditText) findViewById(R.id.alarmName);
        alarmHour = (MaterialNumberPicker) findViewById(R.id.alarmHour);
        alarmMinute = (MaterialNumberPicker) findViewById(R.id.alarmMinute);

        notify = (CheckBox) findViewById(R.id.notification);


        volume1 = (SeekBar) findViewById(R.id.volume1);
        vibro1 = (CheckBox) findViewById(R.id.vibro1);
        vibroLenth1 = (TextView) findViewById(R.id.vibroLenth1);
        vibroInterval1 = (TextView) findViewById(R.id.vibroInterval1);
        vibroRepeat1 = (TextView) findViewById(R.id.vibroRepeat1);
        setAlarmButton = (Button) findViewById(R.id.setAlarmButton);
        repeatsListView = (ListView) findViewById(R.id.repeatsListView);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    Integer.parseInt(s.toString());
                    setStartSections();
                } catch (Exception ex) {

                }

            }
        };

        NumberPicker.OnValueChangeListener updateRepeatTime = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                repeat.setStartHour(alarmHour.getValue());
                repeat.setStartMinute(alarmMinute.getValue());
                setStartSections();
            }
        };

        alarmMinute.setOnValueChangedListener(updateRepeatTime);
        alarmHour.setOnValueChangedListener(updateRepeatTime);

        long alarm_id = getIntent().getLongExtra("alarm_id", -1);

        if(alarm_id < 0) {

            if(true) throw new RuntimeException();

            alarm = new AlarmItem();

            repeat = new RepeatData(alarm);
            repeat.save();

            alarm.getRepeats().add(repeat);

            repeatsListAdapter = new RepeatsListAdapter(this, R.layout.repeat_item, alarm.getRepeats());
            repeatsListView.setAdapter(repeatsListAdapter);

            DateTime dt = DateTime.now();

            repeat.setStartHour(dt.getHourOfDay());
            repeat.setStartMinute(dt.getMinuteOfHour());


        } else {
            alarm = AlarmItem.findById(AlarmItem.class, alarm_id);
            alarmName.setText(alarm.getTitle());
            //startTime.setText(pad(alarm.startHour)+":"+pad(alarm.startMinute));
            audioFile.setText(repeat.getFile());

            setRepeatSectionsFromAlarm();

            notify.setChecked(repeat.getNotifications());

            volume1.setProgress(repeat.getVolume());

            vibro1.setChecked(repeat.getVibro());

            vibroLenth1.setText(""+repeat.getVibroLenth());

            vibroInterval1.setText(""+repeat.getVibroInterval());

            vibroRepeat1.setText(""+repeat.getVibroRepeat());

        }

        alarmHour.setValue(repeat.getStartHour());
        alarmMinute.setValue(repeat.getStartMinute());

    }

    public void onSaveAlarm(View v) {

        if (audioFile.getText().toString().trim().length() <= 0
                && !notify.isChecked() && !vibro1.isChecked()) {
            Toast.makeText(this, "Не указан файл|вибро|нотификация", Toast.LENGTH_LONG).show();
            return;
        }

        alarm.setTitle(alarmName.getText().toString());

        repeat.setNotifications(notify.isChecked());

        // TODO: savign

        alarm.save();

        Toast.makeText(this, "СОХРАНЕНО", Toast.LENGTH_SHORT).show();

    }

    public void onUnsetAlarm(View v) {
        Intent intent = this.getIntent();
        intent.putExtra("alarm_id", alarm.getId());

        if(displayIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(displayIntent);
        }

        this.setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void onSetAlarm(View v) {

        if (audioFile.getText().toString().trim().length() <= 0
                && !notify.isChecked() && !vibro1.isChecked()) {
            Toast.makeText(this, "Не указан файл|вибро|нотификация", Toast.LENGTH_LONG).show();
            return;
        }

        onSaveAlarm(v);

            Intent intent = this.getIntent();
            intent.putExtra("alarm_id", alarm.getId());

            this.setResult(RESULT_OK, intent);
            finish();
    }

    public void onSetStartTime(View v) {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setStartTime(10, 10)
                .setDoneText("Сохранить")
                .setCancelText("Отказаться")
                .setThemeDark();

        rtpd.setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialTimePickerDialogFragment dialog, int h, int m) {
                repeat.setStartHour(h);
                repeat.setStartMinute(m);
                //startTime.setText(pad(h)+":"+pad(m));
            }
        });
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
    }

    public void onTestPlay(View v) {
        if(repeat.getFile() != null && repeat.getFile() != "") {
            if(!isPlaying) {
                isPlaying = true;
                mediaPlayer = MediaPlayer.create(AlarmDetails.this, Uri.parse(repeat.getFile()));
                float fv = ((float)volume1.getProgress())/10f;
                mediaPlayer.setVolume(fv,fv);
                playButton.setText("Стоп");
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playButton.setText("Проиграть");
                        isPlaying = false;
                    }
                });
                mediaPlayer.start();
            } else {
                mediaPlayer.stop();
                mediaPlayer.reset();
                playButton.setText("Проиграть");
                isPlaying = false;
            }
        }
    }

    public void onChooseFile(View v) {
        DialogProperties properties=new DialogProperties();
        properties.selection_mode=DialogConfigs.SINGLE_MODE;
        properties.selection_type=DialogConfigs.FILE_SELECT;
        properties.root=new File(DialogConfigs.DEFAULT_DIR);

        if(!AlarmConfig.getInstance().getPreffFoldeer().isEmpty()) {
            properties.root=new File(AlarmConfig.getInstance().getPreffFoldeer());
        }

        properties.error_dir=new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions=null;
        dialog = new FilePickerDialog(this,properties);
        dialog.setTitle("Select a File");

        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {

                String file = files[0];

                Log.e(TAG, "index: "+file.lastIndexOf("/"));
                Log.e(TAG, "folder: "+ file.substring(0,file.lastIndexOf("/")));
                AlarmConfig conf = AlarmConfig.getInstance();
                conf.setPreffFoldeer(file.substring(0,file.lastIndexOf("/")));
                conf.save();

                Log.e(TAG, files[0]);
                audioFile.setText(files[0]);
                repeat.setFile(files[0]);
            }
        });

        dialog.show();
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }




    //Add this method to show Dialog when the required permission has been granted to the app.
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[],@NonNull int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(dialog!=null)
                    {   //Show dialog if the read permission has been granted.
                        dialog.show();
                    }
                }
                else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(this,"Permission is Required for getting list of files",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    public void showNotiyfy(View v) {
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

    public void onVibroTest(View v) {

        Vibrator vibrate = (Vibrator)this.getSystemService(VIBRATOR_SERVICE);

        int vrep = Integer.parseInt(vibroRepeat1.getText().toString());
        int vlen = Integer.parseInt(vibroLenth1.getText().toString());;
        int vint = Integer.parseInt(vibroInterval1.getText().toString());;

        for(int i=0; i<vrep; i++) {
            Log.e("BZZZZ","BZZZZ");
            vibrate.vibrate(vlen);
            try {
                Thread.sleep(vint+vlen);
            } catch (InterruptedException e) {
            }
        }

    }


}
