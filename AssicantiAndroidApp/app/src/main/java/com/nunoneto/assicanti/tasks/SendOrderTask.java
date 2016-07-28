package com.nunoneto.assicanti.tasks;

import android.os.AsyncTask;

import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.entity.SendOrderCodes;
import com.nunoneto.assicanti.model.entity.SendOrderResult;
import com.nunoneto.assicanti.model.entity.realm.CustomerData;
import com.nunoneto.assicanti.network.RequestConstants;
import com.nunoneto.assicanti.ui.fragment.CustomerDataFragment;
import com.nunoneto.assicanti.webscraper.WebScrapper;

/**
 * Created by NB20301 on 28/07/2016.
 */
public class SendOrderTask extends AsyncTask<SendOrderCodes,Void,SendOrderResult> {

    private CustomerDataFragment fragment;

    public SendOrderTask(CustomerDataFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected SendOrderResult doInBackground(SendOrderCodes... sendOrderCodes) {
        SendOrderCodes codes = sendOrderCodes[0];
        CustomerData data = DataModel.getInstance().getCurrentOrder().getCustomerData();
        return WebScrapper.getInstance().SendOrder(
                RequestConstants.SendOrder.buildFormQuery(
                    data.getName(),
                    data.getEmail(),
                    data.getAddress(),
                    data.getContact(),
                    data.getComment(),
                    data.getCompanyCode(),
                    data.getNif(),
                    codes != null? codes.getCod() : "",
                    codes != null? codes.getHash() : ""
                )
        );
    }

    @Override
    protected void onPostExecute(SendOrderResult sendOrderResult) {
        super.onPostExecute(sendOrderResult);
        fragment.onSendOrderComplete(sendOrderResult);
    }
}
