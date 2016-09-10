package uk.co.sintildate.sintil;

import android.app.Activity;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
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

import com.github.lzyzsd.randomcolor.RandomColor;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Random;

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
        String[] sList = { "Burger King", "McDonald's", "Subway", "Pizza Hut","Bahama Breeze",
                "Panda Express", "Sushiology", "Brio", "Olive Garden", "LongHorn",
                "Carrabbas", "Seito", "Fridays", "Cheese Cake Factory", "Amura", "Chipotle",
                "WaWa", "Pio Pio", "Panera Bread", "Mimis Cafe"}; //Array with our personal favorite choice

        String[] dList = {"The quick brown fox jumps over the lazy dog",
                "My Mum tries to be cool by saying that she likes all the same things that I do",
            "If the Easter Bunny and the Tooth Fairy had babies would they take your teeth and leave chocolate for you?",
                "A purple pig and a green donkey flew a kite in the middle of the night and ended up sunburnt",
        "What was the person thinking when they discovered cow’s milk was fine for human consumption… and why did they do it in the first place!?",
                "Last Friday in three week’s time I saw a spotted striped blue worm shake hands with a legless lizard",
        "Wednesday is hump day, but has anyone asked the camel if he’s happy about it?",
        "If Purple People Eaters are real… where do they find purple people to eat?",
        "A song can make or ruin a person’s day if they let it get to them",
        "Sometimes it is better to just walk away from things and go back to them later when you’re in a better frame of mind"
/*
        11. Writing a list of random sentences is harder than I initially thought it would be.

        12. Where do random thoughts come from?

        13. Lets all be unique together until we realise we are all the same.

        14. I will never be this young again. Ever. Oh damn… I just got older.

        15. If I don’t like something, I’ll stay away from it.

        16. I love eating toasted cheese and tuna sandwiches.

        17. If you like tuna and tomato sauce- try combining the two. It’s really not as bad as it sounds.

        18. Someone I know recently combined Maple Syrup & buttered Popcorn thinking it would taste like caramel popcorn. It didn’t and they don’t recommend anyone else do it either.

        19. Sometimes, all you need to do is completely make an ass of yourself and laugh it off to realise that life isn’t so bad after all.

        20. When I was little I had a car door slammed shut on my hand. I still remember it quite vividly.

        21. The clock within this blog and the clock on my laptop are 1 hour different from each other.

        22. I want to buy a onesie… but know it won’t suit me.

        23. I was very proud of my nickname throughout high school but today- I couldn’t be any different to what my nickname was.

        24. I currently have 4 windows open up… and I don’t know why.

        25. I often see the time 11:11 or 12:34 on clocks.

        26. This is the last random sentence I will be writing and I am going to stop mid-
          */
        };
        for(int i=0; i< Integer.parseInt(randnumber.getText().toString()); i++) {
            Random r = new Random();
            RandomColor randomColor = new RandomColor();
            int mycolor = randomColor.randomColor();
            //DateTime dt1 = new DateTime(nowMinus24Hrs, DateTimeZone.getDefault());
            Events myEvent = new Events();

            myEvent.set_eventname(sList[r.nextInt(sList.length - 1)]);
            //myEvent.set_eventinfo(txtDesc.getText().toString());
            myEvent.set_eventinfo(dList[r.nextInt(dList.length - 1)]);
            //myEvent.set_evtime(epoch);
            myEvent.set_evtime(r.nextInt(1473214474));
            //myEvent.set_direction(direction);
            myEvent.set_direction(r.nextInt(2));
            myEvent.set_evtype("R");
            myEvent.set_evstatus("A");
            //myEvent.set_bgcolor(mColor);
            myEvent.set_bgcolor(mycolor);
            myEvent.set_timeunits("3"); // needs sorting

            dbHandler.addEvent(myEvent);
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
