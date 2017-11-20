package androidapp.feedbook.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Vector;

import androidapp.feedbook.R;
import androidapp.feedbook.controller.ManageFeed;
import androidapp.feedbook.exceptions.FeedException;
import androidapp.feedbook.exceptions.JSONFileException;
import androidapp.feedbook.exceptions.RSSException;


/**Class to accept the feed Url from the user and add to DB.json
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class SubscribeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

   // private OnFragmentInteractionListener mListener;

    private ManageFeed manObj;
    private String category;
    private String url;
    private String inputUser;
    private Vector choices ;

    private static final String user = "user";

    public SubscribeFragment() {
        // Required empty public constructor
    }

    public static SubscribeFragment newInstance(String username) {
        SubscribeFragment fragment = new SubscribeFragment();
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
     * Method to create the elements for accepting the feed url and and spinner to select category
     * @param inflater - the layout inflater to create the layouts
     * @param container - the viewgroup container
     * @param savedInstanceState  - instance data
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);

        Button mButton = (Button) view.findViewById(R.id.btn_add);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSubscribe();
            }
        });

        manObj = new ManageFeed(this.getContext());
        // Inflate the layout for this fragment
        Spinner    spinner = (Spinner) view.findViewById(R.id.spinner);
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

    //Method to call the controller to add the url to json file
    private void doSubscribe() {

        try {


            EditText editurl = (EditText) getView().findViewById(R.id.edit_url);
            url = editurl.getText().toString();

            if(url==null || url.trim().isEmpty()){

                Toast.makeText(this.getContext(), "No Feed Url Given To Subscribe", Toast.LENGTH_LONG).show();
            }else {

                if (manObj.subscribeFeed(category, url, inputUser)) {

                    Toast.makeText(this.getContext(), "Subscribed Successfully !!", Toast.LENGTH_LONG).show();

                    editurl.setText("");
                }
            }
        } catch (FeedException | JSONFileException | RSSException e) {
            Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        category = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
       // Toast.makeText(parent.getContext(), "Selected: " + category, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }

}
