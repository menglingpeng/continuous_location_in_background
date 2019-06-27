package com.menglingpeng.continuouslocationinbackground.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.menglingpeng.continuouslocationinbackground.Util.Utils;

public class LocationHelperService extends Service {

    private Utils.CloseServiceReceiver closeReceiver;
    private ServiceConnection serviceConnection;

    //上一次的定位是否成功
    private boolean mPriorSuccLocated = false;

    //屏幕亮时可以定位
    private boolean mPirorLocatableOnScreen = false;

    //是否存在屏幕亮而且可以定位的情况的key
    private String IS_LOCABLE_KEY = "is_locable_key";

    //IS_LOCABLE_KEY 的过期时间
    private String LOCALBLE_KEY_EXPIRE_TIME_KEY = "localble_key_expire_time_key";

    //过期时间为10分钟
    private static final long MINIMAL_EXPIRE_TIME = 30 * 60 * 1000;

    private static final String PREFER_NAME = LocationStatusManager.class.getSimpleName();

    private static final long DEF_PRIOR_TIME_VAL = -1;


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

    /**
     * 如果isLocable，则存入正确的过期时间，否则存默认值
     *
     * @param context
     * @param isLocable
     */
    public void saveStateInner(Context context, boolean isLocable) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOCABLE_KEY, isLocable);
        editor.putLong(LOCALBLE_KEY_EXPIRE_TIME_KEY, isLocable ? System.currentTimeMillis() : DEF_PRIOR_TIME_VAL);
        editor.commit();
    }


    /**
     * 从preference读取，判断是否存在网络状况ok，而且亮屏情况下，可以定位的情况
     */
    public boolean isLocableOnScreenOn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        boolean res = sharedPreferences.getBoolean(IS_LOCABLE_KEY, false);
        long priorTime = sharedPreferences.getLong(LOCALBLE_KEY_EXPIRE_TIME_KEY, DEF_PRIOR_TIME_VAL);
        if (System.currentTimeMillis() - priorTime > MINIMAL_EXPIRE_TIME) {
            saveStateInner(context, false);
            return false;
        }

        return res;

    }
}
