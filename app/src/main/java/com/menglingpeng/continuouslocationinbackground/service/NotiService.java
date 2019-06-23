package com.menglingpeng.continuouslocationinbackground.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.menglingpeng.continuouslocationinbackground.Util.Utils;

public class NotiService extends Service {

    /**i
     * startForeground的 noti_id
     */
    private static int NOTI_ID = 321;

    private Utils.CloseServiceReceiver closeReceiver;
    public Binder binder;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        closeReceiver = new Utils.CloseServiceReceiver(this);
        registerReceiver(closeReceiver, Utils.getCloseServiceFilter());
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        if (closeReceiver != null) {
            unregisterReceiver(closeReceiver);
            closeReceiver = null;
        }

        super.onDestroy();
    }

    public void unApplyNotiKeepMech() {
        stopForeground(true);
    }



    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new LocationServiceBinder();
        }
        return binder;
    }
}
