package co.com.autolagos.rtaxi.local.driver.view.map.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import co.com.autolagos.rtaxi.local.driver.R;
import co.com.autolagos.rtaxi.local.driver.model.entities.Petition;

public class SheetPetition extends BottomSheetDialogFragment {

    private static Petition petition;
    private static Context context;

    public static SheetPetition newInstance(Context context_instance, Petition petition_instance) {
        context = context_instance;
        petition = petition_instance;
        return new SheetPetition();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_petition, container, false);
        ButterKnife.bind(this, view);
        return view;
    }



}
