package com.plexoc.helpma.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.button.MaterialButton;
import com.plexoc.helpma.Adpter.AutoSuggestAdapter;
import com.plexoc.helpma.Adpter.SearchHistoryAdpter;
import com.plexoc.helpma.MedicineDetailActivity;
import com.plexoc.helpma.Model.ListResponse;
import com.plexoc.helpma.Model.MedicineSearch;
import com.plexoc.helpma.Model.OpenFda;
import com.plexoc.helpma.Model.Results;
import com.plexoc.helpma.Model.Search;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Service.ApiClientMedicineApi;
import com.plexoc.helpma.Service.AppServiceMedicineApi;
import com.plexoc.helpma.Utils.DrawerUtil;
import com.plexoc.helpma.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends BaseFragment {

    private String[] strings = {"abc", "abcd", "test", "medicine", "name", "qwerty", "abcdefgh", "aaa"};
    private ArrayList<String> arrayList;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private AutoCompleteTextView textView;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private List<Results<OpenFda>> resultsList;
    private Results<OpenFda> openFdaResults;
    private String searchedText;
    private RecyclerView recyclerView;
    private static final String API_KEY = "FtUSzS8Ho2SxNfHQeRu7Ad5gGVsHqb67CjPJGIbQ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Search");
        toolbar.setNavigationIcon(R.drawable.menu);
        DrawerUtil.getDrawer(getActivity(), toolbar, user);
        arrayList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerview_SearchHistory);

        getSearchHisitory();


        textView = view.findViewById(R.id.searchView);
        autoSuggestAdapter = new AutoSuggestAdapter(getActivity(), R.layout.item_search_suggetion);
        textView.setThreshold(3);
        textView.setAdapter(autoSuggestAdapter);

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    searchedText = resultsList.get(position).openfda.brand_name[0];

                    Intent intent = new Intent(getActivity(), MedicineDetailActivity.class);
                    if (resultsList.get(position).purpose != null)
                        intent.putExtra("purpose", resultsList.get(position).purpose[0]);
                    if (resultsList.get(position).when_using != null)
                        intent.putExtra("whenusing", resultsList.get(position).when_using[0]);
                    if (resultsList.get(position).pregnancy_or_breast_feeding != null)
                        intent.putExtra("pregnancy", resultsList.get(position).pregnancy_or_breast_feeding[0]);
                    if (resultsList.get(position).indications_and_usage != null)
                        intent.putExtra("usage", resultsList.get(position).indications_and_usage[0]);
                    if (resultsList.get(position).ask_doctor != null)
                        intent.putExtra("askdoctor1", resultsList.get(position).ask_doctor[0]);
                    if (resultsList.get(position).ask_doctor_or_pharmacist != null)
                        intent.putExtra("askdoctor2", resultsList.get(position).ask_doctor_or_pharmacist[0]);
                    if (resultsList.get(position).dosage_and_administration != null)
                        intent.putExtra("direction", resultsList.get(position).dosage_and_administration[0]);
                    if (resultsList.get(position).adverse_reactions != null)
                        intent.putExtra("reaction", resultsList.get(position).adverse_reactions[0]);
                    if (resultsList.get(position).stop_use != null)
                        intent.putExtra("stopuse", resultsList.get(position).stop_use[0]);
                    if (resultsList.get(position).do_not_use != null)
                        intent.putExtra("donotuse", resultsList.get(position).do_not_use[0]);
                    if (resultsList.get(position).overdosage != null)
                        intent.putExtra("overdose", resultsList.get(position).overdosage[0]);
                    if (resultsList.get(position).openfda.brand_name != null)
                        intent.putExtra("name", resultsList.get(position).openfda.brand_name[0]);


                    try {
                        getApiClient().search(user.Id, searchedText).enqueue(new Callback<ListResponse<Search>>() {
                            @Override
                            public void onResponse(Call<ListResponse<Search>> call, Response<ListResponse<Search>> response) {
                                if (response.isSuccessful()) {
                                    startActivity(intent);
                                    textView.getText().clear();
                                }
                            }

                            @Override
                            public void onFailure(Call<ListResponse<Search>> call, Throwable t) {
                                Log.e("insertSerachFail", t.getMessage());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(textView.getText())) {
                        getSerchText(textView.getText().toString());
                    }
                }
                return false;
            }
        });

        MaterialButton button = view.findViewById(R.id.btn_Search);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MedicineDetailActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void getSearchHisitory() {
        try {
            getApiClient().getSearchHistory(user.Id, 0, 10).enqueue(new Callback<ListResponse<Search>>() {
                @Override
                public void onResponse(Call<ListResponse<Search>> call, Response<ListResponse<Search>> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().Values.isEmpty()) {
                            SearchHistoryAdpter historyAdpter = new SearchHistoryAdpter(getContext(), response.body().Values);
                            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                            recyclerView.setAdapter(historyAdpter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListResponse<Search>> call, Throwable t) {
                    Log.e("getSearchHistoryFail", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getSerchText(String Text) {
        try {
            ApiClientMedicineApi medicineApi = AppServiceMedicineApi.createService(ApiClientMedicineApi.class);
            medicineApi.SearchMedicine(API_KEY, "openfda.brand_name:" + Text, 8).enqueue(new Callback<MedicineSearch<Results<OpenFda>>>() {
                @Override
                public void onResponse(Call<MedicineSearch<Results<OpenFda>>> call, Response<MedicineSearch<Results<OpenFda>>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().error == null) {
                                resultsList = response.body().results;
                                autoSuggestAdapter.setData(resultsList);
                                autoSuggestAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<MedicineSearch<Results<OpenFda>>> call, Throwable t) {
                    Log.e("getMedicineFail", t.getMessage());

                }
            });

        } catch (
                Exception e) {
            e.printStackTrace();

        }
    }

}
