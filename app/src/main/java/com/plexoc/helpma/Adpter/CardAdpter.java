package com.plexoc.helpma.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.plexoc.helpma.Model.Card;
import com.plexoc.helpma.R;

import java.util.List;

public class CardAdpter extends RecyclerView.Adapter<CardAdpter.ViewHolder> {

    private Context context;
    private List<Card> cardList;
    private CallBack callBack;

    public CardAdpter(Context context, List<Card> cardList, CallBack callBack) {
        this.context = context;
        this.cardList = cardList;
        this.callBack = callBack;
    }


    public interface CallBack {
        void getCardId(String cardID);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_list, parent, false);
        return new CardAdpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if (cardList.get(position).DefaultCCId.toLowerCase().equals("default")) {
                holder.textView_defualt.setVisibility(View.VISIBLE);
                holder.textView_make_defualt.setVisibility(View.GONE);
            } else {
                holder.textView_defualt.setVisibility(View.GONE);
                holder.textView_make_defualt.setVisibility(View.VISIBLE);
            }
            holder.textView_cardNumber.setText("**** **** **** " + cardList.get(position).Last4);
            holder.textView_cardExpiry.setText("VALID THRU : " + cardList.get(position).ExpMonth + "/" + cardList.get(position).ExpYear);
            holder.textView_card_brand.setText(cardList.get(position).Brand);

            if (cardList.get(position).DefaultCCId != null)
                holder.textView_make_defualt.setOnClickListener(v -> callBack.getCardId(cardList.get(position).Id));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView textView_cardNumber, textView_cardExpiry, textView_defualt, textView_card_brand;
        private MaterialButton textView_make_defualt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_cardNumber = itemView.findViewById(R.id.textview_AccountNumber);
            textView_cardExpiry = itemView.findViewById(R.id.textview_CardHolderName);
            textView_card_brand = itemView.findViewById(R.id.textview_CardType);
            textView_defualt = itemView.findViewById(R.id.textview_Default);
            textView_make_defualt = itemView.findViewById(R.id.textview_MakeDefualt);
        }
    }

}
