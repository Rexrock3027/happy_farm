package com.city2farmer;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class H1301 extends AppCompatActivity implements View.OnClickListener {
    //    --------------H1201_F_Map???-------------------
    public static boolean locationPermissionGranted = false;
    //    --------------------------------------------
    private static final String DB_FILE= "happyfarm.db", DB_TABLE13 = "HA1300";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private int DBversion= HappyfarmDB.VERSION;
    private HappyfarmDB dbHf13;
    private SQLiteDatabase myHappyfarmDB;
    //??????????????????
    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String AppId = "c89c8caf79aa808cb9a26346ba139323";
    public static String lang = "zh_tw";
    public String  lat;
    public String lon;
    private String iconurl, tr0202,tr0203,tr0204,tr0205;
    private LocationManager manager;

    //--------------------??????????????????
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    //    ========================================
    private String bestgps;
    // ???????????????????????????
    int minTime = 1800000; // ??????
    float minDistance = 20000; // ??????    //*********************************************************
    //private GoogleSignInClient mGoogleSignInClient;
    //private static final int RC_SIGN_IN = 9001;

    //*********************************************************
    private String TAG = "happy_farm==>";

    private List<String> permissionsList = new ArrayList<String>();

    private static final String[] permissionsArray = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION ,
            Manifest.permission.READ_CALENDAR ,
            Manifest.permission.CALL_PHONE ,
    };
    //----------------------????????????????????????
    private static final String[] permissionsName = new String[]{
            "??????????????????" , "????????????" , "????????????"
    };
    //-------------------------------------------------------
    private Intent intent = new Intent();
    private ImageButton b001, b002, b003, b004, b005, b006,b008, t001, b010;
    private TextView t010,wt01,wt02,wt03,wt04,wt05,tt010,tt011;
    private LinearLayout h1301lv01, h1301lh01, h1301lh02;
    private MediaPlayer mp;
    private Dialog mLoginDlg;
    private MenuItem login;
    private MenuItem logout;
    private Menu menu;
    private int user = 0;
    private CircleImgbutton img;
    private GoogleSignInAccount account;
    private Button Diaout;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_GET_TOKEN=9002;
    private Uri User_IMAGE;
    private ImageView i001;
    private Location currentLocation;

    private int in = 0;
    private Uri uri;

