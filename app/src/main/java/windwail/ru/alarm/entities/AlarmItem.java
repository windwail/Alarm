package windwail.ru.alarm.entities;

import com.orm.SugarRecord;

public class AlarmItem extends SugarRecord {

    public String title = "";
    public Integer startHour = 0;
    public Integer startMinute = 0;
    public String file= "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
