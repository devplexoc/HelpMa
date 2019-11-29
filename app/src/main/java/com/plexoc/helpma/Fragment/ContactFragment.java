package com.plexoc.helpma.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.plexoc.helpma.Adpter.ContactAdpter;
import com.plexoc.helpma.Model.EmergancyContact;
import com.plexoc.helpma.Model.ListResponse;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.DrawerUtil;
import com.plexoc.helpma.Utils.LoadingDialog;
import com.plexoc.helpma.Utils.SQLiteDBHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private TextInputEditText editText_Name, editText_Mobile, editText_Email, editText_Relation, editText_police_number, editText_ambulance_number;
    private TextInputLayout inputLayout_Name, inputLayout_Mobile, inputLayout_Email, inputLayout_Relation;
    private AppCompatTextView textView_DialogHeader;
    private View alertDialogView;
    private AlertDialog dialog;
    private ContactAdpter contactAdpter;
    private MaterialButton button_AddPoliceNumber, button_AddAmbulanceNumber;
    private SQLiteDBHelper sqLiteDBHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setTitle("Emergency Contact");
        DrawerUtil.getDrawer(getActivity(), toolbar, user);

        recyclerView = view.findViewById(R.id.recyclerview_Contact);
        floatingActionButton = view.findViewById(R.id.fab);

        editText_police_number = view.findViewById(R.id.edittext_PoliceNumber);
        editText_ambulance_number = view.findViewById(R.id.edittext_AmbulanceNumber);
        button_AddAmbulanceNumber = view.findViewById(R.id.btn_Add_Ambulance);
        button_AddPoliceNumber = view.findViewById(R.id.btn_Add_Police);

        sqLiteDBHelper = new SQLiteDBHelper(getContext());

        inflater = this.getLayoutInflater();
        alertDialogView = inflater.inflate(R.layout.alert_select_contact, null);

        floatingActionButton.setOnClickListener(v -> OpenAddContactPopup());

        getEmergancyContact();

        //sqLiteDBHelper.DeleteNumbers();

        button_AddPoliceNumber.setOnClickListener(v -> {
            try {
                if (editText_police_number.getText().toString().trim().isEmpty()) {
                    showMessage("Please Enter Police Number");
                } else {
                    if (!editText_police_number.getText().toString().trim().equals(sqLiteDBHelper.getPoliceNumber())) {
                        if (sqLiteDBHelper.getPoliceNumber() == null) {
                            sqLiteDBHelper.insertPoliceNumber(editText_police_number.getText().toString().trim());
                            fillData();
                        } else {
                            sqLiteDBHelper.updatePoliceNumber(editText_police_number.getText().toString().trim());
                            fillData();
                            //showMessage("Please Edit Number");
                        }
                    }
                    editText_police_number.clearFocus();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        button_AddAmbulanceNumber.setOnClickListener(v -> {
            try {
                if (editText_ambulance_number.getText().toString().trim().isEmpty()) {
                    showMessage("Please Enter Ambulance Number");
                } else {
                    if (!editText_ambulance_number.getText().toString().trim().equals(sqLiteDBHelper.getAmbulanceNumber())) {
                        if (sqLiteDBHelper.getAmbulanceNumber() == null) {
                            sqLiteDBHelper.insertAmbulanceNumber(editText_ambulance_number.getText().toString().trim());
                            fillData();
                        } else {
                            sqLiteDBHelper.updateAmbulanceNumber(editText_ambulance_number.getText().toString().trim());
                            fillData();
                        }
                    }
                    editText_ambulance_number.clearFocus();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        fillData();
        return view;
    }

    private void fillData() {
        try {
            String PoliceNumber = sqLiteDBHelper.getPoliceNumber();
            String AmbulanceNumber = sqLiteDBHelper.getAmbulanceNumber();

            if (PoliceNumber != null)
                editText_police_number.setText(PoliceNumber);

            if (AmbulanceNumber != null)
                editText_ambulance_number.setText(AmbulanceNumber);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OpenAddContactPopup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setView(alertDialogView);
        alertDialog.setCancelable(false);
        dialog = alertDialog.create();
        dialog.show();

        editText_Name = alertDialogView.findViewById(R.id.edittext_ContactName);
        editText_Mobile = alertDialogView.findViewById(R.id.edittext_ContactPhome);
        editText_Email = alertDialogView.findViewById(R.id.edittext_ContactEmail);
        editText_Relation = alertDialogView.findViewById(R.id.edittext_ContactRelation);

        inputLayout_Name = alertDialogView.findViewById(R.id.textinput_Name);
        inputLayout_Email = alertDialogView.findViewById(R.id.textinput_Email);
        inputLayout_Mobile = alertDialogView.findViewById(R.id.textinput_Phone);
        inputLayout_Relation = alertDialogView.findViewById(R.id.textinput_Relation);

        textView_DialogHeader = alertDialogView.findViewById(R.id.textview_Header);
        textView_DialogHeader.setText("Add Emergency Contact");

        MaterialButton btn_Cancel = alertDialogView.findViewById(R.id.btnCancel);
        MaterialButton btn_AddContact = alertDialogView.findViewById(R.id.btnOk);

        btn_AddContact.setOnClickListener(v -> {
            try {
                if (doValidate()) {
                    dialog.dismiss();
                    callAddContactApi();
                    if (!editText_Name.getText().toString().isEmpty()) {
                        editText_Name.getText().clear();
                    }
                    if (!editText_Mobile.getText().toString().isEmpty()) {
                        editText_Mobile.getText().clear();
                    }
                    if (!editText_Relation.getText().toString().isEmpty()) {
                        editText_Relation.getText().clear();
                    }
                    if (!editText_Email.getText().toString().isEmpty()) {
                        editText_Relation.getText().clear();
                    }
                    ((ViewGroup) alertDialogView.getParent()).removeView(alertDialogView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btn_Cancel.setOnClickListener(v -> {
            dialog.dismiss();
            ((ViewGroup) alertDialogView.getParent()).removeView(alertDialogView);
        });

        editText_Name.setOnClickListener(v -> ReadContatct());
    }

    private void callAddContactApi() {
        try {
            LoadingDialog.showLoadingDialog(getContext());
            getApiClient().AddEmergancyContact(user.Id, editText_Name.getText().toString(), editText_Email.getText().toString(), editText_Mobile.getText().toString()
                    , editText_Relation.getText().toString()).enqueue(new Callback<com.plexoc.helpma.Model.Response<EmergancyContact>>() {
                @Override
                public void onResponse(Call<com.plexoc.helpma.Model.Response<EmergancyContact>> call, Response<com.plexoc.helpma.Model.Response<EmergancyContact>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().Item != null) {
                            Toast.makeText(getContext(), "Contact Added Successfully", Toast.LENGTH_SHORT).show();
                            getEmergancyContact();
                        } else
                            showMessage(response.body().Message);
                    } else
                        showMessage(Constants.DefaultErrorMsg);
                    LoadingDialog.cancelLoading();
                }

                @Override
                public void onFailure(Call<com.plexoc.helpma.Model.Response<EmergancyContact>> call, Throwable t) {
                    LoadingDialog.cancelLoading();
                    Log.e("AddEmergancyContactFail", t.getMessage());
                    showMessage(Constants.DefaultErrorMsg);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean doValidate() {

        if (editText_Name.getText().toString().isEmpty()) {
            inputLayout_Name.setError("Please Enter Name");
            editText_Name.requestFocus();
            return false;
        } else {
            editText_Name.clearFocus();
            inputLayout_Name.setError("");
        }

        if (editText_Mobile.getText().toString().isEmpty()) {
            inputLayout_Mobile.setError("Please Enter Mobile Number");
            editText_Mobile.requestFocus();
            return false;
        } else {
            editText_Mobile.clearFocus();
            inputLayout_Mobile.setError("");
        }

        if (editText_Mobile.getText().length() != 10) {
            inputLayout_Mobile.setError("Please Enter Valid Mobile Number");
            editText_Mobile.requestFocus();
            return false;
        } else {
            editText_Mobile.clearFocus();
            inputLayout_Mobile.setError("");
        }

        return true;
    }

    private void getEmergancyContact() {
        try {
            LoadingDialog.showLoadingDialog(getContext());
            getApiClient().getEmergancyContact(user.Id, null, 0, 100000).enqueue(new Callback<ListResponse<EmergancyContact>>() {
                @Override
                public void onResponse(Call<ListResponse<EmergancyContact>> call, Response<ListResponse<EmergancyContact>> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().Values.isEmpty()) {
                            contactAdpter = new ContactAdpter(getActivity(), response.body().Values, user);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(contactAdpter);
                        } else
                            showMessage("No Emergancy Contacts Found.Please add one!");
                    } else
                        showMessage(Constants.DefaultErrorMsg);
                    LoadingDialog.cancelLoading();
                }

                @Override
                public void onFailure(Call<ListResponse<EmergancyContact>> call, Throwable t) {
                    Log.e("getEmergancyContactFail", t.getMessage());
                    LoadingDialog.cancelLoading();
                    showMessage(Constants.DefaultErrorMsg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Click() {
        ReadContatct();
    }

    public void ReadContatct() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c = getActivity().managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getActivity().getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));
                            System.out.println("number is:" + cNumber);
                            editText_Mobile.setText(cNumber);
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        System.out.println("name is:" + name);
                        editText_Name.setText(name);


                    }
                }
                break;
        }
    }
}
