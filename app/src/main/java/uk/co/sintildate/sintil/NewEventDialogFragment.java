package uk.co.sintildate.sintil;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


import uk.co.sintildate.sintil.R;

/**
 * Created by hector on 01/09/16.
 */
public class NewEventDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener onItemClickListener;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Resources res;
    DateValidator dateValidator;
    String DEBUG_TAG = "NEA";

    public NewEventDialogFragment () {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_event, container, false);

    }

    public void setOnClickListener(DialogInterface.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Dialog dialog = new Dialog(getActivity());

        dateValidator = new DateValidator();
        btnDatePicker=(Button)view.findViewById(R.id.btn_date);
        btnTimePicker=(Button)view.findViewById(R.id.btn_time);
        txtDate=(EditText)view.findViewById(R.id.in_date);
        txtTime=(EditText)view.findViewById(R.id.in_time);

        ((Button) view.findViewById(R.id.btn_date)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                //caller.doCancelConfirmClick();
            }
        });

        //btnDatePicker.setOnClickListener(this);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(dialog, 0);
            }
        });
        //btnTimePicker.setOnClickListener(this);
        res = getResources();

        RadioGroup cd = (RadioGroup) view.findViewById(R.id.radioDirection);
        final RadioButton countDown = (RadioButton) view.findViewById(R.id.radioButtonCountDown);
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
}
