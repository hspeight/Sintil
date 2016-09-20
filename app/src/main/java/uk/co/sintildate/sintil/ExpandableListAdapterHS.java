package uk.co.sintildate.sintil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapterHS extends BaseExpandableListAdapter {

    //MyDBHandler dbHandler;
    public static final String MyPREFERENCES = "MyPreferences_001";

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private List<String> _listDataSubHeader; // Sub header titles
    private FloatingActionButton _fab;
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    SharedPreferences pref;
    int bgColor, rowColor, textColor;
    View linLayout;
    View relLayout;
    MyDBHandler dbHandler;
    String DEBUG_TAG = "ELA";

    public ExpandableListAdapterHS(Context context, List<String> listDataHeader, List<String> listDataSubHeader,
                                   HashMap<String, List<String>> listChildData, FloatingActionButton fab) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataSubHeader = listDataSubHeader;
        this._listDataChild = listChildData;
        this._fab = fab;

        //this.stars = new boolean[listChildData.size()];
        dbHandler = new MyDBHandler(context, null, null, 1);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);
        //System.out.println("!!-  " + childText);
        final String gp = String.valueOf(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.event_list_child_view, null);
            //System.out.println("!!-  " + " returning convertview");
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);

        ImageView imgChildDelete = (ImageView) convertView.findViewById(R.id.imageViewDelete);
        imgChildDelete.setTag(groupPosition);
        imgChildDelete.findViewById(R.id.imageViewDelete);
        imgChildDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(DEBUG_TAG,"grouppos is " + groupPosition);
                int recordID = HSFrag.eventRecord.get(groupPosition).get_id();
                dbHandler.deleteEvent(recordID);
                _listDataHeader.remove(groupPosition);
                notifyDataSetChanged();
                HSFrag.eventRecord.remove(groupPosition);
                Activity activity = (Activity) _context;
                activity.setTitle(activity.getPackageManager().getApplicationLabel(activity.getApplicationInfo()) + " (" + HSFrag.eventRecord.size() + ")");
            }
        });

        ImageView imgChildEdit = (ImageView) convertView.findViewById(R.id.imageViewEdit);
        imgChildEdit.setTag(groupPosition);
        imgChildEdit.findViewById(R.id.imageViewEdit);
        imgChildEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(DEBUG_TAG,"grouppos is " + groupPosition);
                //int recordID = HSFrag.eventRecord.get(groupPosition).get_id();
                //HSFrag hsFrag = new HSFrag();
                //pass fab buttonID & recordID back to method in HSFRAG
                //hsFrag.simulateFabClick(_fab, HSFrag.eventRecord.get(groupPosition).get_id());

                //Activity activity = (Activity) _context;
                //FragmentManager fm = getActivity().getSupportFragmentManager();
                //EventEditorDialogFragment newEventDialogFragment = new EventEditorDialogFragment();
                //newEventDialogFragment.show(fm, "fragment_new_event");
                Intent intent = new Intent(_context, EventCounterFragment.class);
                intent.putExtra("ROW_INDEX",groupPosition); // Start viewpager at this record
                intent.putExtra("MODE", "E"); // E=Edit
                intent.putExtra("LASTADDED", HSFrag.eventRecord.get(groupPosition).get_id());
                _context.startActivity(intent);
            }
        });

        ImageView imgChildPlay = (ImageView) convertView.findViewById(R.id.imageViewPlay);
        imgChildPlay.setTag(groupPosition);
        imgChildPlay.findViewById(R.id.imageViewPlay);
        imgChildPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, EventCounterFragment.class);
                intent.putExtra("ROW_INDEX",groupPosition); // Start viewpager at this record
                intent.putExtra("MODE", "P"); // P=Play
                _context.startActivity(intent);
            }
        });

        relLayout = convertView.findViewById(R.id.relLayoutChildBG); // Background of row's child view

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
    public Object getGroupSub(int groupPosition) {
        return this._listDataSubHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        String headerSubTitle = (String) getGroupSub(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.event_list_parent_view, null);
            //SharedPreferences pref = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            pref = _context.getSharedPreferences(MyPREFERENCES, 0);
            rowColor = pref.getInt("listRowColor", -16776961); // Get row color from pref file
            bgColor = pref.getInt("listBgColor", -16776961); // Get background color from pref file
            textColor = pref.getInt("listTextColor", -1); // Get text color from pref file
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTextColor(textColor);

        TextView lblListSubHeader = (TextView) convertView.findViewById(R.id.lblListSubHeader);
        lblListSubHeader.setTextColor(textColor);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        linLayout = convertView.findViewById(R.id.linLayoutParentBG);
        linLayout.setBackgroundColor(rowColor);

        lblListSubHeader.setText(headerSubTitle);
        //lblListSubHeader.setBackgroundColor(Color.DKGRAY);

        return convertView;
    }


    public void removeGroup(int group) {
        //TODO: Remove the according group. Dont forget to remove the children aswell!
        //Log.d(DEBUG_TAG, "Removing group " +group);
        //notifyDataSetChanged();
    }

    public void removeChild(int group, int child) {
        //TODO: Remove the according child
        //Log.d(DEBUG_TAG, "Removing child " +child +" in group " +group);
       // notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}