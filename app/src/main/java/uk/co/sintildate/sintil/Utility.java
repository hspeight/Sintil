package uk.co.sintildate.sintil;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

//import android.content.Intent;
//import android.preference.PreferenceManager;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;


public class Utility extends Fragment implements View.OnClickListener {

    EditText randnumber;
    public Events[] eventArray;
    MyDBHandler dbHandler;
    View view;
    String DEBUG_TAG="UTF";

    public Utility() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_utility, container, false);
        randnumber = (EditText) view.findViewById(R.id.editTextNumber);
        dbHandler = new MyDBHandler(getActivity(), null, null, 1);

        Button loadEvents = (Button) view.findViewById(R.id.buttonInsert);
        loadEvents.setOnClickListener(this);
        Button clearEvents = (Button) view.findViewById(R.id.buttonClear);
        clearEvents.setOnClickListener(this);
        Button buttonRand = (Button) view.findViewById(R.id.buttonRand);
        buttonRand.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonInsert:
                loadEvents(v);
                break;
            case R.id.buttonClear:
                clearEvents(v);
                break;
            case R.id.buttonRand:
                insertRandom(v);
                break;
            case R.id.buttonClrearIns:
                clearEvents(v);
                loadEvents(v);
                break;
        }
    }

    public void loadEvents(View view) {

        long nowMinus24Hrs = (System.currentTimeMillis() - (60 * 60 * 24 * 1000));
        long nowPlus24Hrs = (System.currentTimeMillis() + (60 * 60 * 24 * 1000));
        long nowPlus1Min = (System.currentTimeMillis() + (60 * 1000));
        long nowPlus15Sec = (System.currentTimeMillis() + (60 * 1000 / 4));
        long nowPlus5Min = (System.currentTimeMillis() + (60 * 1000 * 5));
        //nowMinus24Hrs *= 1000;
        DateTime dt1 = new DateTime(nowMinus24Hrs, DateTimeZone.getDefault());
        DateTimeFormatter dtf1 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        DateTime dt2 = new DateTime(nowPlus24Hrs, DateTimeZone.getDefault());
        DateTimeFormatter dtf2 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        DateTime dt3 = new DateTime(nowPlus1Min, DateTimeZone.getDefault());
        DateTimeFormatter dtf3 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        DateTime dt4 = new DateTime(nowPlus5Min, DateTimeZone.getDefault());
        DateTimeFormatter dtf4 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        DateTime dt5 = new DateTime(nowPlus15Sec, DateTimeZone.getDefault());
        DateTimeFormatter dtf5 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        DateTimeFormatter dtf6 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm:ss");
        // System.out.println("!!- " + dtf1.print(dt1) + "/" + dtf2.print(dt2));
        //System.out.println("!!- " + "Count up from a future date`0`" + nowPlus5Min / 1000 + "`I`1`0`0`1");

        String[] eString = new String[16];

        eString[0] = "Count up starting in 15 sec from " + dtf6.print(dt5)+"`A`0`" + nowPlus15Sec / 1000 + "`A`R`0`0`1";
        eString[1] = dtf5.print(dt5) + "``1`" + nowPlus15Sec / 1000 + "`A`R`0`0`1";
        //eString[1] = "Da Ting`Example of an event with optional text`0`1419825700`A`R`0`0`0";
        eString[2] = "Countdown to polling day`This is an example of an event with a very long optional text string that contains so much text it will probably go over multiple lines! Oh Gawd!`1`1430978400`A`R`0`1`0";
        eString[3] = "Count up from a future date`A`0`" + nowPlus24Hrs / 1000 + "`A`R`0`0`1";
        eString[4] = dtf4.print(dt4) + "``1`" + nowPlus5Min / 1000 + "`A`R`0`0`1";
        eString[5] = "This is the first ever event!``0`1423440390`A`R`1`1`1";
        eString[6] = "And this is the second``1`1424540190`I`R`0`0`0";
        eString[7] = "Curly Watts Anniversary Countdown``0`1414440190`I`R`1`1`0";
        eString[8] = "Since my 55th Birthday``0`1421317800`I`R`0`0`1";
        eString[9] = "Until my 57th Birthday``1`1484476228`A`R`1`1`0";
        eString[10] = dtf1.print(dt1) + "``0`" + nowMinus24Hrs / 1000 + "`A`R`1`0`1";
        eString[11] = dtf2.print(dt2) + "``0`" + nowPlus24Hrs / 1000 + "`A`R`0`0`1";
        eString[12] = dtf3.print(dt3) + "``1`" + nowPlus1Min / 1000 + "`A`R`0`0`1";
        eString[13] = "Event with a veeeeeeeeery long title to see how it looks in the list except it needs to be even longer``0`1414840190`A`R`0`0`1";
        eString[14] = "Since my 55th``0`1421318386`A`R`0`0`0";
        eString[15] = "the final count down`Da da da da.. da da da da da`1`" + nowPlus24Hrs / 1000 + "`A`R`0`0`1";

        //eString[9] = dtf2.print(dt2) + "`1`" + nowPlus24Hrs / 1000 + "`0";

        //int i;

        //for (int i=0; i < eString.length; i++ ) {
        for (String s : eString) {

            //String[] parts = eString[i].split("`");
            String[] parts = s.split("`");

            //System.out.println("!!- " + i + " done");
            Events event = new Events(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[4],
                    parts[5],Integer.parseInt(parts[6]),Integer.parseInt(parts[7]),Integer.parseInt(parts[8]),null,"[3]",0);
            dbHandler.addEvent(event);

        }

        long n = dbHandler.getRowCount("ALL"); // needs attention
        //Toast.makeText(getApplicationContext(), "DB contains " + n + " events", Toast.LENGTH_SHORT).show();

    }

    public void clearEvents(View view) {

        dbHandler.deleteAllEvents("R"); // Real i.e. not samples
        dbHandler.deleteAllEvents("S");
        dbHandler.deleteAllEvents("T"); //Template
        //HSFrag.eventRecord.clear();
        Toast.makeText(this.getActivity(), "All events cleared", Toast.LENGTH_SHORT).show();
    }

    public void clearPrefs(View view) {
        SharedPreferences settings;
        //SharedPreferences settings = getSharedPreferences("MyPreferences_001", Context.MODE_PRIVATE);
        //settings.edit().clear().apply();
        //settings = getSharedPreferences("MyPreferences_002", Context.MODE_PRIVATE);
        //settings.edit().clear().apply();
        settings = getActivity().getSharedPreferences("MyPreferences_ftp", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        settings.edit().clear().apply();
       // Toast.makeText(getApplicationContext(), "Phew!", Toast.LENGTH_SHORT).show();
        //finish();
    }

    public void insertRandom (View view) {

        //DateTime dt1 = new DateTime(nowMinus24Hrs, DateTimeZone.getDefault());
        DateTimeFormatter dtf1 = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        //Log.d(DEBUG_TAG, "Integer.parseInt(randnumber.getText().toString() " + "is " +  Integer.parseInt(randnumber.getText().toString()));
        long now = (System.currentTimeMillis()) / 1000;
        int nowPlus15Sec = (int) now + 20;
        Log.d(DEBUG_TAG, "np is " + +nowPlus15Sec + "/");
        for(int i=0; i< Integer.parseInt(randnumber.getText().toString()); i++) {
            Events event = new Events("Event No " + i,
                                        "Created at " + now,
                                        1,
                                        nowPlus15Sec,
                                        "A",
                                        "R",0,1,0,null, "[4]", 1
            );
            dbHandler.addEvent(event);
        }
    }
    public void dumpData(View view) {

        String evstring = dbHandler.getEventIDs("A"); // Fetch Id's of active events
        String[] foods = evstring.split(":");
        //System.out.println("!!- " + "foods=" + evstring);
        //String[] newfoods = rebuildArray(foods,initialId);
        //System.out.println("!!- " + "new foods=" + Arrays.toString(newfoods));

        //for (int i = 0; i < foods.length; i++) {
            //Events myEvent = dbHandler.getMyEvent(Integer.parseInt(foods[i]));
           // System.out.println("!!- " + myEvent.get_id() + "/" +
           //         myEvent.get_eventname() + "/" +
           //         myEvent.get_bgimage()
            //);}
        //Toast.makeText(getApplicationContext(), "dumping", Toast.LENGTH_SHORT).show();
        //System.out.println("!!- " + "have a dump");
    }

    public void setInactive(View view) {

        String evstring = dbHandler.getEventIDs("A");
        //dbHandler.deleteAllEvents();
        //System.out.println("!!- " + evstring);
        String[] foods = evstring.split(":"); // array of row_id's
        eventArray = new Events[foods.length];
        for (int i = 0; i < foods.length; i++) {
            //eventArray[i] = dbHandler.getMyEvent(Integer.parseInt(foods[i]));
            dbHandler.deleteEvent(Integer.parseInt(foods[i]));

        }
        //Toast.makeText(getApplicationContext(), "All events cleared", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
