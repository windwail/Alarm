package windwail.ru.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.util.List;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import windwail.ru.alarm.entities.AlarmConfig;
import windwail.ru.alarm.entities.AlarmItem;
import windwail.ru.alarm.entities.RepeatData;

import static windwail.ru.alarm.R.id.repeatsListView;

public class TabbedAlarmDetails extends AppCompatActivity {

    private static String ALARM_ID = "alarm_id";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private AlarmItem alarm;

    private RepeatData repeat;

    private List<RepeatData> repeats;

    private long alarm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alarm_id = getIntent().getLongExtra("alarm_id", -1);

        this.alarm = AlarmItem.findById(AlarmItem.class,alarm_id);

        this.repeats = this.alarm.getRepeats();

        this.repeat = this.repeats.get(0);

        if(alarm == null || repeat == null) {
            throw new RuntimeException("Alarm or main repeat data is null!");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_alarm_details2);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int current = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("PAGE: ", ""+position);

                if(position != current) {

                    if(position == 0) {
                        detailsFragment.restoreFromObject();
                    }

                    if(position == 1) {
                        detailsFragment.saveFromForm();
                        repeatsFragment.updateInvalidate();
                    }

                    current = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private DetailsFragment detailsFragment;

    public static class DetailsFragment extends Fragment {

        private FilePickerDialog dialog;
        private RepeatData repeat;
        private AlarmItem alarm;

        private Button playButton;
        private EditText audioFile;
        private EditText alarmName;
        private MaterialNumberPicker alarmHour, alarmMinute;
        private MediaPlayer mediaPlayer;
        private SeekBar volume1;
        private CheckBox vibro1;
        private CheckBox vibroUntilButton;
        private TextView vibroLenth1;
        private TextView vibroInterval1;
        private TextView vibroRepeat1;
        private TextView info;
        private CheckBox notify;
        private boolean isPlaying;

        private Button save, set, play, chooseFile, vibroTest;

        private TabbedAlarmDetails tabbedAlarmDetails;

        public static DetailsFragment newInstance(TabbedAlarmDetails ta) {
            DetailsFragment fragment = new DetailsFragment();
            fragment.tabbedAlarmDetails = ta;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            //long alarm_id = getArguments().getLong(ALARM_ID);
            this.alarm = tabbedAlarmDetails.alarm;
            this.repeat = tabbedAlarmDetails.repeat;

            View rootView = inflater.inflate(R.layout.fragment_alarm_details, container, false);

            audioFile = (EditText) rootView.findViewById(R.id.fileName);
            playButton = (Button) rootView.findViewById(R.id.playButton);
            alarmName = (EditText) rootView.findViewById(R.id.alarmName);

            alarmName.setText(alarm.getTitle());

            alarmHour = (MaterialNumberPicker) rootView.findViewById(R.id.alarmHour);
            alarmMinute = (MaterialNumberPicker) rootView.findViewById(R.id.alarmMinute);

            notify = (CheckBox) rootView.findViewById(R.id.notification);

            volume1 = (SeekBar) rootView.findViewById(R.id.volume1);
            vibro1 = (CheckBox) rootView.findViewById(R.id.vibro1);
            vibroUntilButton = (CheckBox) rootView.findViewById(R.id.vibroStop);
            vibroLenth1 = (TextView) rootView.findViewById(R.id.vibroLenth1);
            vibroInterval1 = (TextView) rootView.findViewById(R.id.vibroInterval1);
            vibroRepeat1 = (TextView) rootView.findViewById(R.id.vibroRepeat1);

            info = (TextView) rootView.findViewById(R.id.info);

            StringBuilder sb = new StringBuilder();

            for(RepeatData r: alarm.getRepeats()) {
                sb.append("id:"+r.getId()+" повторов:"+r.getRepeats() + "\n");
            }

            info.setText(sb.toString());

            save = (Button) rootView.findViewById(R.id.saveAlarm);
            set = (Button) rootView.findViewById(R.id.setAlarmButton);
            chooseFile = (Button) rootView.findViewById(R.id.chooseFile);
            play = (Button) rootView.findViewById(R.id.playButton);
            vibroTest = (Button) rootView.findViewById(R.id.vibroTest);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSaveAlarm(null);
                }
            });

            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSetAlarm(null);
                }
            });

            vibroTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vibrator vibrate = (Vibrator)tabbedAlarmDetails.getSystemService(VIBRATOR_SERVICE);

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
            });

            chooseFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogProperties properties=new DialogProperties();
                    properties.selection_mode= DialogConfigs.SINGLE_MODE;
                    properties.selection_type=DialogConfigs.FILE_SELECT;
                    properties.root=new File(DialogConfigs.DEFAULT_DIR);

                    if(!AlarmConfig.getInstance().getPreffFoldeer().isEmpty()) {
                        properties.root=new File(AlarmConfig.getInstance().getPreffFoldeer());
                    }

                    properties.error_dir=new File(DialogConfigs.DEFAULT_DIR);
                    properties.extensions=null;
                    dialog = new FilePickerDialog(tabbedAlarmDetails,properties);
                    dialog.setTitle("Select a File");

                    dialog.setDialogSelectionListener(new DialogSelectionListener() {
                        @Override
                        public void onSelectedFilePaths(String[] files) {

                            String file = files[0];

                            AlarmConfig conf = AlarmConfig.getInstance();
                            conf.setPreffFoldeer(file.substring(0,file.lastIndexOf("/")));
                            conf.save();

                            Log.e("Selected file:", files[0]);
                            audioFile.setText(files[0]);
                            repeat.setFile(files[0]);
                        }
                    });

                    dialog.show();
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(repeat.getFile() != null && repeat.getFile() != "") {
                        if(!isPlaying) {
                            isPlaying = true;
                            mediaPlayer = MediaPlayer.create(tabbedAlarmDetails, Uri.parse(repeat.getFile()));
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
            });


            NumberPicker.OnValueChangeListener updateRepeatTime = new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    repeat.setStartHour(alarmHour.getValue());
                    repeat.setStartMinute(alarmMinute.getValue());
                    //setStartSections();
                }
            };

            alarmMinute.setOnValueChangedListener(updateRepeatTime);
            alarmHour.setOnValueChangedListener(updateRepeatTime);

            restoreFromObject();

            PendingIntent pendingIntent = getPendingIntent();

            if(pendingIntent!=null) {
                set.setText("ОТКЛЮЧИТЬ");

                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlarmManager am = (AlarmManager) tabbedAlarmDetails.getSystemService(ALARM_SERVICE);
                        PendingIntent pi = getPendingIntent();

                        alarm.setInfo("<ОТКЛЮЧЕН>");
                        alarm.save();

                        if(pi != null) {
                            pi.cancel();
                            am.cancel(pi);
                            Toast.makeText(tabbedAlarmDetails,"Будильник отключен!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(tabbedAlarmDetails,"Будильник Не установлен!", Toast.LENGTH_LONG).show();
                        }

                        tabbedAlarmDetails.setResult(RESULT_CANCELED, tabbedAlarmDetails.getIntent());
                        tabbedAlarmDetails.finish();
                    }
                });
            }

            return rootView;
        }

        public PendingIntent getPendingIntent() {
            Intent alarmReceiverIntent = new Intent(tabbedAlarmDetails, AlarmReceiver.class);
            alarmReceiverIntent.setData(Uri.parse("custom://" + alarm.getId()));
            alarmReceiverIntent.setAction(String.valueOf(alarm.getId()));
            alarmReceiverIntent.putExtra("alarm_id", alarm.getId());

            return PendingIntent.getBroadcast(tabbedAlarmDetails, alarm.getId().intValue(),
                    alarmReceiverIntent,  PendingIntent.FLAG_NO_CREATE);
        }

        public void restoreFromObject() {
            alarm.setTitle(alarmName.getText().toString());

            alarmName.setText(alarm.getTitle());
            alarmHour.setValue(repeat.getStartHour());
            alarmMinute.setValue(repeat.getStartMinute());
            volume1.setProgress(repeat.getVolume());

            vibro1.setChecked(repeat.getVibro());
            vibroLenth1.setText("" + repeat.getVibroLenth());
            vibroInterval1.setText("" + repeat.getVibroInterval());
            vibroRepeat1.setText("" + repeat.getVibroRepeat());
            vibroUntilButton.setChecked(repeat.getVibroUntilPressed());

            audioFile.setText(repeat.getFile());
            notify.setChecked(repeat.getNotifications());
        }

        public void saveFromForm() {
            alarm.setTitle(alarmName.getText().toString());
            repeat.setStartHour(alarmHour.getValue());
            repeat.setStartMinute(alarmMinute.getValue());

            repeat.setVolume(volume1.getProgress());

            repeat.setVibro(vibro1.isChecked());
            Log.e("VIBRO", ""+vibro1.isChecked());
            repeat.setVibroInterval(Integer.parseInt(vibroInterval1.getText().toString()));
            repeat.setVibroLenth(Integer.parseInt(vibroLenth1.getText().toString()));
            repeat.setVibroRepeat(Integer.parseInt(vibroRepeat1.getText().toString()));
            repeat.setVibroUntilPressed(vibroUntilButton.isChecked());

            repeat.setFile(audioFile.getText().toString());
            repeat.setNotifications(notify.isChecked());

        }

        public void onSaveAlarm(View v) {
            Log.e("SAVE 2", "SAVE 2");

            tabbedAlarmDetails.repeatsFragment.saveAndRemove();

            saveFromForm();

            alarm.save();
            repeat.save();

            Toast.makeText(tabbedAlarmDetails, "Сохранено!", Toast.LENGTH_LONG);
            Intent intent = tabbedAlarmDetails.getIntent();
            intent.putExtra("alarm_id", alarm.getId());

            tabbedAlarmDetails.logAlarm();

            tabbedAlarmDetails.setResult(RESULT_CANCELED, intent);
            tabbedAlarmDetails.finish();
        }

        public void onSetAlarm(View v) {
            Log.e("SAVE 2", "SAVE 2");

            tabbedAlarmDetails.repeatsFragment.saveAndRemove();

            saveFromForm();

            alarm.save();
            repeat.save();

            for(RepeatData r: alarm.getRepeats()) {
                r.setRepeats(0);
                r.save();
            }

            Toast.makeText(tabbedAlarmDetails, "Сохранено!", Toast.LENGTH_LONG);
            Intent intent = tabbedAlarmDetails.getIntent();
            intent.putExtra("alarm_id", alarm.getId());

            tabbedAlarmDetails.logAlarm();

            tabbedAlarmDetails.setResult(RESULT_OK, intent);
            tabbedAlarmDetails.finish();
        }

    }

    public RepeatsFragment repeatsFragment;

    public static class RepeatsFragment extends Fragment {

        private TabbedAlarmDetails tabbedAlarmDetails;
        private ListView repeatsList;
        private RepeatsListAdapter repeatsListAdapter;

        private Button add;

        private Button save;

        private Button set;

        public void updateInvalidate() {
            if(repeatsListAdapter != null) {
                repeatsListAdapter.notifyDataSetInvalidated();
            }
        }

        public static RepeatsFragment newInstance(TabbedAlarmDetails ta) {
            RepeatsFragment fragment = new RepeatsFragment();
            fragment.tabbedAlarmDetails = ta;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_alarm_repeats, container, false);

            repeatsList = (ListView) rootView.findViewById(R.id.repeatsListView);

            repeatsListAdapter = new RepeatsListAdapter(tabbedAlarmDetails, R.layout.repeat_item, tabbedAlarmDetails.repeats);
            repeatsList.setAdapter(repeatsListAdapter);

            save = (Button) rootView.findViewById(R.id.saveAlarm);
            set = (Button) rootView.findViewById(R.id.setAlarm);
            add = (Button) rootView.findViewById(R.id.addRepeat);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSaveAlarm(null);
                }
            });

            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSetAlarm(null);
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tabbedAlarmDetails.repeats.add(new RepeatData(tabbedAlarmDetails.alarm));
                    repeatsListAdapter.notifyDataSetInvalidated();
                }
            });

            return rootView;
        }

        public void saveAndRemove() {
            repeatsListAdapter.saveAndRemove();
        }

        public void onSaveAlarm(View v) {
            tabbedAlarmDetails.alarm.save();
            tabbedAlarmDetails.repeat.save();

            repeatsListAdapter.saveAndRemove();

            Toast.makeText(tabbedAlarmDetails, "Сохранено!", Toast.LENGTH_LONG);
            Intent intent = tabbedAlarmDetails.getIntent();
            intent.putExtra("alarm_id", tabbedAlarmDetails.alarm.getId());

            tabbedAlarmDetails.logAlarm();

            tabbedAlarmDetails.setResult(RESULT_CANCELED, intent);
            tabbedAlarmDetails.finish();
        }

        public void onSetAlarm(View v) {
            tabbedAlarmDetails.alarm.save();
            tabbedAlarmDetails.repeat.save();

            repeatsListAdapter.saveAndRemove();

            Toast.makeText(tabbedAlarmDetails, "Сохранено!", Toast.LENGTH_LONG);
            Intent intent = tabbedAlarmDetails.getIntent();
            intent.putExtra("alarm_id", tabbedAlarmDetails.alarm.getId());

            tabbedAlarmDetails.logAlarm();

            tabbedAlarmDetails.setResult(RESULT_OK, intent);
            tabbedAlarmDetails.finish();
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {

                if (detailsFragment == null) {
                    detailsFragment = DetailsFragment.newInstance(TabbedAlarmDetails.this);
                }

                return detailsFragment;
            } else {

                if (repeatsFragment == null) {
                    repeatsFragment = RepeatsFragment.newInstance(TabbedAlarmDetails.this);
                }

                return repeatsFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ОСНОВНОЕ";
                case 1:
                    return "ПОВТОРЫ";
            }
            return null;
        }
    }

    public void logAlarm() {
        FileUtil.log("----- Сохранен будильник: -----");

        FileUtil.log(alarm.toString());

        int cnt = 0;
        for(RepeatData r: alarm.getRepeats()) {
            FileUtil.log("----- Повтор:"+ ++cnt +" -----");
            FileUtil.log(r.toString());
        }

        FileUtil.log("-------------------------------");
    }

}
