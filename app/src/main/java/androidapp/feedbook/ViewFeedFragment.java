package androidapp.feedbook;


import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import androidapp.feedbook.controller.ManageFeed;
import androidapp.feedbook.exceptions.FeedException;
import androidapp.feedbook.exceptions.JSONFileException;
import androidapp.feedbook.exceptions.RSSException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFeedFragment extends Fragment implements AdapterView.OnItemSelectedListener,Button.OnClickListener{
    private ManageFeed manObj;
    private String category;
    private String url;
    private String inputUser;
    private Vector choices ;
    private JSONArray userarrFeed;

    private static final String user = "user";


    public ViewFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewFeedFragment newInstance(String username) {
        ViewFeedFragment fragment = new ViewFeedFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_feed, container, false);
        // Inflate the layout for this fragment
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

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        category = parent.getItemAtPosition(position).toString();

        userFeeds(inputUser,category);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

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
                final RadioButton[] rb = new RadioButton[30];
                RadioGroup rg = new RadioGroup(this.getContext()); //create the RadioGroup
                rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL

                for (Object Obj : userarrFeed) {
                    JSONObject listObj = (JSONObject) Obj;

                    if(listObj.get("category").toString().equals(category)){

                        //TODO: accept Feed name or get from feed xml
                        rb[i-1]  = new RadioButton(this.getContext());
                        rb[i-1].setText(listObj.get("url").toString());
                        rb[i-1].setId(id+i);
                        rg.addView(rb[i-1]); //the RadioButtons are added to the radioGroup instead of the layout

                        nofeed = false;
                        i++;
                    }

                }

                ll.addView(rg);

                if(!nofeed) {
                    Button btn_view = new Button(this.getContext());
                    btn_view.setText("VIEW ARTICLES");
                    btn_view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT));
                    btn_view.setOnClickListener(this);
                    btn_layout.addView(btn_view);
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

    @Override
    public void onClick(View view) {
        readFeed();

    }

    private void readFeed(){
        int id =1;
        ArrayList<String> articles = null;
        LinearLayout parent = (LinearLayout) getView().findViewById(id); //or whatever your root control is
        for(int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if(child instanceof RadioGroup ) {
                //Support for RadioGroup
                RadioGroup rad = (RadioGroup)child;
                int selectedId = rad.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) getView().findViewById(selectedId);

                String url = radioButton.getText().toString();

                try {
                    articles = (ArrayList<String>) manObj.readFeed(category, url, inputUser);

                } catch (FeedException | JSONFileException | RSSException e) {
                    Log.e("Read Feed Exception:",e.getMessage());
                }

            }

        }

        ViewArticlesFragment nextFrag= ViewArticlesFragment.newInstance(inputUser,articles);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, nextFrag,"findThisFragment")
                .addToBackStack(null)
                .commit();

    }
}
