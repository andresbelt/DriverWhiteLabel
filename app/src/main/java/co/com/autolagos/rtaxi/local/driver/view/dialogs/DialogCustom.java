package co.com.autolagos.rtaxi.local.driver.view.dialogs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import co.com.autolagos.rtaxi.local.driver.R;

public class DialogCustom {

    private ProgressDialog progressDialog;
    private Context context;

    public DialogCustom(Context context) {
        this.context = context;
    }

    public void dialogProgress(String message) {
        progressDialog = new ProgressDialog(context, R.style.AppThemeDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    //Funcion que permite cancelar una alerta asincronica
    public void cancelProgress() {
        progressDialog.cancel();
    }

    public void dialogQuestionGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("El GPS esta desactivado")
                .setMessage("¿Mostrar los ajustes de ubicación?")
                .setNegativeButton("No", setupButtonsDialogGps())
                .setPositiveButton("Si", setupButtonsDialogGps())
                .show();
    }

    private DialogInterface.OnClickListener setupButtonsDialogGps() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
    }

}

