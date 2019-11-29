package com.plexoc.helpma.Steps;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.plexoc.helpma.Model.MedicalDetail;
import com.plexoc.helpma.Model.Response;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Service.ApiClient;
import com.plexoc.helpma.Service.AppService;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tiper.MaterialSpinner;

import retrofit2.Call;
import retrofit2.Callback;

public class MedicalDetailStep extends Fragment implements Step {

    private User user;
    private TextInputEditText editText_Height, editText_Weight, editText_Alergy, editText_Medicine,
            editText_DoctorName, editText_DoctorEmail, editText_DoctorNumber, editText_DoctorAddress;

    private TextInputLayout inputLayout_Height, inputLayout_Weight, inputLayout_Alergy, inputLayout_Medicine,
            inputLayout_DoctorName, inputLayout_DoctorEmail, inputLayout_DoctorNumber, inputLayout_DoctorAddress;

    private AppCompatSpinner materialSpinner;
    private String[] BloodArray;
    private String BloodGroup;
    private ApiClient apiClient;
    public static MedicalDetail medicalDetailstart;

    public MedicalDetailStep(User user) {
        this.user = user;
        apiClient = AppService.createService(ApiClient.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.medical_detail_step, container, false);


        materialSpinner = view.findViewById(R.id.spinner);

        inputLayout_Height = view.findViewById(R.id.textinput_height);
        inputLayout_Weight = view.findViewById(R.id.textinput_weight);
        inputLayout_Alergy = view.findViewById(R.id.textinput_allergy);
        inputLayout_Medicine = view.findViewById(R.id.textinput_medicine);
        inputLayout_DoctorName = view.findViewById(R.id.textinput_DoctorName);
        inputLayout_DoctorEmail = view.findViewById(R.id.textinput_DoctorEmail);
        inputLayout_DoctorNumber = view.findViewById(R.id.textinput_DoctorContact);
        inputLayout_DoctorAddress = view.findViewById(R.id.textinput_DoctorAddress);


        editText_Height = view.findViewById(R.id.edittext_Height);
        editText_Weight = view.findViewById(R.id.edittext_weight);
        editText_Alergy = view.findViewById(R.id.edittext_Allergy);
        editText_Medicine = view.findViewById(R.id.edittext_medicine);
        editText_DoctorName = view.findViewById(R.id.edittext_doctorName);
        editText_DoctorEmail = view.findViewById(R.id.edittext_doctorEmail);
        editText_DoctorNumber = view.findViewById(R.id.edittext_doctorContact);
        editText_DoctorAddress = view.findViewById(R.id.edittext_doctorAddress);


        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence.equals("")) { // for backspace
                    return charSequence;
                }
                if (charSequence.toString().matches("[a-zA-Z,/ ]+")) {
                    return charSequence;
                }
                return "";
            }
        };

        editText_DoctorName.setFilters(new InputFilter[]{inputFilter});
        editText_Alergy.setFilters(new InputFilter[]{inputFilter});
        editText_Medicine.setFilters(new InputFilter[]{inputFilter});

        medicalDetailstart = new MedicalDetail();
        BloodArray = getActivity().getResources().getStringArray(R.array.blood_groups);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.blood_groups, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(arrayAdapter);


        materialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    BloodGroup = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (!doValidate()) {
            return new VerificationError("Please Enter Valid Detail");
        } else {
            CallApi();
            return null;
        }
    }

    private void CallApi() {
        try {
            apiClient.insertMedicalDetail(0, user.Id, "Female", BloodGroup, editText_Weight.getText().toString(), editText_Height.getText().toString(), editText_Alergy.getText().toString(),
                    editText_Medicine.getText().toString(), editText_DoctorName.getText().toString(), editText_DoctorNumber.getText().toString(), editText_DoctorEmail.getText().toString(),
                    editText_DoctorAddress.getText().toString(), null, null, null, null, null, null, null).enqueue(new Callback<Response<MedicalDetail>>() {
                @Override
                public void onResponse(Call<Response<MedicalDetail>> call, retrofit2.Response<Response<MedicalDetail>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().Item != null) {
                            medicalDetailstart = response.body().Item;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<MedicalDetail>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.makeText(getContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    private boolean doValidate() {


        if (!editText_DoctorNumber.getText().toString().isEmpty()) {

            if (editText_DoctorNumber.getText().length() != 10) {
                inputLayout_DoctorNumber.setError("Please Enter Valid Mobile Number");
                editText_DoctorNumber.requestFocus();
                return false;
            } else {
                editText_DoctorNumber.clearFocus();
                inputLayout_DoctorNumber.setError("");
            }
        } else {
            editText_DoctorNumber.clearFocus();
            inputLayout_DoctorNumber.setError("");
        }

        return true;
    }
}
