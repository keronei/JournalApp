package com.de.k3ron3i.journalapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.de.k3ron3i.journalapp.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {
    // T: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Button sign_out;

    // T: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView emailM,Username;

    private static final int RC_SIGN_IN = 007;

    SignInButton signInButton;
    //Object mGoogleSignInClient;

    private OnFragmentInteractionListener mListener;

    private GoogleApiClient mGoogleApiClient;

    GoogleSignInOptions account;

    private FirebaseAuth firebaseAuth;

    //GoogleApiClient x = "923870848129-9lng388g5fuvhmthgn8t6ia8ge2fimh7.apps.googleusercontent.com";

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // : Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();






       // Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(923870848129-9lng388g5fuvhmthgn8t6ia8ge2fimh7.apps.googleusercontent.com);
       // startActivityForResult(signInIntent, RC_SIGN_IN);

         account = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();



        mGoogleApiClient = new GoogleApiClient.Builder(getContext())

                .addApi(Auth.GOOGLE_SIGN_IN_API, account)
                .build();


       //mGoogleApiClient = GoogleSignIn.getClient(getContext(),account);

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());

        if(googleSignInAccount !=null){
            /*
            hide this fragment because there is a user signed in
             */


        }



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        whatsVisible(false);
                    }
                });
    }


    private void handleResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // If everything went well... do the rest of updates
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();



            String personName = googleSignInAccount.getDisplayName();
            String personPhotoUrl = googleSignInAccount.getPhotoUrl().toString();
            String email = googleSignInAccount.getEmail();


            Username.setText(personName);

            emailM.setText(email);

            whatsVisible(true);




        } else {
            // Signed out, show unauthenticated UI.

            whatsVisible(false);

        }
    }

public void whatsVisible(boolean see) {

    if (see) {

        signInButton.setVisibility(View.GONE);

        emailM.setVisibility(View.VISIBLE);

        Username.setVisibility(View.VISIBLE);

        sign_out.setVisibility(View.VISIBLE);
    }else {

        signInButton.setVisibility(View.VISIBLE);

        sign_out.setVisibility(View.GONE);

        emailM.setVisibility(View.GONE);

        Username.setVisibility(View.GONE);

    }

}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =
        inflater.inflate(R.layout.fragment_sign_in, container, false);

        // Set the dimensions of the sign-in button.
        signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        emailM = (TextView)view.findViewById(R.id.email);

        Username = (TextView)view.findViewById(R.id.username);

        sign_out = (Button)view.findViewById(R.id.sign_out);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();

                whatsVisible(false);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Intent signInIntent = mGoogleApiClient.getSignInIntent();

                Intent x = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

                startActivityForResult(x, RC_SIGN_IN);
            }
        });

        return  view;
    }

    // : Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // : Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
