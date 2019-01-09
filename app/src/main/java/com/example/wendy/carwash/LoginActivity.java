package com.example.wendy.carwash;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.wendy.carwash.client.BookingActivity;
import com.example.wendy.carwash.client.Client;
import com.example.wendy.carwash.models.MySingleton;
import com.example.wendy.carwash.client.RegisterActivity;
import com.example.wendy.carwash.staff.Staff;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {
    //Defining views
    Button sign_in_btn;
    EditText et_username, et_password;
    String username, password;
    TextView tv_forgot_password, tv_register;
    private static final int REQUEST_READ_CONTACTS = 0;

    AlertDialog.Builder builder;
    String url = "http://evolve-ict.co.za/login.php";
    private AutoCompleteTextView mEmailView, mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Getting id by their xml
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        sign_in_btn =  findViewById(R.id.bt_sign_in);
        et_username =  findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_register = findViewById(R.id.tv_register);


        builder = new AlertDialog.Builder(LoginActivity.this);
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(LoginActivity.this, BookingActivity.class);
                startActivity(history);
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(history);
            }
        });
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                password = et_password.getText().toString();
                boolean cancel = false;
                View focusView = null;
                //tv_forgot_password.setText(username + " " + password);

                if (TextUtils.isEmpty(username)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                } else if (!isEmailValid(username)) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailView;
                    cancel = true;
                }
                else if (TextUtils.isEmpty(password)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                }
                else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");

                                        builder.setTitle("Response");
                                        builder.setMessage(message);

                                        if(code.equals("login_success")){

                                            String user_type = jsonObject.getString("user_type");
                                            user_type = user_type.toLowerCase();
                                            String name = jsonObject.getString("name");
                                            String user_id = jsonObject.getString("user_id");
                                            CreateSessions(user_id, name);
                                            //DisplayAlert(code, user_type);
                                            if (code.equals("login_success")) {

                                                if(user_type.equals("staff") ){
                                                    Intent mainPage = new Intent(LoginActivity.this, Client.class);
                                                    startActivity(mainPage);
                                                }
                                                else if(user_type.equals("client") ){
                                                    Intent mainPage = new Intent(LoginActivity.this, Staff.class);
                                                    startActivity(mainPage);
                                                }
                                            }


                                        }
                                        if(code.equals("login_failed")){
                                            DisplayAlert(code);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("username", username);
                            params.put("password", password);

                            return params;
                        }
                    };
                    MySingleton.getInstance(LoginActivity.this).addToRequestque(stringRequest);

                }
            }
        });
    }

    private boolean isEmailValid(String username) {
            //TODO: Replace this with your own logic
            return username.contains("@");
        }

        private boolean isPasswordValid(String password) {
            //TODO: Replace this with your own logic
            return password.length() > 4;
        }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, LoginActivity.this);
    }
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
    public void CreateSessions(String user_id, String name) {
        //***************** Session *****************
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);

        String user_id_session = preferences.getString(user_id + "data", user_id);
        String name_session = preferences.getString(name + "data", name);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id", user_id);
        editor.putString("name", name);

        editor.commit();
        //*******************************************
    }

    public void DisplayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error")) {
                    et_password.setText("");
                }

                else if (code.equals("login_failed")) {
                    et_password.setText("");
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void DisplayAlert(final String code, final String user_type) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error")) {
                    et_password.setText("");
                }
                else if (code.equals("login_failed")) {
                    et_password.setText("");
                }
                else if (code.equals("login_success")) {

                    if(user_type.equals("staff") ){
                        Intent mainPage = new Intent(LoginActivity.this, Client.class);
                        startActivity(mainPage);
                    }
                    else if(user_type.equals("client") ){
                        Intent mainPage = new Intent(LoginActivity.this, Staff.class);
                        startActivity(mainPage);
                    }
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public Loader<Object> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object o) {

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }
}
