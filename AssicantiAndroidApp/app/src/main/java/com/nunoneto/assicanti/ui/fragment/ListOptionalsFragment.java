package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.OptionalItem;
import com.nunoneto.assicanti.network.RequestConstants;
import com.nunoneto.assicanti.network.RestService;
import com.nunoneto.assicanti.network.response.AddOptionalResponse;
import com.nunoneto.assicanti.ui.adapters.OptionalsListAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOptionalsFragment extends Fragment {

    public static final String NAME = "LIST_OPTIONALS";

    private OnOrderFragmentListener mListener;

    private static final String TAG = "LISTOPTIONALS_FRAG";
    private static final String PARAM_POSITION = "POSITION";
    private static final String PARAM_GROUP_ID = "GROUP_ID";

    private int position;
    private String groupId;

    // Views
    private RecyclerView optionalsRecyclerView;
    private ContentLoadingProgressBar contentLoadingProgressBar;

    public ListOptionalsFragment() {
    }

    public static ListOptionalsFragment newInstance(int position, String groupId) {
        ListOptionalsFragment fragment = new ListOptionalsFragment();
        Bundle b = new Bundle();
        b.putInt(PARAM_POSITION,position);
        b.putString(PARAM_GROUP_ID,groupId);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            position = getArguments().getInt(PARAM_POSITION);
            groupId = getArguments().getString(PARAM_GROUP_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_optionals, container, false);
        optionalsRecyclerView = (RecyclerView)view.findViewById(R.id.optionalsRecyclerView);
        contentLoadingProgressBar = (ContentLoadingProgressBar)view.findViewById(R.id.loading);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        optionalsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        optionalsRecyclerView.setHasFixedSize(true);
        optionalsRecyclerView.setAdapter(new OptionalsListAdapter(
                DataModel.getInstance().getOptionalGroups().get(position).getItems(),
                new OptionalsListAdapter.OptionalItemClickListener() {
                    @Override
                    public void onItemChecked(boolean checked, OptionalItem item, CompoundButton compoundButton) {
                        addRemoveIngredient(item,compoundButton, checked);
                    }
                }
        ));
        contentLoadingProgressBar.hide();
    }

    private void addRemoveIngredient(OptionalItem item, final CompoundButton compoundButton, final boolean checked ){
        contentLoadingProgressBar.show();
        compoundButton.setEnabled(false);
        RestService.getInstance().getAssicantiService()
                .addRemoveOptional(
                        checked ? RequestConstants.AddIngredients.ACTION : RequestConstants.RemoveIngredients.ACTION,
                        checked ? RequestConstants.AddIngredients.TYPE : RequestConstants.RemoveIngredients.TYPE,
                        item.getItemId(),
                        groupId,
                        item.getMultiId(),
                        item.getMultiType()
                ).enqueue(new Callback<AddOptionalResponse>() {
            @Override
            public void onResponse(Call<AddOptionalResponse> call, Response<AddOptionalResponse> response) {
                Snackbar.make(getView(),checked ? "Opcional adicionado!" : "Opcional removido!",Snackbar.LENGTH_SHORT).show();
                compoundButton.setEnabled(true);
                contentLoadingProgressBar.hide();
            }

            @Override
            public void onFailure(Call<AddOptionalResponse> call, Throwable t) {
                Log.e(TAG,"Failed to get ingredients with reason:");
                t.printStackTrace();
                Snackbar.make(getView(),"Não foi adicionar o opcional",Snackbar.LENGTH_LONG).show();
                compoundButton.setChecked(false);
                compoundButton.setEnabled(true);
                contentLoadingProgressBar.hide();
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
