package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.entity.realm.OptionalGroup;
import com.nunoneto.assicanti.network.RequestConstants;
import com.nunoneto.assicanti.network.RestService;
import com.nunoneto.assicanti.network.response.AddToCart1Response;
import com.nunoneto.assicanti.network.response.AddToCart2Response;
import com.nunoneto.assicanti.network.response.GetOptionalsResponse;
import com.nunoneto.assicanti.tasks.DoRegisterRequestTask;
import com.nunoneto.assicanti.tasks.GetOptionalsTask;
import com.nunoneto.assicanti.ui.adapters.OptionalsPagerAdapter;
import com.nunoneto.assicanti.webscraper.WebScrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionalsFragment extends Fragment implements Callback<GetOptionalsResponse> {

    public static final String TAG = "OPTIONALS_FRAG";

    private OnOrderFragmentListener mListener;

    public static final String PARAM_ITEMID = "PARAM_ITEMID";
    public static final String PARAM_SIZE = "PARAM_SIZE";
    public static final String PARAM_TIER = "PARAM_TIER";
    public static final String PARAM_PRICE_ID = "PARAM_PRICE_ID";

    private String itemId, size, tier, priceId;

    //View
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private AppCompatButton nextButton;

    // Tasks
    private GetOptionalsTask getOptionalsTask;
    private DoRegisterRequestTask doRegisterRequestTask;

    public OptionalsFragment() {
    }

    public static OptionalsFragment newInstance(String itemId, String size, String tier, String priceId) {
        OptionalsFragment fragment = new OptionalsFragment();
        Bundle b = new Bundle();
        b.putString(PARAM_ITEMID,itemId);
        b.putString(PARAM_SIZE,size);
        b.putString(PARAM_TIER,tier);
        b.putString(PARAM_PRICE_ID,priceId);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if(b != null){
            this.itemId = b.getString(PARAM_ITEMID);
            this.size = b.getString(PARAM_SIZE);
            this.tier = b.getString(PARAM_TIER);
            this.priceId = b.getString(PARAM_PRICE_ID);
        }else{
            //TODO: deal with failure -.-
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getOptionals();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_optionals, container, false);
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
        nextButton = (AppCompatButton)view.findViewById(R.id.continueBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButton.setEnabled(false);
                addToCart1();
            }
        });
        contentLoadingProgressBar = (ContentLoadingProgressBar)view.findViewById(R.id.loading);

        contentLoadingProgressBar.bringToFront();
        contentLoadingProgressBar.show();

        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#ffffff"));

        return view;
    }

    private void getOptionals(){
        RestService.getInstance().getAssicantiService()
                .getOptionals(RequestConstants.GetIngredients.ACTION, RequestConstants.GetIngredients.TYPE,itemId,tier,size)
                .enqueue(this);
    }

    @Override
    public void onResponse(Call<GetOptionalsResponse> call, Response<GetOptionalsResponse> response) {
        getOptionalsTask = new GetOptionalsTask(OptionalsFragment.this);
        getOptionalsTask.execute(response.body().getBody());
    }

    @Override
    public void onFailure(Call<GetOptionalsResponse> call, Throwable t) {
        contentLoadingProgressBar.hide();
        Log.e(TAG,"Failed to get ingredients with reason:");
        t.printStackTrace();
        Snackbar.make(getView(),"Não foi possível carregar os pedidos opcionais",Snackbar.LENGTH_LONG).show();
    }

    public void loadOptionals(List<OptionalGroup> optionalGroupList){
        if(this.isAdded()){

            DataModel.getInstance().setOptionalGroups(optionalGroupList);
            List<Fragment> fragments = new ArrayList<>();
            String[] tabTitles = new String[optionalGroupList.size()];
            for(int i = 0;i<optionalGroupList.size();i++){
                OptionalGroup group = optionalGroupList.get(i);

                TabLayout.Tab tab = tabLayout.newTab()
                        .setText(group.getName())
                        .setContentDescription(group.getName());
                tabLayout.addTab(tab);

                ListOptionalsFragment frag = ListOptionalsFragment.newInstance(i,itemId);
                fragments.add(frag);
                tabTitles[i] = group.getName();
            }
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            OptionalsPagerAdapter adapter = new OptionalsPagerAdapter(getActivity().getSupportFragmentManager(),fragments,tabTitles);

            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            contentLoadingProgressBar.hide();
            nextButton.setEnabled(true);
            contentLoadingProgressBar.bringToFront();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(getOptionalsTask != null && (getOptionalsTask.getStatus() == AsyncTask.Status.PENDING || getOptionalsTask.getStatus() == AsyncTask.Status.RUNNING)){
            getOptionalsTask.cancel(true);
        }
        if(doRegisterRequestTask != null && (doRegisterRequestTask.getStatus() == AsyncTask.Status.PENDING || doRegisterRequestTask.getStatus() == AsyncTask.Status.RUNNING)){
            doRegisterRequestTask.cancel(true);
        }
        EventBus.getDefault().unregister(this);
    }

    private void addToCart1(){
        contentLoadingProgressBar.show();
        contentLoadingProgressBar.setVisibility(View.VISIBLE);
        RestService.getInstance()
                .getAssicantiService()
                .addToCart1(
                        RequestConstants.AddToCart1.ACTION,
                        RequestConstants.AddToCart1.TYPE,
                        DataModel.getInstance().getOptionalGroups().size() > 0 ? DataModel.getInstance().getOptionalGroups().get(0).getMultiType() : "1",
                        "",
                        ""
                )
                .enqueue(new Callback<AddToCart1Response>() {
                    @Override
                    public void onResponse(Call<AddToCart1Response> call, Response<AddToCart1Response> response) {
                        Log.e(TAG,"Going to AddCart2");
                        addToCart2();
                    }

                    @Override
                    public void onFailure(Call<AddToCart1Response> call, Throwable t) {
                        Log.e(TAG,"Could not complete addToCart 1 with cause: ");
                        t.printStackTrace();
                        contentLoadingProgressBar.hide();
                        Snackbar.make(getView(),"Não foi possível completar o pedido. Por favor tente mais tarde",Snackbar.LENGTH_SHORT).show();
                        nextButton.setEnabled(true);
                    }
                });
    }

    private void addToCart2(){
        RestService.getInstance().getAssicantiService()
                .addToCart2(
                        RequestConstants.AddToCart2.ACTION,
                        RequestConstants.AddToCart2.TYPE,
                        priceId,
                        "1",
                        ""
                ).enqueue(new Callback<AddToCart2Response>() {
            @Override
            public void onResponse(Call<AddToCart2Response> call, Response<AddToCart2Response> response) {
                register();
            }

            @Override
            public void onFailure(Call<AddToCart2Response> call, Throwable t) {
                contentLoadingProgressBar.hide();
                Log.e(TAG,"Could not complete addToCart 2 with cause: ");
                t.printStackTrace();
                Snackbar.make(getView(),"Não foi possível completar o pedido. Por favor tente mais tarde",Snackbar.LENGTH_SHORT).show();
                nextButton.setEnabled(true);
            }
        });
    }

    private void register(){
        doRegisterRequestTask = new DoRegisterRequestTask(OptionalsFragment.this);
        doRegisterRequestTask.execute();
    }

    public void onRegisterResponse(String hash){
        contentLoadingProgressBar.hide();
        nextButton.setEnabled(true);
        if(mListener != null)
            mListener.showForm();

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

    /**
     * disable/enable the next button on event
     * @param event
     */
    @Subscribe
    public void onMessageEvent(ToggleNextEvent event){
        nextButton.setEnabled(event.isToggle());

    }

    /**
     * This events serves to toggle the netx button in this fragment when one of the child listOptionals fragments
     * is in the process of checking/uncheking an ingredient, in order to avoid inconsistencies in the flow
     */
    public static class ToggleNextEvent{

        private boolean toggle;

        public ToggleNextEvent(boolean toggle) {
            this.toggle = toggle;
        }

        public boolean isToggle() {
            return toggle;
        }

        public void setToggle(boolean toggle) {
            this.toggle = toggle;
        }
    }

}
