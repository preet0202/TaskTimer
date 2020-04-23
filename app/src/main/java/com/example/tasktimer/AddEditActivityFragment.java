package com.example.tasktimer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditActivityFragment extends Fragment {

    private static final String TAG = "AddEditActivityFragment";

    public enum FragmentEditMode {EDIT, ADD};
    private FragmentEditMode mMode;

    private EditText mNameTextView;
    private EditText mDescriptionTextView;
    private EditText mSortOrderTextView;
    private Button mSaveButton;
    private OnSaveClicked mSaveListener = null;

    interface OnSaveClicked{
        void onSaveClicked();
    }


    public AddEditActivityFragment() {
        Log.d(TAG, "AddEditActivityFragment: constructor called");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: starts");
        super.onAttach(context);

        //Activities containing this ragments must implement its callbacks

        Activity activity = getActivity();
        if(!(activity instanceof OnSaveClicked)){
            throw new ClassCastException(activity.getClass().getSimpleName()
            + " must implement AddeditActivityFragment.OnSaveClicked interface");
        }
        mSaveListener = (OnSaveClicked) activity;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: starts");
        super.onDetach();
        mSaveListener = null ;
    }

    @Override
    //called by main activity
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_add_edit, container ,false);

        mNameTextView = (EditText) view.findViewById(R.id.addedit_name);
        mDescriptionTextView = (EditText) view.findViewById(R.id.addedit_description);
        mSortOrderTextView = (EditText) view.findViewById(R.id.addedit_sortorder);
        mSaveButton = (Button) view.findViewById(R.id.addedit_save);

        //Bundle arguments = getActivity().getIntent().getExtras();
        Bundle arguments = getArguments();

        final Task task;
        if(arguments != null) {

            Log.d(TAG, "onCreateView: retriving task details");
            task = (Task) arguments.getSerializable(Task.class.getSimpleName());
            if(task!= null){
                Log.d(TAG, "onCreateView: Tasks details found, editing...");
                mNameTextView.setText(task.getName());
                mDescriptionTextView.setText(task.getDescription());
                mSortOrderTextView.setText(Integer.toString(task.getSortOrder()));
                mMode = FragmentEditMode.EDIT;
            }else{
                // No tasks, so we must be adding a new task not editing a existing one
                mMode = FragmentEditMode.ADD;
            }
        }else{
            task = null;
            Log.d(TAG, "onCreateView: No arguments, adding new records");
            mMode = FragmentEditMode.ADD;
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Update the database if atleast one field is changed,
                //-There is no need to hit the database unless this has happened
                int so; // to save repeated conversions to int
                if(mSortOrderTextView.length()>0) {
                    so= Integer.parseInt(mSortOrderTextView.getText().toString());
                } else{
                    so=0;
                }

                ContentResolver contentResolver = getActivity().getContentResolver();
                ContentValues values = new ContentValues();

                switch(mMode){
                    case EDIT:
                        if(!mNameTextView.getText().toString().equals(task.getName())){
                            values.put(TasksContract.Columns.TASKS_NAME, mNameTextView.getText().toString());
                        }
                        if(!mDescriptionTextView.getText().toString().equals(task.getDescription())){
                            values.put(TasksContract.Columns.TASKS_DESCRIPTION, mDescriptionTextView.getText().toString());
                        }
                        if(so!= task.getSortOrder()){
                            values.put(TasksContract.Columns.TASKS_SORTORDER, so);
                        }
                        if(values.size() != 0){
                            Log.d(TAG, "onClick: updating task");
                            contentResolver.update(TasksContract.buildTaskUri(task.getId()), values , null ,null);
                            break;
                        }
                    case ADD:
                        if(mNameTextView.length()>0){
                            Log.d(TAG, "onClick: adding new task");
                            values.put(TasksContract.Columns.TASKS_NAME , mNameTextView.getText().toString());
                            values.put(TasksContract.Columns.TASKS_DESCRIPTION, mDescriptionTextView.getText().toString());
                            values.put(TasksContract.Columns.TASKS_SORTORDER, so);
                            contentResolver.insert(TasksContract.CONTENT_URI , values);
                        }
                        break;
                }
                Log.d(TAG, "onClick: Done Editing");

                if(mSaveButton != null){
                    mSaveListener.onSaveClicked();
                }
            }
        });
        Log.d(TAG, "onCreateView: Exiting...");
        return view;
    }

}
