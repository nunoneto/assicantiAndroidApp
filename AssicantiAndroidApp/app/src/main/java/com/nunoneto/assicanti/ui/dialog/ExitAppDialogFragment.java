package com.nunoneto.assicanti.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.nunoneto.assicanti.R;

/**
 * Created by NB20301 on 28/07/2016.
 */
public class ExitAppDialogFragment extends DialogFragment {

    public final static String TAG = "EXITAPP_DIAG";

    private YesNoDialogListener listener;

    public static ExitAppDialogFragment newInstance(YesNoDialogListener dialogListener){
        ExitAppDialogFragment frag = new ExitAppDialogFragment();
        frag.setListener(dialogListener);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.dialog_exitapp_title))
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.yes();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.no();
                    }
                })
                .create();

    }

    public void setListener(YesNoDialogListener listener) {
        this.listener = listener;
    }
}
