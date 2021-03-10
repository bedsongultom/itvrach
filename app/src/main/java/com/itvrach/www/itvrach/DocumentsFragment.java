package com.itvrach.www.itvrach;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ZoomControls;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;


/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentsFragment extends Fragment {
    private PDFView pdfView;
    private ZoomControls zoomControls;
    private int pageNumber =0;
    private String pageFileName;
    private Context context;



    public static DocumentsFragment newInstance() {
        return new DocumentsFragment();
    }


    public DocumentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_documents, container, false);
        initViews(view);
        return view;
    }


    private void initViews(View view){
        pdfView = (PDFView) view.findViewById(R.id.pdfView);
        zoomControls = (ZoomControls) view.findViewById(R.id.zoomControls);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = pdfView.getScaleX();
                float y = pdfView.getScaleY();

                pdfView.setScaleX((float) x+1);
                pdfView.setScaleY((float) y+1);


            }
        });


        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = pdfView.getScaleX();
                float y = pdfView.getScaleY();

                pdfView.setScaleX((float) x-1);
                pdfView.setScaleY((float) y-1);

            }
        });


        //this function read pdf from url
        //new RetrievePDFStream().execute(URI_SEGMENT_UPLOAD+"aass.pdf");


        //this function read pdf from bytes
        new RetrievePDFBytes().execute(URI_SEGMENT_UPLOAD+"mongodb.pdf");

        selectAreaToSign();

    }

    private void selectAreaToSign(){
        pdfView.stopFling();
        pdfView.clearFocus();
        pdfView.jumpTo(2);
       // pdfView.setEnabled(false);
        pdfView.clearAnimation();
        pdfView.clearFocus();

        boolean isFirstTime = true;
        if(isFirstTime){
            pdfView.zoomWithAnimation(0,1700,(float) 2.00);

        }else{
            resetAnimation();

            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pdfView.zoomWithAnimation(4500, 1700, (float) 2.00);
                }
            }, 1000);
        }

    }

    private void resetAnimation(){
        pdfView.resetZoomWithAnimation();
        pdfView.zoomTo(3);

    }

   /* @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;

        setTitle( String.format("%s %s/ %s", pageFileName, page+1, pageCount));
    }

    public void setTitle(String title) {
        title = title;
    }
*/

    private class RetrievePDFBytes extends AsyncTask<String , Void, byte[]>{

        @Override
        protected byte[] doInBackground(String... params) {
            InputStream inputStream = null;
            try{
                URL url = new URL(params[0]);
                HttpURLConnection  urlConnection = (HttpURLConnection)url.openConnection();
                if(urlConnection.getResponseCode()==200)

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
            }
            catch (IOException e){
                e.printStackTrace();
                return null;
            }
            try {
                return IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes){
            // super.onPostExecute(inputStream);
            pdfView.fromBytes(bytes)
                    .defaultPage(pageNumber)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableAnnotationRendering(true)
                    .load();
        }
    }





  /*  class RetrievePDFStream extends AsyncTask<String , Void, InputStream>{

        @Override
        protected InputStream doInBackground(String... params) {
            InputStream inputStream = null;
            try{
                URL url = new URL(params[0]);
                HttpURLConnection  urlConnection = (HttpURLConnection)url.openConnection();
                if(urlConnection.getResponseCode()==200)

                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
            }
            catch (IOException e){
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream){
           // super.onPostExecute(inputStream);
            pdfView.fromStream(inputStream).load();
        }



    }
*/

}
