package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nunoneto.assicanti.R;

public class CustomerDataFragment extends Fragment {

    public final static String TAG = "CUSTOMERDATA_FRAGMENT";

    private OnFragmentInteractionListener mListener;

    // Views
    private EditText name,address,companyCode,comment,email,contact,nif;

    public CustomerDataFragment() {}

    public static CustomerDataFragment newInstance() {
        CustomerDataFragment fragment = new CustomerDataFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_customer_data, container, false);

        name = (EditText) view.findViewById(R.id.name);
        contact = (EditText) view.findViewById(R.id.contact);
        email = (EditText) view.findViewById(R.id.email);
        comment = (EditText) view.findViewById(R.id.comments);
        companyCode = (EditText) view.findViewById(R.id.companyCode);
        nif = (EditText) view.findViewById(R.id.nif);
        address = (EditText) view.findViewById(R.id.address);

        ((TextInputLayout)name.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_name)));
        ((TextInputLayout)address.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_address)));
        ((TextInputLayout)comment.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_comment)));
        ((TextInputLayout)companyCode.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_companyCode)));
        ((TextInputLayout)contact.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_contact)));
        ((TextInputLayout)email.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_mail)));
        ((TextInputLayout)nif.getParent()).setHint(getResources().getString(R.string.hint_nif));

        return view;
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
}
