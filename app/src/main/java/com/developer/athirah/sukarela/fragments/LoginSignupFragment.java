package com.developer.athirah.sukarela.fragments;


import android.content.Intent;
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

import com.developer.athirah.sukarela.MainActivity;
import com.developer.athirah.sukarela.R;
import com.developer.athirah.sukarela.models.ModelUser;
import com.developer.athirah.sukarela.utilities.UserHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginSignupFragment extends Fragment {

    // declare view
    private EditText name, contact;
    private Button submit;


    public LoginSignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init view
        name = view.findViewById(R.id.signup_field_name);
        contact = view.findViewById(R.id.signup_field_contact);
        submit = view.findViewById(R.id.signup_button_submit);

        initFragment();
    }

    private void initFragment() {
        // setup submit
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        // collect register data
        String data1 = name.getText().toString().trim();
        String data2 = contact.getText().toString().trim();

        // validate input field first
        if (data1.isEmpty() || data2.isEmpty()){

            Toast.makeText(getContext(), "Isi semua ruang!", Toast.LENGTH_SHORT).show();

            return;
        }

        // compile data into object
        ModelUser user = new ModelUser();
        user.setName(data1);
        user.setUid(data2);

        UserHelper.getInstance().register(user, new UserHelper.OnUserCompletedListener() {

            @Override
            public void completed(ModelUser user, String message) {

                if (user != null) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                    assert getActivity() != null;
                    getActivity().finish();
                }
            }
        });
    }
}
