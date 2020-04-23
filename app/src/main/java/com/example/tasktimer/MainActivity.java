package com.example.tasktimer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.OnTaskClickListener, AddEditActivityFragment.OnSaveClicked {
    private static final String TAG = "MainActivity";

    //whether or not the activity is in 2-pane mode
    //ie runnig on landscape mode
    private boolean mTwoPane = false;
    private static final String ADD_EDIT_FRAGMENT = "AddEditFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.task_details_container) != null){
            //The details container view container will be present only in the large-screen layout (res/value-land and res/values-sw600dp
            //if this view is present ,then the activity should be in 2 pane mode
            mTwoPane = true;
        }
    }

    @Override
    //function to remove the Addeditfragment after the saved button is pressed
    public void onSaveClicked() {
        Log.d(TAG, "onSaveClicked: starts");
        FragmentManager  fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
        if(fragment != null ){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.remove(fragment);
//            fragmentTransaction.commit();
        }
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
        switch(id) {
            case R.id.menumain_addTask:
                taskEditRequest(null);
                break;
            case R.id.menumain_showDuration:
                break;
            case R.id.action_settings:
                break;
            case R.id.menumain_showAbout:
                break;
            case R.id.menumain_generate:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEditClick(Task task) {
        taskEditRequest(task);
    }

    @Override
    public void onDeleteClick(Task task) {
        getContentResolver().delete(TasksContract.buildTaskUri(task.getId()), null,null);
    }

    private void taskEditRequest(Task task){
        Log.d(TAG, "taskEditRequest: starts");
        if(mTwoPane){
            Log.d(TAG, "taskEditRequest: in 2 Pane mode (tablet)");
            AddEditActivityFragment fragment = new AddEditActivityFragment();

            Bundle arguements = new Bundle();
            arguements.putSerializable(Task.class.getSimpleName(), task);
            fragment.setArguments(arguements);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.task_details_container, fragment)
                    .commit();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.task_details_container, fragment);
//            fragmentTransaction.commit();

        }else{
            Log.d(TAG, "taskEditRequest: in single pane mode(phone)");
            // in single_pane mode , start the detail activity for the selected item id
            Intent detailIntent = new Intent(this, AddEditActivity.class);
            if(task != null){// editing a task
                detailIntent.putExtra(Task.class.getSimpleName() , task);
                startActivity(detailIntent);
            }else{// adding a new task
                startActivity(detailIntent);
            }
        }
    }
    
}
