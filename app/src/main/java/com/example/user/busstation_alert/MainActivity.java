package com.example.user.busstation_alert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView lstPrefer;

    String[] BusStation={"工學院","循環站","火車站"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //取得介面元件
        lstPrefer=(ListView)findViewById(R.id.ListView);
        //建立 ArrayAdapter
        ArrayAdapter<String> adapterBusStation=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,BusStation);
        //設定 ListView 的資料來源
        lstPrefer.setAdapter(adapterBusStation);

        lstPrefer.setOnItemClickListener(lstListener);

        //檢查權限
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

    }

    private ListView.OnItemClickListener lstListener=new ListView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent,View v,int position,long id){
            try {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Second.class);
                Bundle bundle = new Bundle();
                double[] L = {0, 0};
                String title;
                switch (position) {
                    //選擇工學院
                    case 0:
                        //將經緯度資料傳到 second.java
                        L[0] = 25.150160;
                        L[1] = 121.780682;
                        title = "工學院";
                        bundle.putString("title", title);
                        bundle.putDoubleArray("L", L);
                        break;

                    //循環站
                    case 1:
                        L[0] = 25.129868;
                        L[1] = 121.743116;
                        title = "循環站";
                        bundle.putString("title", title);
                        bundle.putDoubleArray("L", L);
                        break;

                    //火車站
                    case 2:
                        L[0] = 25.132471;
                        L[1] = 121.740015;
                        title = "火車站";
                        bundle.putString("title", title);
                        bundle.putDoubleArray("L", L);
                        break;
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
                Log.d("TAG", e.toString());
            }


        }
    };
}
