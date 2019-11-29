package com.plexoc.helpma.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plexoc.helpma.Adpter.TransctionAdpter;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Utils.DrawerUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends BaseFragment {


    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Transction History");
        toolbar.setNavigationIcon(R.drawable.menu);
        DrawerUtil.getDrawer(getActivity(),toolbar,user);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_transction_list);
        TransctionAdpter transctionAdpter = new TransctionAdpter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(transctionAdpter);

        return view;
    }

}
