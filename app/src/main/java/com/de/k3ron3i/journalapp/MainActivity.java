package com.de.k3ron3i.journalapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.de.k3ron3i.journalapp.DbHelper.Definitions;
import com.de.k3ron3i.journalapp.DbHelper.NotesDbHelper;
import com.de.k3ron3i.journalapp.Fragments.AddNoteFragment;


public class MainActivity extends AppCompatActivity implements AddNoteFragment.OnFragmentInteractionListener {

    //initializing items to be used
    EditText NotesContent;

    SQLiteDatabase notesDb;

    NotesListAdapter mNotesAdapter;

    Toolbar toolbar;

    ActionBar actionBar;

    ImageButton addNote;


    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       actionBar = getSupportActionBar();
//initialize the recyclerview
         recyclerView= (RecyclerView)this.findViewById(R.id.all_notes_list_view);
        //append linearlayout to the recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//initialize the dbHelper class
        NotesDbHelper notesDbHelper = new NotesDbHelper(this);

        //keep a reference to this database as far as it is alive
        notesDb = notesDbHelper.getWritableDatabase();

        Cursor cursor = getAllNotes();



        mNotesAdapter = new NotesListAdapter(getApplicationContext(),cursor);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),re );
        //recyclerView.setAdapter(recyclerAdapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.resizer));
        recyclerView.addItemDecoration(itemDecoration);


        recyclerView.setAdapter(mNotesAdapter);


        addNote = (ImageButton)findViewById(R.id.add_note);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditor();
            }
        });

        

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }else if(id ==  android.R.id.home){
saveChanges();
whatsVisible(false);

            //setContentView(R.layout.activity_main);
           mNotesAdapter.swapCursor(getAllNotes());
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
actionBar.setTitle("Journal App");
actionBar.setDisplayHomeAsUpEnabled(false);




        }

        return super.onOptionsItemSelected(item);
    }

public void openEditor(){
        whatsVisible(true);
   // ((LinearLayout)findViewById(R.id.fragment_container)).removeAllViews();
 getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new AddNoteFragment()).commit();

}





    //Fixing
    public void resetBar(Boolean child){
        if(child){

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("New Note");
        }else {
            Toast.makeText(getApplicationContext(), "Nothing Yet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //this one will get all the notes entries
    private Cursor getAllNotes() {
        return notesDb.query(
                Definitions.NoteslistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Definitions.NoteslistEntry.COLUMN_TIMESTAMP
        );
    }
    public boolean popFragment() {

    boolean isPop = false;


        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
        isPop = true;
        getSupportFragmentManager().popBackStackImmediate();
    }

    return isPop;
}

    @Override
    public void onBackPressed() {
        if (!popFragment()) {
            finish();
        }
    }


public void saveChanges(){
    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

//if you added fragment via layout xml
    AddNoteFragment fragment = (AddNoteFragment) fm.findFragmentById(R.id.fragment_container);
    fragment.addNewNote();
}


public void whatsVisible(boolean which){

    if(which){
        addNote.setVisibility(View.GONE);

        recyclerView.setVisibility(View.GONE);
    }else{

        recyclerView.setVisibility(View.VISIBLE);
        addNote.setVisibility(View.VISIBLE);
    }

}

}
