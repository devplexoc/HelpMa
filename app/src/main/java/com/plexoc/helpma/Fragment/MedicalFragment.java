package com.plexoc.helpma.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.plexoc.helpma.Model.ListResponse;
import com.plexoc.helpma.Model.MedicalDetail;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.DrawerUtil;
import com.plexoc.helpma.Utils.LoadingDialog;
import com.tiper.MaterialSpinner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MedicalFragment extends BaseFragment {

    //private MaterialSpinner materialSpinner;
    private AppCompatSpinner materialSpinner;
    private TextInputEditText editText_Height, editText_Weight, editText_Alergy, editText_Medicine,
            editText_DoctorName, editText_DoctorEmail, editText_DoctorNumber, editText_DoctorAddress;

    private TextInputLayout inputLayout_Height, inputLayout_Weight, inputLayout_Alergy, inputLayout_Medicine,
            inputLayout_DoctorName, inputLayout_DoctorEmail, inputLayout_DoctorNumber, inputLayout_DoctorAddress;

    private MaterialButton btn_UpdateMedicalDetail;
    private View view;
    private String BloodGroup = null;
    private MedicalDetail medicalDetail;
    private int id, Position;
    private String[] BloodArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_medical, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Medical Detail (Optional)");
        toolbar.setNavigationIcon(R.drawable.menu);

        DrawerUtil.getDrawer(getActivity(), toolbar, user);
        bindView();
        getMedicalRecord();

        medicalDetail = new MedicalDetail();
        BloodArray = getActivity().getResources().getStringArray(R.array.blood_groups);

        /*ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.blood_groups, android.R.layout.simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(arrayAdapter);

        materialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, View view, int i, long l) {
                BloodGroup = materialSpinner.getSelectedItem().toString();
                materialSpinner.clearFocus();

            }

            @Override
            public void onNothingSelected(MaterialSpinner materialSpinner) {

            }
        });*/

        materialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    BloodGroup = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_UpdateMedicalDetail.setOnClickListener(v -> {
            if (doValidate()) {
                try {
                    LoadingDialog.showLoadingDialog(getContext());
                    getApiClient().insertMedicalDetail(id, user.Id, "Female", BloodGroup, editText_Weight.getText().toString(), editText_Height.getText().toString(), editText_Alergy.getText().toString(),
                            editText_Medicine.getText().toString(), editText_DoctorName.getText().toString(), editText_DoctorNumber.getText().toString(), editText_DoctorEmail.getText().toString(),
                            editText_DoctorAddress.getText().toString(), medicalDetail.FavoriteFood, medicalDetail.Vegan, medicalDetail.Diet, medicalDetail.Hospital, medicalDetail.Condition, medicalDetail.Pregnant,
                            medicalDetail.Health).enqueue(new Callback<com.plexoc.helpma.Model.Response<MedicalDetail>>() {
                        @Override
                        public void onResponse(Call<com.plexoc.helpma.Model.Response<MedicalDetail>> call, Response<com.plexoc.helpma.Model.Response<MedicalDetail>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().Item != null) {
                                    medicalDetail = response.body().Item;
                                    //Toast.makeText(getContext(), "Medical Detail Saved Succesfully", Toast.LENGTH_SHORT).show();
                                    //getActivity().onBackPressed();
                                    replaceFragment(new MedicalQuestionsFragment(medicalDetail), null);
                                } else
                                    showMessage(response.body().Message);
                            } else
                                showMessage(Constants.DefaultErrorMsg);
                            LoadingDialog.cancelLoading();
                        }

                        @Override
                        public void onFailure(Call<com.plexoc.helpma.Model.Response<MedicalDetail>> call, Throwable t) {
                            Log.e("insertMedicalDetailFail", t.getMessage());
                            LoadingDialog.cancelLoading();
                            showMessage(Constants.DefaultErrorMsg);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private void getMedicalRecord() {
        try {
            getApiClient().getMedicalDetail(user.Id, 0, 10000).enqueue(new Callback<ListResponse<MedicalDetail>>() {
                @Override
                public void onResponse(Call<ListResponse<MedicalDetail>> call, Response<ListResponse<MedicalDetail>> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().Values.isEmpty()) {
                            editText_Alergy.setText(response.body().Values.get(0).Allergy);
                            editText_Height.setText(response.body().Values.get(0).Height);
                            editText_Weight.setText(response.body().Values.get(0).Weight);
                            editText_Medicine.setText(response.body().Values.get(0).Medicines);
                            editText_DoctorAddress.setText(response.body().Values.get(0).DoctorAddress);
                            editText_DoctorName.setText(response.body().Values.get(0).DoctorName);
                            editText_DoctorNumber.setText(response.body().Values.get(0).DoctorContact);
                            editText_DoctorEmail.setText(response.body().Values.get(0).DoctorEmail);
                            id = response.body().Values.get(0).Id;
                            medicalDetail = response.body().Values.get(0);
                            if (response.body().Values.get(0).BloodGroup != null) {
                                for (int i = 0; i < BloodArray.length; i++) {
                                    if (BloodArray[i].contains(response.body().Values.get(0).BloodGroup))
                                        Position = i;
                                }
                                materialSpinner.setSelection(Position);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListResponse<MedicalDetail>> call, Throwable t) {
                    Log.e("GetMedicalDetialFail", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindView() {

        btn_UpdateMedicalDetail = view.findViewById(R.id.btn_saveMedicalInfo);
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

    }

    private boolean doValidate() {

        if (!editText_DoctorEmail.getText().toString().isEmpty()) {

            if (!isValidEmail(editText_DoctorEmail.getText().toString())) {
                inputLayout_DoctorEmail.setError("Please Enter Valid Email address");
                editText_DoctorEmail.requestFocus();
                return false;
            } else {
                editText_DoctorEmail.clearFocus();
                inputLayout_DoctorEmail.setError("");
            }
        } else {
            inputLayout_DoctorEmail.setError("");
            editText_DoctorEmail.clearFocus();
        }

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
