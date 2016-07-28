package com.nunoneto.assicanti.tasks;

import android.os.AsyncTask;

import com.nunoneto.assicanti.ui.fragment.OptionalsFragment;
import com.nunoneto.assicanti.webscraper.WebScrapper;

/**
 * Created by NB20301 on 28/07/2016.
 */
public class DoRegisterRequestTask extends AsyncTask<Void,Void,String> {

    private OptionalsFragment optionalsFragment;

    public DoRegisterRequestTask(OptionalsFragment frag) {
        this.optionalsFragment = frag;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return WebScrapper.getInstance().parseRegisterOrder();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        optionalsFragment.onRegisterResponse(s);
    }
}
