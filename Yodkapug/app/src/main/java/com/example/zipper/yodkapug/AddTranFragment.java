package com.example.zipper.yodkapug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Dejavuz on 4/21/2017.
 */

public class AddTranFragment extends Fragment {
    public AddTranFragment() {
        super();
    }
    public String state,date1,type1,commend1,price1 ;
    private ToggleButton togg;
    private EditText date,type,commend,price;
    private Button save,del;


    public static AddTranFragment newInstance() {
        AddTranFragment fragment = new AddTranFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
        onRestoreInstanceState(savedInstanceState);

         date1 =date.getText().toString();
         type1 = type.getText().toString();
         commend1=commend.getText().toString();
         price1 = price.getText().toString();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_trans, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState
        date = (EditText) rootView.findViewById(R.id.date);
        type = (EditText) rootView.findViewById(R.id.type);
        commend = (EditText) rootView.findViewById(R.id.commend);
        price = (EditText) rootView.findViewById(R.id.price);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }

}
