package com.insulin.diabalance;  //complete

import android.app.Activity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class join1 extends Activity {
    private static String url = "http://diabalance.dothome.co.kr/DiaBalance/";
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join1);

        EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        EditText idEdit = (EditText) findViewById(R.id.idEdit);
        EditText rAPIEdit = (EditText) findViewById(R.id.rAPIEdit);
        nameEdit.setFilters(new InputFilter[]{filterKor});
        idEdit.setFilters(new InputFilter[]{idFilterAlphaNum});
        rAPIEdit.setFilters(new InputFilter[]{readAPIFilterAlphaNum});
        idEdit.requestFocus();

        Button joinbtn = (Button) findViewById(R.id.joinbtn);
        Button returnBtn2 = (Button) findViewById(R.id.backbtn);

        joinbtn.setOnClickListener(new OnClickListener() {



            @Override
            public void onClick(View v) {
                
            	EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
                EditText pwEdit = (EditText) findViewById(R.id.pwEdit);
                EditText idEdit = (EditText) findViewById(R.id.idEdit);
                EditText byEdit = (EditText) findViewById(R.id.byEdit);
                EditText weightEdit = (EditText) findViewById(R.id.weightEdit);
                EditText heightEdit = (EditText) findViewById(R.id.heightEdit);
                EditText pwchkEdit = (EditText) findViewById(R.id.pwchkEdit);
                EditText cnEdit = (EditText) findViewById(R.id.cnEdit);
                EditText rAPIEdit = (EditText) findViewById(R.id.rAPIEdit);

                String name = nameEdit.getText().toString();
                String password = pwEdit.getText().toString();
                String id = idEdit.getText().toString();
                String birthyearString = byEdit.getText().toString();
                String passwordcheck = pwchkEdit.getText().toString();
                String readAPI=rAPIEdit.getText().toString();

                int check=0, idcheck=0;
                try {
                    String qry = "select MEMBERID from MEMBER where MEMBERID = '" + id + "'";

                    String jsonString = JSONParser.getJSONFromUrl(new URL(url + "select.php?qry=" + URLEncoder.encode(qry, "UTF-8")));
                    if (jsonString.substring(0,1).equals("[")) {
                        idcheck = 1;
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
                int weight=0,height=0,birthyear=0,channel=0;
                if(weightEdit.getText().toString().length()>0) {
                    weight = Integer.parseInt(weightEdit.getText().toString());
                }
                if(heightEdit.getText().toString().length()>0) {
                    height = Integer.parseInt(heightEdit.getText().toString());
                }
                if(birthyearString.length()>0) {
                    birthyear = Integer.parseInt(byEdit.getText().toString());
                }
                if(cnEdit.getText().toString().length()>0){
                    channel = Integer.parseInt(cnEdit.getText().toString());
                }
                if(idcheck==1){
                    Toast.makeText(getBaseContext(), "중복되는 ID가 존재 합니다.\n다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    check=1;
                }
                if(password.length()<4) {
                    Toast.makeText(getBaseContext(), "비밀번호를 4자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                    check=1;
                }
                if(birthyearString.length()!=4){
                    Toast.makeText(getBaseContext(), "생년은 4자리로 입력해주세요. (ex:1992)", Toast.LENGTH_SHORT).show();
                    check=1;
                }
                if(id.length()<4){
                    Toast.makeText(getBaseContext(), "id는 4자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                    check=1;
                }
                if(!(password.equals(passwordcheck))&&check==0){
                    Toast.makeText(getBaseContext(), "비밀번호가 같지 않습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    check=1;
                }
                if(name.length()==0 || weight==0 || height==0 || channel==0 || readAPI.length()==0){
                    Toast.makeText(getBaseContext(), "입력되지 않은 부분이 있습니다.\n 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(check==0){
                    Toast.makeText(getBaseContext(), "회원 가입을 환영합니다!\n로그인해주세요~", Toast.LENGTH_SHORT).show();
                    try {
                        URL text=new URL(url + "insertMEMBER.php?memberid=" + URLEncoder.encode(id, "UTF-8")
                                + "&membername=" +  URLEncoder.encode(name, "UTF-8")
                                + "&pw=" + URLEncoder.encode(password, "UTF-8")
                                + "&birthyear=" + birthyear
                                + "&height=" + height
                                + "&weight=" + weight
                                + "&channel=" + channel
                                + "&readapi=" + URLEncoder.encode(readAPI, "UTF-8"));

                        text.openStream();
                        HttpURLConnection conn = (HttpURLConnection)text.openConnection();

                        conn.connect();
                        conn.disconnect();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        
        returnBtn2.setOnClickListener(new OnClickListener() {
        	
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK,resultIntent);
                
                finish();
            }
        });
	}
	
	public InputFilter filterKor = new InputFilter() { 
	    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) { 
	        Pattern ps = Pattern.compile("^[a-zA-Zㄱ-ㅎ가-힣ㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2025]+$");
	        if (!ps.matcher(source).matches()) {
                Toast.makeText(getBaseContext(), "이름은 영문자나 한글로만 입력해주세요.", Toast.LENGTH_SHORT).show();
	            return ""; 
	        } 
	        return null; 
	    } 
	};
    public InputFilter idFilterAlphaNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]*$");
            if (!ps.matcher(source).matches()) {
                Toast.makeText(getBaseContext(), "ID는 영문자나 숫자로만 입력주세요.", Toast.LENGTH_SHORT).show();
                return "";
            }
            return null;
        }
    };
    public InputFilter readAPIFilterAlphaNum = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]*$");
            if (!ps.matcher(source).matches()) {
                Toast.makeText(getBaseContext(), "readAPI는 영문자나 숫자로만 입력주세요.", Toast.LENGTH_SHORT).show();
                return "";
            }
            return null;
        }
    };




}
