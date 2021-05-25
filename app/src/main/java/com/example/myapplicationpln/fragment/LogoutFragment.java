package com.example.myapplicationpln.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutFragment extends Fragment {
    Button logout ;
    SessionPrefference sessionPrefference ;
    public LogoutFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.logout_fragment, container, false);
        sessionPrefference = new SessionPrefference(getActivity());
        logout = rootView.findViewById(R.id.logout_user);
        Log.d("Body getUserId login", "login: "+  sessionPrefference.getUserId());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                sessionPrefference.setPhone("");
                sessionPrefference.setPassword("");
                sessionPrefference.setUserId("");
                sessionPrefference.setIdPelanggan("");
                sessionPrefference.logoutUser();
                sessionPrefference.setIsLogin(false);
            }
        });

        return rootView;
    }
}
