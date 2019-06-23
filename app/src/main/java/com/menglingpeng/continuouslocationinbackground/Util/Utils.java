package com.menglingpeng.continuouslocationinbackground.Util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class Utils {

    private static String CLOSE_BRODECAST_INTENT_ACTION_NAME= "CloseService";

    public static class CloseServiceReceiver extends BroadcastReceiver {

        Service service;

        public CloseServiceReceiver(Service service) {
            this.service = service;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (service == null) {
                return;
            }
            service.onDestroy();
        }
    }


    public static IntentFilter getCloseServiceFilter() {
        return new IntentFilter(CLOSE_BRODECAST_INTENT_ACTION_NAME);
    }
}
