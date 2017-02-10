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

import java.util.Iterator;
import java.util.List;

import windwail.ru.alarm.entities.AlarmItem;

/**
 * Created by icetusk on 16.06.16.
 */
public class AlarmListAdapter extends ArrayAdapter<AlarmItem> {

    private List<AlarmItem> alarms;

    Context context;


    public void updateAll() {
        alarms = AlarmItem.listAll(AlarmItem.class);
        notifyDataSetInvalidated();
    }

    public AlarmItem get(int pos) {
        return alarms.get(pos);
    }

    public void add(AlarmItem ai) {
        for(int i=0; i<alarms.size(); i++) {
            if(ai.getId()==alarms.get(i).getId()) {
                alarms.set(i, ai);
                return;
            }
        }
        alarms.add(ai);
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
        Button delete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;

        final AlarmItem alarm = alarms.get(position);

        final DataHandler handler;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.alarm_item, parent, false);

            handler = new DataHandler();
            handler.alarmName = (TextView) row.findViewById(R.id.alarmName);
            handler.alarmInfo = (TextView) row.findViewById(R.id.alarmInfo);
            handler.delete = (Button) row.findViewById(R.id.deleteAlarm);

            row.setTag(handler);
        } else {
            handler = (DataHandler) row.getTag();
        }

        handler.alarmName.setText(alarm.getTitle());
        handler.alarmInfo.setText("id:"+alarm.getId()+"   "+alarm.getInfo());
        handler.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator<AlarmItem> aiter = alarms.iterator();
                Log.e("TRY DELETE", ""+alarm.getId());

                while(aiter.hasNext()) {
                    AlarmItem item = aiter.next();
                    if(alarm.getId() == item.getId()) {
                        aiter.remove();
                        alarm.delete();
                    }
                }
                AlarmListAdapter.this.notifyDataSetInvalidated();
            }
        });

        return row;
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
