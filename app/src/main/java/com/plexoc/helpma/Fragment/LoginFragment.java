package com.plexoc.helpma.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

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
import com.plexoc.helpma.Model.Error;
import com.plexoc.helpma.Model.Response;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.LoadingDialog;
import com.plexoc.helpma.Utils.Prefs;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

import static com.plexoc.helpma.MainActivity.Token;


public class LoginFragment extends BaseFragment {

    private AppCompatTextView textView;
    private MaterialButton btn_Login;
    private TextInputLayout inputLayout_Email, inputLayout_Password;
    private TextInputEditText editText_Email, editText_Password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        textView = view.findViewById(R.id.textview_SignUp);
        btn_Login = view.findViewById(R.id.btn_Login);

        inputLayout_Email = view.findViewById(R.id.textinput_Username);
        inputLayout_Password = view.findViewById(R.id.textinput_Password);
        editText_Email = view.findViewById(R.id.edittext_Username);
        editText_Password = view.findViewById(R.id.edittext_Password);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), CompleteProfileActivity.class);
                startActivity(intent);*/
                replaceFragment(new RegistrationFragment(), null);
            }
        });

        btn_Login.setOnClickListener(v -> {
            if (doValidate()) {
                try {
                    LoadingDialog.showLoadingDialog(getContext());
                    getApiClient().Login(editText_Email.getText().toString(), editText_Password.getText().toString(),Token).enqueue(new Callback<Response<User>>() {
                        @Override
                        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().Item != null) {
                                    Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                                    user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);
                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();

                                } else
                                    showMessage(Constants.DefaultErrorMsg);
                            }
                            if (response.code() == 404) {
                                try {
                                    Error error = new Gson().fromJson(response.errorBody().string(), Error.class);
                                    showMessage(error.Message);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (response.code() == 400) {
                                try {
                                    Error error = new Gson().fromJson(response.errorBody().string(), Error.class);
                                    showMessage(error.Message);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            LoadingDialog.cancelLoading();
                        }

                        @Override
                        public void onFailure(Call<Response<User>> call, Throwable t) {
                            Log.e("LoginFail", t.getMessage());
                            showMessage(Constants.DefaultErrorMsg);
                            LoadingDialog.cancelLoading();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //showMessage("Success");
            }
        });
        return view;
    }


    private boolean doValidate() {

        if (editText_Email.getText().toString().isEmpty()) {
            inputLayout_Email.setError("Please Enter Username");
            editText_Email.requestFocus();
            return false;
        } else {
            inputLayout_Email.setError("");
            editText_Email.clearFocus();
        }

        if (editText_Email.getText().toString().startsWith(" ")) {
            inputLayout_Email.setError("Username can not starts with whitespace");
            editText_Email.requestFocus();
            return false;
        } else {
            inputLayout_Email.setError("");
            editText_Email.clearFocus();
        }

        if (editText_Password.getText().toString().isEmpty()) {
            inputLayout_Password.setError("Please Enter Password");
            editText_Password.requestFocus();
            return false;
        } else {
            inputLayout_Password.setError("");
            editText_Password.clearFocus();
        }

        if (editText_Password.getText().toString().startsWith(" ")) {
            inputLayout_Password.setError("Password can not starts with whitespace");
            editText_Password.requestFocus();
            return false;
        } else {
            inputLayout_Password.setError("");
            editText_Password.clearFocus();
        }

        return true;
    }

}
