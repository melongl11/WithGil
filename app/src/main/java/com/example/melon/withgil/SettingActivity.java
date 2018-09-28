package com.example.melon.withgil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle("환경 설정");
        setContentView(R.layout.activity_setting);

        ImageView iv = findViewById(R.id.iv_guardian);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent protectorIntent = new Intent(SettingActivity.this, ProtectorActivity.class);
                startActivity(protectorIntent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        //Log.d("Click Log", String.valueOf(view.getId()));
        switch (view.getId()){
            case R.id.iv_guardian:
                Intent protectorIntent = new Intent(SettingActivity.this, ProtectorActivity.class);
                startActivity(protectorIntent);
                break;


        }

    }
}
