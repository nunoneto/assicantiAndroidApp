package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.OptionalGroup;
import com.nunoneto.assicanti.network.RequestConstants;
import com.nunoneto.assicanti.network.RestService;
import com.nunoneto.assicanti.network.response.GetOptionalsResponse;
import com.nunoneto.assicanti.tasks.GetOptionalsTask;
import com.nunoneto.assicanti.ui.adapters.OptionalsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionalsFragment extends BaseFragment implements Callback<GetOptionalsResponse> {

    private OnFragmentInteractionListener mListener;
    private final static String TAG = "OPTIONALS_FRAG";

    public static final String ITEMID= "ITEMID";
    public static final String SIZE = "SIZE";
    public static final String TIER = "TIER";

    private String itemId, size, tier;
    private GetOptionalsTask getOptionalsTask;

    //View
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public OptionalsFragment() {
    }

    public static OptionalsFragment newInstance(String itemId, String size, String tier) {
        OptionalsFragment fragment = new OptionalsFragment();
        Bundle b = new Bundle();
        b.putString(ITEMID,itemId);
        b.putString(SIZE,size);
        b.putString(TIER,tier);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if(b != null){
            this.itemId = b.getString(ITEMID);
            this.size = b.getString(SIZE);
            this.tier = b.getString(TIER);
            getOptionals();
        }else{
            //TODO: deal with faliure -.-
        }

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
    }

    @Override
    public void onStop() {
        super.onStop();
        if(getOptionalsTask != null && (getOptionalsTask.getStatus() == AsyncTask.Status.PENDING || getOptionalsTask.getStatus() == AsyncTask.Status.RUNNING)){
            getOptionalsTask.cancel(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_optionals, container, false);
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
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

    public interface OnFragmentInteractionListener {
    }
}
