package com.lovec.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.lovec.mobilesafe.R;
import com.lovec.mobilesafe.service.GPSService;

/**
 * Created by lovec on 2016/8/6.
 */
public class SmsReceiver extends BroadcastReceiver {

    Context context;
    private static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, Admin.class);
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
                Intent intent_gps = new Intent(context, GPSService.class);
                context.startService(intent_gps);
                abortBroadcast();
            } else if ("#*alarm*#".equals(body)) {
                //播放报警音乐
                playAlarm(context);
                abortBroadcast();
            } else if ("#*wipedata*#".equals(body)) {
                //远程删除数据
                System.out.println("远程删除数据");

                if (devicePolicyManager.isAdminActive(componentName)) {
                    devicePolicyManager.wipeData(0);
                }
                abortBroadcast();
            } else if ("#*lockscreen*#".equals(body)) {
                //远程锁屏
                System.out.println("远程锁屏");

                if (devicePolicyManager.isAdminActive(componentName)) {
                    devicePolicyManager.lockNow();
                }

                abortBroadcast();
            }

        }
    }

    /*
    * 播放报警音乐
    * */
    private void playAlarm(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //设置音量大小
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        if (mediaPlayer != null) {
            mediaPlayer.release();//释放资源
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
        mediaPlayer.start();
    }
}
