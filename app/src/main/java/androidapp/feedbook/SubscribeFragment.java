package androidapp.feedbook;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidapp.feedbook.controller.ManageFeed;
import androidapp.feedbook.exceptions.FeedException;
import androidapp.feedbook.exceptions.JSONFileException;
import androidapp.feedbook.exceptions.RSSException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class SubscribeFragment extends Fragment {

   // private OnFragmentInteractionListener mListener;

    private ManageFeed manObj;
    private String category;
    private String url;
    private String inputUser;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);

        Button mButton = (Button) view.findViewById(R.id.btn_add);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribe();
            }
        });

        manObj = new ManageFeed(this.getContext());
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void subscribe() {
        try {

            EditText editcategory = (EditText) getView().findViewById(R.id.edit_cat);
            category = editcategory.getText().toString();

            EditText editurl = (EditText) getView().findViewById(R.id.edit_url);
            url = editurl.getText().toString();

            if (manObj.subscribeFeed(category, url, inputUser)) {

            Toast.makeText(this.getContext(), "Subscribed Successfully !!", Toast.LENGTH_LONG).show();
        }
        } catch (FeedException | JSONFileException | RSSException e) {
            Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
/*        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
     //   mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */
}
