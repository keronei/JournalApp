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


public class MainActivity extends AppCompatActivity implements AddNoteFragment.OnFragmentInteractionListener,DetailFragment.OnFragmentInteractionListener {

    //initializing items to be used
    EditText NotesContent;

    SQLiteDatabase notesDb;

    NotesListAdapter mNotesAdapter;

    Toolbar toolbar;

    ActionBar actionBar;

    TextView addNote;

    ImageView Arrow;



    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       actionBar = getSupportActionBar();

Arrow = (ImageView)findViewById(R.id.arrow);
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
         //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),);
        //recyclerView.setAdapter(recyclerAdapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.resizer));
        recyclerView.addItemDecoration(itemDecoration);




        recyclerView.setAdapter(mNotesAdapter);


        mNotesAdapter.setOnNoteClickedListener(new NotesListAdapter.OnNoteClickedListener() {
            @Override
            public void onNoteClicked(int note,String noteAwaited) {

                openDetail(noteAwaited,note);

               // getNoteToEdit(note);


            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                Toast.makeText(getApplicationContext(),"Item Tapped",Toast.LENGTH_LONG).show();
                mNotesAdapter.swapCursor(getAllNotes());

                return true;
            }

            @Override
            public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {


                final long id = (long)viewHolder.itemView.getTag();

                mNotesAdapter.swapCursor(getAllNotes());


                if(direction==ItemTouchHelper.LEFT){


                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked

                                    notesDb.delete(Definitions.NoteslistEntry.TABLE_NAME,"_id = ?", new String[]{String.valueOf(id)});

                                    Toast.makeText(getApplicationContext(),"Removed",Toast.LENGTH_LONG).show();

                                    mNotesAdapter.swapCursor(getAllNotes());

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
            }
            else
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
                                        }

        else if(id ==  android.R.id.home){
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(fragment.getTag()=="create") {
                saveChanges();
            }else
                {
updateChanges();
                }

              whatsVisible(false);
              //actionBar.setDisplayHomeAsUpEnabled(false);
            //setContentView(R.layout.activity_main);
           mNotesAdapter.swapCursor(getAllNotes());

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
 getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new AddNoteFragment(),"create").commit();

}




    public void openDetail(String noteToEdit,int noteId){
    /*
        instead of using remove all views, this function will instead hide all the components of the activity_main.xml
        as far as there is content from the fragment to avoid overlapping
        */

    DetailFragment extraArgs = new DetailFragment();

        Bundle arguments = new Bundle();

        arguments.putString("note_",noteToEdit);

        arguments.putInt("id_",noteId);

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




    @Override
    public void onBackPressed() {
      super.onBackPressed();
    }


public void saveChanges(){
    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

//if you added fragment via layout xml
    AddNoteFragment fragment = (AddNoteFragment) fm.findFragmentById(R.id.fragment_container);
    fragment.addNewNote();

}


    public void updateChanges(){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

//if you added fragment via layout xml
        DetailFragment fragment = (DetailFragment) fm.findFragmentById(R.id.fragment_container);
        fragment.updateNote();

    }

/*
The following function is a quck summary of the config depending on the page loaded
Note that this gives smart behaviours of various components in the layouts
 */
public void whatsVisible(boolean which){

    if(which){
        addNote.setVisibility(View.GONE);
        Arrow.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }else{
        Arrow.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        addNote.setVisibility(View.VISIBLE);
    }

}

}
