package uk.co.sintildate.sintil;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.lzyzsd.randomcolor.RandomColor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class EventEditorDialogFragment extends DialogFragment {
    int mRecordID;
    MyDBHandler dbHandler;
    boolean formIsValid;
    private EditText mTitle, mSubtitle, mDateIn, mTimeIn;
    private ImageButton mCalendar;
    Resources res;
    int mDirection;
    private int mYear, mMonth, mDay, mHour, mMinute, direction, epoch, mColor;
    TextInputLayout tilDate, tilTitle;
    Events myEvent;
    RadioGroup rg;
    DatePicker datePicker;
    boolean datePickerDialogShowing;
    String DEBUG_TAG = "NEDF";

    //Create a new instance of EventEditorDialogFragment, providing recordID as an argument.
    static EventEditorDialogFragment newInstance(int recordID) {
        EventEditorDialogFragment f = new EventEditorDialogFragment();

        // Supply recordID input as an argument.
        Bundle args = new Bundle();
        args.putInt("record_id", recordID);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHandler = new MyDBHandler(getActivity(), null, null, 1);

        mRecordID = getArguments().getInt("record_id");
        if(mRecordID > 0) {
            //Log.d(DEBUG_TAG,">" + mRecordID);
            myEvent = dbHandler.getMyEvent(mRecordID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_event, container);
        mTitle = (EditText) view.findViewById(R.id.txtEventTitle);
        mSubtitle = (EditText) view.findViewById(R.id.txtEventDescription);
        mDateIn = (EditText) view.findViewById(R.id.in_date);
        mTimeIn = (EditText) view.findViewById(R.id.in_time);
        mCalendar = (ImageButton) view.findViewById(R.id.imgCalendar);
        tilDate = (TextInputLayout) view.findViewById(R.id.tilDate);
        tilTitle = (TextInputLayout) view.findViewById(R.id.tilTitle);
        res = getResources();
        getDialog().setTitle("New Event");
        getActivity().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //prevent keyboard popup
        //getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT); //prevent keyboard popup
        // Watch for button clicks.
        Button button1 = (Button) view.findViewById(R.id.btnCancel);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                //((FragmentDialog)getActivity()).showDialog();
                dismiss();
            }
        });
        Button button2 = (Button) view.findViewById(R.id.btnOK);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                //((FragmentDialog)getActivity()).showDialog();
                boolean formValid = validate_form();
                //Log.d(DEBUG_TAG, "formValid=" +formValid);
                if (formValid) {
                    add_record();
                    //getActivity().startActivityForResult(getActivity().getIntent(), 10);
                    Intent intent = new Intent(getActivity(), EventCounterFragment.class);
                    intent.putExtra("ROW_INDEX", HSFrag.eventRecord.size() - 1); // Start viewpager at this record
                    getActivity().startActivity(intent);
                    dismiss();
                }
            }
        });

        rg = (RadioGroup) view.findViewById(R.id.radioDirection);
        final RadioButton countDown = (RadioButton) view.findViewById(R.id.radioButtonCountDown);
        mDirection = countDown.isChecked() ? 1 : 0; // convert direction button to int
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mDirection = countDown.isChecked() ? 1 : 0; // convert direction button to int
                //construct_summary();

            }
        });

        mDateIn.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // your code here....
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    //Log.d(DEBUG_TAG, "touched");
                    process_touch(view);
                }
                return false;
            }
        });
        mCalendar.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // your code here....
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    //Log.d(DEBUG_TAG, "touched");
                    process_touch(view);
                }
                return false;
            }
        });
        /*
        mTimeIn.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // your code here....
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    //Log.d(DEBUG_TAG, "touched");
                    process_touch(view);
                }
                return false;
            }
        });
        */

        if (mRecordID > 0)
            populate_form();

        return view;
    }

    public void populate_form() {

        mTitle.setText(myEvent.get_eventname());
        mSubtitle.setText(myEvent.get_eventinfo());
        rg.check(rg.getChildAt(1 - myEvent.get_direction()).getId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm", java.util.Locale.getDefault());
        String s[] = sdf.format((long)myEvent.get_evtime() * 1000).split(" ");
        mDateIn.setText(s[0]);
        mTimeIn.setText(s[1]);

        //mDateIn.setText(sdf.format(myEvent.get_evtime()));


    }

    public void process_touch(View v) {
        // This will be called when either the date or time edittext has been touched
        switch (v.getId()) {
            case R.id.in_date:
                Toast.makeText(getActivity(), "Date touched", Toast.LENGTH_SHORT).show();
                //do_date(v);
                break;
            case R.id.in_time:
                Toast.makeText(getActivity(), "Time touched", Toast.LENGTH_SHORT).show();
                //do_time(v);
                break;
            case R.id.imgCalendar:
                show_dialog();
                //Toast.makeText(getActivity(), "Calendar touched", Toast.LENGTH_SHORT).show();
                //do_time(v);
                break;
        }
    }

    public void show_dialog() {
        //Toast.makeText(getActivity(), "Calendar touched", Toast.LENGTH_SHORT).show();

        final View dialogView = View.inflate(getActivity(), R.layout.date_time_custom_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        //Log.d(DEBUG_TAG,"datePicker " + R.id.datePicker);
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                //TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);

                //Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                //        datePicker.getMonth(),
                //        datePicker.getDayOfMonth()

                        //timePicker.getCurrentHour(),
                        //timePicker.getCurrentMinute());
                 //      );
                mDateIn.setText(String.format(res.getString(R.string.display_date),datePicker.getDayOfMonth(),datePicker.getMonth() +1, datePicker.getYear()));
                mTimeIn.setText("12:34");
                //time = calendar.getTimeInMillis();
                alertDialog.dismiss();
                datePickerDialogShowing = false;
            }});
        alertDialog.setView(dialogView);
        //Log.d(DEBUG_TAG,"Showing");
        alertDialog.show();
        datePickerDialogShowing = true;
    }

    public boolean validate_form() {

        formIsValid = true;

        if (TextUtils.isEmpty(mTitle.getText().toString())) {
            formIsValid = false;
            tilTitle.setError(res.getString(R.string.title_error1));
        } else {
            tilTitle.setError(null);
        }

        if (formIsValid) {
            //String value = mTimeIn.getText().toString();
            //Log.d(DEBUG_TAG,mTimeIn.getText().toString());

            epoch = 0;
            //Log.d(DEBUG_TAG, ">> " + mDateIn.getText().toString() + mDateIn.getText().toString());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm", java.util.Locale.getDefault());
            //String dateInString = "03-09-2014 21:17";
            Date mDate = new Date();
            try {
                //Log.d(DEBUG_TAG,mDateIn.getText().toString() + " " + mTimeIn.getText().toString());
                mDate = sdf.parse(mDateIn.getText().toString() + " " + mTimeIn.getText().toString());
            } catch (ParseException e) {
                formIsValid = false;
                e.printStackTrace();
            }
            epoch = (int)(mDate.getTime() / 1000);
            //Log.d(DEBUG_TAG, ">>" + mDateIn.getText().toString() + " " + mTimeIn.getText().toString() + "<<");
            boolean valid_date = validate_date_and_time(epoch);
            //Log.d(DEBUG_TAG,epoch + "/" + System.currentTimeMillis() / 1000);
            if (valid_date) {
                tilDate.setError(null);
                //Toast.makeText(this, "Good date ", Toast.LENGTH_LONG).show();
            } else {
                //Log.d(DEBUG_TAG,"ERROR");
                formIsValid = false;
                tilDate.setError(res.getString(R.string.date_error1)); // need to set a string res for this
            }
            if (TextUtils.isEmpty(mTimeIn.getText().toString())) {
                final Calendar c = Calendar.getInstance();
                //Log.d(DEBUG_TAG,"in");
                mTimeIn.setText(String.format(res.getString(R.string.display_time), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)));
            }
        }

        //if (formIsValid) {
        //    //Toast.makeText(this, "Form is good", Toast.LENGTH_LONG).show();
        //    add_record();
        //    getActivity().finish(); // need to check result was good before finish
        //}

        return formIsValid;
    }

    public boolean validate_date_and_time(int dtIn) {





        //if((mDirection == 1 && dtIn > System.currentTimeMillis() / 1000) || (mDirection == 0)) {
        //    return true;
        //} else {
        //    return false;
        //}

        return ((mDirection == 1 && dtIn > System.currentTimeMillis() / 1000) || mDirection == 0);
    }


    public void add_record() {
        Random r = new Random();
        RandomColor randomColor = new RandomColor();
        int mycolor = randomColor.randomColor();

        Events myEvent = new Events();

        myEvent.set_eventname(mTitle.getText().toString());
        myEvent.set_eventinfo(mSubtitle.getText().toString());
        myEvent.set_evtime(epoch);
        //myEvent.set_evtime(r.nextInt(1473214474));
        myEvent.set_direction(mDirection);
        myEvent.set_evtype("R");
        myEvent.set_evstatus("A");
        //myEvent.set_bgcolor(mColor);
        myEvent.set_bgcolor(mycolor);
        myEvent.set_timeunits("3"); // needs sorting

        dbHandler.addEvent(myEvent);
        HSFrag.eventRecord.add(myEvent);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("dpShowing", datePickerDialogShowing);
        /*
        if(datePicker != null && datePicker.isShown()) {
            Log.d(DEBUG_TAG, "picker showing");
        } else {
            Log.d(DEBUG_TAG, "picker not showing");
        }
    */
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //Log.d(DEBUG_TAG,"onViewStateRestored with savedInstanceState " + savedInstanceState);
        if(savedInstanceState != null && (savedInstanceState.getSerializable("dpShowing") != null)) {
            datePickerDialogShowing = (boolean) savedInstanceState.getSerializable("dpShowing");
            if(datePickerDialogShowing)
                show_dialog();
        //    savedInstanceState.clear();
       }
    }
}