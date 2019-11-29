package com.plexoc.helpma.Steps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.plexoc.helpma.HomeActivity;
import com.plexoc.helpma.Model.MedicalDetail;
import com.plexoc.helpma.Model.Response;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Service.ApiClient;
import com.plexoc.helpma.Service.AppService;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import retrofit2.Call;
import retrofit2.Callback;

import static com.plexoc.helpma.Steps.MedicalDetailStep.medicalDetailstart;

public class MedicalQuestionsStep extends Fragment implements Step {

    private User user;

    private TextInputEditText editText_PregenancyQuestion, editText_otherQuestion, editText_foodQuestion, editText_dietQuestion, editText_veganQuestion, editText_conditionQuestion;
    private RadioGroup radioGroup;
    private RadioButton rbYes, rbNo;
    private String Hospital;
    private ApiClient apiClient;
    private Context context;

    public MedicalQuestionsStep(Context context, User user) {
        this.user = user;
        this.context = context;
        apiClient = AppService.createService(ApiClient.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medical_questions_step, container, false);

        editText_PregenancyQuestion = view.findViewById(R.id.edittext_preganancyQuestions);
        editText_otherQuestion = view.findViewById(R.id.edittext_OtherQuestions);
        editText_foodQuestion = view.findViewById(R.id.edittext_foodQuestions);
        editText_veganQuestion = view.findViewById(R.id.edittext_veganQuestions);
        editText_dietQuestion = view.findViewById(R.id.edittext_dietQuestions);
        editText_conditionQuestion = view.findViewById(R.id.edittext_hospitalConditionQuestions);

        radioGroup = view.findViewById(R.id.radiogroup_hospitalQuestions);
        rbYes = view.findViewById(R.id.radioYes);
        rbNo = view.findViewById(R.id.radioNo);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.radioYes == group.getCheckedRadioButtonId()) {
                    Hospital = "Yes";
                } else
                    Hospital = "No";
            }
        });

        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        apiClient.insertMedicalDetail(medicalDetailstart.Id, medicalDetailstart.UserId, medicalDetailstart.Gender, medicalDetailstart.BloodGroup, medicalDetailstart.Weight, medicalDetailstart.Height,
                medicalDetailstart.Allergy, medicalDetailstart.Medicines, medicalDetailstart.DoctorName, medicalDetailstart.DoctorContact, medicalDetailstart.DoctorEmail, medicalDetailstart.DoctorAddress,
                editText_foodQuestion.getText().toString(), editText_veganQuestion.getText().toString(), editText_dietQuestion.getText().toString(),
                Hospital, editText_conditionQuestion.getText().toString(), editText_PregenancyQuestion.getText().toString(), editText_otherQuestion.getText().toString()).enqueue(new Callback<Response<MedicalDetail>>() {
            @Override
            public void onResponse(Call<Response<MedicalDetail>> call, retrofit2.Response<Response<MedicalDetail>> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(context, HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<Response<MedicalDetail>> call, Throwable t) {

            }
        });
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
