package com.plexoc.helpma.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.plexoc.helpma.Fragment.ArticleFragment;
import com.plexoc.helpma.Fragment.ContactFragment;
import com.plexoc.helpma.Fragment.HomeFragment;
import com.plexoc.helpma.Fragment.ManageCardFragment;
import com.plexoc.helpma.Fragment.MedicalFragment;
import com.plexoc.helpma.Fragment.ProfileFragment;
import com.plexoc.helpma.Fragment.SearchFragment;
import com.plexoc.helpma.Fragment.TransactionFragment;
import com.plexoc.helpma.MainActivity;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Service.ApiClient;
import com.plexoc.helpma.Service.AppService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DrawerUtil {
    public static Context context;

    private static ApiClient apiClientlogout = AppService.createService(ApiClient.class);

    public static void getDrawer(final Activity activity, Toolbar toolbar, User user) {

        PrimaryDrawerItem item = new PrimaryDrawerItem().withIdentifier(0).withName("Home").withSelectable(false);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("My Profile").withSelectable(false);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(2).withName("Emergency Contact").withSelectable(false);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(3).withName("Medical Detail").withSelectable(false);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(4).withName("Articles").withSelectable(false);
        PrimaryDrawerItem item10 = new PrimaryDrawerItem().withIdentifier(5).withName("Search Medicine").withSelectable(false);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(6).withName("Manage Cards").withSelectable(false);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(7).withName("Transaction History").withSelectable(false);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(8).withName("Notifications").withSelectable(false);
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(9).withName("Data Analysis").withSelectable(false);
        PrimaryDrawerItem item11 = new PrimaryDrawerItem().withIdentifier(11).withName("Logout").withSelectable(false);


        try {
            //Log.e("ProfilePic",user.ProfileUrl);
            AccountHeader accountHeader = new AccountHeaderBuilder()
                    .withActivity(activity)
                    .withSelectionListEnabled(false)
                    .withCompactStyle(true)
                    .withHeaderBackground(R.drawable.logo)
                    .withTypeface(Typeface.DEFAULT)
                    .withTextColor(activity.getResources().getColor(R.color.md_white_1000))
                    .build();

            Drawer drawer = new DrawerBuilder()
                    .withActivity(activity)
                    .withToolbar(toolbar)
                    //.withStickyHeader(R.layout.material_drawer_header)
                    .withAccountHeader(accountHeader)
                    .addDrawerItems(
                            item,
                            item1,
                            item7,
                            item2,
                            item5,
                            item10,
                            new DividerDrawerItem(),
                            item3,
                            item6,
                            item4,
                            new DividerDrawerItem(),
                            item8,

                            item11
                    )
                    .withSelectedItem(-1)
                    .build();

            drawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {

                switch (String.valueOf(drawerItem.getIdentifier())) {

                    case "0":
                        replaceFragment(new HomeFragment(), null, activity);
                        drawer.closeDrawer();
                        break;
                    case "1":
                        replaceFragment(new ProfileFragment(), null, activity);
                        drawer.closeDrawer();
                        break;
                    case "2":
                        replaceFragment(new ContactFragment(), null, activity);
                        drawer.closeDrawer();
                        break;
                    case "3":
                        replaceFragment(new MedicalFragment(), null, activity);
                        drawer.closeDrawer();
                        break;
                    case "4":
                        replaceFragment(new ArticleFragment(), null, activity);
                        drawer.closeDrawer();
                        break;
                    case "5":
                        replaceFragment(new SearchFragment(), null, activity);
                        drawer.closeDrawer();
                        break;
                    case "6":
                        replaceFragment(new ManageCardFragment(), null, activity);
                        drawer.closeDrawer();
                        break;
                    case "7":
                        replaceFragment(new TransactionFragment(), null, activity);
                        drawer.closeDrawer();
                        break;
                    case "8":
                        //replaceFragment(new HomeFragment(), null, activity);
                        drawer.closeDrawer();
                        break;
                    case "9":
                        Toast.makeText(activity, activity.getResources().getString(R.string.data_analysis), Toast.LENGTH_LONG).show();
                        drawer.closeDrawer();
                        break;
                    case "10":
                        //replaceFragment(new HomeFragment(), null, activity);
                        drawer.closeDrawer();
                        break;
                    case "11":
                        PreferenceManager.getDefaultSharedPreferences(activity).edit().clear().apply();
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                        drawer.closeDrawer();
                        apiClientlogout.logout(user.Id).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(activity);
                                sqLiteDBHelper.DeleteNumbersPolice();
                                sqLiteDBHelper.DeleteNumbersAmbulance();
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
                        break;
                }
                return true;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void replaceFragment(Fragment fragment, String fragmentTag, Activity activity) {
        try {
            FragmentManager manager = ((FragmentActivity) activity).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(fragmentTag == null ? fragment.getClass().getName() : fragmentTag);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
