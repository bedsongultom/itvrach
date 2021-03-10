package com.itvrach.www.itvrach;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.itvrach.model.Books;
import com.itvrach.model.ResponseBook;
import com.itvrach.services.APIClient;
import com.itvrach.services.BookService;
import com.itvrach.sessions.SessionManagement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PieChartFragment extends Fragment implements OnChartValueSelectedListener {
    private static String TAG = "PieChartFragment";
    private List<Books> booksList = new ArrayList<>();
    private PieChart pieChart;
    private PieData pieData;
    private PieDataSet dataset;
    private SessionManagement session;
    private TextView tvTitle;
    private ImageView imgFile;


    public static PieChartFragment newInstance() {
        PieChartFragment fragment = new PieChartFragment();
        return fragment;
    }


    public PieChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        setupToolbar(view);
        initViews(view);
        return view;
    }



    public void onResme(){
        super.onResume();
        Log.d(TAG, ": onResume");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, ": onStart");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, ": onStop");
    }


    public void onDetacth(){
        super.onDetach();
        Log.d(TAG, ": onDetacth" );
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, ": onDestroy");
    }



    private void initViews(View view) {
        session = new SessionManagement(getContext());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        final String userid = user.get(SessionManagement.KEY_USERID);
        /*final String username       = user.get(SessionManagement.KEY_USERNAME);
        final String type           = user.get(SessionManagement.KEY_TYPE);
        final String firstname      = user.get(SessionManagement.KEY_FIRSTNAME);
        final String lastname       = user.get(SessionManagement.KEY_LASTNAME);
        final String fullname       = user.get(SessionManagement.KEY_FULLNAME);
        final String email          = user.get(SessionManagement.KEY_EMAIL);
        final String sessionid      = user.get(SessionManagement.KEY_SESSIONID);
        final String fileImg        = user.get(SessionManagement.KEY_FILE);
        final String hp             = user.get(SessionManagement.KEY_HP);
        final String address        = user.get(SessionManagement.KEY_ADDRESS);*/

        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        pieChart.setOnChartValueSelectedListener(this);
        getListBook();


    }

    private void setupToolbar(View view) {
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //noinspection deprecation
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);

        tvTitle = (TextView) ((Welcome) getActivity()).findViewById(R.id.tvTitle);
        tvTitle.setTextSize(14);
        tvTitle.setText(R.string.piechart_title);
        tvTitle.setVisibility(View.VISIBLE);
        imgFile = (ImageView) ((Welcome) getActivity()).findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Home())
                        .addToBackStack(null)
                        .commit();

                ((Welcome)getActivity()).enableChartLayoutTabs(false);

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            }
        });

    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());

    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "nothing selected");
    }


    private void getListBook() {
        Log.d(TAG, "add DataSet started");

        final ProgressDialog pdPie;
        pdPie = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        pdPie.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdPie.setCancelable(false);
        pdPie.setIndeterminate(true);
        pdPie.getWindow().setGravity(Gravity.CENTER);
        pdPie.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        pdPie.show();
        pdPie.getWindow().setLayout(245, 200);
        pdPie.getWindow().setGravity(Gravity.CENTER);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pdPie.dismiss();
            }
        }, 2000);


        BookService bookService = APIClient.getClient().create(BookService.class);
        Call<ResponseBook> call = bookService.getFindAll();

        call.enqueue(new Callback<ResponseBook>() {
            class dataGraph implements IValueFormatter {
                private DecimalFormat percentageFormat;

                dataGraph() {
                    percentageFormat = new DecimalFormat("###,###,##0.0");
                }

                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    //return percentageFormat.format(value) + " %";
                    return percentageFormat.format(value) + " eksemplar";
                }
            }

            @Override
            public void onResponse(Call<ResponseBook> call, Response<ResponseBook> response) {

                final ResponseBook responseBook = response.body();
                if (response.isSuccessful()) {

                    if (responseBook.getSuccess().equals("true")) {
                        booksList = response.body().getResult();
                        if (booksList.size() != 0) {

                            ArrayList<PieEntry> yEntrys = new ArrayList<PieEntry>();
                            for (int i = 0; i < booksList.size(); i++) {
                                yEntrys.add(new PieEntry(Float.parseFloat(String.valueOf(booksList.get(i).getStock())), booksList.get(i).getItem_name(), i));

                            }

                            // entries.add(new PieEntry(valueFloat, String.valueOf(valueFloat).concat(" units")));
                            PieDataSet dataset = new PieDataSet(yEntrys, null);
                            dataset.setSliceSpace(3);
                            dataset.setSelectionShift(5);
                            ArrayList<Integer> colors = new ArrayList<Integer>();
                            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                                colors.add(c);

                            for (int c : ColorTemplate.JOYFUL_COLORS)
                                colors.add(c);

                            for (int c : ColorTemplate.COLORFUL_COLORS)
                                colors.add(c);

                            for (int c : ColorTemplate.LIBERTY_COLORS)
                                colors.add(c);

                            for (int c : ColorTemplate.PASTEL_COLORS)
                                colors.add(c);

                            colors.add(ColorTemplate.getHoloBlue());
                            dataset.setColors(colors);

                            Legend legend = pieChart.getLegend();
                            legend.setForm(Legend.LegendForm.SQUARE);
                            //noinspection deprecation
                            legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
                            legend.setTextSize(8);
                            legend.setTextColor(Color.MAGENTA);

                            Description description = new Description();
                            description.setText("Description of Stocks");

                            pieChart.setDescription(description);
                            // pieChart.setTextAlignment((int)0.f);
                            //  pieChart.setExtraOffsets(5, 10, 5, 5);

                            description.setTextColor(Color.BLUE);
                            // pieChart.setUsePercentValues(true);
                            //   pieChart.setRotationAngle(0);
                            pieChart.setRotationEnabled(true);
                            pieChart.getLegend().setWordWrapEnabled(true);

                            pieChart.setHoleColor(Color.TRANSPARENT);
                            //pieChart.setCenterTextColor(Color.BLACK);
                            pieChart.setHoleRadius(25f);
                            pieChart.setTransparentCircleColor(Color.WHITE);
                            pieChart.setTransparentCircleAlpha(50);
                            pieChart.setCenterText("Chart");
                            pieChart.setCenterTextSize(8);
                            // pieChart.setUsePercentValues(true);
                            //  pieChart.setMaxAngle(360f);
                            pieChart.setDrawEntryLabels(true);
                            pieChart.setEntryLabelTextSize(8);
                            pieChart.setEntryLabelColor(Color.MAGENTA);
                            pieChart.setTransparentCircleColor(10);
                            //  pieChart.setTransparentCircleRadius(50f);
                            pieData = new PieData(dataset);
                            pieData.setValueFormatter(new dataGraph());

                            // pieData.setValueFormatter(new PercentFormatter());
                            pieData.setValueTextColor(Color.BLUE);
                            pieChart.setData(pieData);
                            //pieChart.animateXY(1400,1400);
                            pieChart.animateY(2000);
                            pieChart.notifyDataSetChanged();
                            //pieChart.highlightValues(null);
                            pieChart.invalidate();
                            pdPie.dismiss();

                        }
                    }

                } else {
                    pdPie.dismiss();
                    Log.d(TAG, " : data is empty");
                    Toast.makeText(getContext(), responseBook.getSuccess(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBook> call, Throwable t) {
                pdPie.dismiss();
                Log.d(TAG, " say : Not Connected");
            }

        });

    }
}
