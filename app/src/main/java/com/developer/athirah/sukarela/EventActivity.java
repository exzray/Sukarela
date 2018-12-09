package com.developer.athirah.sukarela;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.developer.athirah.sukarela.adapters.FragmentEventAdapter;
import com.developer.athirah.sukarela.adapters.RecyclerEventAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventActivity extends AppCompatActivity {

    public static String uid;

    // declare component
    private FragmentEventAdapter adapter;

    // declare view
    private TabLayout tab;
    private ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // get pass uid of event
        uid = getIntent().getStringExtra(RecyclerEventAdapter.EXTRA_EVENT_UID);

        // init component
        adapter = new FragmentEventAdapter(getSupportFragmentManager(), uid);

        // init view
        tab = findViewById(R.id.event_tab);
        pager = findViewById(R.id.event_pager);

        initUI();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    private void initUI() {
        // setup pager
        pager.setAdapter(adapter);

        // setup tab
        tab.setupWithViewPager(pager);
    }
}
