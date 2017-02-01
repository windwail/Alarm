package windwail.ru.alarm;

import com.facebook.stetho.Stetho;

/**
 * Created by icetsuk on 01.02.17.
 */

public class Application extends com.orm.SugarApp {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
