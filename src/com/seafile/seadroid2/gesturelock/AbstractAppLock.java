/*
 * App passcode library for Android, master branch
 * Dual licensed under MIT, and GPL.
 * See https://github.com/wordpress-mobile/Android-PasscodeLock
 */
package com.seafile.seadroid2.gesturelock;

import android.app.Application;

/**
 * Abstract AppLock
 */
public abstract class AbstractAppLock implements Application.ActivityLifecycleCallbacks {

    protected String[] appLockDisabledActivities = new String[0];

    /*
     * There are situations where we don't want call the AppLock on activities (sharing items to out app for example).
     */
    public void setDisabledActivities( String[] disabledActs ) {
        this.appLockDisabledActivities = disabledActs;
    }

    public abstract void enable();
    public abstract void disable();
}
