package com.example.melon.withgil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle("환경 설정");
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_guardian:
                Intent protectorIntent = new Intent(SettingActivity.this, ProtectorActivity.class);
                startActivity(protectorIntent);
                break;


        }

    }
}
