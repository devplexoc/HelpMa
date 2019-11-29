package com.plexoc.helpma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.plexoc.helpma.Fragment.HomeFragment;
import com.plexoc.helpma.Fragment.PlansFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addFragment(new HomeFragment());
        //addFragment(new PlansFragment());
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {

        if (fragmentTag == null) {
            getSupportFragmentManager().popBackStack(fragment.getClass().getName(), 1);
        } else {
            getSupportFragmentManager().popBackStack(fragmentTag, 1);
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(fragmentTag == null ? fragment.getClass().getName() : fragmentTag);
        transaction.commitAllowingStateLoss();

    }

    public void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof HomeFragment) {
            finish();
        } else {
            //replaceFragment(new HomeFragment(), null);
            super.onBackPressed();
        }

    }
}
