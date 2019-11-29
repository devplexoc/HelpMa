package com.plexoc.helpma.Adpter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plexoc.helpma.Model.Plans;
import com.plexoc.helpma.R;

import java.util.List;

public class PlanAdpter extends RecyclerView.Adapter<PlanAdpter.ViewHolder> {

    private int selectedPosition = -1;

    private Context context;
    private List<Plans> plans;
    private CardCallBack cardCallBack;


    public PlanAdpter(Context context, List<Plans> plans, CardCallBack cardCallBack) {
        this.context = context;
        this.plans = plans;
        this.cardCallBack = cardCallBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_plan_list_item, parent, false);
        return new PlanAdpter.ViewHolder(view);
    }

    public interface CardCallBack {
        void getPlan(String planID, String Name, String Price);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
        /*try {
            if (position == 0) {
                holder.textView_price.setText("$9.99");
                holder.textView_duration.setText("For 1 Month");
            }

            if (position == 1) {
                holder.textView_price.setText("$29.97");
                holder.textView_duration.setText("For 3 Months");
            }

            if (position == 2) {
                holder.textView_price.setText("$59.97");
                holder.textView_duration.setText("For 6 Months");
            }

            if (position == 3) {
                holder.textView_price.setText("$89.91");
                holder.textView_duration.setText("For 9 Month");
            }

            if (position == 4) {
                holder.textView_price.setText("$119.88");
                holder.textView_duration.setText("For 1 Year");
            }*/


            holder.textView_price.setText("$" + plans.get(position).PlanAmount);
            holder.textView_duration.setText("For " + plans.get(position).PlanName);


            if (selectedPosition == position)
                holder.cardView_plan.setCardBackgroundColor(Color.parseColor("#50D01E76"));
            else
                holder.cardView_plan.setCardBackgroundColor(Color.parseColor("#ffffff"));

            holder.cardView_plan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                    cardCallBack.getPlan(plans.get(position).PlanId, plans.get(position).PlanName,plans.get(position).PlanAmount);

                }
            });

        } catch (
                Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textView_price, textView_duration;
        ConstraintLayout constraintLayout_plan;
        MaterialCardView cardView_plan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_price = itemView.findViewById(R.id.textview_plan_price);
            textView_duration = itemView.findViewById(R.id.textview_plan_duration);
            constraintLayout_plan = itemView.findViewById(R.id.consraint_layout_plan);
            cardView_plan = itemView.findViewById(R.id.cardview_plan);

        }
    }
}
