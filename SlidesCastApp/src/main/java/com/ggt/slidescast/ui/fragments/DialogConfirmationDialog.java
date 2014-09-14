package com.ggt.slidescast.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.ggt.slidescast.R;

/**
 * Confirmation dialog.
 *
 * @author guiguito
 */
public class DialogConfirmationDialog extends DialogFragment {

    public interface DialogConfirmationDialogListener {
        void onOkClicked();

        void onCancelClicked();
    }

    public static final String KEY_MESSAGE = "KEY_MESSAGE";
    public static final String KEY_TITLE = "KEY_TITLE";

    private Dialog mDialog;
    private String mMessage;
    private String mTitle;
    private DialogConfirmationDialogListener mDialogConfirmationDialogListener;

    public void setDialogConfirmationDialogListener(DialogConfirmationDialogListener dialogConfirmationDialogListener) {
        mDialogConfirmationDialogListener = dialogConfirmationDialogListener;
    }

    public DialogConfirmationDialog() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessage = getArguments().getString(KEY_MESSAGE);
        mTitle = getArguments().getString(KEY_TITLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mTitle).setMessage(mMessage).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mDialogConfirmationDialogListener != null) {
                    mDialogConfirmationDialogListener.onOkClicked();
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (mDialogConfirmationDialogListener != null) {
                    mDialogConfirmationDialogListener.onCancelClicked();
                }
            }
        });
        // Create the AlertDialog object and return it
        mDialog = builder.create();
        return mDialog;
    }
}