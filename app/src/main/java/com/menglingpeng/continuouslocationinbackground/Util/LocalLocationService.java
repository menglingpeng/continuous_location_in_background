package com.menglingpeng.continuouslocationinbackground.Util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;

public class LocalLocationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getLatitude() == 0f || aMapLocation.getLongitude() <= 0.001f) {
            return;
        }
        //计算当前定位点跟上一保存点的距离为itemDistance，当lastSaveLocation为null时itemDistance为0.
        double itemDistance = LocationComputeUtil.getDistance(aMapLocation, lastSaveLocation);
        if (lastSaveLocation == null && aMapLocation.getLatitude() > 0f) {

            //record的第一个埋点，插入数据库
        }else {//可能在原地打点，不存入新数据，update endTime。
            long timestamp = lastSaveLocation.getTime();
            long endTime = System.currentTimeMillis();//todo 需要考虑定位时间跟系统时间的差值。
            long duration = endTime - timestamp;
            //更新LastSaveLocation的 endTime，duration值
        }


    }



}
