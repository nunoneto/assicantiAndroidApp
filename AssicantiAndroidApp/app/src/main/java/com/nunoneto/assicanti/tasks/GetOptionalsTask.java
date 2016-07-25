package com.nunoneto.assicanti.tasks;

import android.os.AsyncTask;

import com.nunoneto.assicanti.model.entity.OptionalGroup;
import com.nunoneto.assicanti.ui.fragment.OptionalsFragment;
import com.nunoneto.assicanti.webscraper.WebScrapper;

import java.util.List;

/**
 * Created by NB20301 on 14/07/2016.
 */
public class GetOptionalsTask extends AsyncTask<String,Void,List<OptionalGroup>> {

    private OptionalsFragment optionalsFragment;

    public GetOptionalsTask(OptionalsFragment optionalsFragment){
        this.optionalsFragment = optionalsFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<OptionalGroup> doInBackground(String... html) {
        return WebScrapper.getInstance().parseOptionals(html[0]);
    }

    @Override
    protected void onPostExecute(List<OptionalGroup> optionalGroups) {
        super.onPostExecute(optionalGroups);
        optionalsFragment.loadOptionals(optionalGroups);
    }
}
