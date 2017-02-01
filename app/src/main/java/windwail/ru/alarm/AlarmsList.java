package windwail.ru.alarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlarmsList extends AppCompatActivity {

    ArrayList<String> list;
    HashMap<String,String> map;
    SimpleAdapter adapter;
    ListView lv;
    String[] from;
    int[] to;
    List<HashMap<String, String>> fillMaps;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms_list);
        lv=(ListView) findViewById(R.id.alarms);
        list=new ArrayList<String>(); //list of product id
        list.add("pr100");
        list.add("pr10");
        list.add("pr1000");
        list.add("px100");

        map=new HashMap<String,String>();
        map.put("pr100","300");
        map.put("pr10","30");
        map.put("pr1000","400");
        map.put("px100","230");


        // create the grid item mapping
        String[] from = new String[] {"alarmName", "alarmInfo"};
        int[] to = new int[] { R.id.alarmName, R.id.alarmInfo };

        // prepare the list of all records
        fillMaps = new ArrayList<HashMap<String, String>>();
        for(int i = 1; i <= list.size(); i++){
            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("alarmName", "Alarm no " + i);
            map1.put("alarmInfo", "Some numeric info!");
            fillMaps.add(map1);
        }

        // fill in the grid_item layout
        adapter = new SimpleAdapter(this, fillMaps, R.layout.alarm_item, from, to);
        lv.setAdapter(adapter);
    }


    public void deleteRow(View v){
        RelativeLayout llMain = (RelativeLayout)v.getParent();
        TextView row=(TextView)llMain.getChildAt(0);
        String row_no=row.getText().toString();
        int row_no_int = Integer.parseInt(row_no);//get row number of deleted item from list
        list.remove(row_no_int-1);
        fillMaps.remove(row_no_int-1);
        adapter.notifyDataSetChanged();

    }

    public void OnAdding(View v){
        list.add("pz11");//adding new product id into list
        map.put("pz11","30");//putting price of product id
        int size=list.size();
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("rowid", "" + size);
        map1.put("col_1",list.get(size-1));
        map1.put("col_2", map.get(list.get(size-1)));
        map1.put("col_3", "X");
        fillMaps.add(map1);
        adapter.notifyDataSetChanged();//refreshing adapter
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
