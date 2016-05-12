package com.cbn.abcmall.utils;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * 调试信息到SD卡
 * Created by Administrator on 2015/10/19.
 */
public class DebugUtil {

    public static void put(String s,String name)
    {
        try
        {
            FileOutputStream outStream = new FileOutputStream("/sdcard/"+name+".txt",true);
            OutputStreamWriter writer = new OutputStreamWriter(outStream,"utf-8");
            writer.write(s);
            writer.write("/n");
            writer.flush();
            writer.close();//记得关闭

            outStream.close();
        }
        catch (Exception e)
        {
            Log.e("m", "file write error");
        }
    }
}
