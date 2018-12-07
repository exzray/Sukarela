package com.developer.athirah.sukarela;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.developer.athirah.sukarela.dialogs.TimeDialog;
import com.developer.athirah.sukarela.utilities.NotificationReceiver;

import java.text.DateFormat;
import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, android.app.TimePickerDialog.OnTimeSetListener {

    // declare view
    private TextView text;
    private Button set, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // init view
        text = findViewById(R.id.notification_text);
        set = findViewById(R.id.notification_set);
        cancel = findViewById(R.id.notification_cancel);

        initUI();
    }

    @Override
    public void onClick(View v) {

        if (v.equals(set)) {

            // button set
            TimeDialog dialog = new TimeDialog();
            dialog.show(getSupportFragmentManager(), "time picker");

        } else {

            // button cancel
            cancelAlarm();

        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        updateText(calendar);
        startAlarm(calendar);
    }

    private void initUI() {
        // setup button
        set.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void updateText(Calendar calendar) {
        String str_time = "Set time: ";
        str_time += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());

        text.setText(str_time);
    }

    private void startAlarm(Calendar calendar) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra(NotificationReceiver.CHANNEL, "sukarela"); // will be replace with event id
        intent.putExtra(NotificationReceiver.TITLE, "Peringatan aktiviti");
        intent.putExtra(NotificationReceiver.CONTENT, "Anda mempunyai aktiviti yang disertai pada hari ini!");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        manager.cancel(pendingIntent);
        text.setText("cancel");
    }
}
