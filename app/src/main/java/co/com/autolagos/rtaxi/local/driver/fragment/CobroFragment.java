package co.com.autolagos.rtaxi.local.driver.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.autolagos.rtaxi.local.driver.Activity.HomeActivity;
import co.com.autolagos.rtaxi.local.driver.R;
import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.career.CareerPreference;

@SuppressLint("ValidFragment")
public class CobroFragment extends Fragment {

    public static final String TAG = CobroFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.valor)
    TextView valor;

    @BindView(R.id.cobro)
    TextView cobro;

    @BindView(R.id.tiempo)
    TextView tiempo;

    @BindView(R.id.distancia)
    TextView distancia;

    @BindView(R.id.nombre_user)
    TextView nombre_user;

    @BindView(R.id.bnt_cerrar)
    LinearLayout bnt_cerrar;

    private OnFragmentInteractionListener mListener;


    public CobroFragment(OnFragmentInteractionListener mListener) {
        this.mListener=mListener;
    }


    public static CobroFragment newInstance(OnFragmentInteractionListener mListener,String param1, String param2) {
        CobroFragment fragment = new CobroFragment(mListener);
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cobro, container, false);
        ButterKnife.bind(this, v);


        Long tiempoTranscurrido = CareerPreference.getTimeRoute();

        long diffSeconds = tiempoTranscurrido / 100 %60;
        long diffMin = tiempoTranscurrido / (60*1000) % 60;
        long DiffHours = tiempoTranscurrido/ (60*60*1000) % 24;

        DecimalFormat df = new DecimalFormat("00");

        valor.setText(String.valueOf(CareerPreference.getTarifa()));
        tiempo.setText(String.format("tardo: %s:%s:%s",df.format(DiffHours),df.format(diffMin),df.format(diffSeconds)));
        distancia.setText(String.format("distancia: %s",df.format(CareerPreference.getDistance())));
        nombre_user.setText("");
        bnt_cerrar.setOnClickListener(v1 -> {
            onButtonPressed();
            getActivity().onBackPressed();
        });
        return v;
    }

    public void onButtonPressed() {
        if (mListener != null) {
           mListener.onFragmentInteraction();
      }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
