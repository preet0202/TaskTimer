package com.example.tasktimer;


import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.InvalidParameterException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivityFragment";

    public static final int LOADER_ID = 0;
    private CursorRecyclerViewAdapter mAdapter;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: starts");
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_main, container,false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new CursorRecyclerViewAdapter(null, (CursorRecyclerViewAdapter.OnTaskClickListener)getActivity());
        recyclerView.setAdapter(mAdapter);

        Log.d(TAG, "onCreateView: returning");
        return view;
        // Inflate the layout for this fragment
        }


    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader: starts with id" + id);
        String projection [] = {TasksContract.Columns._ID, TasksContract.Columns.TASKS_NAME,
                                TasksContract.Columns.TASKS_DESCRIPTION,TasksContract.Columns.TASKS_SORTORDER};
        //<order by> Task.SortOrder ,Task.NAme CollateNOCASE
        String sortOrder = TasksContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns.TASKS_NAME + " COLLATE NOCASE";

        switch(id){
            case LOADER_ID:
                return new CursorLoader(getActivity(),
                        TasksContract.CONTENT_URI,
                        projection,
                       null,
                       null,
                        sortOrder);
                        //break;

            default:
                throw new InvalidParameterException(TAG + ".onCreateLoader called with invalid loader id"+ id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "entering onLoadFinished: ");
        mAdapter.swapCursor(data);
        int count= mAdapter.getItemCount();

        Log.d(TAG, "onLoadFinished: count is "+ count);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: starts");
        mAdapter.swapCursor(null);
    }
}
