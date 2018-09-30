package com.example.melon.withgil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

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

        Spinner spinner1 = findViewById(R.id.spinner1);
        final Spinner spinner2 = findViewById(R.id.spinner2);

        final MapInfoDatabase mapInfoDatabase = new MapInfoDatabase(this, "mapinfo.db", null, 1);
        final DefaultRegionDatabase defaultRegionDatabase = new DefaultRegionDatabase(this, "defaultregion.db", null, 1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view,
                                       final int position, long id) {
                //선택시 상황
                ArrayList<String> region = mapInfoDatabase.getRegionName((String) parent.getItemAtPosition(position));
                ArrayAdapter<String>adapter = new ArrayAdapter<>(SettingActivity.this,
                        android.R.layout.simple_spinner_item, region);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        defaultRegionDatabase.addDR((String) parent.getItemAtPosition(position), (String) adapterView.getItemAtPosition(i));
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("district", (String) parent.getItemAtPosition(position));
                        resultIntent.putExtra("region", (String) adapterView.getItemAtPosition(i));
                        setResult(RESULT_OK, resultIntent);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
