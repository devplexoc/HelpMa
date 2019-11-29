package com.plexoc.helpma.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.helpma.CompleteProfileActivity;
import com.plexoc.helpma.HomeActivity;
import com.plexoc.helpma.Model.Response;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.LoadingDialog;
import com.plexoc.helpma.Utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;


import static com.plexoc.helpma.MainActivity.Token;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends BaseFragment {

    private TextInputLayout inputLayout_firstname, inputLayout_lastname, inputLayout_email, inputLayout_password, inputLayout_mobile;
    private TextInputEditText editText_firstname, editText_lastname, editText_email, editText_password, editText_mobilr;
    private MaterialButton btn_Register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.registration_personal_detail_step, container, false);


        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Registration");
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());


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


        inputLayout_firstname = view.findViewById(R.id.textinput_FirstName);
        inputLayout_lastname = view.findViewById(R.id.textinput_LastName);
        inputLayout_email = view.findViewById(R.id.textinput_Email);
        inputLayout_password = view.findViewById(R.id.textinput_Password);
        inputLayout_mobile = view.findViewById(R.id.textinput_Mobile1);

        editText_email = view.findViewById(R.id.edittext_email);
        editText_lastname = view.findViewById(R.id.edittext_lastname);
        editText_firstname = view.findViewById(R.id.edittext_firstname);
        editText_password = view.findViewById(R.id.edittext_Password);
        editText_mobilr = view.findViewById(R.id.edittext_Mobile1);


        editText_firstname.setFilters(new InputFilter[]{inputFilter});
        editText_lastname.setFilters(new InputFilter[]{inputFilter});

        btn_Register = view.findViewById(R.id.btn_Register);
        btn_Register.setOnClickListener(v -> {
            if (doValidate()) {
                try {
                    closeKeybord();
                    LoadingDialog.showLoadingDialog(getContext());
                    getApiClient().SignUp(editText_firstname.getText().toString(), editText_lastname.getText().toString(), editText_email.getText().toString(), editText_password.getText().toString(),
                            editText_mobilr.getText().toString(), Token).enqueue(new Callback<Response<User>>() {
                        @Override
                        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().Item != null) {
                                    //Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                                    //user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);
                                    replaceFragment(new PlansFragment(response.body().Item),null);
                                    /*Intent intent = new Intent(getActivity(), CompleteProfileActivity.class);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();*/
                                } else {
                                    showMessage(response.body().Message);
                                }
                            } else {
                                showMessage(Constants.DefaultErrorMsg);
                            }
                            LoadingDialog.cancelLoading();
                        }

                        @Override
                        public void onFailure(Call<Response<User>> call, Throwable t) {
                            LoadingDialog.cancelLoading();
                            showMessage(Constants.DefaultErrorMsg);
                            Log.e("RegistrationFail", t.getMessage());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    private boolean doValidate() {

        if (editText_firstname.getText().toString().isEmpty()) {
            inputLayout_firstname.setError("Please Enter First Name");
            editText_firstname.requestFocus();
            return false;
        } else {
            inputLayout_firstname.setError("");
            editText_firstname.clearFocus();
        }

        if (editText_firstname.getText().toString().startsWith(" ")) {
            inputLayout_firstname.setError("First Name can not starts with whitespace");
            editText_firstname.requestFocus();
            return false;
        } else {
            inputLayout_firstname.setError("");
            editText_firstname.clearFocus();
        }

        if (editText_lastname.getText().toString().isEmpty()) {
            inputLayout_lastname.setError("Please Enter Last Name");
            editText_lastname.requestFocus();
            return false;
        } else {
            inputLayout_lastname.setError("");
            editText_lastname.clearFocus();
        }

        if (editText_lastname.getText().toString().startsWith(" ")) {
            inputLayout_lastname.setError("Last Name can not starts with whitespace");
            editText_lastname.requestFocus();
            return false;
        } else {
            inputLayout_lastname.setError("");
            editText_lastname.clearFocus();
        }

        if (editText_email.getText().toString().isEmpty()) {
            inputLayout_email.setError("Please Enter Email");
            editText_email.requestFocus();
            return false;
        } else {
            inputLayout_email.setError("");
            editText_email.clearFocus();
        }

        if (editText_email.getText().toString().startsWith(" ")) {
            inputLayout_email.setError("Email can not starts with whitespace");
            editText_email.requestFocus();
            return false;
        } else {
            inputLayout_email.setError("");
            editText_email.clearFocus();
        }

        if (!isValidEmail(editText_email.getText().toString())) {
            inputLayout_email.setError("Please Enter Valid Email address");
            editText_email.requestFocus();
            return false;
        } else {
            inputLayout_email.setError("");
            editText_email.clearFocus();
        }

        if (editText_password.getText().toString().isEmpty()) {
            inputLayout_password.setError("Please Enter Password");
            editText_password.requestFocus();
            return false;
        } else {
            inputLayout_password.setError("");
            editText_password.clearFocus();
        }

        if (editText_password.getText().toString().startsWith(" ")) {
            inputLayout_password.setError("Password can not starts with whitespace");
            editText_password.requestFocus();
            return false;
        } else {
            inputLayout_password.setError("");
            editText_password.clearFocus();
        }

        if (editText_mobilr.getText().toString().isEmpty()) {
            inputLayout_mobile.setError("Please Enter Mobile Number");
            editText_mobilr.requestFocus();
            return false;
        } else {
            inputLayout_mobile.setError("");
            editText_mobilr.clearFocus();
        }

        if (editText_mobilr.getText().toString().startsWith(" ")) {
            inputLayout_mobile.setError("Mobile Number can not starts with whitespace");
            editText_mobilr.requestFocus();
            return false;
        } else {
            inputLayout_mobile.setError("");
            editText_mobilr.clearFocus();
        }

        if (editText_mobilr.getText().length() != 10) {
            inputLayout_mobile.setError("Please Enter Valid Mobile Number");
            editText_mobilr.requestFocus();
            return false;
        } else {
            inputLayout_mobile.setError("");
            editText_mobilr.clearFocus();
        }

        return true;
    }


}
