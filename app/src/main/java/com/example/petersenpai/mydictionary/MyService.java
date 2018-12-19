package com.example.petersenpai.mydictionary;


import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MyService extends Service {

    private ClipboardManager myClipboard;
    private long m_last_clip_time = 0;

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (myClipboard == null) {
            myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            myClipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    // cancel redundant callback event
                    long now_time = System.currentTimeMillis();
                    if (now_time - m_last_clip_time < 500) {
                        m_last_clip_time = now_time;
                        return;
                    }

                    // get clipboard text
                    ClipData.Item item = myClipboard.getPrimaryClip().getItemAt(0);
                    if (item.getText() == null)
                        return;
                    m_last_clip_time = now_time;
                    String text = item.getText().toString();
                    // ignore non-word strings, words are consist of letters and space
                    if (!text.matches("^[a-zA-Z ]+$"))
                        return;
                    Log.d("system.out", text);

                    // TODO: translate word
                    new NetworkRequestThread(getApplicationContext(), text).start();
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Util.SaveHistoryWordsToFile(this);
        super.onDestroy();
    }
}