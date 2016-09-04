package uk.co.sintildate.sintil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class EventCounterFragment extends FragmentActivity {
    public static final String ARG_OBJECT = "object";
    int index, current_pg, oboypg;
    View rootView;
    MyDBHandler dbHandler;
    String DEBUG_TAG = "ECF";
    public static Events[] eventArray;
    private int mTime;
    private int mDirection;
    boolean ok_to_start, timer_has_started;
    Resources res;
   // long currentTime;
    TextView textSecs;
    TextView textMins;
    TextView textHour;
    TextView textDays;
    TextView textYears;
    LinearLayout linLayTimer_counter, linLay;
    CountDownTimer cdt;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_counter);
         linLay = (LinearLayout) findViewById(R.id.linLayoutCounterBG);
        //linLay.setBackgroundColor(Color.BLUE);
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        /*
        int[][][] THEME_COLORS = new int[3][3][3];
        THEME_COLORS[0][0][0]=255;
        THEME_COLORS[0][1][0]=0;
        THEME_COLORS[0][0][1]=0;
        THEME_COLORS[1][0][0]=0;
        THEME_COLORS[1][1][0]=255;
        THEME_COLORS[1][0][1]=0;
        THEME_COLORS[2][0][0]=255;
        THEME_COLORS[2][1][0]=255;
        THEME_COLORS[2][0][1]=0;
        */
        //rootView = inflater.inflate(R.layout.event_counter, container, false);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            //index = args.getInt(ARG_OBJECT);
            index = args.getInt("ROW_INDEX");
        }

        dbHandler = new MyDBHandler(this, null, null, 1);
        res = getResources();

        setup_counter(index);
        //final GradientLinearLayout layout = (GradientLinearLayout) rootView.findViewById(R.id.linLayoutCounterBG);

        //return rootView;
    }

    public void start_timer() {
        //long millis = mTime;
        Log.d(DEBUG_TAG,"starting");
        long td = System.currentTimeMillis() / 1000;
        final long timeDiff = td - mTime ;
        final long millisToStart = 86500000; //86400000 = milliseconds in 1 day

        cdt = new CountDownTimer(millisToStart, 1000) {
            public void onTick(long millisUntilFinished) {
                timer_has_started = true;
                long secs;
                int modDays;
                long modSecs, mins;
                secs = timeDiff + ((millisToStart / 1000) - ((int) (millisUntilFinished / 1000)));
                if (mDirection == 1) // 1 = countdown
                    secs *= -1;
                final long currentTime = System.currentTimeMillis() / 1000;
                Log.d(DEBUG_TAG,HSFrag.eventRecord.get(index).get_id() + "/" + mDirection + "/" + mTime + "/" + currentTime);
                modSecs = secs % 60;
                mins = (secs / 60) % 60;
                long hours = TimeUnit.SECONDS.toHours(secs) % 24;
                int days = (int) TimeUnit.SECONDS.toDays(secs);
                double years = days / 365.25;
                //if (mUsedayyear == 0)
                //    modDays = days;
                //else
                modDays = (int) Math.floor(days % 365.25);
                if ((mDirection == 0 && mTime < currentTime) ||
                        (mDirection == 1 && mTime > currentTime)) {
                    // this must be an active countup or countdown
                    Log.d(DEBUG_TAG,"ticking with child count " + linLayTimer_counter.getChildCount());
                    //if (linLayTimer_counter.getChildCount() == 1) {
                    if (findViewById(R.id.textSecs) == null) {
                        // this can happen when a countup time is reached whilst the counter is on screen
                        Log.d(DEBUG_TAG,"secs index is " + findViewById(R.id.textSecs));
                        LinearLayout linLayTimer_Temp = (LinearLayout) findViewById(R.id.linLayoutTemp);
                        linLayTimer_Temp.removeAllViews();
                        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View myView = inflater.inflate(R.layout.content_timer_counter, null);
                        //myView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        myView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        //lp.weight = 0.1f;
                        //myView.setLayoutParams(lp);
                        linLayTimer_Temp.addView(myView);
                        textSecs = (TextView) findViewById(R.id.textSecs);
                        textDays = (TextView) findViewById(R.id.textDays);
                        textYears = (TextView) findViewById(R.id.textYears);
                        textMins = (TextView) findViewById(R.id.textMins);
                        textHour = (TextView) findViewById(R.id.textHour);
                    }

                    textSecs.setText(String.valueOf(modSecs));
                    textMins.setText(String.valueOf(mins));
                    textHour.setText(String.valueOf(hours));
                    //textDays.setText(String.valueOf(modDays));
                    textDays.setText(String.valueOf(modDays));
                    textYears.setText(String.valueOf((int) Math.floor(years)));
                }
                if (mDirection == 1 && mTime < currentTime)
                    // this will happen if the countdown reaches zero whilst on screen
                    cdt.cancel();
            }
            public void onFinish() {
                textSecs.setText("done!");
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer_has_started)
            cdt.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start ticker
    }

    public void DispNextPage(View v) {
        //Toast.makeText(EventCounterFragment.this, "Next pressed", Toast.LENGTH_SHORT).show();
        //Log.d(DEBUG_TAG,"index=" + index + "/size=" + HSFrag.eventRecord.size());
        //++index;
        if (index < HSFrag.eventRecord.size() -1) {
            index++;
            //Log.d(DEBUG_TAG,"index before setup=" + index);
            setup_counter(index);
        }

    }
    public void DispPrevPage(View v) {
        //Toast.makeText(EventCounterFragment.this, "Prev pressed", Toast.LENGTH_SHORT).show();
        if (index > 0) {
            index-- ;
            //Log.d(DEBUG_TAG,"index before setup=" + index);
            setup_counter(index);
        }
    }

    public void setup_counter(int index) {
        //Log.d(DEBUG_TAG,"index+++++++++++++++++++++++++=" + index);
        linLay.setBackgroundColor(HSFrag.eventRecord.get(index).get_bgcolor());
        if (timer_has_started)
            cdt.cancel();
        timer_has_started = false;
        mTime = HSFrag.eventRecord.get(index).get_evtime();
        mDirection = HSFrag.eventRecord.get(index).get_direction();
        //mDirection = HSFrag.eventRecord.get(index).get_direction();
        //mUsedayyear = HSFrag.eventRecord.get(index).get_dayyears();
        //final long timeDiff = (System.currentTimeMillis() / 1000) - mTime;

        //if ((timeDiff < 0) && (mDirection == 0)) { // Count up starting in future
        //    ((TextView) rootView.findViewById(R.id.textViewFuture)).setText(sdf.format((long) mTime * 1000));
        ((TextView) findViewById(R.id.textEvTitle)).setText(HSFrag.eventRecord.get(index).get_eventname());
        ((TextView) findViewById(R.id.textOptionalInfo)).setText(HSFrag.eventRecord.get(index).get_eventinfo());
        ((TextView) findViewById(R.id.textViewFuture)).setText(String.valueOf(HSFrag.eventRecord.get(index).get_id()));
        final long currentTime = System.currentTimeMillis() / 1000;
        //final int timeDiff = (int) currentTime - mTime;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a", java.util.Locale.getDefault());
        textDays = (TextView) findViewById(R.id.textDays);
        textYears = (TextView) findViewById(R.id.textYears);
        textSecs = (TextView) findViewById(R.id.textSecs);
        textMins = (TextView) findViewById(R.id.textMins);
        textHour = (TextView) findViewById(R.id.textHour);
        linLayTimer_counter = (LinearLayout) findViewById(R.id.linLayoutTimer);
        ok_to_start = true;
        //if(HSFrag.eventRecord.get(index).get_paused() == 1) {
        if ((HSFrag.eventRecord.get(index).get_direction() == 1 && (HSFrag.eventRecord.get(index).get_evtime() < currentTime)) ||
                (HSFrag.eventRecord.get(index).get_direction() == 0 && (HSFrag.eventRecord.get(index).get_evtime() > currentTime))) {
            Log.d(DEBUG_TAG, "count = " + linLayTimer_counter.getChildCount());
            linLayTimer_counter.removeAllViews();
            TextView tv = new TextView(this);
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (HSFrag.eventRecord.get(index).get_evtime() < currentTime) {
                tv.setText(String.format(res.getString(R.string.countdown_ended), "\n" + sdf.format((long) HSFrag.eventRecord.get(index).get_evtime() * 1000)));
                ok_to_start = false;
            } else {
                tv.setText(String.format(res.getString(R.string.future_countup), "\n" + sdf.format((long) HSFrag.eventRecord.get(index).get_evtime() * 1000)));
            }
            tv.setTextSize(18);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            linLayTimer_counter.addView(tv);

            //} else {
            //    ok_to_start = true;

            //mTime = HSFrag.eventRecord.get(index).get_evtime();
            //mUsedayyear = HSFrag.eventRecord.get(index).get_dayyears();
        }
/*
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.RED)
                .borderWidthDp(1)
                .oval(false)
                .build();
*/
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this)
                .load(R.drawable.bass)
                //.transform(transformation)
                .into(imageView);

        if (ok_to_start) {
            start_timer();
        }

    }
}
