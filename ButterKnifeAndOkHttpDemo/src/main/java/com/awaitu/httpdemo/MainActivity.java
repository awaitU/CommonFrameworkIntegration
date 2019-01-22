package com.awaitu.httpdemo;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private ImageView image;
    //pull解析xml数据
    private static String strUrl = "http://192.168.1.110:8080/person.xml";
    private static final String URL = "https://api.github.com/users/basil2style";
    HttpsURLConnection conn;
    private Handler testHandler = new Handler();
    private TextView testView;
    private static final String TAG = "MainActivity";
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "handleMessage: 获取网页数据成功" );
        }
    };
    @BindView( R.id.testButtrerKnife1)
    public Button button1;
    @BindView(R.id.testButtrerKnife2)
    public Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getHtmlData();
//        testView = (TextView)this.findViewById(R.id.showMessage);
        image = (ImageView)this.findViewById(R.id.showINPicture);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                testOkHttp();
                getAsynHttp();
            }
        }).start();
//        sendRequestWithHttpClient();
        ButterKnife.bind(this);
    }
    @OnClick({R.id.testButtrerKnife1,R.id.testButtrerKnife2})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.testButtrerKnife1:
                button1.setText("来了，老弟");
                break;
            case R.id.testButtrerKnife2:
                button2.setText("我来了");
                break;
        }
    }
//一. HttpsURLConnection
    private void getHtmlData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(strUrl);
                    conn = (HttpsURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(8000);
                    conn.setConnectTimeout(6000);
                    if(conn.getResponseCode() != 200){
                        throw new RuntimeException("运行异常！");
                    }
                    InputStream is = conn.getInputStream();
                    XmlPullParser xmlPullParser= Xml.newPullParser();
                    xmlPullParser.setInput(is,"UTF-8");
                    int type=xmlPullParser.getEventType();
                    while(type!=XmlPullParser.END_DOCUMENT) {
                        switch (type) {
                            case XmlPullParser.START_TAG:
                                //获取开始标签的名字
                                String starttgname = xmlPullParser.getName();
                                if ("person".equals(starttgname)) {
                                    //获取id的值
                                    String id = xmlPullParser.getAttributeValue(0);
                                    Log.i("test", id);
                                } else if ("name".equals(starttgname)) {
                                    String name = xmlPullParser.nextText();
                                    Log.i("test", name);
                                } else if ("age".equals(starttgname)) {
                                    String age = xmlPullParser.nextText();
                                    Log.i("test", age);
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                break;
                        }//细节：
                        type = xmlPullParser.next();
                    }
//                    String str = convertStreamToString(is);
//                    Log.e(TAG, "run: getHtmlData=="+str );
                }catch (Exception e){

                }
                conn.disconnect();
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }
    private String convertStreamToString(InputStream is){
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while( (line = bf.readLine()) != null){
                sb.append(line+"\n");
            }
        }catch (IOException e) {

        }finally {
            try {
                is.close();
            }catch (IOException e){

            }
        }
        return sb.toString();
    }
    //二. HttpClient方式
    private void sendRequestWithHttpClient() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                //用HttpClient发送请求，分为五步
                //第一步：创建HttpClient对象
                HttpClient httpCient = new DefaultHttpClient();
                //第二步：创建代表请求的对象,参数是访问的服务器地址
                HttpGet httpGet = new HttpGet("http://www.baidu.com");

                try {
                    //第三步：执行请求，获取服务器发还的相应对象
                    HttpResponse httpResponse = httpCient.execute(httpGet);
                    //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //第五步：从相应对象当中取出数据，放到entity当中
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串

                        //在子线程中将Message对象发出去
//                        Message message = new Message();
//                        message.what = SHOW_RESPONSE;
//                        message.obj = response.toString();
//                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();//这个start()方法不要忘记了

    }
    //三. OkHttp框架
    private void getAsynHttp() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://avatar.csdn.net/B/0/1/1_new_one_object.jpg")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                testHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        testView.setText("获取数据失败");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()){
                    Log.e(TAG, "onResponse: 1111111111111"+response.code() );
                    Log.e(TAG, "onResponse: 2222222222222"+response.body().toString() );
                    InputStream is = response.body().byteStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(is);
                    testHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            image.setImageBitmap(bitmap);
                        }
                    });
                    //将图片保存到本地存储卡中
//                    File file = new File(Environment.getExternalStorageDirectory(), "image.png");
//                    FileOutputStream fileOutputStream = new FileOutputStream(file);
//                    byte[] temp = new byte[128];
//                    int length;
//                    while ((length = is.read(temp)) != -1) {
//                        fileOutputStream.write(temp, 0, length);
//                    }
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//                    is.close();
                }
            }
        });
    }


}