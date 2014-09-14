package com.ggt.slidescast.utils;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Homemade UncaughtExceptionHandler sometimes used for debugging.
 *
 * @author guiguito
 */
public class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler mDefautHandler;

    public MyUncaughtExceptionHandler() {
        mDefautHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        GLog.e(getClass().getName(), ex.getMessage());
        mDefautHandler.uncaughtException(thread, ex);
    }
}
