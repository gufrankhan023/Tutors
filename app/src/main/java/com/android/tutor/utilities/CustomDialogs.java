package com.android.tutor.utilities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutor.R;

/**
 * Created by Gufran on 10/20/2017.
 */

public class CustomDialogs {
    public static void getInternetAlertDialog(final Context context, String Message, String tag) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("");
        dialog.setMessage(Message);
        dialog.setCancelable(false);

        dialog.setNegativeButton("Ok",

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }


        );

        dialog.setPositiveButton("Try again",

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (CommonMethods.isNetworkAvailable(context)) ;
                    }
                }
        );

        Dialog dial = dialog.create();
        dial.show();

    }

    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        dialog.setContentView(R.layout.custom_progressbar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        return dialog;
    }

    public static void dismissDialog(ProgressDialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static void getAlertDialog(final Context context, final ICustomAlertDialogListener listener, String Message, final String tag) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.view_alert_dialog);
        TextView title = dialog.findViewById(R.id.titleTV);
        TextView messageTV = dialog.findViewById(R.id.messageTV);
        Button okBTN = dialog.findViewById(R.id.okBTN);
        Button cancelBTN = dialog.findViewById(R.id.cancelBTN);
        messageTV.setText(Message);
        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (listener != null)
                    listener.onAlertDialogYesClick(tag);
            }
        });
        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (listener != null)
                    listener.onAlertDialogNoClick(tag);
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    public static void getBottomSheetDialog(Context context) {
        final IOnBottomSheetClickListener listener = (IOnBottomSheetClickListener) context;
        final Dialog dialog = new Dialog(context, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.view_bottom_sheet);
        ImageView galleryIV = dialog.findViewById(R.id.galleryIV);
        ImageView cameraIV = dialog.findViewById(R.id.cameraIV);
        galleryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                listener.onOptionClick(view);
            }
        });
        cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                listener.onOptionClick(view);
            }
        });
        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    public interface IOnBottomSheetClickListener {
        void onOptionClick(View view);
    }

    public interface ICustomAlertDialogListener {
        void onAlertDialogYesClick(String tag);

        void onAlertDialogNoClick(String tag);
    }
}
