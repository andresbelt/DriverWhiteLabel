package co.com.autolagos.rtaxi.local.driver.view.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.com.autolagos.rtaxi.local.driver.R;
import co.com.autolagos.rtaxi.local.driver.model.entities.BodyLogin;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.presenter.login.LoginPresenter;
import co.com.autolagos.rtaxi.local.driver.presenter.preferences.DriverPresenter;
import co.com.autolagos.rtaxi.local.driver.presenter.preferences.LoginPresenterImpl;
import co.com.autolagos.rtaxi.local.driver.view.dialogs.DialogInterface;
import co.com.autolagos.rtaxi.local.driver.view.login.network.LoginInterface;
import co.com.autolagos.rtaxi.local.driver.view.login.preferences.PreferenceInterface;
import co.com.autolagos.rtaxi.local.driver.view.map.DriverMapActivity;

public class LoginActivity extends AppCompatActivity implements LoginInterface, PreferenceInterface {

    private Context context;
    private DialogInterface dialogInterface;
    private LoginPresenter loginPresenter;
    private DriverPresenter.LoginPreferencePresenter driverPresenter;

    //@BindView(R.id.img_background)
    //ImageView imgBackground;
    @BindView(R.id.txt_placa)
    EditText txtPlaca;
    @BindView(R.id.txt_cedula)
    EditText txtCedula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupActivity();
    }

    private void setupActivity() {
        context = this;
        dialogInterface = new DialogInterface(context);
        loginPresenter = new co.com.autolagos.rtaxi.local.driver.presenter.login.LoginPresenterImpl(this);
        driverPresenter = new LoginPresenterImpl(context, this);
        loadBackground();
    }

    private void loadBackground() {
        //Glide.with(context)
        //        .load(Uri.parse("file:///android_asset/gif_background.gif"))
        //        .into(imgBackground);
    }

    @OnClick(R.id.but_login)
    public void clickLogin() {
        String placa = txtPlaca.getText().toString().trim();
        String cedula = txtCedula.getText().toString().trim();
        if(placa.equalsIgnoreCase("") || cedula.equalsIgnoreCase("")) {
            Snackbar.make(txtPlaca, "Antes de continuar, debe agregar su placa y cedula.", Snackbar.LENGTH_LONG).show();
        } else {
            login(placa, cedula);
        }
    }

    private void login(String placa, String cedula) {
        //String placa = "WNS109";
        //String cedula = "80053743";
        BodyLogin bodyLogin = new BodyLogin(placa, cedula, true);
        dialogInterface.dialogProgress("Iniciando sesión...");
        loginPresenter.login(bodyLogin);
    }

    @Override
    public void loginSuccess(Driver driver) {
        driverPresenter.storeDriver(driver);
    }

    @Override
    public void loginError(String error) {
        dialogInterface.cancelProgress();
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
        Log.e(this.getLocalClassName(), error);
    }

    @Override
    public void successStore(boolean success) {
        dialogInterface.cancelProgress();
        if(success) {
            startActivity(new Intent(context, DriverMapActivity.class));
        } else {
            Snackbar.make(txtPlaca, "Error al guardar los datos del conductor.", Snackbar.LENGTH_LONG).show();
        }
    }
}
