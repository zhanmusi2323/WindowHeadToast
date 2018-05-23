package com.example.windowheadtoast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.windowheadtoast.RomUtils.FloatWindowManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        FloatWindowManager.getInstance().applyOrShowFloatWindow(this);
        //   windowHeadToast.initHeadToastView();
    }

    private void initView() {
        button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button) {
            WindowHeadToast windowHeadToast = new WindowHeadToast(this);
            windowHeadToast.showCustomToast();
        }
    }
}
