package uk.co.sintildate.sintil;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class NewEventPrefsFragment extends PreferenceFragmentCompat {

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
    int[] bgcolorArray;

    SharedPreferences sharedPreferences;
    SharedPreferences settings;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        // Clear any existing preferences
        settings = getActivity().getSharedPreferences("uk.co.sintildate.sintil_preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();

        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        final TextView txtview1 = new TextView(getActivity());
        final TextView txtview2 = new TextView(getActivity());
        // create 3 textviews in the toolbar
        txtview1.setText(getResources().getString(R.string.text_cancel));
        txtview2.setText(getResources().getString(R.string.text_save));
        txtview1.setTextSize(24);
        txtview2.setTextSize(24);
        Toolbar.LayoutParams params1 = new Toolbar.LayoutParams
                    (Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, Gravity.START);
        Toolbar.LayoutParams params2 = new Toolbar.LayoutParams
                (Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, Gravity.END);
        txtview1.setLayoutParams(params1);
        txtview2.setLayoutParams(params2);
        toolbar.addView(txtview1);
        toolbar.addView(txtview2);
        txtview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tidyupToolbarAndExit(txtview1, txtview2);
                //txtview1.setVisibility(View.GONE);
                //txtview2.setVisibility(View.GONE);
                //getActivity().getSupportFragmentManager().popBackStackImmediate();
                //Toast.makeText(getActivity(), "Cancel Touched", Toast.LENGTH_SHORT).show();
            }
        });
        txtview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_record();
                tidyupToolbarAndExit(txtview1, txtview2);
            }
        });

    }

    public void tidyupToolbarAndExit(TextView tv1, TextView tv2) {
        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    public void add_record() {

        dbHandler = new MyDBHandler(getActivity(), null, null, 1);

        Events myEvent = new Events();

        myEvent.set_eventname(settings.getString("event_title_key","dunno what title to use"));
        //myEvent.set_eventinfo(txtDesc.getText().toString());
        myEvent.set_eventinfo("test sub heading");
        //myEvent.set_evtime(epoch);
        myEvent.set_evtime(1409775420);
        //myEvent.set_direction(direction);
        myEvent.set_direction(0);
        myEvent.set_evtype("R");
        myEvent.set_evstatus("A");
        //myEvent.set_bgcolor(mColor);
        myEvent.set_bgcolor(-16537100);
        myEvent.set_timeunits("3"); // needs sorting

        dbHandler.addEvent(myEvent);

    }

    @Override
    public void onPause() {
        super.onPause();

    }
}