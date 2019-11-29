package com.plexoc.helpma.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.plexoc.helpma.Adpter.PlanAdpter;
import com.plexoc.helpma.CompleteProfileActivity;
import com.plexoc.helpma.Model.ListResponse;
import com.plexoc.helpma.Model.Plans;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.Prefs;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlansFragment extends BaseFragment {


    private View dialogview;
    private CardInputWidget mCardInputWidget;
    private String Id, PlanName, PlanPrice;
    private User user;

    public PlansFragment(User user) {
        this.user = user;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plans, container, false);
        dialogview = inflater.inflate(R.layout.layout_payment_dialog, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Select Plan");
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(v -> getActivity());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_plan_list);


        getApiClient().getPlans().enqueue(new Callback<ListResponse<Plans>>() {
            @Override
            public void onResponse(Call<ListResponse<Plans>> call, Response<ListResponse<Plans>> response) {
                if (response.isSuccessful()) {
                    PlanAdpter planAdpter = new PlanAdpter(getContext(), response.body().Values, new PlanAdpter.CardCallBack() {
                        @Override
                        public void getPlan(String planID, String Name, String Price) {
                            Id = planID;
                            PlanName = Name;
                            PlanPrice = Price;
                            OpenPopUP();
                        }
                    });
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    recyclerView.setAdapter(planAdpter);
                }
            }

            @Override
            public void onFailure(Call<ListResponse<Plans>> call, Throwable t) {
            }
        });

        return view;
    }

    private void OpenPopUP() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(dialogview);
        bottomSheetDialog.create();
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((ViewGroup) dialogview.getParent()).removeView(dialogview);
            }
        });
        mCardInputWidget = dialogview.findViewById(R.id.cardInputWidget);
        Button button = dialogview.findViewById(R.id.payButton);
        AppCompatTextView textViewPlanName = dialogview.findViewById(R.id.textview_plan);
        AppCompatTextView textViewPrice = dialogview.findViewById(R.id.textview_plan_price);

        textViewPlanName.setText(PlanName);
        textViewPrice.setText("$" + PlanPrice);

        button.setOnClickListener(v -> {
            Card card = mCardInputWidget.getCard();
            if (card != null) {
                Stripe stripe = new Stripe(getContext(), "pk_test_gTxITeGHmLQLEXsFE5a7CQcs007Di3v1K1");
                stripe.createCardToken(card, new ApiResultCallback<Token>() {
                    @Override
                    public void onSuccess(Token token) {
                       // Log.i("TOKEN", token.getId());

                        getApiClient().MakePayment(user.Email, token.getId(), Id).enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                if (response.isSuccessful()) {

                                    Prefs.putString(Prefs.USER, new Gson().toJson(user));
                                    user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);
                                    Intent intent = new Intent(getActivity(), CompleteProfileActivity.class);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();

                                } else {
                                    showMessage(Constants.DefaultErrorMsg);
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                showMessage(Constants.DefaultErrorMsg);
                            }
                        });
                    }

                    @Override
                    public void onError(@NotNull Exception e) {
                        Log.e("TokenFail", e.getLocalizedMessage());
                    }
                });
            } else
                Toast.makeText(getContext(), "Please Enter Valid Card Detail", Toast.LENGTH_SHORT).show();
        });

    }

}
