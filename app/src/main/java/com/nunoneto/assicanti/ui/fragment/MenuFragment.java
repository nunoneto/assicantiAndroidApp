package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.WeekMenu;
import com.nunoneto.assicanti.webscraper.WebScrapper;

public class MenuFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private final static String TAG = "FRAG_MENUS";

    public MenuFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                WeekMenu menus = WebScrapper.getInstance().getMenus();

            }
        }).start();

        showLoading();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

    }

    private void showLoading(){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,LoadingFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

}