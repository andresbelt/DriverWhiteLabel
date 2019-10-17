package co.com.autolagos.rtaxi.local.driver.view.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import co.com.autolagos.rtaxi.local.driver.R;

public class DialogInterface {

    private ProgressDialog progressDialog;
    private Context context;

    public DialogInterface(Context context) {
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

}

