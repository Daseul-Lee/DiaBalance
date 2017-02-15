package com.insulin.diabalance;

/**
 * Created by SSOGYO on 2016-09-07.
 */
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MadeByActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_by);
        TextView referencetxt=(TextView)findViewById(R.id.referencetxt);
        referencetxt.setText(referencetxt.getText()+"- 당뇨환자의 운동요법, http://terms.naver.com/entry.nhn?docId=2119658&cid=51004&categoryId=51004");
    }

}