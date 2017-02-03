package windwail.ru.alarm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;

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

    private MediaPlayer mediaPlayer;

    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_details);

        audioFile = (EditText) findViewById(R.id.fileName);
        playButton = (Button) findViewById(R.id.playButton);
        //alarmName = (EditText) findViewById(R.id.alarmName);

        long alarm_id = getIntent().getLongExtra("alarm_id", -1);

        if(alarm_id < 0) {
            alarm = new AlarmItem();
        } else {
            alarm = AlarmItem.findById(AlarmItem.class, alarm_id);
            alarmName.setText(alarm.getTitle());
            //startTime.setText(pad(alarm.startHour)+":"+pad(alarm.startMinute));
            audioFile.setText(alarm.file);
        }

    }

    public void onSaveAlarm(View v) {


        if (alarmName.getText().toString().trim().length() <= 0) {
            Toast.makeText(this, "Введите имя будильника", Toast.LENGTH_SHORT).show();
            return;
        }
        /*
        if (startTime.getText().toString().trim().length() <= 0) {
            Toast.makeText(this, "Не указано время", Toast.LENGTH_SHORT).show();
            return;
        }
        */
        if (audioFile.getText().toString().trim().length() <= 0) {
            Toast.makeText(this, "Не указан файл", Toast.LENGTH_SHORT).show();
            return;
        }

        alarm.title = alarmName.getText().toString();
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
