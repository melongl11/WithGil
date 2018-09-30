package com.example.melon.withgil;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    private void initData(MapInfoDatabase mapInfoDatabase, LocationInfoDatabase locationInfoDatabase){
        mapInfoDatabase.addRegion(16, "마포", "망원");
        mapInfoDatabase.addRegion(8, "서대문", "연희");
        mapInfoDatabase.addRegion(18, "마포", "상암");

        locationInfoDatabase.addLocation(18, 37.576278, 126.893896);
        locationInfoDatabase.addLocation(18, 37.577964, 126.896741);

        locationInfoDatabase.addLocation(16, 37.556650, 126.898984);
        locationInfoDatabase.addLocation(16, 37.557738, 126.904251);

        locationInfoDatabase.addLocation(8, 37.562714, 126.931413);
        locationInfoDatabase.addLocation(8, 37.563042, 126.931553);
        locationInfoDatabase.addLocation(8, 37.563735, 126.932880);
        locationInfoDatabase.addLocation(8, 37.564008, 126.933059);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab_emergency_call = (FloatingActionButton) findViewById(R.id.fab_emergency_call);
        FloatingActionButton fab_settings = (FloatingActionButton)  findViewById(R.id.fab_settings);
        FloatingActionButton fab_protector =  (FloatingActionButton) findViewById(R.id.fab_contact_protector);
        FloatingActionButton fab_scout = (FloatingActionButton) findViewById(R.id.fab_scout);
        fab_emergency_call.setOnClickListener(this);
        fab_settings.setOnClickListener(this);
        fab_protector.setOnClickListener(this);
        fab_scout.setOnClickListener(this);


        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied \n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(R.string.rationale_message)
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("bla bla")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
        new TedPermission(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(R.string.rationale_message)
                .setDeniedMessage(
                        "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("bla bla")
                .setPermissions(Manifest.permission.SEND_SMS)
                .check();


    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location initialLocation = locationManager != null ? locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) : null;
        LatLng lastLatLng = new LatLng(37.59788, 126.86443);
        if (initialLocation != null) {
            lastLatLng = new LatLng(initialLocation.getLatitude(), initialLocation.getLongitude());
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, (float) 15));

        MapInfoDatabase mapInfoDatabase = new MapInfoDatabase(this, "mapinfo.db", null, 1);
        LocationInfoDatabase locationInfoDatabase = new LocationInfoDatabase(this, "locationinfo.db", null, 1);
        DefaultRegionDatabase defaultRegionDatabase = new DefaultRegionDatabase(this, "defaultregion.db", null, 1);
        int idx = 0;
        String district = "마포";
        String region = "상암";
        try {
            String[] DR = defaultRegionDatabase.getDR().split(" ");
            district = DR[0];
            region = DR[1];
        } catch (Exception e){

        }
        idx = mapInfoDatabase.getRegionInfo(district, region);
        if(idx == 0){
            initData(mapInfoDatabase, locationInfoDatabase);
            idx = mapInfoDatabase.getRegionInfo(district, region);
        }
        ArrayList<Double> points = locationInfoDatabase.getLocation(idx);

        PolylineOptions polylineOptions = new PolylineOptions();
        for(int i=0; i<points.size();i+=2){
            Log.d("db output point", String.valueOf(points.get(i) + " " +points.get(i+1)));
            polylineOptions.add(new LatLng(points.get(i), points.get(i+1)));
        }
        polylineOptions.width(15)
                .color(Color.argb(255, 114, 62, 189));
                //.geodesic(true);
        Polyline line = mMap.addPolyline(polylineOptions);
        /*
        PolylineOptions polylineOptions = new PolylineOptions()
                .width(25)
                .color(Color.CYAN)
                .geodesic(true);
        //상암 안심길

        for(int i=0; i<points.size();i+=2){
            polylineOptions.add(new LatLng(points.get(i), points.get(i+1)));
        }

        Polyline line = mMap.addPolyline(polylineOptions);
*/

        /*
        mMap.addPolyline((new PolylineOptions()
                .add(new LatLng(37.576278, 126.893896))
                .add(new LatLng(37.577964, 126.896741)).width(200).color(Color.argb(255, 114, 62, 189))));
         */
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_contact_protector:
                UserInfoDatabase userInfoDatabase = new UserInfoDatabase(this, "userinfo.db", null, 1);
                //TODO : USERINFO DB 설계 - 일단 Setting 부분이 확정안됨
                String pNum = null;
                String text = "";
                pNum = userInfoDatabase.getUserInfo();

                //pNum = "01085055354"; 번호 넣고 테스트했을때 문제없음(Permission 요청할때 Send SMS도 추가함)
                text = "지금 출발합니다.";

                if(pNum != null) {  //보호자 전화번호를 DB에서 받아 온 경우
                    final String phoneNo = pNum;
                    final String sms = text;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("문자 전송");
                    alertDialogBuilder
                            .setMessage(pNum + "에 출발 문자를 전송하시겠습니까?")
                            .setCancelable(true)
                            .setPositiveButton("전송",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //문자전송
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage("0" + phoneNo, null, sms, null, null);
                                        }
                                    })
                            .setNegativeButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {    //보호자 전화번호를 DB에서 받아오지 못한 경우
                    Toast.makeText(this, "저장된 보호자 전화번호가 없습니다.\n환경 설정에서 보호자 전화번호를 저장하세요.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.fab_settings:
                // 비밀번호, 문자 셋팅,
                Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(settingIntent, 3000);
                break;

            case R.id.fab_scout:
                Log.d("fab_scout :", "hello");
                Intent dasanCallIntent = new Intent("android.intent.action.DIAL", Uri.parse("tel:120"));
                startActivity(dasanCallIntent);
                break;

            case R.id.fab_emergency_call:
                Intent emergencyCallIntent = new Intent("android.intent.action.DIAL", Uri.parse("tel:112"));
                startActivity(emergencyCallIntent);
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 3000:
                    mMap.clear();

                    MapInfoDatabase mapInfoDatabase = new MapInfoDatabase(this, "mapinfo.db", null, 1);
                    LocationInfoDatabase locationInfoDatabase = new LocationInfoDatabase(this, "locationinfo.db", null, 1);
                    int idx = 0;
                    String district = data.getStringExtra("district");
                    String region = data.getStringExtra("region");
                    idx = mapInfoDatabase.getRegionInfo(district, region);
                    ArrayList<Double> points = locationInfoDatabase.getLocation(idx);

                    PolylineOptions polylineOptions = new PolylineOptions();
                    for(int i=0; i<points.size();i+=2){
                        Log.d("db output point", String.valueOf(points.get(i) + " " +points.get(i+1)));
                        polylineOptions.add(new LatLng(points.get(i), points.get(i+1)));
                    }
                    polylineOptions.width(25)
                        .color(Color.argb(255, 114, 62, 189));
                        //.geodesic(true);
                    Polyline line = mMap.addPolyline(polylineOptions);

                    break;
            }
        }
    }
}
