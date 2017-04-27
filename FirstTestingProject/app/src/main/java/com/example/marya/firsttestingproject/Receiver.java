package com.example.marya.firsttestingproject;

/**
 * Created by marya on 23.4.17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class Receiver extends BroadcastReceiver {
    private static final String tag = "MyTags";
    private static ProgrammList activity;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            callForRemove(intent.getData().getSchemeSpecificPart());
        }
        else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            callForInsert(intent.getData().getSchemeSpecificPart());
        }
    }

    public void setMainActivityHandler(ProgrammList activity) {
        Receiver.activity = activity;
    }

    private void callForInsert(String packageName) {
        if (activity != null) {
            activity.callInserting(packageName);
        }
    }

    private void callForRemove(String packageName) {
        if (activity != null) {
            activity.callRemoving(packageName);
        }
    }

}