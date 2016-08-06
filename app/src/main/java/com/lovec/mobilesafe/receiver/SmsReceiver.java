package com.lovec.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

/**
 * Created by lovec on 2016/8/6.
 */
public class SmsReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        Object[] objs = (Object[]) intent.getExtras().get("pdus");

        for (Object obj : objs) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String body = smsMessage.getMessageBody();
            String sender = smsMessage.getOriginatingAddress();
            if ("#*location*#".equals(body)) {
                //GPS追踪
                System.out.println("GPS追踪");
                //拦截短信
                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            } else if ("#*alarm*#".equals(body)) {
                //播放报警音乐
                System.out.println("播放报警音乐");
                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            } else if ("#*wipedata*#".equals(body)) {
                //远程删除数据
                System.out.println("远程删除数据");
                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            } else if ("#*lockscreen*#".equals(body)) {
                //远程锁屏
                System.out.println("远程锁屏");
                abortBroadcast();//拦截操作,原生android系统,国产深度定制系统中屏蔽,比如小米
            }

        }
    }


}
