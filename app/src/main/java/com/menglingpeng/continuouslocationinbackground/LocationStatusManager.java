package com.menglingpeng.continuouslocationinbackground;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class LocationStatusManager {

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

    /**
     * 由于仅仅处理只有wifi连接的情况下，如果用户手机网络可连接，那么忽略。
     * 定位成功时，重置为定位成功的状态
     *
     * @param isScreenOn 当前屏幕是否为点亮状态
     * @param isMobileable 是否有手机信号
     */
    public void onLocationSuccess(Context context, boolean isScreenOn, boolean isMobileable) {
        if (isMobileable) {
            return;
        }

        mPriorSuccLocated = true;
        if (isScreenOn) {
            mPirorLocatableOnScreen = true;
            saveStateInner(context, true);
        }
    }

    /**
     * reset到默认状态
     *
     * @param context
     */
    public void resetToInit(Context context) {
        this.mPirorLocatableOnScreen = false;
        this.mPriorSuccLocated = false;
        saveStateInner(context, false);
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
