package windwail.ru.alarm;

import android.app.PendingIntent;
import android.media.MediaPlayer;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import com.github.angads25.filepicker.view.FilePickerDialog;

import org.joda.time.DateTime;

import java.util.ArrayList;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import windwail.ru.alarm.entities.AlarmItem;
import windwail.ru.alarm.entities.RepeatData;

public class TabbedAlarmDetails extends AppCompatActivity {

    private static String ALARM_ID = "alarm_id";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private AlarmItem alarm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_alarm_details2);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private DetailsFragment detailsFragment = new DetailsFragment();
    public static class DetailsFragment extends Fragment {

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
        private static final String ARG_ALARM_ID = "section_number";

        public static DetailsFragment newInstance(int alarmId) {
            DetailsFragment fragment = new DetailsFragment();
            Bundle args = new Bundle();
            args.putInt(ALARM_ID, alarmId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            long alarm_id = getArguments().getInt(ALARM_ID);

            View rootView = inflater.inflate(R.layout.fragment_alarm_details, container, false);

            audioFile = (EditText) rootView.findViewById(R.id.fileName);
            playButton = (Button) rootView.findViewById(R.id.playButton);
            alarmName = (EditText) rootView.findViewById(R.id.alarmName);
            alarmHour = (MaterialNumberPicker) rootView.findViewById(R.id.alarmHour);
            alarmMinute = (MaterialNumberPicker) rootView.findViewById(R.id.alarmMinute);

            notify = (CheckBox) rootView.findViewById(R.id.notification);


            volume1 = (SeekBar) rootView.findViewById(R.id.volume1);
            vibro1 = (CheckBox) rootView.findViewById(R.id.vibro1);
            vibroLenth1 = (TextView) rootView.findViewById(R.id.vibroLenth1);
            vibroInterval1 = (TextView) rootView.findViewById(R.id.vibroInterval1);
            vibroRepeat1 = (TextView) rootView.findViewById(R.id.vibroRepeat1);
            setAlarmButton = (Button) rootView.findViewById(R.id.setAlarmButton);
            repeatsListView = (ListView) rootView.findViewById(R.id.repeatsListView);

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
                    //setStartSections();
                }
            };

            alarmMinute.setOnValueChangedListener(updateRepeatTime);
            alarmHour.setOnValueChangedListener(updateRepeatTime);

            long alarm_id = getIntent().getLongExtra("alarm_id", -1);

            if(alarm_id < 0) {
                alarm = new AlarmItem();

                repeat = new RepeatData();
                repeat.save();
                alarm.setCurrent(repeat);

                alarm.setRepeats(new ArrayList<RepeatData>());
                alarm.getRepeats().add(repeat);
                alarm.getRepeats().add(new RepeatData());
                alarm.getRepeats().add(new RepeatData());
                alarm.getRepeats().add(new RepeatData());
                alarm.getRepeats().add(new RepeatData());

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
                //setRepeatSectionsFromAlarm();
                notify.setChecked(repeat.getNotifications());
                volume1.setProgress(repeat.getVolume());
                vibro1.setChecked(repeat.getVibro());
                vibroLenth1.setText(""+repeat.getVibroLenth());
                vibroInterval1.setText(""+repeat.getVibroInterval());
                vibroRepeat1.setText(""+repeat.getVibroRepeat());
            }

            alarmHour.setValue(repeat.getStartHour());
            alarmMinute.setValue(repeat.getStartMinute());

            return rootView;
        }
    }

    public RepeatsFragment repeatsFragment = new RepeatsFragment();
    public static class RepeatsFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_alarm_repeats, container, false);
            return rootView;
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
            if(position == 0) {
                return detailsFragment;
            } else {
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
}
