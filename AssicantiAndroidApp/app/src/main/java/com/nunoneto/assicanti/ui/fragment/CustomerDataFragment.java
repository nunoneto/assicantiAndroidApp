package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.entity.CustomerData;
import com.nunoneto.assicanti.ui.dialog.ExistingCustomerDataDialogFragment;
import com.nunoneto.assicanti.ui.dialog.YesNoDialogListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CustomerDataFragment extends Fragment {

    public final static String TAG = "CUSTOMERDATA_FRAGMENT";

    private OnOrderFragmentListener mListener;

    // Views
    private EditText name,address,companyCode,comment,email,contact,nif;
    private AppCompatButton confirmOrderButton;
    List<EditText> mandatoryFields;

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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_customer_data, container, false);

        name = (EditText) view.findViewById(R.id.name);
        contact = (EditText) view.findViewById(R.id.contact);
        email = (EditText) view.findViewById(R.id.email);
        comment = (EditText) view.findViewById(R.id.comments);
        companyCode = (EditText) view.findViewById(R.id.companyCode);
        nif = (EditText) view.findViewById(R.id.nif);
        address = (EditText) view.findViewById(R.id.address);
        confirmOrderButton = (AppCompatButton) view.findViewById(R.id.confirmOrder);

        mandatoryFields = new ArrayList<>();
        mandatoryFields.add(name);
        mandatoryFields.add(contact);
        mandatoryFields.add(email);
        mandatoryFields.add(companyCode);
        mandatoryFields.add(address);

        ((TextInputLayout)name.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_name)));
        ((TextInputLayout)address.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_address)));
        ((TextInputLayout)comment.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_comment)));
        ((TextInputLayout)companyCode.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_companyCode)));
        ((TextInputLayout)contact.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_contact)));
        ((TextInputLayout)email.getParent()).setHint(Html.fromHtml(getResources().getString(R.string.hint_mail)));
        ((TextInputLayout)nif.getParent()).setHint(getResources().getString(R.string.hint_nif));

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validForm = true;
                for(EditText editText : mandatoryFields){
                    if(editText.getText() == null || editText.getText().toString().isEmpty()){
                        validForm = false;
                        editText.setError(getResources().getString(R.string.order_form_error));
                    }
                }
                if(validForm){
                    saveCustomerData();
                    DataModel.getInstance().getCurrentOrder().setCustomerData(
                            new CustomerData(
                                name.getText().toString(),
                                contact.getText().toString(),
                                comment.getText() != null ? comment.getText().toString() : "",
                                nif.getText() != null ? nif.getText().toString() : "",
                                address.getText().toString(),
                                companyCode.getText().toString(),
                                email.getText().toString(),
                                new Date()
                            )
                    );
                    mListener.goToSummary();
                }

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final Realm realm = Realm.getDefaultInstance();
        final RealmResults<CustomerData> data = realm.where(CustomerData.class).findAllSorted("insertedAt", Sort.DESCENDING);

        if(data != null && data.size() > 0) {
            ExistingCustomerDataDialogFragment dialog = ExistingCustomerDataDialogFragment.newInstance(new YesNoDialogListener() {
                @Override
                public void yes() {
                    CustomerData customerData = data.first();
                    name.setText(customerData.getName());
                    comment.setText(customerData.getComment());
                    address.setText(customerData.getAddress());
                    companyCode.setText(customerData.getCompanyCode());
                    contact.setText(customerData.getContact());
                    email.setText(customerData.getEmail());
                    nif.setText(customerData.getNif());
                    realm.close();
                }
                @Override
                public void no() {
                }
            });
            dialog.show(getActivity().getFragmentManager(),ExistingCustomerDataDialogFragment.TAG);
        }
        realm.close();

    }

    private void saveCustomerData() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        try{
            CustomerData customerData = realm.createObject(CustomerData.class);
            customerData.setName(name.getText().toString());
            customerData.setAddress(address.getText().toString());
            customerData.setComment(comment.getText() != null ? comment.getText().toString() : "");
            customerData.setCompanyCode(companyCode.getText().toString());
            customerData.setContact(contact.getText().toString());
            customerData.setEmail(email.getText().toString());
            customerData.setNif(nif.getText() != null? nif.getText().toString() : "");
            customerData.setInsertedAt(new Date());

        }catch (Exception e){
            e.printStackTrace();
        }
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrderFragmentListener) {
            mListener = (OnOrderFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOrderFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
