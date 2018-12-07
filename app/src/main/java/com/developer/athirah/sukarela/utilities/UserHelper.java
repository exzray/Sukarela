package com.developer.athirah.sukarela.utilities;

import android.support.annotation.NonNull;

import com.developer.athirah.sukarela.models.ModelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserHelper {

    // declare static component
    private static ModelUser USER;
    private static UserHelper HELPER;

    // firebase
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    private UserHelper() {
        HELPER = this;
    }

    public static UserHelper getInstance() {
        if (HELPER == null) new UserHelper();

        return HELPER;
    }

    public void register(final ModelUser user, final OnUserCompletedListener listener) {
        // avoid mistake passing ull user
        if (user == null) {
            listener.completed(null, "Null user object!");

            return;
        }

        // user document
        final DocumentReference reference = firestore.collection("users").document(user.getUid());

        // check user already exist or not
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();

                if (task.isSuccessful() && snapshot != null) {

                    if (snapshot.exists()) {

                        // user already register
                        listener.completed(null, "Pengguna sudah wujud!");

                    } else {

                        // upload user data
                        reference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                // new register user
                                listener.completed(user, "Berjaya mendaftar.");
                                USER = user;
                            }
                        });
                    }
                }
            }
        });
    }

    public void login(String uid, final OnUserCompletedListener listener) {
        // avoid mistake passing null uid
        if (uid == null || uid.isEmpty()) {
            listener.completed(null, "Id pendaftaran tidak lengkap!");

            return;
        }
        ;

        // user document
        final DocumentReference reference = firestore.collection("users").document(uid);

        // find user data
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();

                if (task.isSuccessful() && snapshot != null) {

                    if (snapshot.exists()) {

                        // convert snapshot into user object
                        ModelUser user = snapshot.toObject(ModelUser.class);

                        if (user != null){
                            user.setUid(snapshot.getId());
                            USER = user;

                            listener.completed(user, "Selamat datang " + user.getName() + "!");
                        }

                    } else {

                        listener.completed(null, "Pengguna tidak wujud!");

                    }
                }
            }
        });
    }

    public void logout(OnUserCompletedListener listener) {

        listener.completed(USER, "Berjaya log keluar.");

        if (USER != null) USER = null; // reset user
    }

    public ModelUser get() {

        return USER;
    }

    public interface OnUserCompletedListener {

        void completed(ModelUser user, String message);
    }
}
