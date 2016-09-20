package uk.co.sintildate.sintil;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
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
    private int mDirection, lastAdded;
    boolean ok_to_start, timer_has_started;
    private int PICK_IMAGE_REQUEST = 1;
    String counterMode;
    Resources res;
   // long currentTime;
    TextView textSecs;
    TextView textMins;
    TextView textHour;
    TextView textDays;
    TextView textYears;
    EditText mTitle, mSubTitle;
    ImageView imageViewPic;
    LinearLayout linLayTimer_counter, linLay, linLayTimer_Units;
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
            counterMode = args.getString("MODE");
            lastAdded = args.getInt("LASTADDED", 0);
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
                        linLayTimer_Units.removeAllViews();
                        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View myView = inflater.inflate(R.layout.content_timer_counter, null);
                        //myView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        myView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        //lp.weight = 0.1f;
                        //myView.setLayoutParams(lp);
                        linLayTimer_Units.addView(myView);
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
        //Log.d(DEBUG_TAG,"timer_has_started=" + timer_has_started);
        if (timer_has_started) {
            cdt.cancel();
            if(counterMode.equals("N") || counterMode.equals("E")) { // New or Edit
                Log.d(DEBUG_TAG,index + "");
                Events myEvent = dbHandler.getMyEvent(lastAdded);
                myEvent.set_eventname(mTitle.getText().toString());
                myEvent.set_eventinfo(mSubTitle.getText().toString());
                myEvent.set_bgimage("file:///storage/emulated/0/Download/2013-04-06%2013.38.53.jpg");
                dbHandler.updateEvent(myEvent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d(DEBUG_TAG,"onresume timer_has_started=" + timer_has_started);

        //start_timer();
    }

    public void DispNextPage(View v) {
        //Toast.makeText(EventCounterFragment.this, "Next pressed", Toast.LENGTH_SHORT).show();
        //Log.d(DEBUG_TAG,"index=" + index + "/size=" + HSFrag.eventRecord.size());
        //++index;
        if (index < HSFrag.eventRecord.size() -1) {
            index++;
            //Log.d(DEBUG_TAG,"index before setup=" + index);
            //cdt.cancel();
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
        if (timer_has_started) {
            //Log.d(DEBUG_TAG,"cancelling cdt");
            cdt.cancel();
        }
        timer_has_started = false;
        mTime = HSFrag.eventRecord.get(index).get_evtime();
        mDirection = HSFrag.eventRecord.get(index).get_direction();
        imageViewPic = (ImageView) findViewById(R.id.imageViewPic);
        linLayTimer_Units = (LinearLayout) findViewById(R.id.linLayoutUnits);
        //mUsedayyear = HSFrag.eventRecord.get(index).get_dayyears();
        //final long timeDiff = (System.currentTimeMillis() / 1000) - mTime;

        //if ((timeDiff < 0) && (mDirection == 0)) { // Count up starting in future
        //    ((TextView) rootView.findViewById(R.id.textViewFuture)).setText(sdf.format((long) mTime * 1000));
        mTitle = ((EditText) findViewById(R.id.textEvTitle));
        mSubTitle = ((EditText) findViewById(R.id.textOptionalInfo));
        mTitle.setText(HSFrag.eventRecord.get(index).get_eventname());
        mSubTitle.setText(HSFrag.eventRecord.get(index).get_eventinfo());
        //((TextView) findViewById(R.id.textOptionalInfo)).setText(HSFrag.eventRecord.get(index).get_eventinfo());
        //Log.d(DEBUG_TAG,"newevent is " + newevent);
        //mTitle.setEnabled(false);

        if(counterMode.equals("N") || counterMode.equals("E")) { // New or Edit
            // Ah ha new record from fab button
            mTitle.selectAll();
            mTitle.setEnabled(true); // only for newly added events
            mTitle.requestFocus();
            mTitle.setSelectAllOnFocus(true);
            //Log.d(DEBUG_TAG,"am i here?");
            mSubTitle.setEnabled(true); // only for newly added events
            mSubTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        mSubTitle.selectAll();
                    }
                }
            });
            imageViewPic.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    //Toast.makeText(EventCounterFragment.this, "Image pressed.. yippee", Toast.LENGTH_SHORT).show();
                    //cdt.cancel();
                    launch_chooser();
                    //start_timer(); // timer will have been stopped by image choser activity
                }});
            linLayTimer_Units.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    //Toast.makeText(EventCounterFragment.this, "thingy pressed.. yippee", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getSupportFragmentManager();
                    DateTimeDialogFragment overlay = new DateTimeDialogFragment();
                    overlay.show(fm, "FragmentDialog");
                }});
        } else {
            mTitle.setInputType(0); // make the edittext non editable
            mSubTitle.setInputType(0); // make the edittext non editable

        }

        final long currentTime = System.currentTimeMillis() / 1000;
        //final int timeDiff = (int) currentTime - mTime;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a", java.util.Locale.getDefault());
        ((TextView) findViewById(R.id.textViewFuture)).setText(sdf.format((long) HSFrag.eventRecord.get(index).get_evtime() * 1000));

        textDays = (TextView) findViewById(R.id.textDays);
        textYears = (TextView) findViewById(R.id.textYears);
        textSecs = (TextView) findViewById(R.id.textSecs);
        textMins = (TextView) findViewById(R.id.textMins);
        textHour = (TextView) findViewById(R.id.textHour);
        linLayTimer_counter = (LinearLayout) findViewById(R.id.linLayoutTimer);

        if(counterMode.equals("N")) {
            // new event so disable prev & next buttons
            ImageButton pgLeft = (ImageButton) findViewById(R.id.pageLeftImgBtn);
            ImageButton pgRight = (ImageButton) findViewById(R.id.pageRightImgBtn);
            pgRight.setVisibility(View.INVISIBLE);
            pgLeft.setVisibility(View.INVISIBLE);
        }

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
        //Picasso.with(this)
        String evimage = HSFrag.eventRecord.get(index).get_bgimage();
        Uri uri = null;
        Log.d(DEBUG_TAG,"evimage is " + evimage);
        if (evimage != null)
            uri = Uri.parse(new File(evimage).toString());

        Glide.with(this)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.error(R.drawable.daftlogo2) // probably better than a placeholder
                //.dontAnimate()
                .into(this.imageViewPic);

        if (ok_to_start) {
            //Log.d(DEBUG_TAG,"oktostart timer_has_started=" + timer_has_started);

            start_timer();
        }

    }
    public void launch_chooser() {

        Intent intent = new Intent();
        // Show only images, no videos or anything else... hmmm maybe vids one day
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        setup_counter(index);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            //Picasso.with(this).setLoggingEnabled(true);
            //try {
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //Log.d(DEBUG_TAG, ">>" + String.valueOf(uri) + "<<");
                //Picasso.with(this)
                Glide.with(this)
                        .load(uri)
                        //.centerInside()
                        //.fit()
                        .into(imageViewPic);
                //ImageView imageView = (ImageView) findViewById(R.id.imageViewPic);
                //imageView.setImageBitmap(bitmap);
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
        }

    }
    private class DownloadTileToCacheTask extends AsyncTask<String, Void, File> {
        @Override
        protected File doInBackground(String... params) {
            FutureTarget<File> future = Glide.with(getApplicationContext())
                    .load(params[0])
                    .downloadOnly(256, 256);

            File file = null;
            try {
                file = future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return file;
        }
    }

}
