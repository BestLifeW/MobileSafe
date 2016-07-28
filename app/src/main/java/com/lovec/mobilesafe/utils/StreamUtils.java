package com.lovec.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by lovec on 2016/7/28.
 */
public class StreamUtils {
    /*
    * 把流信息转换成字符串返回
    * @return
    * */
    public static String parserStreamUtils(InputStream in) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));

        StringWriter sw = new StringWriter();

        String str = null;
        while ((str = bf.readLine()) != null) {
            sw.write(str);
        }
        sw.close();
        bf.close();

        return sw.toString();
    }
}
