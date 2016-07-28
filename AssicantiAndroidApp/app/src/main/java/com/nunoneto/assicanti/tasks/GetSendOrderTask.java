package com.nunoneto.assicanti.tasks;

import android.os.AsyncTask;

import com.nunoneto.assicanti.model.entity.SendOrderCodes;
import com.nunoneto.assicanti.ui.fragment.CustomerDataFragment;
import com.nunoneto.assicanti.webscraper.WebScrapper;

/**
 * Created by NB20301 on 28/07/2016.
 */
public class GetSendOrderTask extends AsyncTask<Void,Void,SendOrderCodes> {

    private  CustomerDataFragment fragment;

    public GetSendOrderTask(CustomerDataFragment fragment){
        this.fragment = fragment;
    }

    @Override
    protected SendOrderCodes doInBackground(Void... voids) {
        return WebScrapper.getInstance().getSendOrderHash();
    }

    @Override
    protected void onPostExecute(SendOrderCodes s) {
        super.onPostExecute(s);
        fragment.setSendOrderCodes(s);
    }
}
