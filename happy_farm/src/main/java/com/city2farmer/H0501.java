package com.city2farmer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class H0501 extends AppCompatActivity {
    private static final String DB_FILE = "happyfarm.db", DB_TABLE05 = "ha0500";
    private static final int DBversion = HappyfarmDB.VERSION;
    private TextView t003, add, ID;
    private Intent intent = new Intent();
    private ImageView im06,im07, t011;
    private EditText e001, e002;
    private Button b001, b002, b_modify, b_clear, b_del, mod_ok;
    private String year01, month01, day01;
    private Spinner sp01, sp02;
    private String strData;
    private HappyfarmDB dbHfarm;
    private String tn0101, tn0102, tn0103, tn0104, tn0105,tn0106;
    private int bb;
    private int index = 0;
    private ArrayList<String> recSet05;
    //private int rowsAffected;
    private String msg;
    private String sqlctl;
    private String TAG="tcnr05==>";
    private int old_index;
    private int up_item=0;
    private View header;
    private String u_email;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //================???????????????APP================================
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //======================================================
        enableStrictMode(this); //?????????????????????
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h0501);
        setupViewCompoent();
    }

    //**************************SQLi??????*****************************************
    private void enableStrictMode(H0501 h0501) {
        //-------------????????????????????????????????????------------------------------
        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()
                        .detectDiskWrites()
                        .detectNetwork()
                        .penaltyLog()
                        .build());
        StrictMode.setVmPolicy(
                new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects()
                        .penaltyLog()
                        .build());
    }
    //*********************************************************************

    private void initDB() {
        //        ??????sql????????????
        if (dbHfarm == null) {
            dbHfarm = new HappyfarmDB(getApplicationContext(), DB_FILE, null, DBversion);
        }
        recSet05 = dbHfarm.getRecSet05();
    }

    private void setupViewCompoent() {
        //	--????????????--
        Intent intent = this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        //
        add = (TextView) findViewById(R.id.h0501_add);
        add.setOnClickListener(b002ON);  //????????????
        //
        t011 = (ImageView) findViewById(R.id.h0501_t011);
        t011.setOnClickListener(b001ON); //????????????
        //
        b001 = (Button) findViewById(R.id.h0501_b001);
        b001.setOnClickListener(dateON);    //???????????????
        //
        im06 = (ImageView) findViewById(R.id.h0501_im06);
        im06.setOnClickListener(b001ON); //??????
        //
        //
        im07 = (ImageView) findViewById(R.id.h0501_im07);
        im07.setOnClickListener(b001ON); //??????
        //
        t003 = findViewById(R.id.h0501_t003); //???????????????
        b002 = (Button) findViewById(R.id.h0501_b002); //Button??????
        b002.setOnClickListener(b002ON);
        b_modify = findViewById(R.id.h0501_modify); //Button??????
        e001 = (EditText) findViewById(R.id.h0501_e001);    //??????
        e002 = (EditText) findViewById(R.id.h0501_e002);    //????????????
        sp01 = findViewById(R.id.h0501_sp01); //spinner
        //sp02 = findViewById(R.id.h0501_sp02); //spinner?????????
        ID = findViewById(R.id.h0501_ID); //??????ID
        b_del = findViewById(R.id.h0501_b_del);
        //============================================
        b_clear = findViewById(R.id.h0501_clear);//????????????????????????
        b_clear.setOnClickListener(b002ON);
        //================?????????============================
        initDB();
        showRec(index);//?????????????????????????????????
        //u_setspinner();

        //????????????:????????????????????????
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date curDate = new Date(System.currentTimeMillis()); //????????????????????
        strData = formatter.format(curDate);
        t003.setText(strData);
        add.performClick(); //??????add,callOnClick();
        dbmysql();
    }

    private void u_setspinner() {
        //recSet05 = dbHfarm.getRecSet05();
                ArrayAdapter<String> PlantList = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item);
                for (int i = 0; i < recSet05.size(); i++) {
                    //Map<String, Object> item = new HashMap<String, Object>();
                    String[] fld = recSet05.get(i).split("#");
                    PlantList.add(fld[0] + ". " + fld[1]);
                }
                PlantList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp01.setAdapter(PlantList);
                sp01.setOnItemSelectedListener(plant);

    }

    private void showRec(int index) {   //????????????
        if (recSet05.size() != 0) {
            String[] fld = recSet05.get(index).split("#"); //??????(2??????????????????#)
            ID.setText(fld[0]);
            e001.setText(fld[1]);
            t003.setText(fld[2]);
            b001.setText(fld[3]);
            e002.setText(fld[4]);
        } else {
            e001.setText("");
            t003.setText(strData);
            b001.setText("");
            e002.setText("");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dbHfarm != null) {
            dbHfarm.close();
            dbHfarm = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbHfarm == null) {
            dbHfarm = new HappyfarmDB(this, DB_FILE, null, DBversion);
        }
        //===??????====
        long_in();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dbHfarm != null) {
            dbHfarm.close();
            dbHfarm = null;
        }
    }

    //============??????Intent
    private View.OnClickListener b001ON = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.h0501_im06:
                    finish();
                    break;

                case R.id.h0501_im07:
                    uri = Uri.parse("https://m.facebook.com/city2farmer");
                    Intent it=new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(it);
                    break;

                case R.id.h0501_t011:
                    intent.putExtra("class_title", getString(R.string.h1301_t003));
                    intent.setClass(H0501.this, H1302_about.class);
                    startActivity(intent);
                    break;
            }
        }
    };
    //==============????????????==============================
    private View.OnClickListener b002ON = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.h0501_add:
                    e001.setText("");
                    e002.setText("");
                    b001.setText(""); //????????????
                    b002.setVisibility(View.VISIBLE);
                    t003.setText(strData);
                    b_modify.setVisibility(View.GONE);
                    //-=================
