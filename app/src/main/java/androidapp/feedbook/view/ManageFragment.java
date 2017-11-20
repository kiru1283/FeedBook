package androidapp.feedbook.view;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Vector;

import androidapp.feedbook.R;
import androidapp.feedbook.controller.ManageFeed;
import androidapp.feedbook.exceptions.FeedException;
import androidapp.feedbook.exceptions.JSONFileException;


/**Class to create a screen for Unsubscribing from the feed url
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageFragment extends Fragment implements AdapterView.OnItemSelectedListener, Button.OnClickListener{

    private ManageFeed manObj;
    private String category;
    private String url;
    private String inputUser;
    private Vector choices ;
    private JSONArray userarrFeed;

    private static final String user = "user";


    public ManageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *@param - username of the logged in user
     * @return A new instance of fragment ManageFragment.
     */

    public static ManageFragment newInstance(String username) {
        ManageFragment fragment = new ManageFragment();
        Bundle args = new Bundle();
        args.putString(user, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inputUser = getArguments().getString(user);

        }
    }

    /**
     * Method to create the layout and the spinner for selecting category of feed
     * @param inflater - the layout inflater to create the layouts
     * @param container - the viewgroup contained
     * @param savedInstanceState  - instance data
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        manObj = new ManageFeed(this.getContext());
        // Inflate the layout for this fragment
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_manage);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        choices = new Vector<CharSequence>();
        choices.add("News");
        choices.add("Sports");
        choices.add("Food");
        choices.add("Science");
        choices.add("Travel");

        // Create an ArrayAdapter using the string list and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, this.choices);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Button button_submit=(Button)view.findViewById(R.id.btn_unsubs);

        //button_submit.setOnClickListener(this);


        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        category = parent.getItemAtPosition(position).toString();

        userFeeds(inputUser,category);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //method to fetch feeds linked to the current user name and display them with an option to remove
    private boolean userFeeds(String inputUser,String category) {

        boolean nofeed = true;

        JSONArray userFeeds = null;
        try {

            userFeeds = manObj.viewFeeds();

        } catch (JSONFileException e) {
            Log.e("JSONException", e.getMessage());
            Toast.makeText(this.getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
        }
        if (userFeeds != null) {

            userarrFeed = manObj.userFeeds(userFeeds, inputUser);
            if (userarrFeed != null) {
                int i = 1;
                LinearLayout content = (LinearLayout) this.getView().findViewById(R.id.linear_check);
                LinearLayout btn_layout = (LinearLayout) this.getView().findViewById(R.id.linear_btn);
                //clear the layout for every category
                 content.removeAllViewsInLayout();
                 btn_layout.removeAllViewsInLayout();


                ScrollView sv = new ScrollView(getActivity());
                sv .setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));
                LinearLayout ll = new LinearLayout(getActivity());
                ll.setId(i);
                ll.setLayoutParams(new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT));
                ll.setOrientation(LinearLayout.VERTICAL);
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(3, Color.GRAY);
                drawable.setCornerRadius(8);
                //drawable.setColor(Color.BLUE);
                ll.setBackgroundDrawable(drawable);

                sv.addView(ll);
                int id = 101;
                for (Object Obj : userarrFeed) {
                    JSONObject listObj = (JSONObject) Obj;

                    if(listObj.get("category").toString().equals(category)){

                        //TODO: accept Feed name or get from feed xml

                            CheckBox cb = new CheckBox(getActivity());
                            cb.setId(id+i);
                            ll.setLayoutParams(new FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.WRAP_CONTENT,
                                    FrameLayout.LayoutParams.WRAP_CONTENT));
                            cb.setText(listObj.get("url").toString());
                            ll.addView(cb);

                        nofeed = false;
                        i++;
                    }

                }

                if(!nofeed) {

                    Button btn_unsubsc = new Button(this.getContext());
                    btn_unsubsc.setText("REMOVE FEED");
                    btn_unsubsc.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT));
                    btn_unsubsc.setOnClickListener(this);
                    btn_layout.addView(btn_unsubsc);

                    //add the scroll, linearlayout and checkbox to the mainlayout
                    content.addView(sv);
                }else
                {
                    Toast.makeText(this.getContext(), "No feeds subscribed in this category.", Toast.LENGTH_LONG).show();
                }

            }

        } else {
            Toast.makeText(this.getContext(), "No feeds currently subscribed.Please subscribe to a Feed!!", Toast.LENGTH_LONG).show();
        }

        return nofeed;
    }

    /**
     * Method to remove  the feed url from DB.json file
     * @param view - the view holding the url which has to be removed
     */
    public void doUnsubscribe(View view){
        int id =1;
        LinearLayout parent = (LinearLayout) getView().findViewById(id); //or whatever your root control is
        for(int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if(child instanceof CheckBox) {
                //Support for Checkboxes
                CheckBox cb = (CheckBox)child;
                if (cb.isChecked()){
                   String url = cb.getText().toString();
                    try {
                        if (!manObj.removeFeed(category, url, inputUser)) {
                            Toast.makeText(this.getContext(),"Error writing to file.",Toast.LENGTH_LONG).show();
                        } else {

                            Toast.makeText(this.getContext(),"Feed removed successfully",Toast.LENGTH_SHORT).show();
                            //reload the list
                            userFeeds(inputUser, category);
                        }
                    } catch (FeedException | JSONFileException e) {
                        Log.e("Remove Feed Exception:",e.getMessage());
                    }
                }

            }

        }
    }

    /**
     * On click method for the Remove Feed button
     * @param view - the view holding the url which has to be removed
     */
    @Override
    public void onClick(View view) {
        doUnsubscribe(view);
    }
}
