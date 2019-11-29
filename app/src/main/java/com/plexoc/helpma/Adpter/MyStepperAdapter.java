package com.plexoc.helpma.Adpter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.Steps.MedicalDetailStep;
import com.plexoc.helpma.Steps.MedicalQuestionsStep;
import com.plexoc.helpma.Steps.PersonalInfoStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class MyStepperAdapter extends AbstractFragmentStepAdapter {
    private User user;

    public MyStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context, User user) {
        super(fm, context);
        this.user = user;
    }

    @Override
    public Step createStep(int position) {
        if (position == 0) {
            PersonalInfoStep personalInfoStep = new PersonalInfoStep(user);
            return personalInfoStep;
        } else if (position == 1) {
            MedicalDetailStep medicalDetailStep = new MedicalDetailStep(user);
            return medicalDetailStep;
        } else {
            MedicalQuestionsStep medicalQuestionsStep = new MedicalQuestionsStep(context, user);
            return medicalQuestionsStep;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(int position) {
        if (position == 0) {
            return new StepViewModel.Builder(context).setTitle("Personal Info").create();
        } else if (position == 1) {
            return new StepViewModel.Builder(context).setTitle("Medical Detail").create();
        } else {
            return new StepViewModel.Builder(context).setTitle("Medical Questions").create();
        }
    }
}
