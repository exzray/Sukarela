package com.developer.athirah.sukarela.models;

import android.util.Log;

import com.developer.athirah.sukarela.utilities.UserHelper;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelEvent {

    public enum Status {Open, Ongoing, Cancel, Complete}

    private String uid;
    private String title;
    private String image;
    private String location;
    private String description;

    private Date date;

    private GeoPoint point;

    private Status status;

    private List<String> people;
    private Map<String, List<String>> task;


    public ModelEvent() {

    }

    // exclude from firebase
    @Exclude
    public void setUid(String uid) {
        this.uid = uid;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    @Exclude
    public boolean isJoinEvent() {
        ModelUser user = UserHelper.getInstance().get();

        if (user != null) {

            return getPeople().contains(user.getUid());
        }

        return false;
    }

    public boolean isJoinTask(ModelTask task) {
        ModelUser user = UserHelper.getInstance().get();
        List<String> list = getTask().get(task.getUid());

        if (list == null) return false;

        return list.contains(user.getUid());
    }

    @Exclude
    public int getTotalJoin() {
        return getPeople().size();
    }

    @Exclude
    public int getTotalJoinTask(ModelTask task) {
        List<String> list = getTask().get(task.getUid());

        if (list != null) return list.size();

        return 0;
    }

    @Exclude
    public String maskStatus(){

        HashMap<Status, String> mask = new HashMap<>();
        mask.put(Status.Cancel, "Dibatalkan");
        mask.put(Status.Complete, "Selesai");
        mask.put(Status.Ongoing, "Akan Datang");
        mask.put(Status.Open, "Terbukan");

        return mask.get(status);
    }

    @Exclude
    public boolean isTaskFull(ModelTask task) {
        return getTotalJoinTask(task) == task.getLimit();
    }

    // getter and setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public Status getStatus() {
        // get date time
        Calendar now = Calendar.getInstance(); // current time

        Calendar event = Calendar.getInstance(); // event time
        event.setTime(date);
        event.add(Calendar.DAY_OF_MONTH, 1);

        Log.i("mymessage", "event: " + DateFormat.getDateInstance().format(event.getTime()));
        Log.i("mymessage", "now: " + DateFormat.getDateInstance().format(now.getTime()));


        if (now.after(event)) {
            status = Status.Complete;
        }

        Log.i("mymessage", "status: " + now.after(event));

        if (status == null) status = Status.Ongoing;

        return status;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    public List<String> getPeople() {

        if (people == null) people = new ArrayList<>();

        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

    public Map<String, List<String>> getTask() {

        if (task == null) task = new HashMap<>();

        return task;
    }

    public void setTask(Map<String, List<String>> task) {
        this.task = task;
    }
}
