package com.moonpi.swiftnotes.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.moonpi.swiftnotes.MainActivity;
import com.moonpi.swiftnotes.R;

import org.json.JSONArray;

import static com.moonpi.swiftnotes.Utils.DataUtils.saveData;

/**
 * Created by joncasagrande on 9/18/16.
 */
public class DialogHelper {


    public static AlertDialog createRateAppDialog(final Context context, final String appPackageName){
       AlertDialog.Builder alert = new AlertDialog.Builder(context)
            .setTitle(R.string.dialog_rate_title)
            .setMessage(R.string.dialog_rate_message)
            .setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + appPackageName)));

                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id="
                                        + appPackageName)));
                    }
                }
            })
            .setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
       return alert.create();
    }

    public static AlertDialog showBackUpDialog(final Context context, final JSONArray notes){
        AlertDialog.Builder backupCheckDialog = new AlertDialog.Builder(context)
            .setTitle(R.string.action_backup)
            .setMessage(R.string.dialog_check_backup_if_sure)
            .setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // If note array not empty -> continue
                    if (notes.length() > 0) {
                        boolean backupSuccessful = saveData(MainActivity.getBackupPath(), notes);
                        if (backupSuccessful){
                            showOKDialog(context, MainActivity.getBackupPath().getAbsolutePath());
                            dialog.dismiss();
                        }else {
                            Toast.makeText(context,
                                    context.getResources().getString(R.string.toast_backup_failed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else { // If notes array is empty -> toast backup no notes found
                        Toast.makeText(context,
                                context.getResources().getString(R.string.toast_backup_no_notes),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            })
            .setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        return backupCheckDialog.create();
    }

    public static AlertDialog showOKDialog(Context context , final String filePath){
        // Dialog to display backup was successfully created in backupPath
        AlertDialog.Builder backupOKDialog = new AlertDialog.Builder(context)
            .setTitle(R.string.dialog_backup_created_title)
            .setMessage(context.getString(R.string.dialog_backup_created) + " "
                    + filePath)
            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        return backupOKDialog.create();
    }

    public static AlertDialog showFailDialog(Context context){
        // Dialog to display restore failed when no backup file found
        AlertDialog.Builder restoreFailedDialog = new AlertDialog.Builder(context)
            .setTitle(R.string.dialog_restore_failed_title)
            .setMessage(R.string.dialog_restore_failed)
            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        return restoreFailedDialog.create();
    }
}
