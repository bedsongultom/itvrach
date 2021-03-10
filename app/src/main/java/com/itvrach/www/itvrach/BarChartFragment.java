package com.itvrach.www.itvrach;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
public class BarChartFragment extends Fragment implements OnChartValueSelectedListener {
    private static String TAG = "BarChartFragment";
    private List<Books> booksList = new ArrayList<Books>();
    private ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
    private ArrayList<String> xVals = new ArrayList<String>();

    private BarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;

    private SessionManagement session;
    private TextView tvTitle;
    private ImageView imgFile;


    public static BarChartFragment newInstance() {
        return new BarChartFragment();
    }


    public BarChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        setUpToolbarMenu();
        initViews(view);
       // disableTabs(false);

        return view;

    }


    private void initViews(View view){
        session = new SessionManagement(getContext());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        final String userid = user.get(SessionManagement.KEY_USERID);

        barChart = (BarChart) view.findViewById(R.id.barChart);
        barChart.setOnChartValueSelectedListener(this);
        getListBook();

    }



    @SuppressLint("PrivateResource")
    public void setUpToolbarMenu() {
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(R.color.white);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);

        TextView tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        tvTitle.setTextSize(14);
        tvTitle.setText(R.string.cart_title);
        tvTitle.setVisibility(View.VISIBLE);

        ImageView imgFile = (ImageView) getActivity().findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Home())
                        .addToBackStack(null)
                        .commit();

                //disableTabs(true);

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
                        + ", barDataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "nothing selected");
    }


    private void getListBook() {
        Log.d(TAG, "add DataSet started");


        final ProgressDialog pdBar;
        pdBar = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        pdBar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdBar.setCancelable(false);
        pdBar.setIndeterminate(true);
        pdBar.getWindow().setGravity(Gravity.CENTER);
        pdBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        pdBar.show();
        pdBar.getWindow().setLayout(245, 200);
        pdBar.getWindow().setGravity(Gravity.CENTER);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pdBar.dismiss();
            }
        }, 2000);


        final BookService bookService = APIClient.getClient().create(BookService.class);
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
                    String data  = entry.getData().toString()+"\n";

                           data += percentageFormat.format(value);

                    return data;
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onResponse(Call<ResponseBook> call, Response<ResponseBook> response) {
                final ResponseBook responseBook = response.body();
                if (response.isSuccessful()) {
                    if (responseBook.getSuccess().equals("true")) {
                        booksList = response.body().getResult();
                        if (booksList.size() != 0) {

                            for (int i = 0; i < booksList.size(); i++) {
                                yVals.add(new BarEntry(i, Float.parseFloat(String.valueOf(booksList.get(i).getStock())), booksList.get(i).getItem_name()));

                               /* Books x = booksList.get(i);
                                String item_name = x.getItem_code();
                                float stock = x.getStock();
                                yVals.add(new BarEntry(stock,i));
                                xVals.add(item_name);*/
                            }

                            for(int z=0; z< booksList.size();z++){
                                xVals.add(booksList.get(z).getItem_code());
                            }

                            barChart.setTouchEnabled(true);
                            barChart.setDragEnabled(true);
                            barChart.setScaleEnabled(true);
                            barChart.setPinchZoom(true);

                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setDrawLabels(true);
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));

                           // xAxis.setGranularity(1f);
                           // xAxis.setCenterAxisLabels(true);
                          //  xAxis.setLabelRotationAngle(-90);

                          /*  xAxis.setValueFormatter(new IAxisValueFormatter() {
                                @Override
                                public String getFormattedValue(float value, AxisBase axis) {
                                 if(Float.parseFloat(String.valueOf(value))< booksList.size()){
                                        return String.valueOf((booksList.get((int) value).getStock()));

                                    }else{
                                        return "";
                                    }
                                }

                                public int getDecimalDigits(){
                                    return 0;
                                }
                            });
*/

                              /*  @Override
                                public String getFormattedValue(float value, AxisBase axis) {
                                    if(value>=0){
                                        if(value<=booksList.size()-1){
                                            return String.valueOf(xVals);
                                        }
                                    }
                                    return null;
                                }
                            });
*/
                          /*  XAxis xl = barChart.getXAxis();
                            xl.setAvoidFirstLastClipping(true);
                            *//*YAxis leftAxis = barChart.getAxisLeft();
                            leftAxis.setInverted(true);*//*
                            YAxis rightAxis = barChart.getAxisRight();
                            rightAxis.setEnabled(true);
*/
                           /* Legend l = barChart.getLegend();
                            l.setForm(Legend.LegendForm.SQUARE);*/


                            Legend legend = barChart.getLegend();
                            legend.setForm(Legend.LegendForm.SQUARE);
                            //noinspection deprecation
                            legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
                            legend.setTextSize(7);
                            legend.setTextColor(Color.MAGENTA);

                            Description description = new Description();
                            description.setText("Description of Stocks");
                            description.setTextColor(Color.MAGENTA);
                            barChart.getLegend().setWordWrapEnabled(true);
                            barChart.setData(barData);
                            barChart.setDescription(description);


                            barDataSet = new BarDataSet(yVals, xVals.toString());
                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                            barData = new BarData(barDataSet);


                            barData.setValueFormatter(new dataGraph());
                            barChart.setData(barData);
                            barChart.animateY(2000);
                            barChart.notifyDataSetChanged();
                            barChart.invalidate();


                            /*barDataSet = new BarDataSet(yVals, String.valueOf(xVals));
                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                            barData = new BarData(barDataSet);
                            barData.setValueFormatter(new dataGraph());
                            barData.getDataSetLabels();

                            LimitLine line = new LimitLine(12f,String.valueOf(barDataSet));
                            line.setTextSize(12f);
                            line.setLineWidth(9f);
                            YAxis leftAxis = barChart.getAxisLeft();
                            leftAxis.addLimitLine(line);


                            Description description = new Description();
                            description.setText("Stocks Description of Programming Books");
                            description.setTextColor(Color.BLUE);
                            barChart.setData(barData);
                            barChart.setDescription(description);

                            barChart.setBorderColor(Color.TRANSPARENT);
                            barChart.animateXY(2000,2000);
                            barChart.notifyDataSetChanged();
                            barChart.invalidate();*/
                            pdBar.dismiss();

                        }
                    }

                } else {
                    pdBar.dismiss();
                    Log.d(TAG, " : data is empty");
                    Toast.makeText(getContext(), responseBook.getSuccess(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBook> call, Throwable t) {
                pdBar.dismiss();
                Log.d(TAG, " say : Not Connected");
            }

        });

    }


    /*public boolean disableTabs(boolean disable){
        if(disable){
            Log.i(TAG, "disable chart's Tabs ");

            ((Welcome)getActivity()).chartLinearLayout.setVisibility(View.GONE);
            ((Welcome) getActivity()).highLightCurrentChartTab(0);
            ((Welcome)getActivity()).viewPagerChart.setCurrentItem(0);

            ((Welcome)getActivity()).tabChartLayout.setVisibility(View.GONE);
            ((Welcome)getActivity()).viewPagerChart.setVisibility(View.GONE);
            ((Welcome)getActivity()).viewPagerChart.setPagingEnabled(false);

            ((ViewGroup) ((Welcome) getActivity()).tabChartLayout.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            ((ViewGroup) ((Welcome) getActivity()).tabChartLayout.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
            ((ViewGroup) ((Welcome) getActivity()).tabChartLayout.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            ((Welcome)getActivity()).tabChartLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }else{

            Log.i(TAG, "enable Chart's Tabs");
            ((Welcome)getActivity()).chartLinearLayout.setVisibility(View.VISIBLE);
            ((Welcome) getActivity()).highLightCurrentChartTab(0);
            ((Welcome)getActivity()).viewPagerChart.setCurrentItem(0);

            ((Welcome)getActivity()).tabChartLayout.setVisibility(View.VISIBLE);
            ((Welcome)getActivity()).viewPagerChart.setVisibility(View.VISIBLE);
            ((Welcome)getActivity()).viewPagerChart.setPagingEnabled(true);

            ((ViewGroup) ((Welcome) getActivity()).tabChartLayout.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
            ((ViewGroup) ((Welcome) getActivity()).tabChartLayout.getChildAt(0)).getChildAt(1).setVisibility(View.VISIBLE);
            ((ViewGroup) ((Welcome) getActivity()).tabChartLayout.getChildAt(0)).getChildAt(2).setVisibility(View.VISIBLE);
            ((Welcome)getActivity()).tabChartLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }
        return disable;
    }
*/


}
