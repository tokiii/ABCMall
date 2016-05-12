package com.cbn.abcmall.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.cbn.abcmall.activites.EmptyActivity;

public class MyService extends Service {

    private boolean alive;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        alive = true;

        new Thread(){
            @Override
            public void run() {
                super.run();

                while (alive) {
                    try {
                        sleep(1000);// 设置启动时间间隔
                        Intent i = new Intent(MyService.this, EmptyActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    @Override
    public void onDestroy() {
        startService(new Intent(this, MyService.class));
        super.onDestroy();
        alive = false;
    }
}
