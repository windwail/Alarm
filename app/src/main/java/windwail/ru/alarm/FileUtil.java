package windwail.ru.alarm;

import android.os.Environment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by icetsuk on 06.02.17.
 */

public class FileUtil {

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static void logTime() {
        DateTimeFormatter df = DateTimeFormat.shortDateTime();
        log(df.print(DateTime.now()));
    }

    public static void log(String message) {
        try {

            FileWriter logWriter = new FileWriter(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/AlarmLogs.txt", true);

            DateTimeFormatter df = DateTimeFormat.shortDateTime();

            BufferedWriter out = new BufferedWriter(logWriter);
            out.write(message.toString());
            out.newLine();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
