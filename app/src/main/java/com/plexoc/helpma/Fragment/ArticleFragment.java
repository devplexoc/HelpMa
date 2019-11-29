package com.plexoc.helpma.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plexoc.helpma.Adpter.ArticleAdpter;
import com.plexoc.helpma.Adpter.ContactAdpter;
import com.plexoc.helpma.Model.Article;
import com.plexoc.helpma.Model.ListResponse;
import com.plexoc.helpma.R;
import com.plexoc.helpma.Utils.Constants;
import com.plexoc.helpma.Utils.DrawerUtil;
import com.plexoc.helpma.Utils.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setTitle("Articles");
        DrawerUtil.getDrawer(getActivity(), toolbar,user);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_Article);

        try {
            LoadingDialog.showLoadingDialog(getContext());
            getApiClient().getAricles(null, 0, 100000).enqueue(new Callback<ListResponse<Article>>() {
                @Override
                public void onResponse(Call<ListResponse<Article>> call, Response<ListResponse<Article>> response) {
                    if (response.isSuccessful()) {
                        if (!response.body().Values.isEmpty()) {
                            ArticleAdpter contactAdpter = new ArticleAdpter(getContext(), response.body().Values);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(contactAdpter);
                        } else
                            showMessage("No Articles Found");
                    } else
                        showMessage(Constants.DefaultErrorMsg);
                    LoadingDialog.cancelLoading();
                }

                @Override
                public void onFailure(Call<ListResponse<Article>> call, Throwable t) {
                    LoadingDialog.cancelLoading();
                    showMessage(Constants.DefaultErrorMsg);
                    Log.e("getArticleFail", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
