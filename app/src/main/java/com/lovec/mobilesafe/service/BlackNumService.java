package com.lovec.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.lovec.mobilesafe.dao.BlackNumDao;

public class BlackNumService extends Service {

    private SmsReceiver smsReceiver;
    private BlackNumDao blackNumDao;
    private TelephonyManager telephonyManager;
    private MyPhoneStateListener myPhoneStateListener;

    public BlackNumService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        blackNumDao = new BlackNumDao(getApplicationContext());
        smsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(1001);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);


        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        //参数1:监听
        //参数2:监听的事件
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class SmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                //解析成SmsMessage
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsMessage.getMessageBody();//获取短信的内容
                String sender = smsMessage.getOriginatingAddress();//获取发件人
                //根据发件人号码,获取号码的拦截模式
                int mode = blackNumDao.queryBlackNumMode(sender);
                //判断是否是短信拦截或者是全部拦截
                if (mode == BlackNumDao.SMS || mode == BlackNumDao.ALL) {
                    abortBroadcast();//拦截广播事件,拦截短信操作
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, final String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //如果是响铃状态,检测拦截模式是否是电话拦截,是挂断
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                //获取拦截模式
                int mode = blackNumDao.queryBlackNumMode(incomingNumber);
                if (mode == BlackNumDao.CALL || mode == BlackNumDao.ALL) {
                    //挂断电话 1.5
                    endCall();
                    //删除通话记录
                    //1.获取内容解析者
                    final ContentResolver resolver = getContentResolver();
                    //2.获取内容提供者地址  call_log   calls表的地址:calls
                    //3.获取执行操作路径
                    final Uri uri = Uri.parse("content://call_log/calls");
                    //4.删除操作
                    //resolver.delete(uri, "number=?", new String[]{incomingNumber});
                    //通过内容观察者观察内容提供者内容,如果变化,就去执行删除操作
                    //notifyForDescendents : 匹配规则,true : 精确匹配   false:模糊匹配
                    resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {
                        //内容提供者内容变化的时候调用
                        @Override
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                            //删除通话记录
                            resolver.delete(uri, "number=?", new String[]{incomingNumber});
                            //注销内容观察者
                            resolver.unregisterContentObserver(this);
                        }
                    });
                }
            }
        }
    }

    private void endCall() {
    }
}
