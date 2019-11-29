package com.plexoc.helpma.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plexoc.helpma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MedicineEffectFragment extends BaseFragment {

    private String Text;
    private AppCompatTextView textView;

    public MedicineEffectFragment(String text) {
        Text = text;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medicine_effect, container, false);
        textView = view.findViewById(R.id.textvieweffect);
        textView.setText(Text);
        return view;
    }

}
