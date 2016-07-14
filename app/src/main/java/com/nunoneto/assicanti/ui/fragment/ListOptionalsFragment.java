package com.nunoneto.assicanti.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nunoneto.assicanti.R;
import com.nunoneto.assicanti.model.DataModel;
import com.nunoneto.assicanti.model.OptionalGroup;
import com.nunoneto.assicanti.model.OptionalItem;
import com.nunoneto.assicanti.ui.adapters.OptionalsListAdapter;

public class ListOptionalsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private static final String TAG = "LISTOPTIONALS_FRAG";
    private static final String PARAM_POSITION = "POSITION";

    private int position;

    // Views
    private RecyclerView optionalsRecyclerView;

    public ListOptionalsFragment() {
    }

    public static ListOptionalsFragment newInstance(int position) {
        ListOptionalsFragment fragment = new ListOptionalsFragment();
        Bundle b = new Bundle();
        b.putInt(PARAM_POSITION,position);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            position = getArguments().getInt(PARAM_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_optionals, container, false);
        optionalsRecyclerView = (RecyclerView)view.findViewById(R.id.optionalsRecyclerView);
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
                    public void onItemChecked(boolean checked, OptionalItem item) {
                        Toast.makeText(getContext(),"Click with state"+(checked? "on":"off")+" in "+item.getName(),Toast.LENGTH_LONG).show();
                    }
                }
        ));
    }

    private void addIngredient(){

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
