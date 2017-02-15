package com.insulin.diabalance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by risha95 on 2016-07-13.
 */
public class RecipeActivity extends Activity {
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TEXT = "text";
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
    String RecipeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        TextView recipeNametxt =(TextView)findViewById(R.id.recipeNametxt);
        recipeNametxt.setText((String) getIntent().getExtras().get(EXTRA_TEXT));
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());
        RecipeName = (String) getIntent().getExtras().get(EXTRA_TEXT);
        try {
            String qry = "select * from RECIPE where RECIPENAME = '" + RecipeName + "'";

            String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
            if (jsonString.substring(0, 1).equals("[")) {
                JSONArray ja = new JSONArray(jsonString);
                JSONObject jo = ja.getJSONObject(0);
                TextView calorietxt =(TextView)findViewById(R.id.calorietxt);
                calorietxt.setText(jo.getInt("CALORIE")+"kcal");
                TextView ingredienttxt =(TextView)findViewById(R.id.ingredienttxt);
                ingredienttxt.setText(jo.getString("INGREDIENT"));
                TextView cooktxt =(TextView)findViewById(R.id.cooktxt);
                cooktxt.setText(jo.getString("COOK"));
            }
            else {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK,resultIntent);
                Toast.makeText(getApplicationContext(), RecipeName+"의 레시피 정보가 없습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
