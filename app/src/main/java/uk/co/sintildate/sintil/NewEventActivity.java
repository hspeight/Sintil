package uk.co.sintildate.sintil;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity implements
        View.OnClickListener {

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Resources res;
    DateValidator dateValidator;
    String DEBUG_TAG = "NEA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_event);
        dateValidator = new DateValidator();
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        res = getResources();

        RadioGroup cd = (RadioGroup) findViewById(R.id.radioDirection);
        final RadioButton countDown = (RadioButton) findViewById(R.id.radioButtonCountDown);
        cd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int direction = countDown.isChecked() ? 1 : 0; // convert direction button to int
                if (direction == 1) {
                    btnDatePicker.setText(res.getString(R.string.target_date));
                    btnTimePicker.setText(res.getString(R.string.target_time));
                } else {
                    btnDatePicker.setText(res.getString(R.string.start_date));
                    btnTimePicker.setText(res.getString(R.string.start_time));
                }
                //Toast.makeText(getApplicationContext(), "button is "  + countUp.isChecked(), Toast.LENGTH_SHORT).show();

            }
        });



    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {
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


}