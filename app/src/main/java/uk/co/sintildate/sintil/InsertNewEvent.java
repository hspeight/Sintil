package uk.co.sintildate.sintil;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.github.lzyzsd.randomcolor.RandomColor;

import java.text.SimpleDateFormat;

public class InsertNewEvent extends Activity{

    static String DEBUG_TAG = "INE";
    static MyDBHandler dbHandler;

    public static long main(){

        dbHandler = new MyDBHandler(App.getContext(), null, null, 1);

        RandomColor randomColor = new RandomColor();
        //Log.d(DEBUG_TAG, "In with " + args);

        Events myEvent = new Events();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", java.util.Locale.getDefault());
        long dateTimeNow = System.currentTimeMillis();
        String s = sdf.format(dateTimeNow);

        myEvent.set_eventname("Brand new event (" + s + ")");
        //myEvent.set_eventinfo(txtDesc.getText().toString());
        myEvent.set_eventinfo("description for the brand new event just created");
        //myEvent.set_evtime(epoch);
        myEvent.set_evtime((int) (dateTimeNow / 1000));
        //myEvent.set_direction(direction);
        myEvent.set_direction(0);
        myEvent.set_evtype("R");
        myEvent.set_evstatus("A");
        //myEvent.set_bgcolor(mColor);
        myEvent.set_bgcolor(randomColor.randomColor());
        myEvent.set_timeunits("3"); // needs sorting

        long lastInserted = dbHandler.addEvent(myEvent);

        HSFrag.eventRecord.add(myEvent);
        //Log.d(DEBUG_TAG,"last inserted=" + lastInserted);

        return lastInserted;

    }

}
