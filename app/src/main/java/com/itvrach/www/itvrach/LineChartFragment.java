package com.itvrach.www.itvrach;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
public class LineChartFragment extends Fragment implements OnChartValueSelectedListener {

    private static String TAG = "BarChartFragment";
    private List<Books> booksList = new ArrayList<Books>();
    private ArrayList<Entry> yVals = new ArrayList<Entry>();
    private ArrayList<String> xVals = new ArrayList<String>();

    private LineChart lineChart;
    private LineData lineData;
    private LineDataSet lineDataSet;

    private SessionManagement session;
    private TextView tvTitle;
    private ImageView imgFile;


    public static LineChartFragment newInstance() {
        LineChartFragment fragment = new LineChartFragment();
        return fragment;
    }





    public LineChartFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_line_chart, container, false);
        initViews(view);
        return view;
    }


    private void initViews(View view){
        session = new SessionManagement(getContext());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        final String userid = user.get(SessionManagement.KEY_USERID);

        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        lineChart.setOnChartValueSelectedListener(this);
        getListBook();



    }

    private void getListBook() {
        Log.d(TAG, "add DataSet started");

        final ProgressDialog pdLine;
        pdLine = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        pdLine.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdLine.setCancelable(false);
        pdLine.setIndeterminate(true);
        pdLine.getWindow().setGravity(Gravity.CENTER);
        pdLine.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        pdLine.show();
        pdLine.getWindow().setLayout(245, 200);
        pdLine.getWindow().setGravity(Gravity.CENTER);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pdLine.dismiss();
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
                   // return percentageFormat.format(value) + " eksemplar";

                    String data  = entry.getData().toString()+"\n";
                    data += percentageFormat.format(value);

                    return data;
                }
            }

            @Override
            public void onResponse(Call<ResponseBook> call, Response<ResponseBook> response) {
                final ResponseBook responseBook = response.body();
                if (response.isSuccessful()) {
                    if (responseBook.getSuccess().equals("true")) {
                        booksList = response.body().getResult();
                        if (booksList.size() != 0) {

                            for (int i = 0; i < booksList.size(); i++) {
                                yVals.add(new Entry(i, Float.parseFloat(String.valueOf(booksList.get(i).getStock())), booksList.get(i).getItem_name()));

                               /* Books x = booksList.get(i);
                                String item_name = x.getItem_code();
                                float stock = x.getStock();
                                yVals.add(new BarEntry(stock,i));
                                xVals.add(item_name);*/
                            }

                            for(int z=0; z< booksList.size();z++){
                                xVals.add(booksList.get(z).getItem_code());
                            }


                            /*for (int i = 0; i < booksList.size(); i++) {
                                Books x = booksList.get(i);
                                String item_name = x.getItem_name();
                                float stock = x.getStock();
                                yVals.add(new BarEntry(stock,i));
                                xVals.add(item_name);
                            }*/


                            lineChart.setTouchEnabled(true);
                            lineChart.setDragEnabled(true);
                            lineChart.setScaleEnabled(true);
                            lineChart.setPinchZoom(true);

                            XAxis xAxis = lineChart.getXAxis();
                            xAxis.setAvoidFirstLastClipping(true);
                            xAxis.setDrawLabels(true);
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));


                            YAxis yLeftAxis = lineChart.getAxisLeft();
                            yLeftAxis.setInverted(true);

                            YAxis rightAxis = lineChart.getAxisRight();
                            rightAxis.setEnabled(false);

                             /*Legend l = lineChart.getLegend();
                            l.setForm(Legend.LegendForm.LINE);
*/

                            Legend legend = lineChart.getLegend();
                            legend.setForm(Legend.LegendForm.LINE);
                            //noinspection deprecation
                            legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
                            legend.setTextSize(7);
                            legend.setTextColor(Color.MAGENTA);


                            Description description = new Description();
                            description.setText("Description of Stocks");
                            description.setTextColor(Color.BLUE);
                            lineChart.getLegend().setWordWrapEnabled(true);

                            lineChart.setData(lineData);
                            lineChart.setDescription(description);
                            lineChart.getLegend().setWordWrapEnabled(true);

                            lineDataSet = new LineDataSet(yVals, String.valueOf(xVals));
                            lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);


                            lineData = new LineData(lineDataSet);
                            lineData.setValueFormatter(new dataGraph());
                            lineChart.setData(lineData);
                            lineChart.animateXY(2000,2000);
                            lineChart.notifyDataSetChanged();
                            lineChart.invalidate();

                            pdLine.dismiss();
                        }
                    }

                } else {
                    pdLine.dismiss();
                    Log.d(TAG, " : data is empty");
                    Toast.makeText(getContext(), responseBook.getSuccess(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBook> call, Throwable t) {
                pdLine.dismiss();
                Log.d(TAG, " say : Not Connected");
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
}
