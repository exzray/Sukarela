package com.developer.athirah.sukarela;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {

    // declare view
    ImageView imageView;
    Button button_call, button_whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // initialize view
        imageView = findViewById(R.id.contact_image);
        button_call = findViewById(R.id.contact_button_call);
        button_whatsapp = findViewById(R.id.contact_button_whatsapp);

        // setup imageView
        Glide
                .with(this)
                .load("https://mvm.org.my/wp-content/uploads/2016/01/logo-mvm.png")
                .into(imageView);

        // setup button contact
        button_call.setOnClickListener(this);
        button_whatsapp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.equals(button_call)) {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+60123456789"));
            startActivity(intent);

        } else if (v.equals(button_whatsapp)) {

            String url = "https://api.whatsapp.com/send?phone=+60123456789";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }
}
