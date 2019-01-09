package com.example.wendy.carwash.client;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.wendy.carwash.models.MySingleton;
import com.example.wendy.carwash.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCarActivity extends AppCompatActivity {
    Button add_vehicle_btn;
    EditText et_reg_no, et_model, et_desc;
    String reg_no, model, desc, user_id;
    AlertDialog.Builder builder;
    String url = "http://evolve-ict.co.za/add_car.php";
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
            Intent logout = new Intent(AddCarActivity.this, LoginActivity.class);
            startActivity(logout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        add_vehicle_btn = findViewById(R.id.btn_add_vehicle);
        et_model =  findViewById(R.id.et_model);
        et_reg_no = findViewById(R.id.et_reg_no);
        et_desc =  findViewById(R.id.et_desc);

        builder = new AlertDialog.Builder(AddCarActivity.this);

        reg_no = et_reg_no.getText().toString();
        model = et_model.getText().toString();
        desc = et_desc.getText().toString();

        add_vehicle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                reg_no = et_reg_no.getText().toString();
                model = et_model.getText().toString();
                desc = et_desc.getText().toString();

                if ( reg_no.equals("") || model.equals("")) {
                    builder.setTitle("Empty Fields");
                    builder.setMessage("Please fill in all required fields");
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

                            params.put("reg_no", reg_no);
                            params.put("model", model);
                            params.put("desc", desc);
                            params.put("user_id", user_id);

                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy( 30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(AddCarActivity.this).addToRequestque(stringRequest);

                }
            }
        });
    }

    public void DisplayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("reg_success")) {
                    et_desc.setText("");
                    et_model.setText("");
                    et_reg_no.setText("");
                    Intent delayed = new Intent(AddCarActivity.this, Client.class);
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
        progressDialog.setMessage("Adding Vehicle...");
        progressDialog.show();
    }
    private void  dismissProgressBar() {
        // To Dismiss progress dialog
        progressDialog.dismiss();
    }
}
