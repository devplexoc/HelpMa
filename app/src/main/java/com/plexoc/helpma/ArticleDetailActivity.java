package com.plexoc.helpma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

public class ArticleDetailActivity extends AppCompatActivity {

    private String Title, Desc;
    private AppCompatTextView textView_title,textView_desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Articel");
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        textView_title = findViewById(R.id.textview_ArticleTitle);
        textView_desc = findViewById(R.id.textview_articleDescription);

        try {
            Intent intent = getIntent();
            Title = intent.getStringExtra("title");
            Desc = intent.getStringExtra("desc");
        } catch (Exception e) {
            e.printStackTrace();
        }

        textView_title.setText(Html.fromHtml(Title));
        textView_desc.setText(Html.fromHtml(Desc));
    }
}
