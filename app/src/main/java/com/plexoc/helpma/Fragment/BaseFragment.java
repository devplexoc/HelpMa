package com.plexoc.helpma.Fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.plexoc.helpma.HomeActivity;
import com.plexoc.helpma.MainActivity;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Service.ApiClient;
import com.plexoc.helpma.Service.AppService;
import com.plexoc.helpma.Utils.Prefs;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    private ApiClient apiClient;
    public User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiClient = AppService.createService(ApiClient.class);
        user = new User();
        if (!Prefs.getString(Prefs.USER).isEmpty())
            user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(fragment, fragmentTag);
        } else {
            ((HomeActivity) getActivity()).replaceFragment(fragment, fragmentTag);
        }
    }

    public void addFragment(Fragment fragment) {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).addFragment(fragment);
        } else {
            ((HomeActivity) getActivity()).addFragment(fragment);
        }
    }

    public void showMessage(String message) {
        try {

            if (getActivity() != null) {

                Snackbar mSnackBar = Snackbar.make(getView(), message, Snackbar.LENGTH_LONG);
                View view = mSnackBar.getView();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                params.gravity = Gravity.BOTTOM;
                view.setLayoutParams(params);
                view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                TextView mainTextView = (TextView) (view).findViewById(R.id.snackbar_text);
                mainTextView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
                mainTextView.setTextSize(12.0f);
                try {
                    Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.roboto_medium);
                    mainTextView.setTypeface(typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mainTextView.setTextColor(Color.WHITE);
                mSnackBar.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeKeybord() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isValidPhone(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches());
    }

}
