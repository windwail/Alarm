package windwail.ru.alarm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import windwail.ru.alarm.entities.AlarmItem;

/**
 * Created by icetusk on 16.06.16.
 */
public class AlarmListAdapter extends ArrayAdapter<AlarmItem> {

    List<AlarmItem> alarms;

    Context context;

    public void updateAlarms() {
        if(alarms != null) {
            alarms.clear();
        }
        alarms = AlarmItem.listAll(AlarmItem.class);
        notifyDataSetChanged();
    }

    public AlarmListAdapter(Context context, int resource, List<AlarmItem> processes) {
        super(context, resource);
        this.alarms = processes;
        this.context = context;
    }

    @Override
    public AlarmItem getItem(int position) {
        return alarms.get(position);
    }

    public int getCount() {
        return alarms.size();
    }

    public static class DataHandler {
        TextView alarmName;
        TextView alarmInfo;
        AlarmItem alarm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;

        AlarmItem alarm = alarms.get(position);

        final DataHandler handler;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.alarm_item, parent, false);

            handler = new DataHandler();
            handler.alarmName = (TextView) row.findViewById(R.id.alarmName);
            handler.alarmInfo = (TextView) row.findViewById(R.id.alarmInfo);
            handler.alarm = alarm;

            Button delete = (Button) row.findViewById(R.id.deleteAlarm);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.alarm.delete();
                    AlarmListAdapter.this.updateAlarms();
                }
            });

            row.setTag(handler);
        } else {
            handler = (DataHandler) row.getTag();
        }

        handler.alarmName.setText(alarm.getTitle());
        handler.alarmInfo.setText(pad(alarm.getStartHour()) + ":" + pad(alarm.getStartMinute()));

        return row;
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
