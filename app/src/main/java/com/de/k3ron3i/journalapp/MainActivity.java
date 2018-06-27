package com.de.k3ron3i.journalapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.de.k3ron3i.journalapp.DbHelper.Definitions;
import com.de.k3ron3i.journalapp.DbHelper.NotesDbHelper;


public class MainActivity extends AppCompatActivity {

    //initializing items to be used
    EditText NotesContent;

    SQLiteDatabase notesDb;

    NotesListAdapter mNotesAdapter;

    Toolbar toolbar;

    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       actionBar = getSupportActionBar();
//initialize the recyclerview
        RecyclerView recyclerView = (RecyclerView)this.findViewById(R.id.all_notes_list_view);
        //append linearlayout to the recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//initialize the dbHelper class
        NotesDbHelper notesDbHelper = new NotesDbHelper(this);

        //keep a reference to this database as far as it is alive
        notesDb = notesDbHelper.getWritableDatabase();

        
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this,EditMode.class);
                startActivity(i);
                fab.setVisibility(View.GONE);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        return super.onOptionsItemSelected(item);
    }





    //Fixing
    public void resetBar(Boolean child){
        if(child){

            actionBar.setDisplayHomeAsUpEnabled(true);
        }else {
            Toast.makeText(getApplicationContext(), "Nothing Yet", Toast.LENGTH_SHORT).show();
        }

    }
}
