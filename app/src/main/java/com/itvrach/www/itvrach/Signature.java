package com.itvrach.www.itvrach;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.gcacace.signaturepad.views.SignaturePad;

/**
 * A simple {@link Fragment} subclass.
 */
public class Signature extends Fragment implements View.OnClickListener , SignaturePad.OnSignedListener{

    private Button saveButton, clearButton;
    private SignaturePad mSignaturePad;
    private String TAG ="Signature";
    public static Signature newInstance() {
        return new Signature();
    }

    public Signature() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.signagture, container, false);
        initViews(view);
        return view;
    }



    public void initViews(View view){
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);

        TextView tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        tvTitle.setTextSize(14);
        tvTitle.setText("Signature");
        tvTitle.setVisibility(View.VISIBLE);

        ImageView imgFile = (ImageView) getActivity().findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);

        mSignaturePad = (SignaturePad) view.findViewById(R.id.signaturePad);
        saveButton    = (Button) view.findViewById(R.id.buttonSave);
        clearButton   = (Button) view.findViewById(R.id.buttonClear);

        saveButton.setEnabled(false);
        clearButton.setEnabled(false);

        saveButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        mSignaturePad.setOnSignedListener(this);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar:
                FragmentTransaction ft;
                ft = getFragmentManager().beginTransaction();
                Home fragment = new Home();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case R.id.buttonSave:
                Toast toast = Toast.makeText(getContext(),"Saved",Toast.LENGTH_SHORT);
                toast.show();
                break;

            case R.id.buttonClear:

                mSignaturePad.clear();

                break;

            default:
                break;

        }

    }

    @Override
    public void onStartSigning() {
        Log.d(TAG,"is Starting");
    }

    @Override
    public void onSigned() {
        saveButton.setEnabled(true);
        clearButton.setEnabled(true);
        Log.d(TAG,": is Signed ");
    }

    @Override
    public void onClear() {
        saveButton.setEnabled(false);
        clearButton.setEnabled(false);
        Log.d(TAG,": is Clear");
    }
}
