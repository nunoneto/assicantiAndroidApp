package com.nunoneto.assicanti.ui.fragment;

import android.support.v4.app.Fragment;

import com.nunoneto.assicanti.R;

/**
 * Created by NB20301 on 14/07/2016.
 */
public class BaseFragment extends Fragment {

        public void toggleLoading(){

        Fragment loadingFrag = getActivity().getSupportFragmentManager().findFragmentByTag("LOADING");
        if(loadingFrag != null){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(loadingFrag)
                    .commit();
        }else{
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container,LoadingFragment.newInstance(),"LOADING")
                    .addToBackStack(null)
                    .commit();
        }
    }


}
