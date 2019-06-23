package com.menglingpeng.continuouslocationinbackground.service;

import android.content.Intent;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LocationService extends NotiService {

    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationOption;
    /**
     * 记录是否需要对息屏关掉wifi的情况进行处理
     */
    private boolean mIsWifiCloseable = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        startLocation();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        unApplyNotiKeepMech();
        stopLocation();

        super.onDestroy();
    }

    /**
     * 启动定位
     */
    void startLocation() {
        stopLocation();

        if (null == locationClient) {
            locationClient = new AMapLocationClient(this.getApplicationContext());
        }

        locationOption = new AMapLocationClientOption();
        // 使用连续
        locationOption.setOnceLocation(false);
        locationOption.setLocationCacheEnable(false);
        // 每10秒定位一次
        locationOption.setInterval(10 * 1000);
        // 地址信息
        locationOption.setNeedAddress(true);
        locationClient.setLocationOption(locationOption);
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }

    /**
     * 停止定位
     */
    void stopLocation() {
        if (null != locationClient) {
            locationClient.stopLocation();
        }
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //发送结果的通知
            sendLocationBroadcast(aMapLocation);

            if (!mIsWifiCloseable) {
                return;
            }

        }
        };
}
