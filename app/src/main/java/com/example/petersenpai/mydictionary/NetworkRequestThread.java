package com.example.petersenpai.mydictionary;

import android.app.Notification;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;
//import com.example.rimble.pdict.NotificationHelper;

public class NetworkRequestThread extends Thread {
    private String text;
    private Context context;

    NetworkRequestThread(Context context, String text) {
        this.context = context;
        this.text = text;
    }

    public void run() {
        String trans_result = Util.get_response(text);

        // TODO: show translation result
        Log.d("system.out", trans_result);
//        Toast.makeText(context, text + " : " + trans_result,
//                Toast.LENGTH_SHORT).show();
        NotificationHelper temp = MainActivity.getHelper();
        Notification.Builder builder = temp.getmyChannel(text,trans_result);
        temp.getManager().notify(new Random().nextInt(), builder.build());


        // update history word list
        Util.addHistoryWord(new Word(text, trans_result));
    }
}