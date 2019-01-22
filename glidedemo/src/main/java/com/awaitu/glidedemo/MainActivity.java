package com.awaitu.glidedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.showINImage)
    ImageView imageView;
    @BindView(R.id.loadINImage)
    Button button1;
    @BindView(R.id.loadRSourceFile)
    Button button2;
    @BindView(R.id.testParseJsonData)
    TextView textView;
    @BindView(R.id.parseJsonData)
    Button button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.loadINImage, R.id.loadRSourceFile, R.id.parseJsonData})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.loadINImage:
                String url = "http://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg";
                Glide.with(this).load(url).into(imageView);
                break;
            case R.id.loadRSourceFile:
                int sourceid = R.mipmap.ic_launcher;
                Glide.with(this).load(sourceid).into(imageView);
                break;
            /*加载本地图片
            File file = new File(getExternalCacheDir() + "/image.jpg");
            Glide.with(this).load(file).into(imageView);
            */
            case R.id.parseJsonData:
                Gson gson = new Gson();
//                Person person = new Person();
//                person.setName("linux");
//                person.setAge(23);
//                String str = gson.toJson(person);
                String jsonData = "{'name':'刘力','age':19}";
                Person person = gson.fromJson(jsonData, Person.class);
                System.out.println(person.getName() + ", " + person.getAge());
                break;
        }
    }
}
