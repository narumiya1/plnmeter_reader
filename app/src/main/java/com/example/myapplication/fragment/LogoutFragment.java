package com.example.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.preference.SessionPrefference;

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
                sessionPrefference.setPhone("");
                sessionPrefference.setPassword("");
//                sessionPrefference.setUserId("");
                sessionPrefference.setIdPelanggan("");
                sessionPrefference.logoutUser();
                sessionPrefference.setIsLogin(false);
            }
        });

        return rootView;
    }
}
