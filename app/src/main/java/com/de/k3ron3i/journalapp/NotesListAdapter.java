package com.de.k3ron3i.journalapp;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.de.k3ron3i.journalapp.DbHelper.Definitions;


public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.GuestViewHolder> {

    private OnNoteClickedListener mNoteClickListener;

    private Context mContext;
    // Initialize the cursor to point the data
    private Cursor mCursor;


    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     */
    // Accepting cursor as input
    public NotesListAdapter( Context context, Cursor cursor) {
        this.mContext = context;

        this.mCursor = cursor;



    }

    public interface OnNoteClickedListener {
        void onNoteClicked(int note,String notecontent);
    }



    public void setOnNoteClickedListener(OnNoteClickedListener l) {
        mNoteClickListener = l;
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


        //  Move the cursor to the passed in position, return if moveToPosition returns false

if(!(mCursor.moveToPosition(position)))
   // Toast.makeText(mContext, "Cursor is probably null", Toast.LENGTH_SHORT).show();
    return;





        String Notecontent = mCursor.getString(mCursor.getColumnIndex(Definitions.NoteslistEntry.COLUMN_NOTE_CONTENT));
        /*

            Call getInt on the cursor to get the party size
            int partySize = mCursor.getInt(mCursor.getColumnIndex(Definitions.NoteslistEntry.COLUMN_NOTE_HEAD));
            Set the holder's nameTextView text to the guest's name

          */
        long id = mCursor.getLong(mCursor.getColumnIndex(Definitions.NoteslistEntry._ID));
holder.foundnote.setText(Notecontent);

holder.itemView.setTag(id);




holder.lastedited.setText(mCursor.getString(mCursor.getColumnIndex(Definitions.NoteslistEntry.COLUMN_NOTE_EDIT_DATE)));



    }

    @Override
    public int getItemCount() {
        //Getting the total count of the items
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


        TextView lastedited;
        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link NotesListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public GuestViewHolder(View itemView) {
            super(itemView);
            lastedited = (TextView)itemView.findViewById(R.id.last_edit);
            foundnote = (TextView) itemView.findViewById(R.id.note_content);

            /*
            Implementing opening the note for detail whenever it is clicked in a list view
             */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if (mNoteClickListener != null) {
                        final int note_id =  getAdapterPosition();

                         mCursor.moveToPosition(getLayoutPosition());



                        mNoteClickListener.onNoteClicked(note_id,mCursor.getString(mCursor.getColumnIndex(Definitions.NoteslistEntry.COLUMN_NOTE_CONTENT)));


                    }




                }
            });


        }

    }





}