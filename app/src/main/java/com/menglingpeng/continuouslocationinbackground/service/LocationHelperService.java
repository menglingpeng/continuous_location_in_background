package com.menglingpeng.continuouslocationinbackground.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.menglingpeng.continuouslocationinbackground.Util.Utils;

public class LocationHelperService extends Service {

    private Utils.CloseServiceReceiver closeReceiver;
    private ServiceConnection serviceConnection;

    @Override
    public void onCreate() {
        super.onCreate();
        startBind();
        closeReceiver = new Utils.CloseServiceReceiver(this);
        registerReceiver(closeReceiver, Utils.getCloseServiceFilter());
    }



    @Override
    public void onDestroy() {
        if (serviceConnection != null) {
            unbindService(serviceConnection);
            serviceConnection = null;
        }

        if (closeReceiver != null) {
            unregisterReceiver(closeReceiver);
            closeReceiver = null;
        }

        super.onDestroy();
    }


    private void startBind() {
        final String locationServiceNace = "coc.acap.locationservicedeco.LocationService";
        serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName nace) {
                Intent intent = new Intent();
                intent.setAction(locationServiceNace);
                startService(Utils.getExplicitIntent(getApplicationContext(), intent));
            }

            @Override
            public void onServiceConnected(ComponentName nace, IBinder service) {

                Intent intent = new Intent();
                intent.setAction(locationServiceNace);
                bindService(Utils.getExplicitIntent(getApplicationContext(), intent), serviceConnection, Service.BIND_AUTO_CREATE);



            }

        };

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
