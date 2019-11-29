package com.plexoc.helpma.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.helpma.Model.Response;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.DrawerUtil;
import com.plexoc.helpma.Utils.LoadingDialog;
import com.plexoc.helpma.Utils.Prefs;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    private TextInputLayout inputLayout_firstname, inputLayout_lastname, inputLayout_email, inputLayout_mobile, inputLayout_DOB, inputLayout_addressline1, inputLayout_addressline2,
            inputLayout_city, inputLayout_state, inputLayout_country, inputLayout_Secondmobile, inputLayout_pass;
    private TextInputEditText editText_firstname, editText_lastname, editText_email, editText_mobile, editText_DOB, editText_addressline1, editText_addressline2, editText_city,
            editText_state, editText_country, editText_secondmobile, editText_pass;

    private View view;

    private MaterialButton btn_updateProfile;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setTitle("Profile");
        DrawerUtil.getDrawer(getActivity(), toolbar, user);

        bindView();

        btn_updateProfile.setOnClickListener(v -> {

            if (doValidate()) {
                try {
                    closeKeybord();
                    LoadingDialog.showLoadingDialog(getContext());
                    getApiClient().updateProfile(user.Id, editText_firstname.getText().toString(), editText_lastname.getText().toString(), editText_email.getText().toString(), editText_mobile.getText().toString(),
                            editText_secondmobile.getText().toString(), editText_pass.getText().toString(), editText_DOB.getText().toString() + " " + "00:00:00.000", editText_addressline1.getText().toString(), editText_addressline2.getText().toString(), editText_city.getText().toString(), editText_state.getText().toString(),
                            editText_country.getText().toString()).enqueue(new Callback<Response<User>>() {
                        @Override
                        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().Item != null) {
                                    Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                                    user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);
                                    Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();
                                } else
                                    showMessage(response.body().Message);
                            } else
                                showMessage(Constants.DefaultErrorMsg);
                            LoadingDialog.cancelLoading();
                        }

                        @Override
                        public void onFailure(Call<Response<User>> call, Throwable t) {
                            LoadingDialog.cancelLoading();
                            Log.e("UpdateProfileFail", t.getMessage());
                            showMessage(Constants.DefaultErrorMsg);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());

        try {
            editText_DOB.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                datePickerDialog = DatePickerDialog.newInstance(this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setMaxDate(Calendar.getInstance());
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private boolean doValidate() {

        try {
            if (editText_firstname.getText().toString().isEmpty()) {
                inputLayout_firstname.setError("Please Enter First Name");
                editText_firstname.requestFocus();
                return false;
            } else {
                editText_firstname.clearFocus();
                inputLayout_firstname.setError("");
            }

            if (editText_lastname.getText().toString().isEmpty()) {
                inputLayout_lastname.setError("Please Enter Last Name");
                editText_lastname.requestFocus();
                return false;
            } else {
                editText_lastname.clearFocus();
                inputLayout_lastname.setError("");
            }

            if (editText_email.getText().toString().isEmpty()) {
                inputLayout_email.setError("Please Enter Email");
                editText_email.requestFocus();
                return false;
            } else {
                editText_email.clearFocus();
                inputLayout_email.setError("");
            }

            if (!isValidEmail(editText_email.getText().toString())) {
                inputLayout_email.setError("Please Enter Valid Email");
                editText_email.requestFocus();
                return false;
            } else {
                editText_email.clearFocus();
                inputLayout_email.setError("");
            }

            if (editText_mobile.getText().toString().isEmpty()) {
                inputLayout_mobile.setError("Please Enter Mobile Number");
                editText_mobile.requestFocus();
                return false;
            } else {
                editText_mobile.clearFocus();
                inputLayout_mobile.setError("");
            }

            if (editText_mobile.getText().length() != 10) {
                inputLayout_mobile.setError("Please Enter Valid Mobile Number");
                editText_mobile.requestFocus();
                return false;
            } else {
                editText_mobile.clearFocus();
                inputLayout_mobile.setError("");
            }

            if (editText_DOB.getText().toString().isEmpty()) {
                inputLayout_DOB.setError("Please Enter Date Of Birth");
                editText_DOB.requestFocus();
                return false;
            } else {
                editText_DOB.clearFocus();
                inputLayout_DOB.setError("");
            }

            if (editText_addressline1.getText().toString().isEmpty()) {
                inputLayout_addressline1.setError("Please Enter Address");
                editText_addressline1.requestFocus();
                return false;
            } else {
                editText_addressline1.clearFocus();
                inputLayout_addressline1.setError("");
            }

            if (editText_city.getText().toString().isEmpty()) {
                inputLayout_city.setError("Please Enter City");
                editText_city.requestFocus();
                return false;
            } else {
                editText_city.clearFocus();
                inputLayout_city.setError("");
            }

            if (editText_state.getText().toString().isEmpty()) {
                inputLayout_state.setError("Please Enter State");
                editText_state.requestFocus();
                return false;
            } else {
                editText_state.clearFocus();
                inputLayout_state.setError("");
            }

            if (editText_country.getText().toString().isEmpty()) {
                inputLayout_country.setError("Please Enter Country");
                editText_country.requestFocus();
                return false;
            } else {
                editText_country.clearFocus();
                inputLayout_country.setError("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void bindView() {

        inputLayout_firstname = view.findViewById(R.id.textinput_FirstName);
        inputLayout_lastname = view.findViewById(R.id.textinput_LastName);
        inputLayout_email = view.findViewById(R.id.textinput_Email);
        inputLayout_mobile = view.findViewById(R.id.textinput_Mobile);
        inputLayout_DOB = view.findViewById(R.id.textinput_Dob);
        inputLayout_addressline1 = view.findViewById(R.id.textinput_addressline1);
        inputLayout_addressline2 = view.findViewById(R.id.textinput_addressline2);
        inputLayout_city = view.findViewById(R.id.textinput_city);
        inputLayout_state = view.findViewById(R.id.textinput_state);
        inputLayout_country = view.findViewById(R.id.textinput_country);
        inputLayout_Secondmobile = view.findViewById(R.id.textinput_SecondMobile);
        inputLayout_pass = view.findViewById(R.id.textinput_password);

        editText_addressline1 = view.findViewById(R.id.edittext_addressline1);
        editText_addressline2 = view.findViewById(R.id.edittext_addressline2);
        editText_firstname = view.findViewById(R.id.edittext_firstname);
        editText_lastname = view.findViewById(R.id.edittext_lastname);
        editText_city = view.findViewById(R.id.edittext_city);
        editText_state = view.findViewById(R.id.edittext_state);
        editText_country = view.findViewById(R.id.edittext_country);
        editText_email = view.findViewById(R.id.edittext_email);
        editText_mobile = view.findViewById(R.id.edittext_mobile);
        editText_DOB = view.findViewById(R.id.edittext_dob);
        editText_secondmobile = view.findViewById(R.id.edittext_Secondmobile);
        editText_pass = view.findViewById(R.id.edittext_password);

        btn_updateProfile = view.findViewById(R.id.btn_updateprofile);


        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence.equals("")) { // for backspace
                    return charSequence;
                }
                if (charSequence.toString().matches("[a-zA-Z ]+")) {
                    return charSequence;
                }
                return "";
            }
        };

        editText_firstname.setFilters(new InputFilter[]{inputFilter});
        editText_lastname.setFilters(new InputFilter[]{inputFilter});


        try {
            editText_firstname.setText(user.FirstName);
            editText_lastname.setText(user.LastName);
            editText_email.setText(user.Email);
            editText_mobile.setText(user.Phone);

            if (user.Dateofbirth != null) {
                String[] Dob = user.Dateofbirth.split("T");
                Log.e("dob1", Dob[0]);
                Log.e("dob2", Dob[1]);
                editText_DOB.setText(Dob[0]);
            }
            editText_addressline1.setText(user.AddressLine1);
            editText_addressline2.setText(user.AddressLine2);
            editText_city.setText(user.City);
            editText_state.setText(user.State);
            editText_country.setText(user.Country);
            editText_secondmobile.setText(user.Phone2);
            editText_pass.setText(user.Password);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        try {
            //String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            Date date1 = dateFormat.parse(date);
            //Log.e("Date",dateFormat.format(date1));
            editText_DOB.setText(dateFormat.format(date1));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
