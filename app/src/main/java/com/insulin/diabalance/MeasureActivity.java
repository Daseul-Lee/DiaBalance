package com.insulin.diabalance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by risha95 on 2016-07-13.
 */
public class MeasureActivity extends Activity {
    Button BSbtn, PSbtn;
    public static final String EXTRA_ID = "ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);
        BSbtn=(Button)findViewById(R.id.BSbtn);
        BSbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BSMeasureActivity.class);
                intent.putExtra(com.insulin.diabalance.BSMeasureActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
                intent.putExtra(com.insulin.diabalance.BSMeasureActivity.EXTRA_TYPE, 1);
                startActivity(intent);
            }
        });
        PSbtn=(Button)findViewById(R.id.PSbtn);
        PSbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BSMeasureActivity.class);
                intent.putExtra(com.insulin.diabalance.BSMeasureActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(com.insulin.diabalance.MainActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
        startActivity(intent);
    }
}
