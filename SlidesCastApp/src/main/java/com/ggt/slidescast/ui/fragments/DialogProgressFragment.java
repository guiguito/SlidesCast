package com.ggt.slidescast.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;

import com.ggt.slidescast.R;

import org.androidannotations.annotations.EFragment;

/**
 * Progress dialog.
 *
 * @author guiguito
 */
@EFragment(R.layout.dialog_progress)
public class DialogProgressFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
