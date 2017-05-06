package com.example.user.busstation_alert;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by user on 2017/5/5.
 */

public class Second extends AppCompatActivity{
    Button end,bt;
    TextView Show_Latitude_and_Longitude,Show_Distance;
    LocationManager locationManager;
    WebView wv;
    Location location;
    float distanceInMeters=0;
    double[] k;
    MediaPlayer mediaplayer=new MediaPlayer();
    boolean check=false;
    //站牌的位置
    Location loc1 = new Location("");
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2);

        //檢查權限是否開啟
        if (ActivityCompat.checkSelfPermission(Second.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Second.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Second.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(Second.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        wv = (WebView) this.findViewById(R.id.web);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/sensorXgooglemap.html");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Show_Latitude_and_Longitude=(TextView)findViewById(R.id.i);
        bt=(Button)findViewById(R.id.MyLocation);
        end=(Button)findViewById(R.id.end);
        Show_Distance=(TextView)findViewById(R.id.Distance);


        //從 MainActivity中取得點選站牌的經緯度
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();

        //將標題改成點選的選項
        String title=bundle.getString("title");
        this.setTitle(title);

        k=bundle.getDoubleArray("L");
        Show_Latitude_and_Longitude.setText(k[0]+"    "+k[1]);
        end.setOnClickListener(listener);

        //將現在位置顯示在地圖上

        String commadStr = LocationManager.GPS_PROVIDER;

        locationManager.requestLocationUpdates(commadStr, 1000, 0, locationListener);
        location = locationManager.getLastKnownLocation(commadStr);
        if (location!=null) {
            String centerURL = "javascript:centerAt("+location.getLatitude()+","+location.getLongitude()+")";
            wv.loadUrl(centerURL);
        }
//        try {
//            locationManager.requestLocationUpdates(commadStr, 0, 0, locationListener);
//             location = locationManager.getLastKnownLocation(commadStr);
//            if (location!=null) {
//                String centerURL = "javascript:centerAt("+location.getLatitude()+","+location.getLongitude()+")";
//                wv.loadUrl(centerURL);
//            }
//        } catch (SecurityException e) {
//            if (ActivityCompat.checkSelfPermission(Second.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(Second.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(Second.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
//                ActivityCompat.requestPermissions(Second.this,new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},1);
//
//                return;
//            }
//            e.printStackTrace();
//            Log.d("TAG", e.toString());
//            //上面這個是將假如出現錯誤的例外資料可以顯示在下面的欄位中
//        }

        bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String commadStr = LocationManager.GPS_PROVIDER;


                try {
                    locationManager.requestLocationUpdates(commadStr, 1000, 0, locationListener);
                    location = locationManager.getLastKnownLocation(commadStr);
                    if (location!=null) {
                        String centerURL = "javascript:centerAt("+location.getLatitude()+","+location.getLongitude()+")";
                        wv.loadUrl(centerURL);

                    }
                } catch (SecurityException e) {
                    if (ActivityCompat.checkSelfPermission(Second.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(Second.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Second.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
                        ActivityCompat.requestPermissions(Second.this,new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},1);

                        return;
                    }

                    e.printStackTrace();
                    Log.d("TAG", e.toString());
                    //上面這個是將假如出現錯誤的例外資料可以顯示在下面的欄位中
                }
                if( distanceInMeters<=100){

                    try{

                        mediaplayer.reset();
                        mediaplayer=MediaPlayer.create(Second.this,R.raw.clockalarm);
                        mediaplayer.start();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("TAG", e.toString());
                    }

                    new AlertDialog.Builder(Second.this)
                            .setTitle("起床囉!~~")
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialoginterface, int i) {
                                    mediaplayer.release();
                                    finish();

                                }
                            })
                            .show();
                }

            }
        });


//        計算距離並顯示出來
        loc1.setLatitude(k[0]);
        loc1.setLongitude(k[1]);

        try {
            if(location!=null)
                distanceInMeters = Math.round(location.distanceTo(loc1));
        }catch (Exception e){
            e.printStackTrace();
            Log.d("dis", e.toString());
        }
        Show_Distance.setText("距離是="+distanceInMeters+"公尺");
    }


    private Button.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();

        }
    };


    public LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //Called when a new location is found by the network location provider.
            String centerURL = "javascript:centerAt("+location.getLatitude()+","+location.getLongitude()+")";
            wv.loadUrl(centerURL);
            loc1.setLatitude(k[0]);
            loc1.setLongitude(k[1]);

            distanceInMeters = (int)loc1.distanceTo(location);
            Show_Distance.setText("距離是="+distanceInMeters+"公尺");
//            try{
                if( distanceInMeters<=100){

                    try{

                        mediaplayer.reset();
                        mediaplayer=MediaPlayer.create(Second.this,R.raw.clockalarm);
                        mediaplayer.start();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d("TAG", e.toString());
                    }

                new AlertDialog.Builder(Second.this)
                        .setTitle("起床囉!~~")
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                mediaplayer.release();
                                finish();

                            }
                        })
                        .show();
                }
//            }catch (Exception e){
//                e.printStackTrace();
//                Log.d("TAG", e.toString());
//            }
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
    };
}
