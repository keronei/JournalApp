package com.de.k3ron3i.journalapp.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.de.k3ron3i.journalapp.DbHelper.Definitions;
import com.de.k3ron3i.journalapp.DbHelper.NotesDbHelper;
import com.de.k3ron3i.journalapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    //  Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //  Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SQLiteDatabase notesDb;

    String Notetoedit;

    int id_of_note;


    EditText editor_to_update;
    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    //  Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        NotesDbHelper notesDbHelper = new NotesDbHelper(getContext());


        notesDb = notesDbHelper.getWritableDatabase();

        Bundle myArgs = getArguments();

       Notetoedit =  myArgs.getString("note_");

       id_of_note = myArgs.getInt("id_") + 1;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_detail, container, false);

        editor_to_update = (EditText)view.findViewById(R.id.editor_to_uptdate);

        editor_to_update.requestFocus();

        editor_to_update.setText(Notetoedit);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editor_to_update.setText("");
    }






    //  Rename method, update argument and hook method into UI event
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
        //  Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void updateNote(){

        if (editor_to_update.getText().length() == 0 ||
                editor_to_update.getText().length() == 0) {

/*
Omit this entry if the user intentionally deletes all the content of the note
 */
            Toast.makeText(getContext(), "Note Removed!", Toast.LENGTH_SHORT).show();


            notesDb.delete(Definitions.NoteslistEntry.TABLE_NAME,"_id = ?",new String[]{String.valueOf(id_of_note)});

        }else {

            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


            ContentValues cv = new ContentValues();

        /*
        Getting the updated note by the user
         */

            cv.put(Definitions.NoteslistEntry.COLUMN_NOTE_CONTENT,editor_to_update.getText().toString() );

            cv.put(Definitions.NoteslistEntry.COLUMN_NOTE_EDIT_DATE,date);

            notesDb.update(Definitions.NoteslistEntry.TABLE_NAME, cv, "_id = ?", new String[]{String.valueOf(id_of_note)});


            Toast.makeText(getContext(), "Note Updated!", Toast.LENGTH_SHORT).show();

        }
    }
}
