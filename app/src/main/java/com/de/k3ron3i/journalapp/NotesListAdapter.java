package com.de.k3ron3i.journalapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.de.k3ron3i.journalapp.Pojo.NotesPojo;

import java.util.ArrayList;
import java.util.List;


public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.GuestViewHolder> {

    private OnNoteClickedListener mNoteClickListener;

    private Context mContext;
    // Initialize the cursor to point the data
    private List<NotesPojo> pojolist;

    private NotesPojo AtStack;


    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     */
    // Accepting cursor as input
    public NotesListAdapter(Context context, List<NotesPojo> list) {
        this.mContext = context;

        this.pojolist = list;



    }

    public interface OnNoteClickedListener {
        void onNoteClicked(String note, String notecontent);
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

        AtStack = pojolist.get(position);


        String Notecontent = AtStack.getmNotes();

        String id = AtStack.getrID();

        holder.foundnote.setText(Notecontent);

        holder.itemView.setTag(id);


        holder.lastedited.setText(AtStack.getmNoteLastEdit());



    }

    @Override
    public int getItemCount() {
        //Getting the total count of the items
        return (pojolist == null) ? 0 : pojolist.size();
    }


/*
 always refresh the list incase of deletion or addition.
  */

    public void RefreshList(List<NotesPojo> newcontent) {

        pojolist = new ArrayList<>();

        pojolist.addAll(newcontent);

        notifyDataSetChanged();

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


                        AtStack = pojolist.get(getAdapterPosition());


                        mNoteClickListener.onNoteClicked(AtStack.getrID(), AtStack.getmNotes());


                    }




                }
            });


        }

    }





}