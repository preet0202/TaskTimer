package com.example.tasktimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.service.autofill.FillRequest;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AppDialog extends DialogFragment {
    private static final String TAG = "AppDialog";

    public static final String DIALOD_ID = "id";
    public static final String DIALOG_MESSAGE = "message";
    public static final String DIALOD_POSITIVE_RID = "positive_rid";
    public static final String DIALOG_NEGATIVE_RID = "negative_rid";

    /**
     * The dialogs callback interface to notify of users selected results (deletion confirmed, etc).
     *
     */
    interface DialogEvents {
        void onPositiveDialogResult(int dialogId , Bundle args);
        void onNegativeDialogResult(int dialogId , Bundle args);
        void onDialogCancelled(int dialogId);
    }

    private DialogEvents mDialogEvents;

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach: entering onAttch activity is " + context.toString());
        super.onAttach(context);

        // Activities containig this fragment must its callbacks
        if(!(context instanceof DialogEvents)){
            throw new ClassCastException(context.toString() + " must implement AppDialogEvents interface ");
        }

        mDialogEvents = (DialogEvents) context;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: entering....");
        super.onDetach();

        //reset the active callback interface , because we don't have an activity any lomger
        mDialogEvents = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: starts");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Bundle arguements = getArguments();
        final int dialogId ;
        String messageString;
        int positiveStringId;
        int negativeStringId;

        if(arguements != null){
            dialogId =  arguements.getInt(DIALOD_ID);
            messageString = arguements.getString(DIALOG_MESSAGE);

            if(dialogId == 0 || messageString == null){
                throw new IllegalArgumentException("DIALOG_ID and/or DIALOG_MESSAGE not present in the bundle");
            }
            positiveStringId = arguements.getInt(DIALOD_POSITIVE_RID);
            negativeStringId = arguements.getInt(DIALOG_NEGATIVE_RID);

            if(positiveStringId == 0){
                positiveStringId = R.string.ok;
            }
            if(negativeStringId == 0){
                negativeStringId = R.string.cancel ;
            }
        }else {
            throw new IllegalArgumentException("Must pass DIALOG_ID and DIALO_MESSAGE in the bundle");
        }
        builder.setMessage(messageString)
                .setPositiveButton(positiveStringId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //callback positiv result method
                        mDialogEvents.onPositiveDialogResult(dialogId,arguements);
                    }
                })
                .setNegativeButton(negativeStringId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //callback negative result method
                        mDialogEvents.onNegativeDialogResult(dialogId , arguements);
                    }
                });

        return super.onCreateDialog(savedInstanceState);
    }

    //when you press anything outside the dialog box
    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        Log.d(TAG, "onCancel: called");
        if(mDialogEvents != null){
            //int dialogId = getArguments().getInt()
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Log.d(TAG, "onDismiss: called");
        super.onDismiss(dialog);
    }
}
