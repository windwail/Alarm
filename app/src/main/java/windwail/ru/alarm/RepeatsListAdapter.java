package windwail.ru.alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import windwail.ru.alarm.entities.RepeatData;

/**
 * Created by icetsuk on 09.02.17.
 */

public class RepeatsListAdapter extends ArrayAdapter<RepeatData> {

    private List<RepeatData> repeats;

    Context context;

    public void add(RepeatData ai) {
        for(int i=0; i<repeats.size(); i++) {
            if(ai.getId()==repeats.get(i).getId()) {
                repeats.set(i, ai);
                return;
            }
        }
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
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;

        final RepeatData repeat = repeats.get(position);

        DataHandler handler = null;
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

            row.setTag(handler);
        } else {
            handler = (DataHandler) row.getTag();
        }

        handler.nextTime.setText(repeat.getNext());
        handler.volume.setProgress(repeat.getVolume());
        handler.repeatCount.setText(""+repeat.getRepeatCount());
        handler.repeatInterval.setText(""+repeat.getRepeatInterval());
        handler.vibroLen.setText(""+repeat.getVibroLenth());
        handler.vibroInt.setText(""+repeat.getVibroInterval());
        handler.vibroRep.setText(""+repeat.getRepeatCount());
        handler.vibro.setChecked(repeat.getVibro());
        handler.vibroStop.setChecked(repeat.getVibroUntilPressed());

        return row;
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
