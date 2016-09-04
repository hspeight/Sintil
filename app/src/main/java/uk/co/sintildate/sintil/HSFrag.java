package uk.co.sintildate.sintil;

//import android.app.Fragment;
import android.content.Intent;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HSFrag extends Fragment {

    int maxAllowableEvents = 12; // not yet in use. Wil be 9999 in paid version

    MyDBHandler dbHandler;

    ExpandableListAdapterHS listAdapter;
    ExpandableListView expListView;
    View linLayout;
    //View linLayoutParent;
    List<String> listDataHeader;
    List<String> listDataSubHeader;
    HashMap<String, List<String>> listDataChild;
    //public static Events[] eventArray;
    public static final String MyPREFERENCES = "MyPreferences_001";
    public static final String FirstTimePref = "MyPreferences_ftp";
    //public int firstTime = 0;
    static ArrayList<Events> eventRecord = new ArrayList<>();
    public String menuAction;
    View view;
    String DEBUG_TAG = "HSF";

    public HSFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_h2, container, false);
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_h2, container, false);

       // view =  inflater.inflate(R.layout.fragment_h2, container, false);

        dbHandler = new MyDBHandler(getActivity(), null, null, 1);

        PreferenceManager.setDefaultValues(getActivity(), R.xml.settings, false);
        // for explanation see http://developer.android.com/guide/topics/ui/settings.html#Fragment

        setup_list();
        /*
        setGroupParents();

        listAdapter = new ExpandableListAdapterHS(getActivity(), listDataHeader, listDataSubHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
       // listAdapter.notifyDataSetChanged();

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override   // collapse list
            public void onGroupExpand(int groupPosition) {
                //setImageRowPosition(groupPosition);
                if(groupPosition != previousItem ) {
                    expListView.collapseGroup(previousItem);
                    //expListView.setAlpha(0.75f);
                }
                previousItem = groupPosition;
            }
        });
        */
        linLayout  = view.findViewById(R.id.linLayoutMainBG);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        //fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                //fab.setVisibility(View.INVISIBLE);
                //getFragmentManager().beginTransaction()
                //        .replace(R.id.fragment_container, new NewEventDialogFragment()).addToBackStack("fragBack")
                //        .commit();
                Intent intent = new Intent(getActivity(), NewEventActivity.class);
                //Intent intent = new Intent(getActivity(), Main2Activity.class);
                startActivity(intent);
               // FragmentManager fm = getActivity().getSupportFragmentManager();
               // NewEventDialogFragment newEventDialogFragment = new NewEventDialogFragment();
               // newEventDialogFragment.show(fm, "fragment_new_event");
            }
        });

        return view;

    }

    public void setup_list() {

        setGroupParents();

        listAdapter = new ExpandableListAdapterHS(getActivity(), listDataHeader, listDataSubHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        // listAdapter.notifyDataSetChanged();

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override   // collapse list
            public void onGroupExpand(int groupPosition) {
                //setImageRowPosition(groupPosition);
                if(groupPosition != previousItem ) {
                    expListView.collapseGroup(previousItem);
                    //expListView.setAlpha(0.75f);
                }
                previousItem = groupPosition;
            }
        });

    }
    // method to add parent & child events
    public void setGroupParents() {

        eventRecord.clear();
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        String evstring = dbHandler.getEventIDs("A");
        String[] foods = evstring.split(":"); // array of row_id's
        listDataHeader = new ArrayList<String>();
        listDataSubHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        int i;
        if (evstring.length() > 0) { // need to display something if there are no events
            for (i = 0; i < foods.length; i++) {

                eventRecord.add(dbHandler.getMyEvent(Integer.parseInt(foods[i])));

                listDataHeader.add(eventRecord.get(i).get_eventname());

                listDataSubHeader.add(generateInfo(eventRecord.get(i).get_direction(), eventRecord.get(i).get_evtime()));
                List<String> child = new ArrayList<>();

                child.add(eventRecord.get(i).get_eventinfo()); // No text to display but required so that child will expand
                listDataChild.put(listDataHeader.get(i), child);

                //update paused column if countdown has expired or countup has not started yet

                //if(eventRecord.get(i).get_paused() == 0) {
                    long currentTime = System.currentTimeMillis() / 1000;
                    if ((eventRecord.get(i).get_direction() == 1 && (eventRecord.get(i).get_evtime() < currentTime)) ||
                            (eventRecord.get(i).get_direction() == 0 && (eventRecord.get(i).get_evtime() > currentTime))) {
                        Events myEvent = dbHandler.getMyEvent(Integer.parseInt(foods[i]));
                        //myEvent.set_paused(1);
                        dbHandler.updateEvent(myEvent);
                        eventRecord.remove(i);
                        eventRecord.add(myEvent);
                    }
                //}

            }
        }
    }

    public String generateInfo(int direction, int dateTime) {

        String indicator = "\u25B2" + " From "; // Up arrow
        if(direction == 1)
            indicator = "\u25BC" + " To "; // Down arrow

        long millis = dateTime;
        millis *= 1000;
        //DateTime dt = new DateTime(millis, DateTimeZone.getDefault()); // needs to be a local date
        //DateTimeFormatter dtf = DateTimeFormat.forPattern("dd MMM yyyy hh:mm a", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a", java.util.Locale.getDefault());

        return sdf.format(new Date(millis));
        //return indicator + dtf.print(dt);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (dbHandler.getRowCount("A") > eventRecord.size()) {
            // Ah ha.. a new one was added
            //Toast.makeText(getActivity(), "Resumed and something was added" , Toast.LENGTH_SHORT).show();
            setup_list();
        //} else {
        //    Toast.makeText(getActivity(), "Resumed with no addition" , Toast.LENGTH_SHORT).show();
        }
    }

    public void AWarmWelcome() {
        //Toast.makeText(getApplicationContext(), "Welcome my friend" , Toast.LENGTH_SHORT).show();
        Intent firsttime = new Intent(String.valueOf(FirstTime.class));
        startActivity(firsttime);
        //return true;
    }

}