//    private MenuItem delete;
//    private MenuItem icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //================???????????????APP================================
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //======================================================
        checkRequiredPermission(this);
        u_checkgps();
        updatePosition();
        getCurrentData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h1301);
        setupViewCompoent();
        //====???????????????
        initDB();
        //*****************************
        //setupSignView();
        //********************************
    }

    private void initDB() {
        //        ??????sql????????????
        if (dbHf13 == null) {
            dbHf13 = new HappyfarmDB(getApplicationContext(), DB_FILE, null, DBversion);
                   }
    }
    private void setupViewCompoent() {

//-------??????????????????
        wt01 = (TextView) findViewById(R.id.h1301_wt01);
        wt02 = (TextView) findViewById(R.id.h1301_wt02);
        wt03 = (TextView) findViewById(R.id.h1301_wt03);
        wt04= (TextView) findViewById(R.id.h1301_wt04);
        wt05=(TextView)findViewById(R.id.h1301_wt05);
        tt010= (TextView) findViewById(R.id.h1301_tt010);
        tt011=  (TextView) findViewById(R.id.h1301_tt011);
        i001 = (ImageView) findViewById(R.id.h1301_i001);
//-------??????Intrent??????Class
        b001 = (ImageButton) findViewById(R.id.h1301_b001);
        b002 = (ImageButton) findViewById(R.id.h1301_b002);
        b003 = (ImageButton) findViewById(R.id.h1301_b003);
        b004 = (ImageButton) findViewById(R.id.h1301_b004);
        b005 = (ImageButton) findViewById(R.id.h1301_b005);
        b006 = (ImageButton) findViewById(R.id.h1301_b006);
        b008 = (ImageButton) findViewById(R.id.h1301_b008);
//       b010=(ImageButton)findViewById(R.id.h1301_b010);
        t001 = (ImageButton) findViewById(R.id.h1301_t001);
        t010 = (TextView) findViewById(R.id.h1301_tt010);
        Diaout=(Button)findViewById(R.id.h1302_Diab001);
        img = (CircleImgbutton) findViewById(R.id.h1301_icon);

//------------------------??????????????????
        h1301lv01 = (LinearLayout) findViewById(R.id.h1301_lv01);
        h1301lh01 = (LinearLayout) findViewById(R.id.h1301_lh01);
        h1301lh02 = (LinearLayout) findViewById(R.id.h1301_lh02);
//        h1301lv01.setBackgroundResource(R.drawable.h1301_bac);
        h1301lv01.setAnimation(AnimationUtils.loadAnimation(this , R.anim.anim_alpha_in));
        h1301lh01.setAnimation(AnimationUtils.loadAnimation(this , R.anim.anim_trans_left));
        h1301lh02.setAnimation(AnimationUtils.loadAnimation(this , R.anim.anim_trans_right));
        t010.setAnimation(AnimationUtils.loadAnimation(this , R.anim.anim_alpha_in));
//---------------??????????????????
        mp = MediaPlayer.create(getApplication() , R.raw.m1301);
        mp.start();
        //--------------------------------
//        //--?????????????????????????????????
        SpannableString sp = new SpannableString(getString(R.string.h1301_t010));
//        //???????????????
//        sp.setSpan(new URLSpan("http://www.google.com.tw") , 5 , 22 ,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        t010.setText(sp);
        //??????TextView?????????
        t010.setMovementMethod(LinkMovementMethod.getInstance());

        b001.setOnClickListener(b001ON);
        b002.setOnClickListener(b001ON);
        b003.setOnClickListener(b001ON);
        b004.setOnClickListener(b001ON);
        b005.setOnClickListener(b001ON);
        b006.setOnClickListener(b001ON);
        b008.setOnClickListener(b001ON);
//        b010.setOnClickListener(b001ON);
        t001.setOnClickListener(b002ON);
        img.setOnClickListener(b002ON);

        //=================================
        // For sample only: make sure there is a valid server client ID.
        validateServerClientID();

        // --START configure_signin--
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        //--END configure_signin---

        // --START build_client--
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //--END build_client--

//        try {
//            Thread.sleep(10000); //  ??????Thread ??????0.5???
//            h1301_layhome.setVisibility(View.GONE);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private void validateServerClientID() {
        String serverClientId = getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in google_sign.xml, must end with " + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    //*****************************************************************************
//    private void setupSignView() {
//
//        // --START configure_signin--
//        // Configure sign-in to request the user's ID, email address, and basic
//        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        // --END configure_signin--
//
//        // --START build_client--
//        // Build a GoogleSignInClient with the options specified by gso.
//        mGoogleSignInClient = GoogleSignIn.getClient(this , gso);
//        //--END build_client--
//
//        // --START customize_button--
//        // Set the dimensions of the sign-in button.
//        // SignInButton signInButton = findViewById(R.id.sign_in_button);
//        // signInButton.setSize(SignInButton.SIZE_STANDARD);
//        // signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
//        // --END customize_button--
//    }
//********************************************************************************************
//------??????API??????????????????
    private void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
//        lat = Double.toString(currentLocation.getLatitude());
//        lon = Double.toString(currentLocation.getLongitude());
        Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, lang, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call , @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    String s_wt01 = (int)(Float.parseFloat("" + weatherResponse.main.temp) - 273.15) + "??C";
                    String s_wt02 = (int)(Float.parseFloat("" + weatherResponse.main.temp_max) - 273.15) + "??C ";
                    String s_wt03 =(int)(Float.parseFloat("" + weatherResponse.main.temp_min) - 273.15) + "??C ";
                    String s_wt04 = getString(R.string.h1301_t012) + (weatherResponse.main.humidity)+" %";
                    String s_wt05 = weatherResponse.weather.get(0).description;
                    wt01.setText(s_wt01);
                    wt02.setText(s_wt02);
                    wt03.setText(s_wt03);
                    wt04.setText(s_wt05);
                    wt05.setText(s_wt04);
                   //====?????????????????????==============
                    tt010.setText(getString(R.string.h1301_t010) + (lat));
                    tt011.setText(getString(R.string.h1301_t011) + (lon));
                    //======?????? Internet ??????==================
//                    int b_id = weatherResponse.weather.get(0).id;
//                    String b_main = weatherResponse.weather.get(0).main;
//                    String b_description = weatherResponse.weather.get(0).description;
                    String b_icon = weatherResponse.weather.get(0).icon;
                    iconurl = "https://openweathermap.org/img/wn/" + b_icon + "@4x.png";
/*+++++++++++++++++++++
+    ??????Picasso????????????                +
+++++++++++++++++++++*/
                    Glide.with(i001)
                            .load(iconurl)
                            .into(i001);
                }
            }
            //----------------??????????????????????????????Bitmap-----------------
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

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                wt01.setText(t.getMessage());
            }
        });
    }


    private void updatePosition() {
        if (currentLocation == null) {
            lat = "3.24";
            lon = "1.52";
        } else {
            lat = Double.toString(currentLocation.getLatitude());
            lon = Double.toString(currentLocation.getLongitude());
        }
    }

    private void u_checkgps() {
 // ?????????????????????LocationManager??????
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
// ?????????????????????GPS
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
// ????????????????????????GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("????????????")
                    .setMessage("GPS???????????????????????????.\n"
                            + "????????????????????????????????????GPS?")
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
// ??????Intent?????????????????????????????????GPS??????
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("?????????", null).create().show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private LocationListener listener= new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
            updatePosition();
        }

        @Override
        public void onStatusChanged(String provider , int status , Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
        @Override
        protected void onResume() {
            super.onResume();
// ??????????????????????????????
            Criteria criteria = new Criteria();
            bestgps = manager.getBestProvider(criteria, true);
            try {
                if (bestgps != null) { // ???????????????????????????,???????????????
                    currentLocation = manager.getLastKnownLocation(bestgps);
                    manager.requestLocationUpdates(bestgps, minTime, minDistance, listener);
                } else { // ???????????????????????????,???????????????
                    currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            minTime, minDistance, listener);
                }
            } catch (SecurityException e) {
                Log.e(TAG, "GPS????????????..." + e.getMessage());
            }
            updatePosition(); // ????????????
        }

    private View.OnClickListener b002ON = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.h1301_icon:
                    if(account == null)
                        //************************************
                        //signIn();
                        //************************************
                    break;

                case R.id.h1301_t001:
                    intent.putExtra("class_title", getString(R.string.h1301_t003));
                    intent.setClass(H1301.this, H1302_about.class);
                    startActivity(intent);
                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//-----------------?????????????????????
            case R.id.h1302_Diab001:    //ok
                mLoginDlg.cancel();
                //*************************
                signOut();
                //*************************
                login.setVisible(true);
                logout.setVisible(false);
                Toast.makeText(getApplicationContext() , R.string.h1302_logoutSucc , Toast.LENGTH_LONG).show();
                img.setOnClickListener(this);
                break;
            case R.id.h1302_Diab002:    //??????
                mLoginDlg.cancel();
                Toast.makeText(getApplicationContext() , R.string.h1302_logoutCanc , Toast.LENGTH_LONG).show();
                break;
        }
    }

    //--------------Intent??????
    private View.OnClickListener b001ON = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.h1301_b001:
                    intent.putExtra("class_title" , getString(R.string.h1301_t006));
                    intent.setClass(H1301.this , H1401.class);
                    startActivity(intent);
                    break;
                case R.id.h1301_b002:
                    intent.putExtra("class_title" , getString(R.string.h1301_t004));
                    intent.setClass(H1301.this , H1501.class);
                    startActivity(intent);
                    break;
                case R.id.h1301_b003:
                    intent.putExtra("class_title" , getString(R.string.h1301_t005));
                    intent.setClass(H1301.this , H0601.class);
                    startActivity(intent);
                    break;
                case R.id.h1301_b004:
                    intent.putExtra("class_title" , getString(R.string.h1301_t008));
                    intent.setClass(H1301.this , H1201.class);
                    startActivity(intent);
                    break;
                case R.id.h1301_b005:
                    intent.putExtra("class_title" , getString(R.string.h1301_t007));
                    intent.setClass(H1301.this , H0701.class);
                    startActivity(intent);
                    break;
                case R.id.h1301_b006:
                    if(tr0203 == "1"){
                        intent.putExtra("class_title" , getString(R.string.h1301_t009));
                        intent.setClass(H1301.this , H0501.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_LONG).show();
                    }

                    break;
                case R.id.h1301_b008:
                    uri = Uri.parse("https://m.facebook.com/city2farmer");
                    Intent it=new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(it);
                    break;
                case R.id.h1301_t001:
                    intent.putExtra("class_title" , getString(R.string.h1301_t003));
                    intent.setClass(H1301.this , H1302_about.class);
                    startActivity(intent);
                    break;
            }
           // startActivity(intent);
        }
    };
    //--------------------------

    //--------------------------??????Menu

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.h1301_menu01 , menu);
        this.menu = menu;
        login = menu.findItem(R.id.h1301_login);
        logout = menu.findItem(R.id.h1301_logout);
        //***************************************************************************
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account == null) {
            u_logout();
        }else
            u_login();
        //***************************************************************************