//                    b_modify.setVisibility(View.VISIBLE);
//                    b_modify.setOnClickListener(modify);
                    b001.setOnClickListener(dateON);
                    b_clear.setVisibility(View.GONE);
                    b_del.setVisibility(View.GONE);
                    break;
//================??????????????????===================
                case R.id.h0501_b002:
                    tn0102 = e001.getText().toString().trim();
                    tn0103 = t003.getText().toString().trim();
                    tn0104 = b001.getText().toString().trim();
                    tn0105 = e002.getText().toString().trim();
                    tn0106 = u_email;
                    if (tn0102.equals("") || tn0103.equals("") || tn0104.equals("")) {
                        Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String msg = null;
                   //***** long rowID = dbHfarm.insertRec05(tn0102, tn0103, tn0104, tn0105);   //????????????SQL
                    //==========???????????????MySQL=======================
                    mysql_insert();
                    dbHfarm.clearRec05(); //???????????????
                    dbmysql();
                    //===============================================
                    long rowID = dbHfarm.RecCount05();
                    if (rowID != -1) {
                        //index = dbHfarm.RecCount() - 1;
                        //u_setspinner();
                        //showRec(index);
                        e001.setHint("??????");
                        t003.setText(strData);
                        // b001.setText("???????????????");
                        e002.setText("???????????????");
                        msg = "??????????????????!";
                    } else {
                        msg = "??????????????????!";
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    add.performClick();
                    break;

                //====================???????????????????????????============================
                case R.id.h0501_clear:
                    int rowsAffected = dbHfarm.clearRec05();
                    msg = "??????????????? !?????????" + rowsAffected + " ???";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    break;
                //============================================================
            }
        }
    };
    //=============??????SQLi====================
    private void mysql_insert() {
        ArrayList<String> nameValuePairs = new ArrayList<>();

        nameValuePairs.add(tn0102);
        nameValuePairs.add(tn0103);
        nameValuePairs.add(tn0104);
        nameValuePairs.add(tn0105);
        nameValuePairs.add(tn0106);
        try {
            Thread.sleep(500);//??????Thread ??????0.5???
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        //--============??????SQL Insert=====================
        String result = DBConnector.executeInsert(nameValuePairs);
        // ---------------------------------------------
    }

    private View.OnClickListener dateON = new View.OnClickListener() {
        @Override
        public void onClick(View v) {   //???????????????Dialog
            b001.setText("");
            Calendar now = Calendar.getInstance();
            //-----------------------------------------------------
            DatePickerDialog date = new DatePickerDialog(H0501.this, dateOnDateSelLis,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));
            date.setTitle(getString(R.string.h0501_datetitle));
            date.setMessage(getString(R.string.h0501_datemessage));
            date.setIcon(android.R.drawable.ic_menu_month);
            date.setCancelable(false);
            date.show();
        }
    };
