package windwail.ru.alarm;

import com.facebook.stetho.Stetho;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by icetsuk on 01.02.17.
 */

public class Application extends com.orm.SugarApp {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        JodaTimeAndroid.init(this);
    }
}
