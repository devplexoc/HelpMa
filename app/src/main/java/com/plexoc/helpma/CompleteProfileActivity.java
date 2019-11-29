package com.plexoc.helpma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.Gson;
import com.plexoc.helpma.Adpter.MyStepperAdapter;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.Utils.Prefs;
import com.stepstone.stepper.StepperLayout;

public class CompleteProfileActivity extends AppCompatActivity {

    private StepperLayout mStepperLayout;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);
        mStepperLayout = findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new MyStepperAdapter(getSupportFragmentManager(), this,user));
    }
}
