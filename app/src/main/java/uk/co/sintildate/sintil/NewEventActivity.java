package uk.co.sintildate.sintil;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    TextView txtSummary;
    private int mYear, mMonth, mDay, mHour, mMinute, direction;
    Resources res;
    DateValidator dateValidator;
    String DEBUG_TAG = "NEA";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_new_event);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        dateValidator = new DateValidator();
        //btnDatePicker=(Button)findViewById(R.id.btn_date);
        //btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        txtSummary=(TextView)findViewById(R.id.summary);

//        btnDatePicker.setOnClickListener(this);
//        btnTimePicker.setOnClickListener(this);
        //txtDate.setOnClickListener(this);
        //touchListener MyTouchListener touchListener = new MyTouchListener();
        txtDate.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // your code here....
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    //Log.d(DEBUG_TAG, "touched");
                    process_touch(view);
                }
                return false;
            }
        });
        txtTime.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // your code here....
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    //Log.d(DEBUG_TAG, "time touched");
                    process_touch(view);
                }
                return false;
            }
        });

        txtDate.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                construct_summary();
            }
        });
        txtTime.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                construct_summary();
            }
        });

        res = getResources();

        RadioGroup cd = (RadioGroup) findViewById(R.id.radioDirection);
        final RadioButton countDown = (RadioButton) findViewById(R.id.radioButtonCountDown);
        cd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                direction = countDown.isChecked() ? 1 : 0; // convert direction button to int
                construct_summary();

            }
        });

    }

    public void process_touch(View v) {
        // This will be called when either the date or time edittext has been touched
        switch (v.getId()) {
            case R.id.in_date:
                //Toast.makeText(NewEventActivity.this, "Date touched", Toast.LENGTH_SHORT).show();
                do_date(v);
                break;
            case R.id.in_time:
                //Toast.makeText(NewEventActivity.this, "Time touched", Toast.LENGTH_SHORT).show();
                do_time(v);
                break;
        }
    }

    public void do_date(View v){
        String mDateIn = txtDate.getText().toString();
        final Calendar cal = Calendar.getInstance();
        // Get Current Date if nothing has been entered
        if(mDateIn.matches("")) { // field is blank
            mYear = cal.get(Calendar.YEAR);
            mMonth = cal.get(Calendar.MONTH);
            mDay = cal.get(Calendar.DAY_OF_MONTH);
        } else {
            boolean result = dateValidator.isThisDateValid(mDateIn, "dd-MM-yyyy");
            //Log.d(DEBUG_TAG,"result for " +  txtDate.getText().toString() + " is " + result);
            if(result) {
                mDay = Integer.parseInt(mDateIn.split("-")[0]);
                mMonth = Integer.parseInt(mDateIn.split("-")[1]) -1;
                mYear = Integer.parseInt(mDateIn.split("-")[2]);
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        txtDate.setText(String.format(res.getString(R.string.display_date),dayOfMonth, monthOfYear + 1, year));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void do_time(View v) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        //String x = String.format(res.getString(R.string.display_time),hourOfDay);
                        txtTime.setText(String.format(res.getString(R.string.display_time),hourOfDay,minute));
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void construct_summary() {
        String s;
        if (direction == 1) {
            s = String.format(res.getString(R.string.count_down_text), txtDate.getText(), txtTime.getText());
        } else {
            s = String.format(res.getString(R.string.count_up_text), txtDate.getText(), txtTime.getText());
        }
        txtSummary.setText(s);
    }
/*
    @Override
    public void onClick(View v) {

        if (v == txtDate) {
            String mDateIn = txtDate.getText().toString();
            final Calendar cal = Calendar.getInstance();
            // Get Current Date if nothing has been entered
            if(mDateIn.matches("")) { // field is blank
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);
            } else {
                //cal.set(Calendar.YEAR, 2014);
                //cal.set(Calendar.MONTH, 11);
                //cal.set(Calendar.MINUTE, 33);
                boolean result = dateValidator.isThisDateValid(mDateIn, "dd-MM-yyyy");
                //Log.d(DEBUG_TAG,"result for " +  txtDate.getText().toString() + " is " + result);
                if(result) {
                    mDay = Integer.parseInt(mDateIn.split("-")[0]);
                    mMonth = Integer.parseInt(mDateIn.split("-")[1]) -1;
                    mYear = Integer.parseInt(mDateIn.split("-")[2]);
                }
                //mYear = 2014;
                //mMonth = 10;
                //mDay = 13;
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            txtDate.setText(String.format(res.getString(R.string.display_date),dayOfMonth, monthOfYear + 1, year));
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                        //String x = String.format(res.getString(R.string.display_time),hourOfDay);
                            txtTime.setText(String.format(res.getString(R.string.display_time),hourOfDay,minute));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
*/



    public void save_clicked (View v) {
        Toast.makeText(this, "OKAY HAS BEEN CLICKED ", Toast.LENGTH_LONG).show();

    }

    public void cancel_clicked (View v) {
        finish();
    }
}