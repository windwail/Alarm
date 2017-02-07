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
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import org.joda.time.DurationFieldType;

import java.io.File;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import windwail.ru.alarm.entities.AlarmConfig;
import windwail.ru.alarm.entities.AlarmItem;


public class AlarmDetails extends AppCompatActivity {

    public static final int TIME_DIALOG_ID = 1;
    public static final int FILE_CODE = 2;
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";
    private static String TAG = "AlarmDetails";

    private FilePickerDialog dialog;

    private AlarmItem alarm;

    private Button playButton;

    private EditText audioFile;
    private EditText alarmName;

    private MaterialNumberPicker alarmHour, alarmMinute;

    private MediaPlayer mediaPlayer;

    private TextView sectionStart1;
    private TextView sectionStart2;
    private TextView sectionStart3;
    private TextView sectionStart4;

    private TextView sectionInterval1;
    private TextView sectionInterval2;
    private TextView sectionInterval3;
    private TextView sectionInterval4;

    private TextView sectionCount1;
    private TextView sectionCount2;
    private TextView sectionCount3;
    private TextView sectionCount4;

    private SeekBar volume1;
    private SeekBar volume2;
    private SeekBar volume3;
    private SeekBar volume4;

    private CheckBox vibro1;
    private CheckBox vibro2;
    private CheckBox vibro3;
    private CheckBox vibro4;

    private TextView vibroLenth1;
    private TextView vibroLenth2;
    private TextView vibroLenth3;
    private TextView vibroLenth4;

    private TextView vibroInterval1;
    private TextView vibroInterval2;
    private TextView vibroInterval3;
    private TextView vibroInterval4;

    private TextView vibroRepeat1;
    private TextView vibroRepeat2;
    private TextView vibroRepeat3;
    private TextView vibroRepeat4;

    private CheckBox notify;

    private boolean isPlaying;

    private PendingIntent displayIntent;

    private Button setAlarmButton ;


    private void setRepeatSectionsFromAlarm() {
        sectionStart1.setText(pad(alarm.getStartHour())+":"+pad(alarm.getStartMinute()));
        sectionStart2.setText(pad(alarm.getRepeatStartHour2())+":"+pad(alarm.getRepeatStartMinute2()));
        sectionStart3.setText(pad(alarm.getRepeatStartHour3())+":"+pad(alarm.getRepeatStartMinute3()));
        sectionStart4.setText(pad(alarm.getRepeatStartHour4())+":"+pad(alarm.getRepeatStartMinute4()));

        sectionCount1.setText(""+alarm.getRepeatCount1());
        sectionCount2.setText(""+alarm.getRepeatCount2());
        sectionCount3.setText(""+alarm.getRepeatCount3());
        sectionCount4.setText(""+alarm.getRepeatCount4());

        sectionInterval1.setText(""+alarm.getRepeatInterval1());
        sectionInterval2.setText(""+alarm.getRepeatInterval2());
        sectionInterval3.setText(""+alarm.getRepeatInterval3());
        sectionInterval4.setText(""+alarm.getRepeatInterval4());


    }