//        icon = menu.findItem(R.id.h1301_icon01);
//        delete = menu.findItem(R.id.h1301_delete);
//        if(user==1)
//         logout();
//        if(user==0)
//        login();

//        login.setVisible(true);
//        logout.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.h1301_login:  //??????
                getIdToken();
               //******************************************************
                // signIn();
                //*****************************************************
//                login.setVisible(false);
//                logout.setVisible(true);
//                Toast.makeText(getApplicationContext(),R.string.h1302_loginSucc,Toast.LENGTH_LONG).show();

                break;
            case R.id.h1301_logout: //??????
                //***************************************************************************
                mLoginDlg = new Dialog(H1301.this);
                mLoginDlg.setTitle(getString(R.string.h1302_Diatitle));
                mLoginDlg.setCancelable(false);
                mLoginDlg.setContentView(R.layout.h1302_login_dlg);
                Button loginBtnOK = (Button) mLoginDlg.findViewById(R.id.h1302_Diab001)  ;
                Button loginBtnCancel = (Button) mLoginDlg.findViewById(R.id.h1302_Diab002);
                loginBtnOK.setOnClickListener( this);
                loginBtnCancel.setOnClickListener(this);
                mLoginDlg.show();
                tr0203="";

                //*****************************************************************************
                break;
                      case R.id.h1301_delete:   //??????
                          //signOut();
                          this.finish();
                          //this.onDestroy();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //==========??????Onclick====================
    private void getIdToken() {
        // Show an account picker to let the user choose a Google account from the device.
        // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
        // consent screen will be shown here.
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GET_TOKEN);

