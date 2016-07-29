package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.entity.realm.CustomerData;
import com.nunoneto.assicanti.model.entity.SendOrderCodes;
import com.nunoneto.assicanti.model.entity.SendOrderResult;
import com.nunoneto.assicanti.network.RequestConstants;
import com.nunoneto.assicanti.network.RestService;
import com.nunoneto.assicanti.tasks.GetSendOrderTask;
import com.nunoneto.assicanti.tasks.SendOrderTask;
import com.nunoneto.assicanti.ui.dialog.ExistingCustomerDataDialogFragment;
import com.nunoneto.assicanti.ui.dialog.YesNoDialogListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerDataFragment extends Fragment {

    public final static String TAG = "CUSTOMERDATA_FRAGMENT";

    private OnOrderFragmentListener mListener;

    // Tasks
    private GetSendOrderTask getSendOrderTask;
    private SendOrderTask sendOrderTask;

    // Views
    private EditText name,address,companyCode,comment,email,contact,nif;
    private AppCompatButton confirmOrderButton;
    List<EditText> mandatoryFields;
    private ContentLoadingProgressBar contentLoadingProgressBar;

    private SendOrderCodes sendOrderCodes;

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
        contentLoadingProgressBar = (ContentLoadingProgressBar) view.findViewById(R.id.loading);
        contentLoadingProgressBar.hide();

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
                confirmOrderButton.setEnabled(false);
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
                    checkIfOpen();
                }else
                    confirmOrderButton.setEnabled(true);

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getSendOrderTask = new GetSendOrderTask(this);
        getSendOrderTask.execute();
    }

    public void setSendOrderCodes(SendOrderCodes codes){
        this.sendOrderCodes = codes;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(getSendOrderTask != null && (getSendOrderTask.getStatus() == AsyncTask.Status.PENDING || getSendOrderTask.getStatus() == AsyncTask.Status.RUNNING))
            getSendOrderTask.cancel(true);
        if(sendOrderTask != null && (sendOrderTask.getStatus() == AsyncTask.Status.PENDING || sendOrderTask.getStatus() == AsyncTask.Status.RUNNING))
            sendOrderTask.cancel(true);
    }

    private void checkIfOpen(){
        contentLoadingProgressBar.show();

        RestService.getInstance().getAssicantiService()
                .checkifopen(
                        RequestConstants.CheckIfOpen.ACTION,
                        RequestConstants.CheckIfOpen.TYPE
                ).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                submitOrder();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Snackbar.make(getView(),"Não foi possível submeter a encomenda",Snackbar.LENGTH_LONG).show();
                Log.e(TAG,"Failed to checkIfOpen");
                t.printStackTrace();
                contentLoadingProgressBar.hide();
                confirmOrderButton.setEnabled(true);
            }
        });

    }

    private void submitOrder() {
        sendOrderTask = new SendOrderTask(CustomerDataFragment.this);
        sendOrderTask.execute(sendOrderCodes);
    }

    public void onSendOrderComplete(SendOrderResult sendOrderResult){
        if(sendOrderResult != null){
            saveOrder();
            mListener.goToSummary(sendOrderResult.getOrderId(),sendOrderResult.getData());
        }else{
            Snackbar.make(getView(),"Não foi possível submeter a encomenda",Snackbar.LENGTH_LONG).show();
        }
        confirmOrderButton.setEnabled(true);
        contentLoadingProgressBar.hide();
    }

    private void saveOrder() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(DataModel.getInstance().getCurrentOrder());
        realm.commitTransaction();
        realm.close();
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
                    realm.close();
                }
            });
            dialog.show(getActivity().getFragmentManager(),ExistingCustomerDataDialogFragment.TAG);
        }

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
