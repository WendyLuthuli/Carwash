package com.example.wendy.carwash.client;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wendy.carwash.LoginActivity;
import com.example.wendy.carwash.MainActivity;
import com.example.wendy.carwash.R;
import com.example.wendy.carwash.adapters.ServicesSpinnerAdapter;
import com.example.wendy.carwash.adapters.TimeSpinnerAdapter;
import com.example.wendy.carwash.adapters.VehicleSpinnerAdapter;
import com.example.wendy.carwash.models.MySingleton;
import com.example.wendy.carwash.models.ServicesDataObject;
import com.example.wendy.carwash.models.TimeDataObject;
import com.example.wendy.carwash.models.VehicleDataObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {
    public Spinner sp_services, sp_vehicle, sp_time;
    protected List<ServicesDataObject>spServicesData;
    protected List<TimeDataObject> spTimeData;
    protected List<VehicleDataObject> spVehicleData;

    String service, time, vehicle, sService, sTime, sVehicle;
    String urlService = "http://evolve-ict.co.za/services.php";
    String urlVehicle = "http://evolve-ict.co.za/vehicles.php";
    String urlAvailTime = "http://evolve-ict.co.za/avail_times.php";
    String urlBooking= "http://evolve-ict.co.za/booking.php";

    TextView tv_details, tv_provider, tv_count;
    AlertDialog.Builder builder;

    SimpleDateFormat dateFormatter, dateFormatter2;

    int mYear, mMonth, mDay;
    String date, dateformatted, booking_date, user_id, users_name;
    TextView tv_date;
    Button submit;

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
            Intent logout = new Intent(BookingActivity.this, LoginActivity.class);
            startActivity(logout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        //********************************************
        SharedPreferences preferences =  BookingActivity.this.getSharedPreferences("MYPREFS", BookingActivity.MODE_PRIVATE);

        user_id = preferences.getString("user_id", "");
        users_name = preferences.getString("name", "");
        //********************************************
        tv_date = findViewById(R.id.tv_date);
        requestJsonObject();

        builder = new AlertDialog.Builder(BookingActivity.this);
        Date now = new Date();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        dateformatted = df.format(c);
        tv_date.setText(dateformatted);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(BookingActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                if (year < mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear,mMonth,mDay);
                                {
                                    booking_date = "0" + dayOfMonth;
                                }

                                SimpleDateFormat dateFormatter;
                                dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                                dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd");

                                Calendar calander = Calendar.getInstance();
                                calander.setTimeInMillis(0);
                                calander.set(year, monthOfYear, dayOfMonth, 0, 0, 0);

                                tv_date.setText(dateFormatter.format(calander.getTime()));
                                dateformatted = dateFormatter2.format(calander.getTime());
                                booking_date = tv_date.getText().toString();

                                getAvailableTime(dateformatted);

                            }
                        }, mYear, mMonth, mDay);
                //dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }

        });

        submit = findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showProgressDialog();
//                if ( username.equals("") || email.equals("") || firstname.equals("") || lastname.equals("")) {
//                    builder.setTitle("Empty Fields");
//                    builder.setMessage("Please fill in all fields");
//                    DisplayAlert("input_error");
//                }else if(!valid_email) {
//                    builder.setTitle("Invalid Fields");
//                    builder.setMessage("Please enter a valid email address");
//                    DisplayAlert("input_error");
//                }else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, urlBooking,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        //dismissProgressBar();

                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");
                                        if(code.equals("booking_failed")) {
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

                            params.put("regno", sVehicle);
                            params.put("timeslot", sTime);
                            params.put("booking_date", booking_date);
                            params.put("service", service);
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy( 30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(BookingActivity.this).addToRequestque(stringRequest);

                //}
            }
        });
    }

    private void getAvailableTime(final String dateformatted) {
        RequestQueue timeQueue = Volley.newRequestQueue(this);
        StringRequest stringTimeRequest = new StringRequest(com.android.volley.Request.Method.GET, urlAvailTime, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spTimeData = Arrays.asList(mGson.fromJson(response, TimeDataObject[].class));
                //display first question to the user
                if(null != spTimeData){
                    sp_time = (Spinner) findViewById(R.id.sp_time);
                    sp_time.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long fault_id) {
                                    TimeDataObject selected = (TimeDataObject) parent.getItemAtPosition(position);
                                    //get selected fault type
                                    time = selected.getTimeFrom();
                                    sTime = selected.getTimeslotID();
                                }
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                    assert sp_vehicle != null;
                    TimeSpinnerAdapter spinnerAdapter = new TimeSpinnerAdapter(BookingActivity.this, spTimeData);
                    sp_time.setAdapter(spinnerAdapter);
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
                params.put("booking_date", dateformatted);
                return params;
            }
        };
        timeQueue.add(stringTimeRequest);
    }

    private void requestJsonObject(){
        RequestQueue vehicleQueue = Volley.newRequestQueue(this);
        StringRequest stringVehicleRequest = new StringRequest(com.android.volley.Request.Method.GET, urlVehicle, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spVehicleData = Arrays.asList(mGson.fromJson(response, VehicleDataObject[].class));
                //display first question to the user
                if(null != spVehicleData){
                    sp_vehicle = (Spinner) findViewById(R.id.sp_vehicle);
                    sp_vehicle.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long fault_id) {
                                    VehicleDataObject selected = (VehicleDataObject) parent.getItemAtPosition(position);
                                    //get selected fault type
                                    vehicle = selected.getVehicle();
                                    sVehicle = selected.getRegNo();
                                }
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                    assert sp_vehicle != null;
                    VehicleSpinnerAdapter spinnerAdapter = new VehicleSpinnerAdapter(BookingActivity.this, spVehicleData);
                    sp_vehicle.setAdapter(spinnerAdapter);
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
                params.put("user_id", "1");
                return params;
            }
        };
        vehicleQueue.add(stringVehicleRequest);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, urlService, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spServicesData = Arrays.asList(mGson.fromJson(response, ServicesDataObject[].class));
                //display first question to the user
                if(null != spServicesData){
                    sp_services = (Spinner) findViewById(R.id.sp_service);
                    sp_services.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long fault_id) {
                                    ServicesDataObject selected = (ServicesDataObject) parent.getItemAtPosition(position);
                                    //get selected fault type
                                    service = selected.getServiceID();
                                    sService = selected.getServiceName();
                                }
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                    assert sp_services != null;
                    ServicesSpinnerAdapter spinnerAdapter = new ServicesSpinnerAdapter(BookingActivity.this, spServicesData);
                    sp_services.setAdapter(spinnerAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    public void DisplayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("reg_success")) {
                    Intent delayed = new Intent(BookingActivity.this, MainActivity.class);
                    startActivity(delayed);
                }
                else {
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
