package com.example.ssl_pinning;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import okhttp3.*;
import android.widget.Toast;
import android.util.Log;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b_http = (Button)findViewById(R.id.rb_http);
        Button b_https = (Button)findViewById(R.id.rb_https);
        Button b_pinning = (Button)findViewById(R.id.rb_pinning);

        OkHttpClient client = new OkHttpClient();
        String s_http = "http://cris.joongbu.ac.kr/course/CCIT/";
        String s_https = "https://leemon.tistory.com";
        String s_pinning = "https://www.naver.com";

        b_http.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Request get_req = new Request.Builder()
                        .url(s_http)
                        .build();
                client.newCall(get_req).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("ccit_http", "Connect Server Error is"+e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("ccit_http", response.body().string());
                    }
                });
                Toast.makeText(getApplicationContext(), "Request sent to "+s_http, Toast.LENGTH_LONG).show();
            }
        });

        b_https.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RequestBody form = new FormBody.Builder()
                        .add("id", "ccit")
                        .add("pw", "pswd")
                        .build();
                Request post_req = new Request.Builder()
                        .url(s_https)
                        .post(form)
                        .build();
                client.newCall(post_req).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("ccit_https", "Connect Server Error is"+e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("ccit_https", response.body().string());
                    }
                });
                Toast.makeText(getApplicationContext(), "Request sent to "+s_https, Toast.LENGTH_LONG).show();
            }
        });

        b_pinning.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CertificatePinner cert = new CertificatePinner.Builder()
                        .add("www.naver.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=") //host, hash
                        .build();
                OkHttpClient ssl_client = new OkHttpClient.Builder().certificatePinner(cert).build();
                Request ssl_req = new Request.Builder()
                        .url(s_pinning)
                        .build();

                ssl_client.newCall(ssl_req).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("ccit_ssl", "Connect Server Error is"+e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("ccit_ssl", response.body().string());
                    }
                });
                Toast.makeText(getApplicationContext(), "Request Sent to"+s_pinning, Toast.LENGTH_LONG).show();
            }
        });
    }
}