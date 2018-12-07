package com.developer.athirah.sukarela;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.developer.athirah.sukarela.adapters.FragmentMainAdapter;
import com.developer.athirah.sukarela.adapters.RecyclerEventAdapter;
import com.developer.athirah.sukarela.adapters.RecyclerJoinAdapter;
import com.developer.athirah.sukarela.models.ModelEvent;
import com.developer.athirah.sukarela.models.ModelUser;
import com.developer.athirah.sukarela.utilities.UserHelper;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener, ViewPager.OnPageChangeListener, EventListener<QuerySnapshot> {

    // declare component
    AHBottomNavigationAdapter navigationAdapter;
    FragmentMainAdapter fragmentAdapter;
    ListenerRegistration listener;

    // declare view
    private AHBottomNavigationViewPager viewPager;
    private AHBottomNavigation navigation;

    // firebase
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference colRef = firestore.collection("events");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init component
        fragmentAdapter = new FragmentMainAdapter(getSupportFragmentManager());
        navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.menu_bottom_navigation);

        // init view
        viewPager = findViewById(R.id.main_pager);
        navigation = findViewById(R.id.main_navigation);

        initUI();

        // start listener to events collection
        listener = colRef.addSnapshotListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (listener != null) listener.remove();

        listener = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (UserHelper.getInstance().get() == null){
            navigation.setVisibility(View.INVISIBLE);
            getMenuInflater().inflate(R.menu.menu_main_1, menu);
        } else {
            navigation.setVisibility(View.VISIBLE);
            getMenuInflater().inflate(R.menu.menu_main_2, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menumain_login:

                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();

                break;

            case R.id.menumain_logout:

                UserHelper.getInstance().logout(new UserHelper.OnUserCompletedListener() {

                    @Override
                    public void completed(ModelUser user, String message) {
                        recreate();
                    }
                });

                break;

            case R.id.menumain_notification:

                Intent notificationIntent = new Intent(this, NotificationActivity.class);
                startActivity(notificationIntent);
                finish();

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {

        viewPager.setCurrentItem(position);

        return true;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        navigation.setCurrentItem(i);
        RecyclerJoinAdapter.ADAPTER.filterOngoing();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void initUI(){
        // setup navigation
        navigation.setAccentColor(Color.parseColor("#009688"));
        navigation.setOnTabSelectedListener(this);
        navigationAdapter.setupWithBottomNavigation(navigation);

        // setup viewPager
        viewPager.setPagingEnabled(true);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

        if (queryDocumentSnapshots != null) {

            // this is important, clear previous data after new update
            RecyclerEventAdapter.LIST.clear();

            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                // convert snapshot into ModelEvent object
                ModelEvent event = snapshot.toObject(ModelEvent.class);

                if (event != null){
                    event.setUid(snapshot.getId());
                    RecyclerEventAdapter.LIST.add(event);
                }
            }

            RecyclerEventAdapter.ADAPTER.notifyDataSetChanged();
            RecyclerJoinAdapter.ADAPTER.update();
        }
    }
}
