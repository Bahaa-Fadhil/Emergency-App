package hsn.inf333finalproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AlarmService extends Service {
    MediaPlayer alarm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        alarm = MediaPlayer.create(this, R.raw.alarm);
        alarm.setLooping(true);
        alarm.setVolume(100, 100);
    }

    public int onStartCommand(Intent i, int flags, int startId) {
        alarm.start();

        return Service.START_STICKY;
    }

    public void onDestroy() {
        alarm.stop();
        alarm.release();
    }
}
