package windwail.ru.alarm.entities;

import com.orm.SugarRecord;

public class AlarmItem extends SugarRecord {

    public String title = "";
    public Integer startHour = 0;
    public Integer startMinute = 0;
    public String file= "";

    public Integer repeatStartHour2 = 0;
    public Integer repeatStartHour3 = 0;
    public Integer repeatStartHour4 = 0;

    public Integer repeatStartMinute2 = 0;
    public Integer repeatStartMinute3 = 0;
    public Integer repeatStartMinute4 = 0;

    public Integer repeatCount1 = 0;
    public Integer repeatCount2 = 0;
    public Integer repeatCount3 = 0;
    public Integer repeatCount4 = 0;

    public Integer repeatInterval1 = 0;
    public Integer repeatInterval2 = 0;
    public Integer repeatInterval3 = 0;
    public Integer repeatInterval4 = 0;

    public Integer repeatPlayed1 = 0;
    public Integer repeatPlayed2 = 0;
    public Integer repeatPlayed3 = 0;
    public Integer repeatPlayed4 = 0;

    public Boolean background = false;

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

    public Integer getRepeatStartHour2() {
        return repeatStartHour2;
    }

    public void setRepeatStartHour2(Integer repeatStartHour2) {
        this.repeatStartHour2 = repeatStartHour2;
    }

    public Integer getRepeatStartHour3() {
        return repeatStartHour3;
    }

    public void setRepeatStartHour3(Integer repeatStartHour3) {
        this.repeatStartHour3 = repeatStartHour3;
    }

    public Integer getRepeatStartHour4() {
        return repeatStartHour4;
    }

    public void setRepeatStartHour4(Integer repeatStartHour4) {
        this.repeatStartHour4 = repeatStartHour4;
    }

    public Integer getRepeatStartMinute2() {
        return repeatStartMinute2;
    }

    public void setRepeatStartMinute2(Integer repeatStartMinute2) {
        this.repeatStartMinute2 = repeatStartMinute2;
    }

    public Integer getRepeatStartMinute3() {
        return repeatStartMinute3;
    }

    public void setRepeatStartMinute3(Integer repeatStartMinute3) {
        this.repeatStartMinute3 = repeatStartMinute3;
    }

    public Integer getRepeatStartMinute4() {
        return repeatStartMinute4;
    }

    public void setRepeatStartMinute4(Integer repeatStartMinute4) {
        this.repeatStartMinute4 = repeatStartMinute4;
    }

    public Integer getRepeatCount1() {
        return repeatCount1;
    }

    public void setRepeatCount1(Integer repeatCount1) {
        this.repeatCount1 = repeatCount1;
    }

    public Integer getRepeatCount2() {
        return repeatCount2;
    }

    public void setRepeatCount2(Integer repeatCount2) {
        this.repeatCount2 = repeatCount2;
    }

    public Integer getRepeatCount3() {
        return repeatCount3;
    }

    public void setRepeatCount3(Integer repeatCount3) {
        this.repeatCount3 = repeatCount3;
    }

    public Integer getRepeatCount4() {
        return repeatCount4;
    }

    public void setRepeatCount4(Integer repeatCount4) {
        this.repeatCount4 = repeatCount4;
    }

    public Integer getRepeatInterval1() {
        return repeatInterval1;
    }

    public void setRepeatInterval1(Integer repeatInterval1) {
        this.repeatInterval1 = repeatInterval1;
    }

    public Integer getRepeatInterval2() {
        return repeatInterval2;
    }

    public void setRepeatInterval2(Integer repeatInterval2) {
        this.repeatInterval2 = repeatInterval2;
    }

    public Integer getRepeatInterval3() {
        return repeatInterval3;
    }

    public void setRepeatInterval3(Integer repeatInterval3) {
        this.repeatInterval3 = repeatInterval3;
    }

    public Integer getRepeatInterval4() {
        return repeatInterval4;
    }

    public void setRepeatInterval4(Integer repeatInterval4) {
        this.repeatInterval4 = repeatInterval4;
    }

    public Integer getRepeatPlayed1() {
        return repeatPlayed1;
    }

    public void setRepeatPlayed1(Integer repeatPlayed1) {
        this.repeatPlayed1 = repeatPlayed1;
    }

    public Integer getRepeatPlayed2() {
        return repeatPlayed2;
    }

    public void setRepeatPlayed2(Integer repeatPlayed2) {
        this.repeatPlayed2 = repeatPlayed2;
    }

    public Integer getRepeatPlayed4() {
        return repeatPlayed4;
    }

    public void setRepeatPlayed4(Integer repeatPlayed4) {
        this.repeatPlayed4 = repeatPlayed4;
    }

    public Integer getRepeatPlayed3() {
        return repeatPlayed3;
    }

    public void setRepeatPlayed3(Integer repeatPlayed3) {
        this.repeatPlayed3 = repeatPlayed3;
    }

    public Boolean getBackground() {
        return background;
    }

    public void setBackground(Boolean background) {
        this.background = background;
    }
}
