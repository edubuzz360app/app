package com.sss.foody.box;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Nointernetfragment extends Fragment {

    boolean connected = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Appstatus appstatus = new Appstatus();

        View v = inflater.inflate(R.layout.fragment_nointernet, container, false);
        v.findViewById(R.id.tryagain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appstatus.isOnline(getActivity())){
                        connected = true;
/*                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else
                    {
                        showHome();
                    }
 */
                    getActivity().getFragmentManager().popBackStack();
                }
            }
        });
        return v;
    }
    private void showHome(){
        Fragment fragment = new Messagefragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }
}