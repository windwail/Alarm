package windwail.ru.alarm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private CheckBox background;

    private boolean isPlaying;


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

        background = (CheckBox) findViewById(R.id.background);



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
            background.setChecked(alarm.background);
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


        if (alarmName.getText().toString().trim().length() <= 0) {
            Toast.makeText(this, "Введите имя будильника", Toast.LENGTH_SHORT).show();
            return;
        }

        if (audioFile.getText().toString().trim().length() <= 0) {
            Toast.makeText(this, "Не указан файл", Toast.LENGTH_SHORT).show();
            return;
        }

        alarm.title = alarmName.getText().toString();
        alarm.background = background.isChecked();
        alarm.save();
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
        properties.error_dir=new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions=null;
        dialog = new FilePickerDialog(this,properties);
        dialog.setTitle("Select a File");

        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
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



}
