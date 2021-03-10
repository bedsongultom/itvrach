package com.itvrach.www.itvrach;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itvrach.adapter.CustomAdapter;
import com.itvrach.model.ResponseModel;
import com.itvrach.model.User;
import com.itvrach.services.APIClient;
import com.itvrach.services.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;


public class SearchActivity extends AppCompatActivity{

    private EditText editTextSearch;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    /*private RecyclerView.Adapter mAdapter;*/
    private RecyclerView.LayoutManager mManager;
    private List<User> usersList = new ArrayList<>();
    private CustomAdapter adapter;
    //private boolean add = false;
    private Paint p = new Paint();


    private static final String TAG = "PdfCreatorActivity";
    private EditText  mContentEditText;
    private Button mCreateButton;
    private File pdfFile;
    final private int STORAGE_PERMISSION_REQUEST_CODE = 1;

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    private String imageUrl = URI_SEGMENT_UPLOAD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);

       /* SelfNoteFragment noteFragment = SelfNoteFragment.newInstance();

        android.support.v4.app.FragmentTransaction fragTransaction= this.getSupportFragmentManager().beginTransaction();
        fragTransaction.replace(R.id.container, noteFragment);
        fragTransaction.commit();*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //Intent intent = new Intent(getApplicationContext(), PdfActivity.class);
                //startActivity(intent);

                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            }
        });


        initSwipe();

        UserService userService = APIClient.getClient().create(UserService.class);
        Call<ResponseModel> call = userService.getFindAll();
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                usersList = response.body().getResult();
                adapter = new CustomAdapter(usersList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                //Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(SearchActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
            }
        });


        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editTextSearch) {
                filter(editTextSearch.toString());
            }
        });
    }

    private void createPdfWrapper() throws FileNotFoundException,DocumentException {
         createPdf();
    }

    private void askPermissions() {

        int permissionCheckStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // we already asked for permisson & Permission granted, call camera intent
        if (permissionCheckStorage == PackageManager.PERMISSION_GRANTED) {

            //do what you want

        } else {

            // if storage request is denied
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You need to give permission to access storage in order to work this feature.");
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        // Show permission request popup
                        ActivityCompat.requestPermissions(SearchActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                STORAGE_PERMISSION_REQUEST_CODE);
                    }
                });
                builder.show();

            } //asking permission for first time
            else {
                // Show permission request popup for the first time
                ActivityCompat.requestPermissions(SearchActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST_CODE);

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // check whether storage permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //do what you want;
                    }
                }
                break;
            default:
                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

     class HeaderFooterPageEvent extends PdfPageEventHelper {
         public void onStartPage(PdfWriter writer, Document document) {
             try {
                // PdfContentByte cb = writer.getDirectContent();
                 Image imgHeader = Image.getInstance(new URL(imageUrl+"d374972de7932bbbfeb2efc8fc0925d9.jpg"));
                 imgHeader.scaleToFit(100,100);
                 imgHeader.setAbsolutePosition(20, 720);
                 document.add(imgHeader);

                // String TxtHeader= " PT.ITVRACH INDONESIA";
                 //Jl.Bhakti IV No.11 Arengka Pekanbaru" +
                   //      " \n E-mail:info@itvrach.com Hp:0813-7889-0333";

                 ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase("PT.ITVRACH INDONESIA\n", new Font(Font.FontFamily.HELVETICA, 16,  Font.BOLD)),110, 780, 0);
                 ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase("Jl.Bhakti IV No.11 Arengka Pekanbaru\n", new Font(Font.FontFamily.HELVETICA, 16,  Font.BOLD)),110, 760, 0);
                 ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase("E-mail:info@itvrach.com Hp:0813-7889-0333 \n\n", new Font(Font.FontFamily.HELVETICA, 16,  Font.BOLD)), 110, 740, 0);
                 LineSeparator ls = new LineSeparator();
                 ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_LEFT, new Phrase(new Chunk(ls)), 0, 717, 0);



                 /*ColumnText ct = new ColumnText(writer.getDirectContent());
                 ct.setSimpleColumn(20,20,20,20);
                 ct.setText(new Phrase("String"));
                 ct.go();
*/

                 // ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("Top Right"), 550, 800, 0);

               //  LineSeparator ls = new LineSeparator();
                 //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(new Chunk(ls)), 550, 800, 0);
               //  LineSeparator ls = new LineSeparator();
                 //document.add(Chunk.NEWLINE);

                 //document.add();


                // ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(new Chunk(ls)), 550, 800, 0);



               /*  //document.Add(image);
                 String TxtHeader= " PT.ITVRACH INDONESIA\n Jl.Bhakti IV No.11 Arengka Pekanbaru" +
                         " \n E-mail:info@itvrach.com Hp:0813-7889-0333";
                 document.add(new Phrase(TxtHeader));*/




                /* Paragraph p = new Paragraph(TxtHeader,new Font(Font.FontFamily.HELVETICA, 16,  Font.BOLD));
                 p.setAlignment(Element.ALIGN_LEFT);
                 p.setIndentationLeft(85);*/
                // ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(TxtHeader), 60, 720,0);

                // document.add(new Paragraph());
               /* ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(new Chunk(ls)), 60, 720, 0);
                 document.add(new Paragraph());
                 document.add(new Paragraph());
                */




                 //document.add(new Paragraph());

             } catch (Exception x) {
                 x.printStackTrace();
             }




            // ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("PT.ITVRACH INDONESIA\n JL.ARIFIN AHMAD NO.11\n ARENGKA-P.BARU 28282\n Hp.0813-7889-0333\n--------------------------------------------"),550,1000,0);
            // ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase("Top Right"), 550, 800, 0);

             //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Top Right"), 550, 800, 0);

         }

         public void onEndPage(PdfWriter writer, Document document) {


             ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("http://www.itvrach.com/"), 110, 20, 0);
             ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("page " + document.getPageNumber()), 550, 20, 0);
             LineSeparator ls = new LineSeparator();
             ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_BOTTOM, new Phrase(new Chunk(ls)), 0, 40, 0);
         }
     }

    private void createPdf() throws FileNotFoundException, DocumentException {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        pdfFile = new File(docsFolder.getAbsolutePath() + timeStamp + ".pdf");

        OutputStream output = new FileOutputStream(pdfFile);
       // Document document = new Document();
        float left = 20;
        float right = 20;
        float top = 130;
        float bottom = 20;
        Document document = new Document(PageSize.A4, left, right, top, bottom);
        PdfWriter writer = PdfWriter.getInstance(document, output);
        HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        writer.setPageEvent(event);
        writer.setPdfVersion(PdfWriter.VERSION_1_5);
        writer.setViewerPreferences(PdfWriter.PageModeFullScreen);
        document.open();
       // document.setMargins(left, right, 30, bottom);




     /*   Paragraph para = new Paragraph("The Raven");
        // .setFontSize(20f);
        para.setRole(H1);
        document.add(para);
        document.add(new Paragraph("Once upon a midnight dreary\nWhile I pondered weak and weary\nOver many a quaint and curious volume\nOf forgotten lore"));
*/
        //document.add(new Phrase("\n"));

      //  document.add(new Paragraph());

        PdfPTable table= new PdfPTable(6);
        table.setSpacingBefore(1);
       // table.setSpacingAfter();
        PdfPCell cell = new PdfPCell(new Paragraph("Supplier Information"));
        cell.setColspan(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.ORANGE);

        table.addCell(cell);
        table.addCell("No");
        table.addCell("Username");
        table.addCell("File");
        table.addCell("Fullname");
        table.addCell("Address");
        table.addCell("E-mail");


     /*   for(User users: usersList ) {
            int No=0;*/



        for (int i =0 ; i < usersList.size(); i++ ) {
                table.addCell(new Paragraph(String.valueOf((i+1))));
                table.addCell(new Paragraph(usersList.get(i).getUsername().toString()));

            try {
                Image img = Image.getInstance(new URL(imageUrl+usersList.get(i).getFile()));
                img.scaleAbsolute(100, 100);
                table.addCell(img);

            } catch (IOException e) {
                e.printStackTrace();
            }

                table.addCell(new Paragraph(usersList.get(i).getFullname().toString()));
                table.addCell(new Paragraph(usersList.get(i).getPlace().toString()));
                table.addCell(new Paragraph(usersList.get(i).getDateofbirth().toString()));

        }

        table.setHeaderRows(2);
        document.add(table);
        table.setSpacingAfter(1);

        addMetaData(document);
        document.close();
        previewPdf();

    }

    private static void addMetaData(Document document) {
        document.addTitle("PDF Dynamic");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Bedson Gultom");
        document.addCreator("Bedson Gultom");
        document.addHeader("Nothing", "No Header");

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

/*
    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
      */
/*  preface.add(new Paragraph("Title of the document", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(
                "Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph(
                "This document describes something which is very important ",
                smallBold));

        addEmptyLine(preface, 8);

        preface.add(new Paragraph(
                "This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
                redFont));
*//*

        document.add(preface);
        // Start a new page
        document.newPage();
    }

*/

    private void previewPdf() {
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");

            startActivity(intent);
        }else{
            Toast.makeText(this,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }
    }





    private void initSwipe(){
            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0
                    , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {
                    final int position = viewHolder.getAdapterPosition();
                    if (direction == ItemTouchHelper.LEFT){

                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                        builder.setTitle("Are you sure to Remove this data?")
                                //.setMessage("----------------------------------------------------------")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                       User item = usersList.get(position);
                                       final int user_id = item.getUser_id();
                                       UserService userService = APIClient.getClient().create(UserService.class);
                                        Call<ResponseModel> call = userService.delete(user_id);
                                        call.enqueue(new Callback<ResponseModel>() {
                                            @Override
                                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                ResponseModel responModel = response.body();
                                                Toast.makeText(SearchActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();
                                                adapter.removeItem(position);
                                            }
                                            @Override
                                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                              //  Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                Toast.makeText(SearchActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                        //SwipeableItemViewHolder.setSwipeItemHorizontalSlideAmount()
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                        builder.setTitle("Are you sure to Edit this data?")
                                //.setMessage("----------------------------------------------------------")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        User item = usersList.get(position);
                                        adapter.editItem(position);
                                        adapter.notifyItemChanged(position);
                                        Intent mIntent = new Intent(SearchActivity.this, EditProfileActivity.class);
                                        mIntent.putExtra("user_id",  item.getUser_id());
                                        mIntent.putExtra("username", item.getUsername());
                                        mIntent.putExtra("password", item.getPassword());
                                        mIntent.putExtra("type",item.getType());
                                        mIntent.putExtra("firstname",item.getFirstname());
                                        mIntent.putExtra("lastname", item.getLastname());
                                        mIntent.putExtra("fullname", item.getFullname());
                                        mIntent.putExtra("place",    item.getPlace());
                                        mIntent.putExtra("dateofbirth", item.getDateofbirth());
                                        mIntent.putExtra("gender", item.getGender());
                                        mIntent.putExtra("email", item.getEmail());
                                        mIntent.putExtra("marital_status", item.getMarital_status());
                                        mIntent.putExtra("religions", item.getReligions());
                                        mIntent.putExtra("age", item.getAge());
                                        mIntent.putExtra("salary", item.getSalary());
                                        startActivity(mIntent);

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                        //SwipeableItemViewHolder.setSwipeItemHorizontalSlideAmount()
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    /*    usersList = position;
                        AlertDialog.setTitle("Edit Country");
                        usersList.getClass(User.class).get(position));
                        AlertDialog.show();*/
                    }
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                    //final TextView textView = null;
                    Bitmap icon;
                    if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;

                        if(dX > 0){
                            p.setColor(Color.parseColor("#303F9F"));
                            RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                            c.drawRect(background,p);

                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                            RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                            c.drawBitmap(icon,null,icon_dest,p);
                           //  textView.setText("DELETE");
                        } else {
                            p.setColor(Color.parseColor("#f39c12"));//D32F2F//f39c12
                            RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                            c.drawRect(background,p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                            c.drawBitmap(icon,null,icon_dest,p);
                            //textView.setText("EDIT");
                        }
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        /*private void removeView(){
            if(view.getParent()!=null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }*/

        /*ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                moveItem(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);*/





    /*void moveItem(int oldPos, int newPos){
        User user = usersList.get(oldPos);

        usersList.remove(oldPos);
        usersList.add(newPos, user);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    void deleteItem(final int position){
        usersList.remove(position);
        adapter.notifyItemRemoved(position);
    }*/




    public void filter(String newText) {
        newText = newText.toLowerCase();
        List<User> newList = new ArrayList<>();
        for (User list : usersList){
            String name = list.getUsername().toLowerCase();
            if (name.contains(newText)){
                newList.add(list);
            }
        }
        adapter.setFilter(newList);
    }


}



