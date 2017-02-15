package com.insulin.diabalance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by risha95 on 2016-07-13.
 */
public class ExerciseManagementActivity extends Activity {
    public static final String EXTRA_ID = "id";
    Button ExerciseSchedulerbtn, Exercisebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisemanagement);
        ExerciseSchedulerbtn=(Button)findViewById(R.id.ExerciseSchedulerbtn);
        ExerciseSchedulerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExerciseSchedulerActivity.class);
                intent.putExtra(com.insulin.diabalance.ExerciseSchedulerActivity.EXTRA_ID, (String) getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });
        Exercisebtn=(Button)findViewById(R.id.Exercisebtn);
        Exercisebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ExerciseActivity.class);
                intent.putExtra(com.insulin.diabalance.ExerciseActivity.EXTRA_ID, (String)getIntent().getExtras().get(EXTRA_ID));
                startActivity(intent);
            }
        });
    }

}
