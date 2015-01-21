/*
 * App passcode library for Android, master branch
 * Dual licensed under MIT, and GPL.
 * See https://github.com/wordpress-mobile/Android-PasscodeLock
 */
package com.seafile.seadroid2.gesturelock;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.seafile.seadroid2.SettingsManager;
import com.seafile.seadroid2.ui.activity.UnlockGesturePasswordActivity;

import java.util.Arrays;
import java.util.Date;

/**
 * Implementation of AppLock
 */
public class DefaultAppLock extends AbstractAppLock {
    public static final String DEBUG_TAG = "DefaultAppLock";

    private Application currentApp; //Keep a reference to the app that invoked the locker
    private SettingsManager settingsMgr;

    public DefaultAppLock(Application currentApp) {
        super();
        this.currentApp = currentApp;
        this.settingsMgr = SettingsManager.instance();
    }

    public void enable() {
        if (android.os.Build.VERSION.SDK_INT < 14)
            return;

        if (isPasswordLocked()) {
            currentApp.unregisterActivityLifecycleCallbacks(this);
            currentApp.registerActivityLifecycleCallbacks(this);
        }
    }

    @Override
    public void disable() {
        if (android.os.Build.VERSION.SDK_INT < 14)
            return;

        currentApp.unregisterActivityLifecycleCallbacks(this);
    }

    //Check if we need to show the lock screen at startup
    public boolean isPasswordLocked() {
        return SettingsManager.instance().isGestureLockEnabled();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(DEBUG_TAG, "onActivityPaused");

        if (activity.getClass() == UnlockGesturePasswordActivity.class)
            return;

        if ((this.appLockDisabledActivities != null) &&
                Arrays.asList(appLockDisabledActivities).contains(activity.getClass().getName()))
            return;

        settingsMgr.saveGestureLockTimeStamp();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(DEBUG_TAG, "onActivityResumed");

        if (activity.getClass() == UnlockGesturePasswordActivity.class)
            return;

        if ((this.appLockDisabledActivities != null) &&
                Arrays.asList(appLockDisabledActivities).contains(activity.getClass().getName()))
            return;

        if (mustShowUnlockSceen()) {
            Intent i = new Intent(activity.getApplicationContext(), UnlockGesturePasswordActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle extras = activity.getIntent().getExtras();
            if (extras != null) {
                i.putExtras(extras);
            }
            activity.getApplication().startActivity(i);
        }

    }

    private boolean mustShowUnlockSceen() {

        return settingsMgr.isGestureLockRequired();
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
