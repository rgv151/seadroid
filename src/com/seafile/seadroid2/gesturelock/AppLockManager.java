/*
 * App passcode library for Android, master branch
 * Dual licensed under MIT, and GPL.
 * See https://github.com/wordpress-mobile/Android-PasscodeLock
 */
package com.seafile.seadroid2.gesturelock;

import android.app.Application;

/**
 * AppLock Manager
 */
public class AppLockManager {

    private static AppLockManager instance;
    private AbstractAppLock currentAppLocker;

    public static AppLockManager getInstance() {
        if (instance == null) {
            instance = new AppLockManager();
        }
        return instance;
    }

    /**
     * Default App lock is available on Android-v14 or higher.
     *
     * @return True if the Passcode Lock feature is available on the device
     */
    public boolean isAppLockFeatureEnabled() {
        if (currentAppLocker == null)
            return false;
        if (currentAppLocker instanceof DefaultAppLock)
            return (android.os.Build.VERSION.SDK_INT >= 14);
        else
            return true;
    }

    public AbstractAppLock getCurrentAppLock() {
        return currentAppLocker;
    }

    public void setCurrentAppLock(AbstractAppLock newAppLocker) {
        if (currentAppLocker != null) {
            currentAppLocker.disable(); //disable the old applocker if available
        }
        currentAppLocker = newAppLocker;
    }

    public void enableDefaultAppLockIfAvailable(Application currentApp) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            currentAppLocker = new DefaultAppLock(currentApp);
            currentAppLocker.enable();
        }
    }
}
