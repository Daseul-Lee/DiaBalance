package com.insulin.diabalance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by risha95 on 2016-07-13.
 */
public class MenuActivity extends Activity {
    public static final String EXTRA_ID = "id";
    Button Recommendbtn, Recipebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Recommendbtn=(Button)findViewById(R.id.Recommendbtn);
        Recommendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuRecommendActivity.class);
                intent.putExtra(com.insulin.diabalance.MenuRecommendActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });
        Recipebtn=(Button)findViewById(R.id.Recipebtn);
        Recipebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecipeSaveActivity.class);
                intent.putExtra(com.insulin.diabalance.RecipeSaveActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
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
