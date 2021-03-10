package com.itvrach.www.itvrach;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UsersListActivity extends AppCompatActivity {
    private static String TAG = "UsersListActivity";
    private List<Books> booksList = new ArrayList<>();


    private PieChart pieChart;
    private PieData pieData;
    private PieDataSet dataset;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_users);

        this.setTitle("CHART OF STOCKS");
        Log.d(TAG, "onCreate: starting to create chart");

        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
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
                 Log.i("PieChartFragment", "nothing selected");

             }
         });


        getListBooks();
    }

    private void getListBooks() {
        Log.d(TAG, "addDataSet started");
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
                    ResponseBook responseBook = response.body();
                    if (responseBook.getSuccess().equals("true")) {
                        booksList = response.body().getResult();

                        ArrayList<PieEntry> yEntrys = new ArrayList<PieEntry>();
                            for (int i=0; i<booksList.size(); i++) {
                            yEntrys.add(new PieEntry(Float.parseFloat(String.valueOf(booksList.get(i).getStock())), booksList.get(i).getItem_name(),i));

                         }

                       // entries.add(new PieEntry(valueFloat, String.valueOf(valueFloat).concat(" units")));
                        PieDataSet dataset = new PieDataSet(yEntrys,null);
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
                        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
                        legend.setTextSize(8);
                        legend.setTextColor(Color.MAGENTA);

                        Description description = new Description();
                        description.setText("Stocks Description of Programming Books");

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

                    }else{
                        Toast.makeText(UsersListActivity.this, responseBook.getSuccess(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBook> call, Throwable t) {

            }
            });
        }
}