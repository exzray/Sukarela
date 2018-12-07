package com.developer.athirah.sukarela.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.developer.athirah.sukarela.R;
import com.developer.athirah.sukarela.models.ModelUser;
import com.developer.athirah.sukarela.utilities.UserHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainProfileFragment extends Fragment implements View.OnClickListener {

    // declare component
    private ModelUser user = UserHelper.getInstance().get();

    // declare view
    private EditText name, age, contact;
    private Button update;

    // firebase
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    public MainProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init view
        name = view.findViewById(R.id.mainprofile_name);
        age = view.findViewById(R.id.mainprofile_age);
        contact = view.findViewById(R.id.mainprofile_contact);
        update = view.findViewById(R.id.mainprofile_update);

        initFragment();
        updateFragment(user);
    }

    @Override
    public void onClick(View v) {
        // for button update
        updateData(user);
    }

    private void initFragment() {
        // setup button
        update.setOnClickListener(this);
    }

    private void updateFragment(ModelUser user) {

        if (user != null) {
            String data1 = user.getName();
            String data2 = user.getAge() == null ? "None" : user.getAge() + "";
            String data3 = user.getUid();

            name.setText(data1);
            age.setText(data2);
            contact.setText(data3);
        }
    }

    private void updateData(final ModelUser user) {

        if (user != null) {
            String data1 = name.getText().toString().trim();
            String data2 = age.getText().toString();

            user.setName(data1);
            user.setAge(Integer.parseInt(data2));

            firestore.collection("users").document(user.getUid()).set(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                updateFragment(user);

                                Toast.makeText(getContext(), "Berjaya kemaskini!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
