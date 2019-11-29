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
public class MedicineUsedForFragment extends BaseFragment {
    private String Text;
    private AppCompatTextView textView;

    public MedicineUsedForFragment(String text) {
        Text = text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medicine_used_for, container, false);
        textView = view.findViewById(R.id.textviewUsedFor);
        textView.setText(Text);
        return view;
    }

}
