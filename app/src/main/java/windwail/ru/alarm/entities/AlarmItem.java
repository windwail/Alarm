package windwail.ru.alarm.entities;

import com.orm.SugarRecord;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlarmItem extends SugarRecord {

    private String title = "";

    private String info = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<RepeatData> getRepeats() {
        List<RepeatData> result = RepeatData.find(RepeatData.class, "alarm = ?", this.getId().toString());

        Collections.sort(result, new Comparator<RepeatData>() {
            @Override
            public int compare(RepeatData o1, RepeatData o2) {
                return (o1.getId().compareTo(o2.getId()));
            }
        });

        return result;
    }

}
