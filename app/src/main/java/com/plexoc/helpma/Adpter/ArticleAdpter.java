package com.plexoc.helpma.Adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.plexoc.helpma.ArticleDetailActivity;
import com.plexoc.helpma.Model.Article;
import com.plexoc.helpma.R;

import java.util.List;

public class ArticleAdpter extends RecyclerView.Adapter<ArticleAdpter.ViewHolder> {

    private Context context;
    private List<Article> articles;

    public ArticleAdpter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artical_list, parent, false);
        return new ArticleAdpter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            holder.textView.setText(articles.get(position).Title);
            holder.materialCardView.setOnClickListener(v -> {
                //Toast.makeText(context, articles.get(position).Description, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                intent.putExtra("title",articles.get(position).Title);
                intent.putExtra("desc",articles.get(position).Description);
                context.startActivity(intent);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView textView;
        private MaterialCardView materialCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview_ArticleTitle);
            materialCardView = itemView.findViewById(R.id.cardview_article);
        }
    }
}
