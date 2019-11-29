package com.plexoc.helpma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.plexoc.helpma.Adpter.TabAdpter;
import com.plexoc.helpma.Fragment.MedicineEffectFragment;
import com.plexoc.helpma.Fragment.MedicineUsedForFragment;
import com.plexoc.helpma.Model.OpenFda;
import com.plexoc.helpma.Model.Response;
import com.plexoc.helpma.Model.Results;
import com.plexoc.helpma.Model.Search;
import com.plexoc.helpma.Service.ApiClient;
import com.plexoc.helpma.Service.AppService;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.LoadingDialog;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MedicineDetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdpter tabAdpter;
    private ApiClient apiClient;
    private Results<OpenFda> resultsList;

    private AppCompatTextView textViewPurpose, textViewPurposeValue, textViewWhenUsing, textViewWhenUsingValue, textViewPregnancy, textViewPregnancyValue, textViewUsage, textViewUsageValue, textViewAskDoctor,
            textViewAskDoctorValue1, textViewAskDoctorValue2, textViewDirection, textViewDirectionValue, textViewReaciton, textViewReacitonValue, textViewStopUse, textViewStopUseValue, textViewDoNotUse,
            textViewDoNotUseValue, textViewOverdose, textViewOverdoseValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);
        apiClient = AppService.createService(ApiClient.class);

//        viewPager = findViewById(R.id.viewpager);
        //      tabLayout = findViewById(R.id.tablayout);

        bindView();

        try {
            if (getIntent().getStringExtra("purpose") != null)
                textViewPurposeValue.setText(getIntent().getStringExtra("purpose"));
            if (getIntent().getStringExtra("pregnancy") != null)
                textViewPregnancyValue.setText(getIntent().getStringExtra("pregnancy"));
            if (getIntent().getStringExtra("whenusing") != null)
                textViewWhenUsingValue.setText(getIntent().getStringExtra("whenusing"));
            if (getIntent().getStringExtra("usage") != null)
                textViewUsageValue.setText(getIntent().getStringExtra("usage"));
            if (getIntent().getStringExtra("donotuse") != null)
                textViewDoNotUseValue.setText(getIntent().getStringExtra("donotuse"));
            if (getIntent().getStringExtra("askdoctor1") != null)
                textViewAskDoctorValue1.setText(getIntent().getStringExtra("askdoctor1"));
            if (getIntent().getStringExtra("askdoctor2") != null)
                textViewAskDoctorValue2.setText(getIntent().getStringExtra("askdoctor2"));
            if (getIntent().getStringExtra("direction") != null)
                textViewDirectionValue.setText(getIntent().getStringExtra("direction"));
            if (getIntent().getStringExtra("stopuse") != null)
                textViewStopUseValue.setText(getIntent().getStringExtra("stopuse"));
            if (getIntent().getStringExtra("reaction") != null)
                textViewReacitonValue.setText(getIntent().getStringExtra("reaction"));
            if (getIntent().getStringExtra("overdose") != null)
                textViewOverdoseValue.setText(getIntent().getStringExtra("overdose"));


        } catch (Exception e) {
            e.printStackTrace();
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        if (getIntent().getStringExtra("name") != null)
            toolbar.setTitle(getIntent().getStringExtra("name"));
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        /*LoadingDialog.showLoadingDialog(this);
        apiClient.medicineDetail().enqueue(new Callback<Response<Search>>() {
            @Override
            public void onResponse(Call<Response<Search>> call, retrofit2.Response<Response<Search>> response) {
                if (response.isSuccessful()) {
                    tabAdpter = new TabAdpter(getSupportFragmentManager());
                    tabAdpter.addFragment(new MedicineUsedForFragment(response.body().Item.Description), "Used For");
                    tabAdpter.addFragment(new MedicineEffectFragment(response.body().Item.SideEffects), "Side Effect");
                    viewPager.setAdapter(tabAdpter);
                    tabLayout.setupWithViewPager(viewPager);
                } else {
                    Toast.makeText(MedicineDetailActivity.this, Constants.DefaultErrorMsg, Toast.LENGTH_SHORT).show();
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Response<Search>> call, Throwable t) {
                LoadingDialog.cancelLoading();
            }
        });*/

        /*tabAdpter = new TabAdpter(getSupportFragmentManager());
        tabAdpter.addFragment(new MedicineUsedForFragment(), "Used For");
        tabAdpter.addFragment(new MedicineEffectFragment(), "Side Effect");*/

    }

    private void bindView() {

        textViewAskDoctor = findViewById(R.id.textview_medicineAskDoctor);
        textViewAskDoctorValue1 = findViewById(R.id.textview_medicineAskDoctorValue1);
        textViewAskDoctorValue2 = findViewById(R.id.textview_medicineAskDoctorValue2);

        textViewPurpose = findViewById(R.id.textview_medicinePurpose);
        textViewPurposeValue = findViewById(R.id.textview_medicinePurposeValue);

        textViewPregnancy = findViewById(R.id.textview_medicinePregnancy);
        textViewPregnancyValue = findViewById(R.id.textview_medicinePregnancyValue);

        textViewWhenUsing = findViewById(R.id.textview_medicineWhenUsing);
        textViewWhenUsingValue = findViewById(R.id.textview_medicineWhenUsingValue);

        textViewUsage = findViewById(R.id.textview_medicineUsage);
        textViewUsageValue = findViewById(R.id.textview_medicineUsageValue);

        textViewDirection = findViewById(R.id.textview_medicineDirection);
        textViewDirectionValue = findViewById(R.id.textview_medicineDirectionValue);

        textViewReaciton = findViewById(R.id.textview_medicineReaction);
        textViewReacitonValue = findViewById(R.id.textview_medicineReactionValue);

        textViewDoNotUse = findViewById(R.id.textview_medicineDoNotUse);
        textViewDoNotUseValue = findViewById(R.id.textview_medicineDoNotUseValue);

        textViewStopUse = findViewById(R.id.textview_medicineStopUse);
        textViewStopUseValue = findViewById(R.id.textview_medicineStopUseValue);

        textViewOverdose = findViewById(R.id.textview_medicineOverDose);
        textViewOverdoseValue = findViewById(R.id.textview_medicineOverDoseValue);


    }
}
