package com.plexoc.helpma.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.plexoc.helpma.Adpter.ContactListAdpter;
import com.plexoc.helpma.Model.EmergancyContact;
import com.plexoc.helpma.Model.ListResponse;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.DrawerUtil;
import com.plexoc.helpma.Utils.LoadingDialog;
import com.plexoc.helpma.Utils.SQLiteDBHelper;

import java.net.URI;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements LocationListener {

    private boolean isCallPolice = false;
    private MaterialCardView cardView_HelpMe;
    private LocationManager locationManagerCompat;
    private String LocationString;
    private boolean islocationEnabled, isNetwork, isGps;
    private GifImageView gifImageView;
    private View view1;
    private RecyclerView recyclerView;
    private SQLiteDBHelper sqLiteDBHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        view1 = inflater.inflate(R.layout.emergencyloader, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.menu);
        sqLiteDBHelper = new SQLiteDBHelper(getContext());

        DrawerUtil.getDrawer(getActivity(), toolbar, user);
        cardView_HelpMe = view.findViewById(R.id.cardview_Helpme);
        cardView_HelpMe.setOnClickListener(v -> SOS());

        MaterialButton materialButton = view.findViewById(R.id.btn_Search);
        materialButton.setOnClickListener(v -> {
            isCallPolice = false;
            CheckPermission();
        });

        MaterialButton btn_CallPolice = view.findViewById(R.id.btn_CallPolice);
        btn_CallPolice.setOnClickListener(v -> {
            isCallPolice = true;
            CheckPermission();
        });

        checkLocationPermission();
        return view;
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        locationManagerCompat = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManagerCompat != null) {
            locationManagerCompat.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
            locationManagerCompat.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

            isGps = locationManagerCompat.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetwork = locationManagerCompat.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
    }

    private void checkLocationPermission() {
        try {
            Dexter.withActivity(getActivity())
                    .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                getLocation();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                Toast.makeText(getActivity(), "Location Permission is Needed.Please Allow permission from settings", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SOS() {
        try {
            if (isGps && isNetwork) {
                LoadingDialog.showLoadingDialog(getContext());
                getApiClient().SOS(user.Id, LocationString).enqueue(new Callback<ListResponse<EmergancyContact>>() {
                    @Override
                    public void onResponse(Call<ListResponse<EmergancyContact>> call, Response<ListResponse<EmergancyContact>> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().Values.isEmpty()) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setView(view1);
                                /*builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        ((ViewGroup) view1.getParent()).removeView(view1);
                                    }
                                });*/
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                recyclerView = view1.findViewById(R.id.recyclerview_AlertContact);
                                ContactListAdpter contactListAdpter = new ContactListAdpter(getActivity(), response.body().Values);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(contactListAdpter);
                                gifImageView = view1.findViewById(R.id.gifimageview);
                                MaterialButton button = view1.findViewById(R.id.btnDismis);
                                button.setOnClickListener(v -> {
                                    alertDialog.dismiss();
                                    ((ViewGroup) view1.getParent()).removeView(view1);
                                });

                                showMessage("Alert Succesfully Sent");
                            }
                        } else
                            replaceFragment(new ContactFragment(), null);

                        LoadingDialog.cancelLoading();
                    }

                    @Override
                    public void onFailure(Call<ListResponse<EmergancyContact>> call, Throwable t) {
                        LoadingDialog.cancelLoading();
                        showMessage(Constants.DefaultErrorMsg);
                        Log.e("SOS-sendFail", t.getMessage());
                    }
                });
            } else
                showLocationSettingsDialog();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLocationSettingsDialog() {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Needs Permission");
            builder.setMessage("Location Permission is required to send sos.Please grant permission from setting.");
            builder.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    GotoLocationSettings();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GotoLocationSettings() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            /*Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent.setData(uri);*/
            startActivityForResult(intent, 102);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CheckPermission() {

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if (isCallPolice) {
                            if (sqLiteDBHelper.getPoliceNumber() != null) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sqLiteDBHelper.getPoliceNumber()));
                                getActivity().startActivity(intent);
                            } else
                                replaceFragment(new ContactFragment(), null);
                        } else {
                            if (sqLiteDBHelper.getAmbulanceNumber() != null) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sqLiteDBHelper.getAmbulanceNumber()));
                                getActivity().startActivity(intent);
                            } else
                                replaceFragment(new ContactFragment(), null);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Needs Permission");
            builder.setMessage("Call Permission is required to make call.Please grant permission from setting.");
            builder.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    GotoSettings();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GotoSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            LocationString = location.getLatitude() + "," + location.getLongitude();
            isCallPolice = true;
            Log.e("Location", location.getLatitude() + "," + location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        islocationEnabled = true;
        Log.e("ProviderEnable", provider);

    }

    @Override
    public void onProviderDisabled(String provider) {
        islocationEnabled = false;
        // Toast.makeText(getContext(), "Please Enable Location", Toast.LENGTH_SHORT).show();
        Log.e("ProviderDisable", provider);
    }
}
