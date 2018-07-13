package com.de.k3ron3i.journalapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.de.k3ron3i.journalapp.DbHelper.Definitions;
import com.de.k3ron3i.journalapp.DbHelper.NotesDbHelper;
import com.de.k3ron3i.journalapp.Fragments.AddNoteFragment;
import com.de.k3ron3i.journalapp.Fragments.DetailFragment;
import com.de.k3ron3i.journalapp.Fragments.SignInFragment;
import com.de.k3ron3i.journalapp.Pojo.NotesPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

//import android.widget.SearchView;


public class MainActivity extends AppCompatActivity implements AddNoteFragment.OnFragmentInteractionListener,
        SignInFragment.OnFragmentInteractionListener, DetailFragment.OnFragmentInteractionListener, SearchView.OnQueryTextListener {

    //initializing items to be used
    EditText NotesContent;

    SQLiteDatabase notesDb;

    NotesListAdapter mNotesAdapter;

    Toolbar toolbar;

    Cursor cursor;

    ActionBar actionBar;

    SearchView searchView;

    TextView addNote;

    ImageView Arrow;


    private List<NotesPojo> NotesList;

    private FirebaseAuth OAuth;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OAuth = FirebaseAuth.getInstance();

//getAllNotes();

        NotesList = new ArrayList<>();


        FirebaseUser firebaseUser = OAuth.getCurrentUser();

        if(firebaseUser ==null)
        {

//openSignIn();


        }



        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        Arrow = (ImageView) findViewById(R.id.arrow);
//initialize the recyclerview
        recyclerView = (RecyclerView) this.findViewById(R.id.all_notes_list_view);
        //append linearlayout to the recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//initialize the dbHelper class
        NotesDbHelper notesDbHelper = new NotesDbHelper(this);

        //keep a reference to this database as far as it is alive
        notesDb = notesDbHelper.getWritableDatabase();


        mNotesAdapter = new NotesListAdapter(getApplicationContext(), getAllNotes());



        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),);
        //recyclerView.setAdapter(recyclerAdapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.resizer));
        recyclerView.addItemDecoration(itemDecoration);




        recyclerView.setAdapter(mNotesAdapter);


        mNotesAdapter.setOnNoteClickedListener(new NotesListAdapter.OnNoteClickedListener() {
            @Override
            public void onNoteClicked(String noteID, String noteAwaited) {

                searchView.setIconified(true);
                openDetail(noteAwaited, noteID);

                // getNoteToEdit(note);


            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                Toast.makeText(getApplicationContext(),"Item Tapped",Toast.LENGTH_LONG).show();
                mNotesAdapter.RefreshList(getAllNotes());

                return true;
            }

            @Override
            public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {


                final long id = (long)viewHolder.itemView.getTag();

                mNotesAdapter.RefreshList(getAllNotes());


                if(direction==ItemTouchHelper.LEFT){


                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked

                                    notesDb.delete(Definitions.NoteslistEntry.TABLE_NAME,"_id = ?", new String[]{String.valueOf(id)});

                                    Toast.makeText(getApplicationContext(),"Removed",Toast.LENGTH_LONG).show();

                                    mNotesAdapter.RefreshList(getAllNotes());

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Sure to remove this ?").setPositiveButton("Yeah", dialogClickListener)
                            .setNegativeButton("Nope", dialogClickListener).show();
                } else
                {



                }
            }

        }).attachToRecyclerView(recyclerView);


        addNote = (TextView) findViewById(R.id.add_note);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //whatsVisible(true);
                openEditor();
                searchView.setIconified(true);

            }
        });



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        MenuItem searcher = menu.findItem(R.id.search);

        searchView = (SearchView) searcher.getActionView();

        //SearchView searchView = (SearchView) Menu

        searchView.setOnQueryTextListener(this);

        searcher.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {

                mNotesAdapter.RefreshList(getAllNotes());
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            openSignIn();

            return true;
        }

        else if(id ==  android.R.id.home){
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(fragment.getTag()=="create") {
                saveChanges();
            }else if(fragment.getTag()=="detail") {
                updateChanges();
            } else {
                Toast.makeText(getApplicationContext(),"Configuring your Backups",Toast.LENGTH_LONG).show();
            }

            whatsVisible(false);
            //actionBar.setDisplayHomeAsUpEnabled(false);
            //setContentView(R.layout.activity_main);
            mNotesAdapter.RefreshList(getAllNotes());

            if(fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            actionBar.setTitle("Journal App");
            actionBar.setDisplayHomeAsUpEnabled(false);




        }

        return super.onOptionsItemSelected(item);
    }

    public void openEditor() {
        whatsVisible(true);
        // ((LinearLayout)findViewById(R.id.fragment_container)).removeAllViews();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new AddNoteFragment(), "create").commit();

    }

    public void openSignIn(){
        whatsVisible(true);

        actionBar.setTitle("My Account");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // ((LinearLayout)findViewById(R.id.fragment_container)).removeAllViews();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new SignInFragment(),"signIn").commit();

    }


    public void openDetail(String noteToEdit, String noteId) {
    /*
        instead of using remove all views, this function will instead hide all the components of the activity_main.xml
        as far as there is content from the fragment to avoid overlapping
        */

        DetailFragment extraArgs = new DetailFragment();

        Bundle arguments = new Bundle();

        arguments.putString("note_",noteToEdit);

        arguments.putString("id_", noteId);

        extraArgs.setArguments(arguments);

        whatsVisible(true);

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("Edit Note");

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,extraArgs,"detail").commit();

    }





    //Fixing
    public void resetBar(Boolean child){
        if(child){

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("New Note");
            searchView.setIconified(true);
        }else {
            Toast.makeText(getApplicationContext(), "Nothing Yet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //this one will get all the notes entries
    private List<NotesPojo> getAllNotes() {


        cursor = //notesDb.rawQuery("Select * from "+Definitions.NoteslistEntry.TABLE_NAME,null);

                notesDb.query(
                        Definitions.NoteslistEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        Definitions.NoteslistEntry.COLUMN_TIMESTAMP
                );


        int Notes = cursor.getColumnIndex(Definitions.NoteslistEntry.COLUMN_NOTE_CONTENT);

        int NotesLastEdit = cursor.getColumnIndex(Definitions.NoteslistEntry.COLUMN_NOTE_EDIT_DATE);

        int id = cursor.getColumnIndex(Definitions.NoteslistEntry._ID);


        if (cursor.moveToFirst()) {


            do {

                NotesPojo notesPojo = new NotesPojo();

                notesPojo.setmNotes(cursor.getString(Notes));

                notesPojo.setrID(cursor.getString(id));

                notesPojo.setmNoteLastEdit(cursor.getString(NotesLastEdit));

                Log.d("Queried Entries", cursor.getString(Notes));

                NotesList.add(notesPojo);


            }

            while (cursor.moveToNext());


        }

        return NotesList;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void saveChanges() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

//if you added fragment via layout xml
        AddNoteFragment fragment = (AddNoteFragment) fm.findFragmentById(R.id.fragment_container);
        fragment.addNewNote();

    }

    public void signIn() {
        whatsVisible(true);
        actionBar.setTitle("Sign In");
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new SignInFragment(), "signIn").commit();
    }

    public void updateChanges(){
        //TODO fix the update bug when changes are updated but both notes are still on cache.
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

//if you added fragment via layout xml
        DetailFragment fragment = (DetailFragment) fm.findFragmentById(R.id.fragment_container);
        fragment.updateNote();

        mNotesAdapter.RefreshList(getAllNotes());

    }

    /*
    The following function is a quck summary of the config depending on the page loaded
    Note that this gives smart behaviours of various components in the layouts
     */
    public void whatsVisible(boolean which) {

        if (which) {
            addNote.setVisibility(View.GONE);
            Arrow.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            Arrow.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            addNote.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String s) {


        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        final List<NotesPojo> newList = filter(NotesList, s);

        mNotesAdapter.RefreshList(newList);
        return true;
    }

    //initialise a function to filter the results

    private List<NotesPojo> filter(List<NotesPojo> list, String query) {

        query = query.toLowerCase();

        final List<NotesPojo> filteredList = new ArrayList<>();

        for (NotesPojo xfilter : list) {

            final String note = xfilter.getmNotes().toLowerCase();

            if (note.contains(query)) {
                filteredList.add(xfilter);
            }


        }

        return filteredList;

    }
}
