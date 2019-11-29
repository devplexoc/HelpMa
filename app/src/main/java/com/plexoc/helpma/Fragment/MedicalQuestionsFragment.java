package com.plexoc.helpma.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.plexoc.helpma.Model.MedicalDetail;
import com.plexoc.helpma.Model.Response;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class MedicalQuestionsFragment extends BaseFragment {

    private MedicalDetail medicalDetail;
    private TextInputEditText editText_PregenancyQuestion, editText_otherQuestion, editText_foodQuestion, editText_dietQuestion, editText_veganQuestion, editText_conditionQuestion;
    private RadioGroup radioGroup;
    private RadioButton rbYes, rbNo;
    private String Hospital;

    public MedicalQuestionsFragment(MedicalDetail medicalDetail) {
        this.medicalDetail = medicalDetail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medical_questions, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Medical Detail (Optional)");
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        editText_PregenancyQuestion = view.findViewById(R.id.edittext_preganancyQuestions);
        editText_otherQuestion = view.findViewById(R.id.edittext_OtherQuestions);
        editText_foodQuestion = view.findViewById(R.id.edittext_foodQuestions);
        editText_veganQuestion = view.findViewById(R.id.edittext_veganQuestions);
        editText_dietQuestion = view.findViewById(R.id.edittext_dietQuestions);
        editText_conditionQuestion = view.findViewById(R.id.edittext_hospitalConditionQuestions);

        radioGroup = view.findViewById(R.id.radiogroup_hospitalQuestions);
        rbYes = view.findViewById(R.id.radioYes);
        rbNo = view.findViewById(R.id.radioNo);

        MaterialButton btn_SaveMedicalQuestions = view.findViewById(R.id.btn_saveMedicalQuestions);
        btn_SaveMedicalQuestions.setOnClickListener(v -> callApi());

        try {
            editText_PregenancyQuestion.setText(medicalDetail.Pregnant);
            editText_otherQuestion.setText(medicalDetail.Health);
            editText_foodQuestion.setText(medicalDetail.FavoriteFood);
            editText_veganQuestion.setText(medicalDetail.Vegan);
            editText_dietQuestion.setText(medicalDetail.Diet);
            editText_conditionQuestion.setText(medicalDetail.Condition);
            if (medicalDetail.Hospital != null) {
                if (medicalDetail.Hospital.equals("Yes"))
                    rbYes.setChecked(true);
                else
                    rbNo.setChecked(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.radioYes == group.getCheckedRadioButtonId()){
                    Hospital = "Yes";
                } else
                    Hospital = "No";
            }
        });

        return view;
    }

    private void callApi() {
        try {
            LoadingDialog.showLoadingDialog(getContext());
            getApiClient().insertMedicalDetail(medicalDetail.Id, medicalDetail.UserId, medicalDetail.Gender, medicalDetail.BloodGroup, medicalDetail.Weight, medicalDetail.Height, medicalDetail.Allergy, medicalDetail.Medicines,
                    medicalDetail.DoctorName, medicalDetail.DoctorContact, medicalDetail.DoctorEmail, medicalDetail.DoctorAddress, editText_foodQuestion.getText().toString(), editText_veganQuestion.getText().toString(), editText_dietQuestion.getText().toString(),
                    Hospital, editText_conditionQuestion.getText().toString(), editText_PregenancyQuestion.getText().toString(), editText_otherQuestion.getText().toString()).enqueue(new Callback<Response<MedicalDetail>>() {
                @Override
                public void onResponse(Call<Response<MedicalDetail>> call, retrofit2.Response<Response<MedicalDetail>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().Item != null) {
                            Toast.makeText(getContext(), "Medical Details Updated", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        } else
                            showMessage(response.body().Message);
                    } else
                        showMessage(Constants.DefaultErrorMsg);
                    LoadingDialog.cancelLoading();
                }

                @Override
                public void onFailure(Call<Response<MedicalDetail>> call, Throwable t) {
                    Log.e("AddMedicalQuestionsFail", t.getMessage());
                    LoadingDialog.cancelLoading();
                    showMessage(Constants.DefaultErrorMsg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
