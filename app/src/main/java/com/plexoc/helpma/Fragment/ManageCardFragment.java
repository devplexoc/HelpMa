package com.plexoc.helpma.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.plexoc.helpma.Adpter.CardAdpter;
import com.plexoc.helpma.CompleteProfileActivity;
import com.plexoc.helpma.Model.BaseCard;
import com.plexoc.helpma.Model.ListResponse;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.DrawerUtil;
import com.plexoc.helpma.Utils.LoadingDialog;
import com.plexoc.helpma.Utils.Prefs;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageCardFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private Button button;
    private View dialogview;
    private CardInputWidget mCardInputWidget;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_card, container, false);
        dialogview = inflater.inflate(R.layout.layout_payment_dialog, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Manage Cards");
        toolbar.setNavigationIcon(R.drawable.menu);
        DrawerUtil.getDrawer(getActivity(), toolbar, user);

        recyclerView = view.findViewById(R.id.recyclerview_Cards);
        button = view.findViewById(R.id.btn_add_card);

        getCardList();

        button.setOnClickListener(v -> {
            callAddCardAPi();
        });
        return view;
    }

    private void callAddCardAPi() {
        try {

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

            textViewPlanName.setVisibility(View.GONE);
            textViewPrice.setVisibility(View.GONE);
            button.setText("Add Card");


            button.setOnClickListener(v -> {
                Card card = mCardInputWidget.getCard();
                if (card != null) {
                    Stripe stripe = new Stripe(getContext(), "pk_test_gTxITeGHmLQLEXsFE5a7CQcs007Di3v1K1");
                    stripe.createCardToken(card, new ApiResultCallback<Token>() {
                        @Override
                        public void onSuccess(Token token) {
                            // Log.i("TOKEN", token.getId());
                            bottomSheetDialog.dismiss();
                            LoadingDialog.showLoadingDialog(getContext());
                            getApiClient().addCard(user.StripeId, token.getId()).enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    if (response.isSuccessful()) {
                                        getCardList();
                                    } else {
                                        showMessage(Constants.DefaultErrorMsg);
                                    }

                                    LoadingDialog.cancelLoading();
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    LoadingDialog.cancelLoading();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCardList() {
        try {
            LoadingDialog.showLoadingDialog(getContext());
            getApiClient().getCardList(user.StripeId).enqueue(new Callback<BaseCard>() {
                @Override
                public void onResponse(Call<BaseCard> call, Response<BaseCard> response) {
                    if (response.isSuccessful()) {
                        CardAdpter cardAdpter = new CardAdpter(getContext(), response.body().stripeCardLists, new CardAdpter.CallBack() {
                            @Override
                            public void getCardId(String cardID) {
                                makeDefault(cardID);
                            }
                        });
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(cardAdpter);
                    } else {
                        showMessage(Constants.DefaultErrorMsg);
                    }
                    LoadingDialog.cancelLoading();
                }

                @Override
                public void onFailure(Call<BaseCard> call, Throwable t) {
                    Log.e("getCardListFail", t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeDefault(String cardID) {
        try {
            LoadingDialog.showLoadingDialog(getContext());
            getApiClient().setDefaultCard(cardID,user.StripeId).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()){
                        getCardList();
                    }else
                        showMessage(Constants.DefaultErrorMsg);
                    LoadingDialog.cancelLoading();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    LoadingDialog.cancelLoading();
                    showMessage(Constants.DefaultErrorMsg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
