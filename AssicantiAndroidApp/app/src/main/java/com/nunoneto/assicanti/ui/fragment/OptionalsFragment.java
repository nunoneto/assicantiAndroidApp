package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.nunoneto.assicanti.model.entity.OptionalGroup;
import com.nunoneto.assicanti.network.RequestConstants;
import com.nunoneto.assicanti.network.RestService;
import com.nunoneto.assicanti.network.response.AddToCart1Response;
import com.nunoneto.assicanti.network.response.AddToCart2Response;
import com.nunoneto.assicanti.network.response.GetOptionalsResponse;
import com.nunoneto.assicanti.tasks.GetOptionalsTask;
import com.nunoneto.assicanti.ui.adapters.OptionalsPagerAdapter;
import com.nunoneto.assicanti.webscraper.WebScrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionalsFragment extends Fragment implements Callback<GetOptionalsResponse> {

    public static final String NAME = "OPTIONALS";

    private OnOrderFragmentListener mListener;
    private final static String TAG = "OPTIONALS_FRAG";

    public static final String PARAM_ITEMID = "PARAM_ITEMID";
    public static final String PARAM_SIZE = "PARAM_SIZE";
    public static final String PARAM_TIER = "PARAM_TIER";
    public static final String PARAM_PRICE_ID = "PARAM_PRICE_ID";

    private String itemId, size, tier, priceId;
    private GetOptionalsTask getOptionalsTask;

    //View
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private AppCompatButton nextButton;

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
            getOptionals();
        }else{
            //TODO: deal with failure -.-
        }

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
                addToCart1();
            }
        });
        contentLoadingProgressBar = (ContentLoadingProgressBar)view.findViewById(R.id.loading);
        contentLoadingProgressBar.bringToFront();
        contentLoadingProgressBar.show();
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
        Log.e(TAG,"Failed to get ingredients with reason:");
        t.printStackTrace();
        Snackbar.make(getView(),"Não foi possível carregar os pedidos opcionais",Snackbar.LENGTH_LONG).show();
    }

    public void loadOptionals(List<OptionalGroup> optionalGroupList){

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

    @Override
    public void onStop() {
        super.onStop();
        if(getOptionalsTask != null && (getOptionalsTask.getStatus() == AsyncTask.Status.PENDING || getOptionalsTask.getStatus() == AsyncTask.Status.RUNNING)){
            getOptionalsTask.cancel(true);
        }
    }

    private void addToCart1(){
        contentLoadingProgressBar.show();
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
                Log.e(TAG,"Could not complete addToCart 2 with cause: ");
                t.printStackTrace();
                contentLoadingProgressBar.hide();
                Snackbar.make(getView(),"Não foi possível completar o pedido. Por favor tente mais tarde",Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    private void register(){
        RestService.getInstance().getAssicantiService()
                .register(
                        RequestConstants.Register.ACTION,
                        RequestConstants.Register.TYPE,
                        RequestConstants.Register.VAL
                )
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String hash = WebScrapper.getInstance().parseRegisterOrder(response.body());
                        contentLoadingProgressBar.hide();
                        mListener.showForm();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG,"Could not complete register with cause: ");
                        t.printStackTrace();
                        contentLoadingProgressBar.hide();
                        Snackbar.make(getView(),"Não foi possível completar o pedido. Por favor tente mais tarde",Snackbar.LENGTH_SHORT).show();
                    }
                });
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
