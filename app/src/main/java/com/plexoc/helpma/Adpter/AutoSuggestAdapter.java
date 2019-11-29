package com.plexoc.helpma.Adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.plexoc.helpma.Model.OpenFda;
import com.plexoc.helpma.Model.Results;
import com.plexoc.helpma.R;

import java.util.ArrayList;
import java.util.List;


public class AutoSuggestAdapter extends ArrayAdapter<String> implements Filterable {

    private List<Results<OpenFda>> mlistData;
    private int itemLayout;
    private Context context;

    public AutoSuggestAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mlistData = new ArrayList<>();
        itemLayout = resource;
        this.context = context;
    }

    public void setData(List<Results<OpenFda>> list) {
        mlistData.clear();
        if (list != null)
            mlistData.addAll(list);
    }

    @Override
    public int getCount() {
        return mlistData.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        }
        try {
            AppCompatTextView textView = convertView.findViewById(R.id.textViewSearch);


            textView.setText(mlistData.get(position).openfda.brand_name[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        if (mlistData.get(position).openfda != null)
            return mlistData.get(position).openfda.brand_name[0];
        else
            return "No Results Found";
    }


    @NonNull
    @Override
    public Filter getFilter() {
        Filter dataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = mlistData;
                    filterResults.count = mlistData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return dataFilter;
    }
}
