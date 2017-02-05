package windwail.ru.alarm.entities;

import com.orm.SugarRecord;

/**
 * Created by icetusk on 05.02.17.
 */

public class AlarmConfig extends SugarRecord {

    public static AlarmConfig getInstance() {
        AlarmConfig config = AlarmConfig.findById(AlarmConfig.class, 1);

        if(config == null) {
            config = new AlarmConfig();
            config.setId(1l);
            config.save();
        }

        return config;
    }

    String preffFoldeer = "";

    Boolean rulesAccepted = false;

    public Boolean getRulesAccepted() {
        return rulesAccepted;
    }

    public void setRulesAccepted(Boolean rulesAccepted) {
        this.rulesAccepted = rulesAccepted;
    }

    public String getPreffFoldeer() {
        return preffFoldeer;
    }

    public void setPreffFoldeer(String preffFoldeer) {
        this.preffFoldeer = preffFoldeer;
    }
}
