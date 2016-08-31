package uk.co.sintildate.sintil;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewEventFragment extends Fragment {

    MyDBHandler dbHandler;
    View view;
    View txtEventTitle;

    public NewEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_event, container, false);

        dbHandler = new MyDBHandler(getActivity(), null, null, 1);

        txtEventTitle = view.findViewById(R.id.txtEventTitle);
        txtEventTitle = view.findViewById(R.id.txtEventDescription);

        //Events event = new Events();

        //event.set_eventname("Test Title");
        //event.set_eventinfo("Test Description");
        //event.set_evstatus("A");
        //event.set_evtype("R");
        //event.set_evtime(1471716931);

        //dbHandler.addEvent(event);

        return  view;
    }
/*
    private void updateEvent(Events myEvent) {
        //Log.d(DEBUG_TAG, "prefs.getString(time_units, Error)" + prefs.getString("time_units", "Error"));
        myEvent.set_eventname(prefs.getString("evtitle", "Error"));
        myEvent.set_eventinfo(prefs.getString("evdesc", "Error"));
        myEvent.set_evtime(prefs.getInt("date_and_time", 946728060));
        myEvent.set_direction(Integer.valueOf(prefs.getString("direction", "0")));
        myEvent.set_timeunits(prefs.getString("time_units", "Error"));
        //Log.d(DEBUG_TAG, ? + " is currently is the summary");
        if (newEvent) {
            myEvent.set_evtype("R");
            myEvent.set_evstatus("A");
            dbHandler.addEvent(myEvent);
        } else {
            dbHandler.updateEvent(myEvent);
        }
    }
*/
}
