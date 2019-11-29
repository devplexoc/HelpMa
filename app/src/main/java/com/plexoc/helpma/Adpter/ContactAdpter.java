package com.plexoc.helpma.Adpter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.plexoc.helpma.Fragment.ContactFragment;
import com.plexoc.helpma.Model.EmergancyContact;
import com.plexoc.helpma.Model.ListResponse;
import com.plexoc.helpma.Model.Response;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Service.ApiClient;
import com.plexoc.helpma.Service.AppService;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.LoadingDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ContactAdpter extends RecyclerView.Adapter<ContactAdpter.ViewHolder> {

    static final int PICK_CONTACT = 1;
    private Context context;
    private List<EmergancyContact> contacts;
    private AlertDialog dialog;
    private View alertDialogView;
    private TextInputEditText editText_Name, editText_Mobile, editText_Email, editText_Relation;
    private TextInputLayout inputLayout_Name, inputLayout_Mobile, inputLayout_Email, inputLayout_Relation;
    private ApiClient apiClient;
    private User user;

    public ContactAdpter(Context context, List<EmergancyContact> contacts, User user) {
        this.context = context;
        this.contacts = contacts;
        this.user = user;
        apiClient = AppService.createService(ApiClient.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_number_list, parent, false);
        alertDialogView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_select_contact, parent, false);
        return new ContactAdpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            holder.textView_contactName.setText(contacts.get(position).PersonName);
            holder.textView_contactNumber.setText(contacts.get(position).PersonMobile);

            holder.btn_EditContact.setOnClickListener(v -> {
                //          ContactFragment contactFragment = new ContactFragment();
//            contactFragment.ReadContatct();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setView(alertDialogView);
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

                MaterialButton btn_Cancel = alertDialogView.findViewById(R.id.btnCancel);
                MaterialButton btn_AddContact = alertDialogView.findViewById(R.id.btnOk);

                try {
                    editText_Name.setText(contacts.get(position).PersonName);
                    editText_Mobile.setText(contacts.get(position).PersonMobile);
                    editText_Email.setText(contacts.get(position).PersonEmail);
                    editText_Relation.setText(contacts.get(position).PersonRelation);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                btn_AddContact.setOnClickListener(v1 -> {

                    if (doValidate()) {
                        ((ViewGroup) alertDialogView.getParent()).removeView(alertDialogView);
                        dialog.dismiss();
                        try {
                            LoadingDialog.showLoadingDialog(context);
                            apiClient.EditEmergancyContact(contacts.get(position).Id, user.Id, editText_Name.getText().toString(), editText_Email.getText().toString(), editText_Mobile.getText().toString()
                                    , editText_Relation.getText().toString()).enqueue(new Callback<Response<EmergancyContact>>() {
                                @Override
                                public void onResponse(Call<Response<EmergancyContact>> call, retrofit2.Response<Response<EmergancyContact>> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().Item != null) {
                                            callgetContactAPI();

                                        } else
                                            Toast.makeText(context, response.body().Message, Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(context, Constants.DefaultErrorMsg, Toast.LENGTH_SHORT).show();
                                    LoadingDialog.cancelLoading();
                                }

                                @Override
                                public void onFailure(Call<Response<EmergancyContact>> call, Throwable t) {
                                    Log.e("EditContactFail", t.getMessage());
                                    Toast.makeText(context, Constants.DefaultErrorMsg, Toast.LENGTH_SHORT).show();
                                    LoadingDialog.cancelLoading();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                btn_Cancel.setOnClickListener(v1 -> {
                    dialog.dismiss();
                    ((ViewGroup) alertDialogView.getParent()).removeView(alertDialogView);
                });
            });

            holder.btn_DeleteContact.setOnClickListener(v -> {
                try {
                    LoadingDialog.showLoadingDialog(context);
                    apiClient.DeleteContact(contacts.get(position).Id).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            if (response.isSuccessful()) {

                                notifyItemRangeChanged(holder.getAdapterPosition(), contacts.size());
                                contacts.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());

                                if (contacts.size() == 0) {
                                    callgetContactAPI();
                                }
                            }
                            LoadingDialog.cancelLoading();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("DeleteContactFail",t.getMessage());
                            LoadingDialog.cancelLoading();
                            Toast.makeText(context, Constants.DefaultErrorMsg, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callgetContactAPI() {
        try {
            LoadingDialog.showLoadingDialog(context);
            apiClient.getEmergancyContact(user.Id, null, 0, 100000).enqueue(new Callback<ListResponse<EmergancyContact>>() {
                @Override
                public void onResponse(Call<ListResponse<EmergancyContact>> call, retrofit2.Response<ListResponse<EmergancyContact>> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().Values.isEmpty()) {
                            contacts = response.body().Values;
                            notifyDataSetChanged();
                        } else {
                            contacts.clear();
                            notifyDataSetChanged();
                            Toast.makeText(context, "No Contacts Found.Please add one", Toast.LENGTH_SHORT).show();
                        }
                    }
                    LoadingDialog.cancelLoading();
                }

                @Override
                public void onFailure(Call<ListResponse<EmergancyContact>> call, Throwable t) {
                    LoadingDialog.cancelLoading();
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

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialButton btn_EditContact, btn_DeleteContact;
        private AppCompatTextView textView_contactName, textView_contactNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_EditContact = itemView.findViewById(R.id.btn_EditContact);
            btn_DeleteContact = itemView.findViewById(R.id.btn_DeleteContact);
            textView_contactName = itemView.findViewById(R.id.textview_ContactName);
            textView_contactNumber = itemView.findViewById(R.id.textview_ContactNumber);
        }
    }
}
