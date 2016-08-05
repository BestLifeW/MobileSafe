package com.lovec.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompleteRecevier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //检查SIM卡是否发生变化
        //1.获取保存的SIM卡号
        String sp_sim = sp.getString("sim", "");
        //2.再次获取本地SIM卡号
        TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //tel.getLine1Number();//获取SIM卡绑定的电话号码    line1:双卡双待.在中国不太适用,运营商一般不会将SIM卡和手机号码绑定
        String sim = tel.getSimSerialNumber();//获取SIM卡序列号,唯一标示
        //3.判断两个SIM卡号是否为空
        if (!TextUtils.isEmpty(sp_sim) && !TextUtils.isEmpty(sim)) {
            //4.判断两个SIM卡是否一致,如果一致就不发送报警短信,不一致发送报警短信
            if (!sp_sim.equals(sim)) {
                //发送报警短信
                //短信的管理者
                SmsManager smsManager = SmsManager.getDefault();
                //destinationAddress : 收件人
                //scAddress :　短信中心的地址　　一般null
                //text : 短信的内容
                //sentIntent : 是否发送成功
                //deliveryIntent : 短信的协议  一般null
                smsManager.sendTextMessage("5556", null, "da ge wo bei dao le,help me!!!!", null, null);
            }

        }
    }
}