//           login.setVisible(false);
//           logout.setVisible(true);

        //Toast.makeText(getApplicationContext(),R.string.h1302_loginSucc,Toast.LENGTH_LONG).show();
    }

    //------------------------------??????????????????(??????app)
    @Override
    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext() , permissionsName[i] + "?????????????????????!" , Toast.LENGTH_LONG).show();
                        locationPermissionGranted = true;
                    } else {
                        Toast.makeText(getApplicationContext() , "?????????????????? " + permissionsName[i] , Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode , permissions , grantResults);
    }

    void checkRequiredPermission(final Activity activity) {
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }else{
                locationPermissionGranted = true;
            }
        }
        if (permissionsList.size()!=0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new
                    String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
        }
    }
    //===================================================================

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), R.string.h1301_toast_back,Toast.LENGTH_LONG).show();
    }

//------------------------------????????????
    private void signOut() {
        initDB();
        dbHf13.clearRec13();    //????????????
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //--START_EXCLUDE--
                        updateUI(null);
                        // [END_EXCLUDE]
                        img.setImageResource(R.drawable.googleg_color); //????????????

                    }
                });
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            //=========Google???????????????==================
            tr0202=account.getId();
            tr0203="1";
            tr0204=account.getEmail();
            tr0205=account.getPhotoUrl().toString().trim();
            dbHf13.insertRec13(tr0202,tr0203,tr0204,tr0205);   //????????????SQL

            //-------????????????--------------
            User_IMAGE = account.getPhotoUrl();
//            int a=0;

            if (User_IMAGE == null) {
                Uri noiconimg = Uri.parse("https://lh3.googleusercontent.com/pw/ACtC-3f7ifqOfGrkeKoxWel1YUubvk1UzdlwSpsIY_Wfxa3jCYE75R1aYZlFtZd-jvFPzp5aUNfJksNAtXYj0OhzV-brFWU7E81L8H5td0SZTDgeWDp7PdVcBwKYxChccjyhUsTjVb2L8Zrqh7xJEGBIuhyK=w200-h192-no?authuser=0");
                User_IMAGE= noiconimg;
                img.setImageURI(User_IMAGE);
                //return;

            }

            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    String url = params[0];
                    return getBitmapFromURL(url);
                }

                @Override
                protected void onPostExecute(Bitmap result) {

                    img.setImageBitmap(result);
                    super.onPostExecute(result);
                }
            }.execute(User_IMAGE.toString().trim());
            //-------------------------
        } else {

//            login_user.setVisibility(View.INVISIBLE);
//            login_b004.setVisibility(View.GONE);
//            login_b001.setVisibility(View.VISIBLE);
        }
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
    //**************************************************************************************


    @Override
    protected void onStart() {
        SharedPreferences A01 =getSharedPreferences("AA", 0);
        A01
                .edit()
                .putInt("AA",0)
                .commit();
        super.onStart();
        // --START on_start_sign_in--
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null && GoogleSignIn.hasPermissions(account, new Scope(Scopes.DRIVE_APPFOLDER))) {
            updateUI(account);
        } else {
            updateUI(null);
            //--END on_start_sign_in--
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_GET_TOKEN) {
            // [START get_id_token]
            // This task is always completed immediately, there is no need to attach an
            // asynchronous listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            // [END get_id_token]
            if(resultCode == -1){
                login.setVisible(false);
                logout.setVisible(true);
            }
        }
    }
    //--END onActivityResult--

    // --START handleSignInResult--
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "handleSignInResult:error", e);
            updateUI(null);
        }
    }
    // --END handleSignInResult--

//========???????????????==============
    private void u_login() {

        login.setVisible(false);
        logout.setVisible(true);

    }
//==========???????????????=============
    private void u_logout() {

        logout.setVisible(false);
        login.setVisible(true);
    }


    @Override
    protected void onDestroy() {
        this.finish();
        super.onDestroy();
    }
}