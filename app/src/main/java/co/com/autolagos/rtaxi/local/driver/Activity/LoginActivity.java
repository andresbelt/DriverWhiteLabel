package co.com.autolagos.rtaxi.local.driver.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import co.com.autolagos.rtaxi.local.driver.utils.Dlog;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.com.autolagos.rtaxi.local.driver.R;
import co.com.autolagos.rtaxi.local.driver.model.entities.BodyLogin;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.DriverPreferences;
import co.com.autolagos.rtaxi.local.driver.presenter.login.LoginPresenter;
import co.com.autolagos.rtaxi.local.driver.presenter.preferences.FirebasePresenter;
import co.com.autolagos.rtaxi.local.driver.presenter.preferences.LoginPresenterImpl;
import co.com.autolagos.rtaxi.local.driver.view.dialogs.DialogCustom;
import co.com.autolagos.rtaxi.local.driver.view.login.network.LoginInterface;
import co.com.autolagos.rtaxi.local.driver.view.login.preferences.PreferenceInterface;

public class LoginActivity extends AppCompatActivity implements LoginInterface, PreferenceInterface {

    public static final String TAG = LoginActivity.class.getSimpleName();
    private Context context;
    private DialogCustom dialogCustom;
    private LoginPresenter loginPresenter;
    private FirebasePresenter.LoginPreferencePresenter driverPresenter;

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
        dialogCustom = new DialogCustom(context);
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
        dialogCustom.dialogProgress("Iniciando sesión...");
        loginPresenter.login(bodyLogin);
    }

    @Override
    public void loginSuccess(Driver driver) {
        driverPresenter.storeDriver(driver);
    }

    @Override
    public void loginError(String error) {
        dialogCustom.cancelProgress();
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
        Dlog.e(this.getLocalClassName(), error);
    }

    @Override
    public void successStore(boolean success) {
        dialogCustom.cancelProgress();
        if(success) {
            startActivity(new Intent(context, HomeActivity.class));
            finish();
        } else {
            Snackbar.make(txtPlaca, "Error al guardar los datos del conductor.", Snackbar.LENGTH_LONG).show();
        }
    }


}
