package com.developer.athirah.sukarela;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.developer.athirah.sukarela.adapters.FragmentLoginAdapter;

public class LoginActivity extends AppCompatActivity {

    // declare component
    private FragmentLoginAdapter adapter;

    // declare view
    private ImageView image;
    private TabLayout tab;
    private ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init component
        adapter = new FragmentLoginAdapter(getSupportFragmentManager());

        // init view
        image = findViewById(R.id.login_image);
        tab = findViewById(R.id.login_tab);
        pager = findViewById(R.id.login_pager);

        initUI();
    }

    private void initUI() {
        // setup image
        Glide
                .with(this)
                .load("https://images.unsplash.com/photo-1515601915049-08c8836c2204?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=133c7e7a614c3a168bbaa5f456b13cc9&auto=format&fit=crop&w=1350&q=80")
                .into(image);

        // setup pager
        pager.setAdapter(adapter);

        // setup tab
        tab.setupWithViewPager(pager);
    }
}
