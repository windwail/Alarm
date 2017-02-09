package windwail.ru.alarm.entities;

import com.orm.SugarRecord;

import java.util.List;

public class AlarmItem extends SugarRecord {

    private String title;

    private List<RepeatData> repeats;

    private RepeatData current;

    private RepeatData main;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RepeatData> getRepeats() {
        return repeats;
    }

    public void setRepeats(List<RepeatData> repeats) {
        this.repeats = repeats;
    }

    public RepeatData getCurrent() {
        return current;
    }

    public void setCurrent(RepeatData current) {
        this.current = current;
    }

    public RepeatData getMain() {
        return main;
    }

    public void setMain(RepeatData main) {
        this.main = main;
    }
}
