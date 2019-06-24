package com.menglingpeng.continuouslocationinbackground.Util;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.util.List;

public class Utils {

    private static String CLOSE_BRODECAST_INTENT_ACTION_NAME= "CloseService";

    public static Notification buildNotification(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentText("service");
        return builder.getNotification();
    }

    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {

        if (context.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            return implicitIntent;
        }

        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }


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
