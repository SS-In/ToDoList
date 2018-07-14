package com.ssin.todolist.ui.ediddialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.ssin.todolist.R;

/**
 * Created by SS-In on 2018-07-12.
 */

public class EditTextDialog {
    private String okStr;
    private String cancelStr;

    private EditText editText;
    private Context context;
    private OnTextSetListener listener;
    private AlertDialog alertDialog;

    public EditTextDialog(Context context) {
        this.context = context;
        okStr = context.getString(R.string.ok);
        cancelStr = context.getString(R.string.cancel);
        editText = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(editText);

        builder.setPositiveButton(okStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null)
                    listener.onTextSet(editText.getText().toString());
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(cancelStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog = builder.create();
    }

    public EditTextDialog(Context context, OnTextSetListener listener) {
        this(context);
        this.listener = listener;
    }


    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public void setOnTextSetListener(OnTextSetListener listener) {
        this.listener = listener;
    }

    public interface OnTextSetListener {
        void onTextSet(String text);
    }
}
