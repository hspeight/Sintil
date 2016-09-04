package uk.co.sintildate.sintil;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import static android.content.Intent.ACTION_PICK;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import petrov.kristiyan.colorpicker.ColorPal;
import petrov.kristiyan.colorpicker.ColorPicker;

public class NewEventActivity extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime, txtTitle, txtDesc;
    TextView txtSummary;
    private int mYear, mMonth, mDay, mHour, mMinute, direction, epoch, mColor;
    Resources res;
    DateValidator dateValidator;
    String DEBUG_TAG = "NEA";
    private Toolbar toolbar;
    TextInputLayout tilDate, tilTitle;
    boolean formIsValid;
    MyDBHandler dbHandler;
    private static final int GALLERY_REQUEST = 4407;
    private String image;
    //ImageView imgViewColor;
    Button imgViewColor;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_new_event);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        dateValidator = new DateValidator();
        //btnDatePicker=(Button)findViewById(R.id.btn_date);
        //btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtTitle = (EditText) findViewById(R.id.txtEventTitle);
        txtDesc = (EditText) findViewById(R.id.txtEventDescription);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);
        txtSummary = (TextView) findViewById(R.id.summary);
        tilDate = (TextInputLayout) findViewById(R.id.tilDate);
        tilTitle = (TextInputLayout) findViewById(R.id.tilTitle);
        //imgViewColor = (ImageView) findViewById(R.id.imgViewPaletteColor);
        //imgViewColor = (Button) findViewById(R.id.imgViewPaletteColor);
        //tilDate.setErrorEnabled(true);

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
        direction = countDown.isChecked() ? 1 : 0; // convert direction button to int
        cd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                direction = countDown.isChecked() ? 1 : 0; // convert direction button to int
                construct_summary();

            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);

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

        String mTimeIn = txtTime.getText().toString();
        final Calendar cal = Calendar.getInstance();
        if(mTimeIn.matches("")) { // field is blank
            // Get Current Time
            mHour = cal.get(Calendar.HOUR_OF_DAY);
            mMinute = cal.get(Calendar.MINUTE);
        } else {
            mHour = Integer.parseInt(mTimeIn.split(":")[0]);
            mMinute = Integer.parseInt(mTimeIn.split(":")[1]);
        }
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

    public void save_clicked (View v) {

        formIsValid = true;
        String value = txtTime.getText().toString();
        if (TextUtils.isEmpty(value)) {
            final Calendar c = Calendar.getInstance();
            //txtTime.setText(c.get(Calendar.HOUR_OF_DAY) + ":" +  c.get(Calendar.MINUTE)); // only if no time is entered
            txtTime.setText(String.format(res.getString(R.string.display_time), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));
        }

       // }
        //Log.d(DEBUG_TAG, ">> in");
        epoch = 0;
        //Log.d(DEBUG_TAG, ">> " + txtDate.getText().toString() + txtTime.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm", java.util.Locale.getDefault());
        //String dateInString = "03-09-2014 21:17";
        try {
            Date mDate = sdf.parse(txtDate.getText().toString() + " " + txtTime.getText().toString());
            epoch = (int)(mDate.getTime() / 1000);
            //Log.d(DEBUG_TAG, ">> " + txtDate.getText().toString() +"/" + epoch);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        boolean valid_date = validate_date_and_time(epoch);
        if (valid_date) {
            tilDate.setError(null);
            //Toast.makeText(this, "Good date ", Toast.LENGTH_LONG).show();
        } else {
            formIsValid = false;
            tilDate.setError("Cannot count down to a past date"); // need to set a string res for this
        }

        if (formIsValid) {
            if (TextUtils.isEmpty(txtTitle.getText().toString())) {
                formIsValid = false;
                tilTitle.setError("Please enter a title for the event");
            } else {
                tilTitle.setError(null);
            }
        }

        if (formIsValid) {
            //Toast.makeText(this, "Form is good", Toast.LENGTH_LONG).show();
            add_event();
            finish(); // need to check result was good before finish
        }

        //Toast.makeText(this, "OKAY HAS BEEN CLICKED ", Toast.LENGTH_LONG).show();
    }

    public void add_event() {

        dbHandler = new MyDBHandler(this, null, null, 1);

        Events myEvent = new Events();

        myEvent.set_eventname(txtTitle.getText().toString());
        myEvent.set_eventinfo(txtDesc.getText().toString());
        myEvent.set_evtime(epoch);
        myEvent.set_direction(direction);
        myEvent.set_evtype("R");
        myEvent.set_evstatus("A");
        myEvent.set_bgcolor(mColor);
        myEvent.set_timeunits("3"); // needs sorting

        dbHandler.addEvent(myEvent);
    }

    public boolean validate_date_and_time(int dtIn) {
        //dtIn = 1472933854;
        //if (direction == 1 && dtIn > currentDate)
        //Log.d(DEBUG_TAG,direction + "/" + dtIn + "/" + System.currentTimeMillis() / 1000);
        return ((direction == 1 && dtIn > System.currentTimeMillis() / 1000) || direction == 0);
    }

    public void cancel_clicked (View v) {
        finish();
    }

    public void camera_clicked(View v){
        Intent gallery = new Intent(ACTION_PICK, EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_REQUEST);
    }

    public void preview_clicked (View v) {
        Toast.makeText(this, "Preview HAS BEEN CLICKED ", Toast.LENGTH_LONG).show();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            image = data.getData().toString();
            //loadImage();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void palette_clicked(View v){
        //Toast.makeText(this, "Pallette HAS BEEN CLICKED ", Toast.LENGTH_LONG).show();
        final ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setRoundColorButton(true).setTitle("Chose the canvas color for this event");
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int color) {
                Toast.makeText(getApplicationContext(), "I chose " + color, Toast.LENGTH_LONG).show();
                Log.d(DEBUG_TAG,color + " chosen");
                fab.setBackgroundTintList(ColorStateList.valueOf(color));
                mColor = color;
                //imgViewColor.setBackgroundTintList(color);
            }
        }).disableDefaultButtons(true).setColumns(5).show();
    }

}