package com.de.k3ron3i.journalapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.de.k3ron3i.journalapp.DbHelper.Definitions;


public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.GuestViewHolder> {

    private Context mContext;
    // TODO (1) Replace the mCount with a Cursor field called mCursor
    private Cursor mCursor;

    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     */
    // TODO (2) Modify the constructor to accept a cursor rather than an integer
    public NotesListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        // TODO (3) Set the local mCursor to be equal to cursor
        this.mCursor = cursor;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.notes_list_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        // TODO (5) Move the cursor to the passed in position, return if moveToPosition returns false

if(!(mCursor.moveToPosition(position)))
   // Toast.makeText(mContext, "Cursor is probably null", Toast.LENGTH_SHORT).show();
    return;

        // TODO (6) Call getString on the cursor to get the guest's name

        String Notecontent = mCursor.getString(mCursor.getColumnIndex(Definitions.NoteslistEntry.COLUMN_NOTE_CONTENT));
        // TODO (7) Call getInt on the cursor to get the party size
//int partySize = mCursor.getInt(mCursor.getColumnIndex(Definitions.NoteslistEntry.COLUMN_NOTE_HEAD));
        // TODO (8) Set the holder's nameTextView text to the guest's name
holder.foundnote.setText(Notecontent);

        // TODO (9) Set the holder's partySizeTextView text to the party size
holder.partySizeTextView.setText("23");
    }

    @Override
    public int getItemCount() {
        // TODO (4) Update the getItemCount to return the getCount of mCursor
        return mCursor.getCount();
    }
//recheck cursor content using this function.

    public void swapCursor(Cursor newCursor) {

        if (mCursor != null) mCursor.close();

        mCursor = newCursor;

        if (newCursor != null) {

            this.notifyDataSetChanged();
        }
    }


    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class GuestViewHolder extends RecyclerView.ViewHolder {

        // Will display the guest name
        TextView foundnote;
        // Will display the party size number
        TextView partySizeTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link NotesListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public GuestViewHolder(View itemView) {
            super(itemView);
            foundnote = (TextView) itemView.findViewById(R.id.note_content);
            partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
        }

    }


}