//    private View.OnClickListener data_off = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            //  ??????????????????
//        }
//    };

    private DatePickerDialog.OnDateSetListener dateOnDateSelLis = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year01 = Integer.toString(year);
            month01 = Integer.toString(month + 1);
            day01 = Integer.toString(dayOfMonth);
            b001.setText(year01 + getString(R.string.nn) + month01 + getString(R.string.nn)
                    + day01);
        }
    };
    //==============spinner?????????sp01(??????)
    AdapterView.OnItemSelectedListener plant = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //============================
            index = position;
            //==============================
            int iSelect = sp01.getSelectedItemPosition(); //???????????????
//            //========index??????=================
//            index=iSelect;
//            //==============================
            //-------???????????????item---
            up_item = iSelect;
            //-------------------------------
            String[] fld = recSet05.get(iSelect).split("#");
            ID.setText(fld[0]);
            e001.setText(fld[1]);
            t003.setText(fld[2]);
            b001.setText(fld[3]);
            e002.setText(fld[4]);
            b002.setVisibility(View.GONE);
            b_modify.setVisibility(View.VISIBLE);
            b_modify.setOnClickListener(modify);
            b_del.setVisibility(View.VISIBLE);
            b_del.setOnClickListener(modify);

            b001.setOnClickListener(null);
        }

        //===========spinner?????????
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            add.performClick(); //??????add,callOnClick();
        }
    };

    //==================aldBtListener=================
    private DialogInterface.OnClickListener aldBtListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:
                    tn0101 = ID.getText().toString().trim();
                    old_index=index;
                    // ---------------------------
                    mysql_del();// ??????MySQL??????
                    dbHfarm.clearRec05(); //???????????????
                    dbmysql();
                    // ---------------------------
                    index=old_index;

                    if (index == dbHfarm.RecCount05()) {
                        int aa =0;
                        if(index>0){
                            index--;
                            int bb =0;
                        }
                        int cc =0;
                    }
                    recSet05=dbHfarm.getRecSet05();
                    u_setspinner();
                    showRec(index);
                    msg = "???????????????" ;
