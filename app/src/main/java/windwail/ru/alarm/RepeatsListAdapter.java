package windwail.ru.alarm;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import windwail.ru.alarm.entities.RepeatData;

/**
 * Created by icetsuk on 09.02.17.
 */

public class RepeatsListAdapter extends ArrayAdapter<RepeatData> {

    private List<RepeatData> repeats;

    private List<RepeatData> removals = new ArrayList<>();

    Context context;

    public void saveAndRemove() {
        for(RepeatData r: repeats) {
            r.save();
        }

        for(RepeatData r: removals) {
            r.delete();
        }
    }

    public void add(RepeatData ai) {
        repeats.add(ai);
    }

    public RepeatsListAdapter(Context context, int resource, List<RepeatData> repeats) {
        super(context, resource);
        this.repeats = repeats;
        this.context = context;
    }

    @Override
    public RepeatData getItem(int position) {
        return repeats.get(position);
    }

    public int getCount() {
        return repeats.size();
    }

    public static class DataHandler {
        TextView nextTime;
        SeekBar volume;
        public EditText repeatCount;
        public EditText repeatInterval;
        public EditText vibroLen;
        public EditText vibroInt;
        public EditText vibroRep;
        public CheckBox vibro;
        public CheckBox vibroStop;

        public TextWatcher textWatcher;

        public FloatingActionButton deleteButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;

        final RepeatData repeat = repeats.get(position);

        final DataHandler handler;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.repeat_item, parent, false);

            handler = new RepeatsListAdapter.DataHandler();

            handler.nextTime = (TextView) row.findViewById(R.id.nextTime);
            handler.volume = (SeekBar) row.findViewById(R.id.volume);
            handler.repeatCount = (EditText) row.findViewById(R.id.repeatCount);
            handler.repeatInterval = (EditText) row.findViewById(R.id.repeatInterval);
            handler.vibroLen = (EditText) row.findViewById(R.id.vibroLen);
            handler.vibroInt = (EditText) row.findViewById(R.id.vibroInt);
            handler.vibroRep = (EditText) row.findViewById(R.id.vibroRep);
            handler.vibro = (CheckBox) row.findViewById(R.id.vibro);
            handler.vibroStop = (CheckBox) row.findViewById(R.id.vibroStop);

            handler.deleteButton = (FloatingActionButton) row.findViewById(R.id.floatingRemove);

            row.setTag(handler);
        } else {
            handler = (DataHandler) row.getTag();
        }

        handler.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removals.add(repeats.get(position));
                repeats.remove(position);
                RepeatsListAdapter.this.notifyDataSetInvalidated();
            }
        });

        if(position == 0) {
            handler.deleteButton.setVisibility(View.INVISIBLE);
        } else {
            handler.deleteButton.setVisibility(View.VISIBLE);
        }

        if(handler.textWatcher != null) {
            handler.repeatCount.removeTextChangedListener(handler.textWatcher);
            handler.repeatInterval.removeTextChangedListener(handler.textWatcher);
            handler.vibroLen.removeTextChangedListener(handler.textWatcher);
            handler.vibroInt.removeTextChangedListener(handler.textWatcher);
            handler.vibroRep.removeTextChangedListener(handler.textWatcher);
        }

        handler.nextTime.setText(repeat.getNext());
        handler.volume.setProgress(repeat.getVolume());
        handler.repeatCount.setText(""+repeat.getRepeatCount());
        handler.repeatInterval.setText(""+repeat.getRepeatInterval());
        handler.vibroLen.setText(""+repeat.getVibroLenth());
        handler.vibroInt.setText(""+repeat.getVibroInterval());
        handler.vibroRep.setText(""+repeat.getVibroRepeat());
        handler.vibro.setChecked(repeat.getVibro());
        handler.vibroStop.setChecked(repeat.getVibroUntilPressed());

        handler.volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                repeat.setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        handler.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().isEmpty()) {
                    return;
                }

                repeat.setRepeatCount(Integer.parseInt(handler.repeatCount.getText().toString()));
                repeat.setRepeatInterval(Integer.parseInt(handler.repeatInterval.getText().toString()));
                repeat.setVibroLenth(Integer.parseInt(handler.vibroLen.getText().toString()));
                repeat.setVibroInterval(Integer.parseInt(handler.vibroInt.getText().toString()));
                repeat.setVibroRepeat(Integer.parseInt(handler.vibroRep.getText().toString()));
            }
        };

        handler.repeatCount.addTextChangedListener(handler.textWatcher);
        handler.repeatInterval.addTextChangedListener(handler.textWatcher);
        handler.vibroLen.addTextChangedListener(handler.textWatcher);
        handler.vibroInt.addTextChangedListener(handler.textWatcher);
        handler.vibroRep.addTextChangedListener(handler.textWatcher);

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                repeat.setVibro(handler.vibro.isChecked());
                repeat.setVibroUntilPressed(handler.vibroStop.isChecked());
            }
        };

        handler.vibro.setOnCheckedChangeListener(checkedChangeListener);
        handler.vibroStop.setOnCheckedChangeListener(checkedChangeListener);

        return row;
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
