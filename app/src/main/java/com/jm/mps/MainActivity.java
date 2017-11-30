package com.jm.mps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //进度时间格式化
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MusicPartSelectView mps_view = (MusicPartSelectView) findViewById(R.id.mps_view);
        textView = (TextView) findViewById(R.id.textView);
        mps_view.setData("", "", 10, new MusicPartSelectView.OnTimeChangeListener() {
            @Override
            public void onChange(int currentTime) {
                Log.i("MusicPartSelectView", "time=" + new Date(currentTime * 1000));
                textView.setText(simpleDateFormat.format(new Date(currentTime * 1000)) + "---" + simpleDateFormat.format(new Date((currentTime + 10) * 1000)));
            }

            @Override
            public void onEnd(int currentTime) {

            }
        });
    }
}
