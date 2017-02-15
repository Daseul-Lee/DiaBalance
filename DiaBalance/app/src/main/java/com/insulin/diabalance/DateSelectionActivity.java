package com.insulin.diabalance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

/**
 * Created by risha95 on 2016-10-07.
 */
public class DateSelectionActivity extends Activity{
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_DATE = "date";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dateselection);

        final DatePicker datepicker = (DatePicker) findViewById(R.id.datepicker);
        Button ckeckbtn =(Button) findViewById(R.id.checkbtn);
        Button canclebtn =(Button) findViewById(R.id.cancelbtn);
        String date =(String) getIntent().getExtras().get(EXTRA_DATE);
        int year =  Integer.parseInt(date.substring(0, 4));
        int month =  Integer.parseInt(date.substring(4, 6));
        int day =  Integer.parseInt(date.substring(6, 8));
        datepicker.updateDate(year,month-1,day);
        ckeckbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int year=datepicker.getYear();
                int month=datepicker.getMonth()+1;
                int day=datepicker.getDayOfMonth();
                String dateString =String.format("%04d%02d%02d", year, month, day);
                Intent intent = new Intent(getApplicationContext(), RecipeSaveActivity.class);
                intent.putExtra(com.insulin.diabalance.RecipeSaveActivity.EXTRA_ID, (String) getIntent().getExtras().get(EXTRA_ID));
                intent.putExtra(com.insulin.diabalance.RecipeSaveActivity.EXTRA_DATE, dateString);

                startActivity(intent);
            }
        });
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK,resultIntent);

                finish();
            }
        });
    }
}
