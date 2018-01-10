package com.roobo.priority;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * 8009录音需要添加权限
 */
public class PriorityManager {

    private static final String TAG = "PriorityManager";
    public static final String SERVICE_NAME = "ROOBO_PRIORITY";

    private PriorityServiceHelper mHelper;
    private static final int MSG_TINYMIX = 2;
    private Handler mH;

    private static PriorityManager instance = null;

    private PriorityManager() {
        mHelper = new PriorityServiceHelper();
    }

    public synchronized static PriorityManager getInstance() {
        if (instance == null) {
            instance = new PriorityManager();
        }
        return instance;
    }


    public void start() {
        HandlerThread thread = new HandlerThread("PRIORITY");
        thread.start();
        mH = new Handler(thread.getLooper()) {
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what) {
                    case MSG_TINYMIX: {
                        IPriority service = mHelper.getService();
                        if (null == service) {
                            mH.removeMessages(MSG_TINYMIX);
                            mH.sendEmptyMessageDelayed(MSG_TINYMIX, 10000);
                            break;
                        } else {
                            try {
                                String cmd = "selfmix";
                                android.util.Log.d(TAG, "[MSG_TINYMIX]: " + cmd);
                                service.exec(cmd);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            mH.removeMessages(MSG_TINYMIX);
                            mH.sendEmptyMessageDelayed(MSG_TINYMIX, 10000);
                        }
                        break;
                    }
                }
            }
        };

        mH.sendEmptyMessageDelayed(MSG_TINYMIX, 1000);
    }

    class PriorityServiceHelper implements IBinder.DeathRecipient {
        private Class classSM;

        private Method methodGetService;

        private IPriority service;

        synchronized IPriority getService() {
            if (null != service && service.asBinder().pingBinder()) {
                return service;
            }
            service = null;
            try {
                if (null == classSM) {
                    classSM = Class.forName("android.os.ServiceManager");
                }
                if (null == methodGetService) {
                    methodGetService = classSM.getDeclaredMethod("getService", String.class);
                }
                IBinder binder = (IBinder) methodGetService.invoke(classSM, PriorityManager.SERVICE_NAME);
                service = IPriority.Stub.asInterface(binder);
                Log.d("ServiceManagerHelper", "service=" + service);
                if (null != service) {
                    service.asBinder().linkToDeath(PriorityServiceHelper.this, 0);
                    return service;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void binderDied() {
            mH.removeMessages(MSG_TINYMIX);
        }
    }

}
