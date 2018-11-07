package rannver.com.chartsdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import rannver.com.chartsdemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btuLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btuLineChart = findViewById(R.id.btu_line);
        btuLineChart.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        Class activity = null;

        switch (v.getId()){
            case R.id.btu_line:
                activity = LineActivity.class;
                break;
        }

        Intent intent = new Intent(MainActivity.this,activity);
        startActivity(intent);

    }
}
