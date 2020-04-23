package com.example.tasktimer;

import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.TaskViewHolder> {
    private static final String TAG = "CursorRecyclerViewAdapt";
    private Cursor mCursor;
    private OnTaskClickListener mListener;

    interface OnTaskClickListener {
        void onEditClick(Task task);
        void onDeleteClick(Task task);
    }

    public CursorRecyclerViewAdapter(Cursor cursor, OnTaskClickListener listener) {
        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called");
        mCursor = cursor;
        mListener = listener;
    }

    public CursorRecyclerViewAdapter(Cursor cursor) {
        Log.d(TAG, "CursorRecyclerViewAdapter: Constructor called");
        mCursor = cursor;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      //  Log.d(TAG, "onCreateViewHolder: newview requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_items,parent ,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
   //     Log.d(TAG, "onBindViewHolder: starts");

        if((mCursor == null) || (mCursor.getCount() == 0)){
            Log.d(TAG, "onBindViewHolder: providing instruction");
            holder.name.setText(R.string.instruction_heading);
            holder.description.setText(R.string.instructions);
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }else {
            if(!mCursor.moveToPosition(position)){
                throw new IllegalStateException("Couldn't move cursor to position " +position);
            }

            final Task task = new Task(mCursor.getLong(mCursor.getColumnIndex(TasksContract.Columns._ID)),
                    mCursor.getString(mCursor.getColumnIndex((TasksContract.Columns.TASKS_NAME))),
                    mCursor.getString(mCursor.getColumnIndex((TasksContract.Columns.TASKS_DESCRIPTION))),
                    mCursor.getInt(mCursor.getColumnIndex((TasksContract.Columns.TASKS_SORTORDER))));

            holder.name.setText(task.getName());
            holder.description.setText(task.getDescription());
            holder.editButton.setVisibility(View.VISIBLE);//todo add onclickListener
            holder.deleteButton.setVisibility(View.VISIBLE);//todo add onclickListener

            View.OnClickListener buttonListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                //    Log.d(TAG, "onClick: starts");
                    switch(view.getId()){
                        case R.id.tli_edit:
                            if(mListener != null) {
                                mListener.onEditClick(task);
                            }
                            break;
                        case R.id.tli_delete:
                            if(mListener != null) {
                                mListener.onDeleteClick(task);
                            }
                            break;
                        default:
                            Log.d(TAG, "onClick: found unexpected button id");
                    }

                 //   Log.d(TAG, "onClick: button with id " + view.getId() + " clicked");
                 //   Log.d(TAG, "onClick: task name is " + task.getName());
                }
            };
            //an example of callback
            class Listener implements View.OnClickListener {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: starts");
                    Log.d(TAG, "onClick: buttons with id"+ view.getId() + " Clicked");
                    Log.d(TAG, "onClick: TASK name is " + task.getName());
                }
            }
           // Listener buttonListener = new Listener();
            holder.editButton.setOnClickListener(buttonListener);
            holder.deleteButton.setOnClickListener(buttonListener);

        }
    }

    @Override
    public int getItemCount() {

      //  Log.d(TAG, "getItemCount: starts");
        if((mCursor == null) || (mCursor.getCount() == 0)){
            return 1;//fib, because we poulate the single ViewHolder with Instruction
        } else{
            return mCursor.getCount();
        }
    }

    /**
     * Swap  in a new Cursor.returnning the old cursor
     * the returned old cursor is <em>not</em> closed
     *
     * @param newCursor The neew cursor to be used
     * @return Returns the previously set cursor or null if there wasn't one
     * if the given new cursor is the same instance as the previous set \Cursor, null is also returned.
     */
    Cursor swapCursor (Cursor newCursor) {
        if(newCursor == mCursor){
            return null;
        }

        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if(newCursor != null){
            //notify the observer about the new Cursor
            notifyDataSetChanged();
        }else{
            //notify the observer about the lack of data
            notifyItemRangeRemoved(0,getItemCount());
        }
        return oldCursor;
    }
    static class TaskViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "TaskViewHolder";

        TextView name = null;
        TextView description = null;
        ImageButton editButton = null;
        ImageButton deleteButton = null;

        public TaskViewHolder(View itemView) {
            super(itemView);
           // Log.d(TAG, "CursorRecyclerViewAdapter: starts");
            this.name = (TextView) itemView.findViewById(R.id.tli_name);
            this.description = (TextView) itemView.findViewById(R.id.tli_description);
            this.editButton = (ImageButton) itemView.findViewById(R.id.tli_edit);
            this.deleteButton = (ImageButton) itemView.findViewById(R.id.tli_delete);
        }
    }
}
