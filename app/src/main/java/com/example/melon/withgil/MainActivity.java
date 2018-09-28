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


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

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
        fab_emergency_call.setOnClickListener(this);
        fab_settings.setOnClickListener(this);
        fab_protector.setOnClickListener(this);


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
                .setPermissions(Manifest.permission.SEND_SMS)
                .check();


    }

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

        PolylineOptions polylineOptions = new PolylineOptions()
                .add(new LatLng(37.576278, 126.893896), new LatLng(37.577964, 126.896741))
                .width(25)
                .color(Color.CYAN)
                .geodesic(true);
        Polyline line = mMap.addPolyline(polylineOptions);
        //상암 안심길

        /*
        mMap.addPolyline((new PolylineOptions()
                .add(new LatLng(37.576278, 126.893896))
                .add(new LatLng(37.577964, 126.896741)).width(200).color(Color.RED)));
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
                startActivity(settingIntent);
                break;

            case R.id.fab_emergency_call:
                Intent emergencyCallIntent = new Intent("android.intent.action.DIAL", Uri.parse("tel:112"));
                startActivity(emergencyCallIntent);
                break;

            case R.id.fab_scout:
                Intent dasanCallIntent = new Intent("android.intent.action.DIAL", Uri.parse("tel:120"));
                startActivity(dasanCallIntent);
                break;
        }
    }
}
