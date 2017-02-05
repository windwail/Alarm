package windwail.ru.alarm.entities;

import com.orm.SugarRecord;

public class AlarmItem extends SugarRecord {

    public String title = "";
    public Integer startHour = 0;
    public Integer startMinute = 0;
    public String file= "";

    public Integer year;
    public Integer month;
    public Integer day;

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

    public Integer volume1 = 10;
    public Integer volume2 = 10;
    public Integer volume3 = 10;
    public Integer volume4 = 10;

    private Boolean vibro1 = false;
    private Boolean vibro2 = false;
    private Boolean vibro3 = false;
    private Boolean vibro4 = false;

    private Integer vibroLenth1 = 1000;
    private Integer vibroLenth2 = 1000;;
    private Integer vibroLenth3 = 1000;;
    private Integer vibroLenth4 = 1000;;

    private Integer vibroInterval1 = 0;
    private Integer vibroInterval2 = 0;
    private Integer vibroInterval3 = 0;
    private Integer vibroInterval4 = 0;

    private Integer vibroRepeat1 = 0;
    private Integer vibroRepeat2 = 0;
    private Integer vibroRepeat3 = 0;
    private Integer vibroRepeat4 = 0;

    public Integer repeats = 0;

    public Boolean background = false;

    public Boolean notifications = false;

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

       public Boolean getBackground() {
        return background;
    }

    public void setBackground(Boolean background) {
        this.background = background;
    }

    public Integer getRepeats() {
        return repeats;
    }

    public void setRepeats(Integer repeats) {
        this.repeats = repeats;
    }

    public Integer getVolume1() {
        return volume1;
    }

    public void setVolume1(Integer volume1) {
        this.volume1 = volume1;
    }

    public Integer getVolume2() {
        return volume2;
    }

    public void setVolume2(Integer volume2) {
        this.volume2 = volume2;
    }

    public Integer getVolume3() {
        return volume3;
    }

    public void setVolume3(Integer volume3) {
        this.volume3 = volume3;
    }

    public Integer getVolume4() {
        return volume4;
    }

    public void setVolume4(Integer volume4) {
        this.volume4 = volume4;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    public Boolean getVibro1() {
        return vibro1;
    }

    public void setVibro1(Boolean vibro1) {
        this.vibro1 = vibro1;
    }

    public Boolean getVibro2() {
        return vibro2;
    }

    public void setVibro2(Boolean vibro2) {
        this.vibro2 = vibro2;
    }

    public Boolean getVibro3() {
        return vibro3;
    }

    public void setVibro3(Boolean vibro3) {
        this.vibro3 = vibro3;
    }

    public Boolean getVibro4() {
        return vibro4;
    }

    public void setVibro4(Boolean vibro4) {
        this.vibro4 = vibro4;
    }

    public Integer getVibroLenth1() {
        return vibroLenth1;
    }

    public void setVibroLenth1(Integer vibroLenth1) {
        this.vibroLenth1 = vibroLenth1;
    }

    public Integer getVibroLenth2() {
        return vibroLenth2;
    }

    public void setVibroLenth2(Integer vibroLenth2) {
        this.vibroLenth2 = vibroLenth2;
    }

    public Integer getVibroLenth3() {
        return vibroLenth3;
    }

    public void setVibroLenth3(Integer vibroLenth3) {
        this.vibroLenth3 = vibroLenth3;
    }

    public Integer getVibroLenth4() {
        return vibroLenth4;
    }

    public void setVibroLenth4(Integer vibroLenth4) {
        this.vibroLenth4 = vibroLenth4;
    }

    public Integer getVibroInterval1() {
        return vibroInterval1;
    }

    public void setVibroInterval1(Integer vibroInterval1) {
        this.vibroInterval1 = vibroInterval1;
    }

    public Integer getVibroInterval2() {
        return vibroInterval2;
    }

    public void setVibroInterval2(Integer vibroInterval2) {
        this.vibroInterval2 = vibroInterval2;
    }

    public Integer getVibroInterval3() {
        return vibroInterval3;
    }

    public void setVibroInterval3(Integer vibroInterval3) {
        this.vibroInterval3 = vibroInterval3;
    }

    public Integer getVibroInterval4() {
        return vibroInterval4;
    }

    public void setVibroInterval4(Integer vibroInterval4) {
        this.vibroInterval4 = vibroInterval4;
    }

    public Integer getVibroRepeat1() {
        return vibroRepeat1;
    }

    public void setVibroRepeat1(Integer vibroRepeat1) {
        this.vibroRepeat1 = vibroRepeat1;
    }

    public Integer getVibroRepeat2() {
        return vibroRepeat2;
    }

    public void setVibroRepeat2(Integer vibroRepeat2) {
        this.vibroRepeat2 = vibroRepeat2;
    }

    public Integer getVibroRepeat3() {
        return vibroRepeat3;
    }

    public void setVibroRepeat3(Integer vibroRepeat3) {
        this.vibroRepeat3 = vibroRepeat3;
    }

    public Integer getVibroRepeat4() {
        return vibroRepeat4;
    }

    public void setVibroRepeat4(Integer vibroRepeat4) {
        this.vibroRepeat4 = vibroRepeat4;
    }
}
