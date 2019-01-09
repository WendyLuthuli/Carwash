package com.example.wendy.carwash.client;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.wendy.carwash.LoginActivity;
import com.example.wendy.carwash.R;
import com.example.wendy.carwash.models.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity1 extends AppCompatActivity {
    Button register_btn;
    EditText et_username, et_email, et_firstname, et_lastname, et_password;
    String username, email, firstname, lastname, password;
    AlertDialog.Builder builder;
    String url = "http://evolve-ict.co.za/register.php";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    boolean valid_email = false;
    ProgressDialog progressDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            Intent logout = new Intent(RegisterActivity1.this, LoginActivity.class);
            startActivity(logout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_btn = findViewById(R.id.btn_register);
        et_username =  findViewById(R.id.et_username);
        et_firstname = findViewById(R.id.et_firstname);
        et_lastname =  findViewById(R.id.et_lastname);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_email);

        builder = new AlertDialog.Builder(RegisterActivity1.this);

        username = et_username.getText().toString();
        email = et_email.getText().toString();
        firstname = et_firstname.getText().toString();
        lastname = et_lastname.getText().toString();
        username = et_username.getText().toString();
        password = et_password.getText().toString();

        et_email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                email = et_email.getText().toString();

                if (email.matches(emailPattern) && s.length() > 0)
                {
                   valid_email = true;
                }
                else
                {valid_email = false;
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showProgressDialog();
                username = et_username.getText().toString();
                email = et_email.getText().toString();
                firstname = et_firstname.getText().toString();
                lastname = et_lastname.getText().toString();
                password = et_password.getText().toString();

                if ( username.equals("") || email.equals("") || firstname.equals("") || lastname.equals("")) {
                    builder.setTitle("Empty Fields");
                    builder.setMessage("Please fill in all fields");
                    DisplayAlert("input_error");
                }else if(!valid_email) {
                    builder.setTitle("Invalid Fields");
                    builder.setMessage("Please enter a valid email address");
                    DisplayAlert("input_error");
                }else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        dismissProgressBar();

                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");
                                        if(code.equals("reg_failed")) {
                                            builder.setTitle("Response");
                                        }
                                        else if(code.equals("mail_error")) {
                                            builder.setTitle("Mail Error");
                                        }
                                        else {
                                            builder.setTitle("Response");
                                        }
                                        builder.setMessage(message);
                                        DisplayAlert(code);
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

                            params.put("email", email);
                            params.put("username", username);
                            params.put("firstname", firstname);
                            params.put("lastname", lastname);
                            params.put("password", password);

                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy( 30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(RegisterActivity1.this).addToRequestque(stringRequest);

                }
            }
        });
    }

    public void DisplayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("reg_success")) {
                    et_email.setText("");
                    et_username.setText("");
                    et_firstname.setText("");
                    et_lastname.setText("");
                    Intent delayed = new Intent(RegisterActivity1.this, LoginActivity.class);
                    startActivity(delayed);
                }
                else {
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Registering RSA ...");
        progressDialog.show();
    }
    private void  dismissProgressBar() {
        // To Dismiss progress dialog
        progressDialog.dismiss();
    }
}
