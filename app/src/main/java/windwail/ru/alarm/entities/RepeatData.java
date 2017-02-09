package windwail.ru.alarm.entities;

import com.orm.SugarRecord;

/**
 * Created by icetsuk on 09.02.17.
 */

public class RepeatData extends SugarRecord {

    private Integer startHour = 0;
    private Integer startMinute = 0;
    private String file= "";

    private String next;

    private Integer year;
    private Integer month;
    private Integer day;

    private Integer repeatCount = 0;
    private Integer repeatInterval = 0;
    private Integer volume = 10;
    private Boolean vibro = false;
    private Integer vibroLenth = 1000;
    private Integer vibroInterval = 0;
    private Integer vibroRepeat = 0;
    private Boolean vibroUntilPressed = false;

    private Integer repeats = 0;

    private Boolean notifications = false;

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

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    public Integer getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(Integer repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Boolean getVibro() {
        return vibro;
    }

    public void setVibro(Boolean vibro) {
        this.vibro = vibro;
    }

    public Integer getVibroLenth() {
        return vibroLenth;
    }

    public void setVibroLenth(Integer vibroLenth) {
        this.vibroLenth = vibroLenth;
    }

    public Integer getVibroInterval() {
        return vibroInterval;
    }

    public void setVibroInterval(Integer vibroInterval) {
        this.vibroInterval = vibroInterval;
    }

    public Integer getVibroRepeat() {
        return vibroRepeat;
    }

    public void setVibroRepeat(Integer vibroRepeat) {
        this.vibroRepeat = vibroRepeat;
    }

    public Boolean getVibroUntilPressed() {
        return vibroUntilPressed;
    }

    public void setVibroUntilPressed(Boolean vibroUntilPressed) {
        this.vibroUntilPressed = vibroUntilPressed;
    }

    public Integer getRepeats() {
        return repeats;
    }

    public void setRepeats(Integer repeats) {
        this.repeats = repeats;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }
}