    private void setStartSections() {

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

        sectionStart1 = (TextView) findViewById(R.id.sectionStart1);
        sectionStart2 = (TextView) findViewById(R.id.sectionStart2);
        sectionStart3 = (TextView) findViewById(R.id.sectionStart3);
        sectionStart4 = (TextView) findViewById(R.id.sectionStart4);

        sectionInterval1 = (TextView) findViewById(R.id.sectionInterval1);
        sectionInterval2 = (TextView) findViewById(R.id.sectionInterval2);
        sectionInterval3 = (TextView) findViewById(R.id.sectionInterval3);
        sectionInterval4 = (TextView) findViewById(R.id.sectionInterval4);

        sectionCount1 = (TextView) findViewById(R.id.sectionRepeat1);
        sectionCount2 = (TextView) findViewById(R.id.sectionRepeat2);
        sectionCount3 = (TextView) findViewById(R.id.sectionRepeat3);
        sectionCount4 = (TextView) findViewById(R.id.sectionRepeat4);

        volume1 = (SeekBar) findViewById(R.id.volume1);
        volume2 = (SeekBar) findViewById(R.id.volume2);
        volume3 = (SeekBar) findViewById(R.id.volume3);
        volume4 = (SeekBar) findViewById(R.id.volume4);

        vibro1 = (CheckBox) findViewById(R.id.vibro1);
        vibro2 = (CheckBox) findViewById(R.id.vibro2);
        vibro3 = (CheckBox) findViewById(R.id.vibro3);
        vibro4 = (CheckBox) findViewById(R.id.vibro4);

        vibroLenth1 = (TextView) findViewById(R.id.vibroLenth1);
        vibroLenth2 = (TextView) findViewById(R.id.vibroLenth2);
        vibroLenth3 = (TextView) findViewById(R.id.vibroLenth3);
        vibroLenth4 = (TextView) findViewById(R.id.vibroLenth4);

        vibroInterval1 = (TextView) findViewById(R.id.vibroInterval1);
        vibroInterval2 = (TextView) findViewById(R.id.vibroInterval2);
        vibroInterval3 = (TextView) findViewById(R.id.vibroInterval3);
        vibroInterval4 = (TextView) findViewById(R.id.vibroInterval4);

        vibroRepeat1 = (TextView) findViewById(R.id.vibroRepeat1);
        vibroRepeat2 = (TextView) findViewById(R.id.vibroRepeat2);
        vibroRepeat3 = (TextView) findViewById(R.id.vibroRepeat3);
        vibroRepeat4 = (TextView) findViewById(R.id.vibroRepeat4);

        setAlarmButton = (Button) findViewById(R.id.setAlarmButton);

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
                sectionStart1.setText(pad(alarmHour.getValue())+":"+pad(alarmMinute.getValue()));
                alarm.setStartHour(alarmHour.getValue());
                alarm.setStartMinute(alarmMinute.getValue());
                setStartSections();
            }
        };

        alarmMinute.setOnValueChangedListener(updateRepeatTime);
        alarmHour.setOnValueChangedListener(updateRepeatTime);

        long alarm_id = getIntent().getLongExtra("alarm_id", -1);

        if(alarm_id < 0) {
            alarm = new AlarmItem();

            DateTime dt = DateTime.now();

            alarm.setStartHour(dt.getHourOfDay());
            alarm.setStartMinute(dt.getMinuteOfHour());



        } else {
            alarm = AlarmItem.findById(AlarmItem.class, alarm_id);
            alarmName.setText(alarm.getTitle());
            //startTime.setText(pad(alarm.startHour)+":"+pad(alarm.startMinute));
            audioFile.setText(alarm.file);

            setRepeatSectionsFromAlarm();

            notify.setChecked(alarm.notifications);

            volume1.setProgress(alarm.getVolume1());
            volume2.setProgress(alarm.getVolume2());
            volume3.setProgress(alarm.getVolume3());
            volume4.setProgress(alarm.getVolume4());

            vibro1.setChecked(alarm.getVibro1());
            vibro2.setChecked(alarm.getVibro2());
            vibro3.setChecked(alarm.getVibro3());
            vibro4.setChecked(alarm.getVibro4());

            vibroLenth1.setText(""+alarm.getVibroLenth1());
            vibroLenth2.setText(""+alarm.getVibroLenth2());
            vibroLenth3.setText(""+alarm.getVibroLenth3());
            vibroLenth4.setText(""+alarm.getVibroLenth4());

            vibroInterval1.setText(""+alarm.getVibroInterval1());
            vibroInterval2.setText(""+alarm.getVibroInterval2());
            vibroInterval3.setText(""+alarm.getVibroInterval3());
            vibroInterval4.setText(""+alarm.getVibroInterval4());

            vibroRepeat1.setText(""+alarm.getVibroRepeat1());
            vibroRepeat2.setText(""+alarm.getVibroRepeat2());
            vibroRepeat3.setText(""+alarm.getVibroRepeat3());
            vibroRepeat4.setText(""+alarm.getVibroRepeat4());


            Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            alarmIntent.setData(Uri.parse("custom://" + alarm.getId()));
            alarmIntent.setAction(String.valueOf(alarm.getId()));


            displayIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);


            //alarmManager.cancel(displayIntent);


        }

        alarmHour.setValue(alarm.getStartHour());
        alarmMinute.setValue(alarm.getStartMinute());
        sectionStart1.setText(pad(alarmHour.getValue())+":"+pad(alarmMinute.getValue()));

        sectionInterval1.addTextChangedListener(textWatcher);
        sectionInterval2.addTextChangedListener(textWatcher);
        sectionInterval3.addTextChangedListener(textWatcher);
        sectionInterval4.addTextChangedListener(textWatcher);

        sectionCount1.addTextChangedListener(textWatcher);
        sectionCount2.addTextChangedListener(textWatcher);
        sectionCount3.addTextChangedListener(textWatcher);
        sectionCount4.addTextChangedListener(textWatcher);




    }

    public void onSaveAlarm(View v) {

        if (audioFile.getText().toString().trim().length() <= 0
                && !notify.isChecked() && !vibro1.isChecked()) {
            Toast.makeText(this, "Не указан файл|вибро|нотификация", Toast.LENGTH_LONG).show();
            return;
        }

        alarm.title = alarmName.getText().toString();
        alarm.notifications = notify.isChecked();

        alarm.volume1 = volume1.getProgress();
        alarm.volume2 = volume2.getProgress();
        alarm.volume3 = volume3.getProgress();
        alarm.volume4 = volume4.getProgress();

        alarm.setVibro1(vibro1.isChecked());
        alarm.setVibro2(vibro2.isChecked());
        alarm.setVibro3(vibro3.isChecked());
        alarm.setVibro4(vibro4.isChecked());

        alarm.setVibroLenth1(Integer.parseInt(vibroLenth1.getText().toString()));
        alarm.setVibroLenth2(Integer.parseInt(vibroLenth2.getText().toString()));
        alarm.setVibroLenth3(Integer.parseInt(vibroLenth3.getText().toString()));
        alarm.setVibroLenth4(Integer.parseInt(vibroLenth4.getText().toString()));

        alarm.setVibroInterval1(Integer.parseInt(vibroInterval1.getText().toString()));
        alarm.setVibroInterval2(Integer.parseInt(vibroInterval2.getText().toString()));
        alarm.setVibroInterval3(Integer.parseInt(vibroInterval3.getText().toString()));
        alarm.setVibroInterval4(Integer.parseInt(vibroInterval4.getText().toString()));

        alarm.setVibroRepeat1(Integer.parseInt(vibroRepeat1.getText().toString()));
        alarm.setVibroRepeat2(Integer.parseInt(vibroRepeat2.getText().toString()));
        alarm.setVibroRepeat3(Integer.parseInt(vibroRepeat3.getText().toString()));
        alarm.setVibroRepeat4(Integer.parseInt(vibroRepeat4.getText().toString()));

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
                alarm.startHour = h;
                alarm.startMinute = m;
                //startTime.setText(pad(h)+":"+pad(m));
            }
        });
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
    }

    public void onTestPlay(View v) {
        if(alarm.file != null && alarm.file != "") {
            if(!isPlaying) {
                isPlaying = true;
                mediaPlayer = MediaPlayer.create(AlarmDetails.this, Uri.parse(alarm.file));
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
                alarm.file = files[0];
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