//                    int rowsAffected = dbHfarm.deleteRec05(tn0101);
//                    if (rowsAffected == -1) {
//                        msg = "??????????????????, ???????????? !";
//                    } else if (rowsAffected == 0) {
//                        msg = "???????????????????????????, ???????????? !";
//                    } else {
//                        msg = e001.getText().toString().trim() + "??????  ????????? ! ";
//                        recSet05 = dbHfarm.getRecSet05();
//                        if (index == dbHfarm.RecCount05()) {
//                            index--; //??????=??????????????????--???
//                        }
//                        u_setspinner();
//                        showRec(index);
//                    }
                    add.performClick(); //??????add,callOnClick();
                    break;

                case BUTTON_NEGATIVE:
                    msg = "?????????????????? !";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    break;
            }
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    };
//============????????????==========================
    private void mysql_del() {
        tn0101 = ID.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(tn0101);
        try {
            Thread.sleep(100); //  ??????Thread ??????0.5???
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//------------------------??????---------------------------------------------------
        String result = DBConnector.executeDelet(nameValuePairs);
        Log.d(TAG, "Delete result:" + result);
//-----------------------------------------------
    }

    //=============???????????????**************
    private View.OnClickListener modify = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.h0501_modify:
                    new AlertDialog.Builder(H0501.this)
                            .setIcon(android.R.drawable.ic_menu_edit)
                            .setTitle("????????????")
                            .setMessage("??????????????????????")
                            .setPositiveButton("??????", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //
//                                    tn0101 = ID.getText().toString().trim();
//                                    tn0102 = e001.getText().toString().trim();
//                                    tn0103 = t003.getText().toString().trim();
//                                    tn0104 = b001.getText().toString().trim();
//                                    tn0105 = e002.getText().toString().trim();

                                    old_index = index;
                                    mysql_update(); // ??????MySQL??????
                                    dbmysql();//SQL????????????
                                    //-------------------------------------
                                    //*****??????????????????recSet = dbHper.getRecSet();****
                                    recSet05 = dbHfarm.getRecSet05();
                                    u_setspinner();
                                    index = old_index;
                                    showRec(index);
                                    msg = "??????  ????????? !";
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    //=================================================================

//                                    rowsAffected = dbHfarm.updateRec05(tn0101, tn0102, tn0103, tn0104, tn0105);
//
//                                    if (rowsAffected == -1) {
//                                        msg = "????????????, ???????????? !";
//                                    } else if (rowsAffected == 0) {
//                                        msg = "???????????????????????????, ???????????? !";
//                                    } else {
//                                        msg = " ??????  ????????? !";
//                                        //*********????????????????????????recSet************
//                                        recSet05 = dbHfarm.getRecSet05();
//                                        showRec(index);
//                                    }
//                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                                    //????????????????????????(???????????????)
//                                    //add.performClick(); //??????add,callOnClick();
                                }
                            }).setNegativeButton("????????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            b002.setVisibility(View.GONE);
                            b_modify.setVisibility(View.VISIBLE);
                        }
                    }).create()
                            .show();
                    break;
                //===========????????????=================
                case R.id.h0501_b_del:
                    FarmAlertDialog aldDial = new FarmAlertDialog(H0501.this);
                    aldDial.setTitle(getString(R.string.m_deltitle));
                    aldDial.setMessage(getString(R.string.m_delmessage));
                    aldDial.setIcon(android.R.drawable.ic_delete);
                    aldDial.setCancelable(false); //???????????????
                    aldDial.setButton(BUTTON_POSITIVE, "????????????", aldBtListener);
                    aldDial.setButton(BUTTON_NEGATIVE, "????????????", aldBtListener);
                    aldDial.show();
                    break;
            }
        }
    };
    //============??????SQLi=============================
    private void mysql_update() {
        tn0101 = ID.getText().toString().trim();
        tn0102 = e001.getText().toString().trim();
        tn0103 = t003.getText().toString().trim();
        tn0104 = b001.getText().toString().trim();
        tn0105 = e002.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(tn0101);
        nameValuePairs.add(tn0102);
        nameValuePairs.add(tn0103);
        nameValuePairs.add(tn0104);
        nameValuePairs.add(tn0105);
        try {
            Thread.sleep(100); //  ??????Thread ??????0.5???
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = DBConnector.executeUpdate( nameValuePairs);
//-----------------------------------------------
    }

    @Override
    public void onBackPressed() {   //??????????????????
//        super.onBackPressed();
        Toast.makeText(getApplicationContext(), R.string.h0501_toast_back, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.h0501main, menu);
        return (true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int aa=0;
        switch (item.getItemId()) {
            case R.id.m_add:    //????????????
                add.performClick(); //??????add,callOnClick();
                break;

            case R.id.m_edit:   //????????????
//                recSet05 = dbHfarm.getRecSet05();
                u_setspinner();
                //int  a=0;
//                ArrayAdapter<String> PlantList = new ArrayAdapter<String>(this,
//                        android.R.layout.simple_spinner_item);
//                for (int i = 0; i < recSet05.size(); i++) {
//                    //Map<String, Object> item = new HashMap<String, Object>();
//                    String[] fld = recSet05.get(i).split("#");
//                    PlantList.add(fld[0] + ". " + fld[1]);
//                }
//                PlantList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                sp01.setAdapter(PlantList);
//                sp01.setOnItemSelectedListener(plant);
                sp01.performClick(); //??????sp01,callOnClick();
                break;

//
            case R.id.m_mysql:  // ??????MySQL
                dbmysql();
                break;

            case R.id.action_settings:  //??????
                //dbHfarm.clearRec05();
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //======??????MySQL ??????=========
    private void dbmysql() {
        sqlctl = "SELECT * FROM ha0500 WHERE n0106="+ "'" + u_email +"'" + " ORDER BY n0101 ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = DBConnector.executeQuery(nameValuePairs);

            /**************************************************************************
             * SQL ??????????????????????????????JSONArray
             * ?????????????????????????????????JSONObject?????? JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL ?????????????????????

                int rowsAffected = dbHfarm.clearRec05();                 // ?????????,????????????SQLite??????

                // ??????JASON ????????????????????????
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) ?????????????????? --?????? jsonObject ????????????("key","value")-----------------------
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); // ??????????????????
                        if (value == null) {
                            continue;
                        } else if ("".equals(value.trim())) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        newRow.put(key, value.toString()); // ???????????????????????????
                        // -------------------------------------------------------------------
                    }
                    // ---(2) ????????????????????????---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------??????SQLite---------------------------------------
                    //Long rowID = dbHfarm.insertRec05_m(newRow);
                    long rowID = dbHfarm.insertRec05_m(newRow);
                    Toast.makeText(getApplicationContext(), "????????? " + Integer.toString(jsonArray.length() ) + " ?????????", Toast.LENGTH_SHORT).show();
                }
                // ---------------------------
            } else {
                Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_LONG).show();
            }
            recSet05 = dbHfarm.getRecSet05();  //????????????SQLite
            //u_setspinner();
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    @Override
    protected void onStart() {
        SharedPreferences A01 = getSharedPreferences("AA", 0);

        bb = A01.getInt("AA", 0);
        if (bb == 1) {
            bb = 0;
            this.finish();
        }
        super.onStart();
    }
//========??????????????????===============================
    public void long_in(){

        if(dbHfarm == null){
            dbHfarm = new HappyfarmDB(H0501.this, "happyfarm.db", null, DBversion);
        }
        String  u_pic=dbHfarm.Find("r0205");
        u_email = dbHfarm.Find("r0204");
        boolean login_status = dbHfarm.status();
        if(login_status){

        }else{
            CircleImgView cc = (CircleImgView) findViewById(R.id.h0501_icon);
            cc.setImageResource(R.drawable.googleg_color);
        }

        Uri user_uri;
        if(u_pic!=null){
            user_uri=Uri.parse(u_pic);
        }else{
            CircleImgView userImage = (CircleImgView)findViewById(R.id.h0501_icon);
            userImage.setImageResource(R.drawable.googleg_color);
            userImage.invalidate();
            user_uri=null;
        }

        if(user_uri!=null){
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    String url = params[0];
                    return getBitmapFromURL(url);
                }
                @Override
                protected void onPostExecute(Bitmap result) {
                    CircleImgView userImage = (CircleImgView) findViewById(R.id.h0501_icon);
                    userImage.setImageBitmap(result);
                    super.onPostExecute(result);
                }
            }.execute(user_uri.toString().trim());
        }
        dbHfarm.close();
    }
    private Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}