package com.menglingpeng.continuouslocationinbackground.service;

import android.content.Intent;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.menglingpeng.continuouslocationinbackground.Util.LocationComputeUtil;

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

            //有时候会返回(0，0)点需要排除
            if (aMapLocation.getLatitude() == 0f || aMapLocation.getLongitude() <= 0.001f) {
                return;
            }
            //计算跟上一个点的距离，当距离不变时或者变化太小时视为同一点，不在inter新的点，而是修改当前点的endTime以及duration。
            double itemDistance = LocationComputeUtil.getDistance(aMapLocation, lastSaveLocation);
            if (lastSaveLocation == null && aMapLocation.getLatitude() > 0f) {
                //record的第一个埋点，插入数据库
                lastSaveLocation = aMapLocation;
            } else if (itemDistance > 1.0f) {
                resetIntervalTimes(0);//新的点
                lastSaveLocation = aMapLocation;
            } else {//可能在原地打点，不存入新数据，update endTime。
                long timestamp = lastSaveLocation.getTime();
                long endTime = System.currentTimeMillis();//todo 需要考虑定位时间跟系统时间的差值。
                long duration = endTime - timestamp;
                resetIntervalTimes(duration);
            }


            //发送结果的通知
            sendLocationBroadcast(aMapLocation);

            if (!mIsWifiCloseable) {
                return;
            }


        }
        };

    //LocationService

    private long intervalTime = LocationConstants.DEFAULT_INTERVAL_TIME;
    private void resetIntervalTimes(long duration) {
        if (duration >= 60 * 60 * 1000){// 90分钟停止自己的服务, 应该还要关闭守护进程
            onDestroy();
            return;
        }
        int intervalTimes = LocationComputeUtil.computeIntervalTimes(duration);
        intervalTime = intervalTimes * LocationConstants.DEFAULT_INTERVAL_TIME;
        mLocationOption.setInterval(intervalTime);
        mLocationClient.setLocationOption(mLocationOption);
    }


    public static int computeIntervalTimes(long duration) {
        long timeMin = 60 * 1000;
        if (duration > timeMin) {
            return 2;
        } else if (duration > 4 * timeMin) {
            return 3;
        } else if (duration > 10 * timeMin) {
            return 5;
        }
        return 1;
    }

}
