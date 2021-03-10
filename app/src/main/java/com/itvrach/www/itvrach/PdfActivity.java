package com.itvrach.www.itvrach;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itvrach.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PdfActivity extends AppCompatActivity {
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
    private List<User> mlist = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        askPermissions();
            mContentEditText = (EditText) findViewById(R.id.edit_text_content);
            mCreateButton = (Button) findViewById(R.id.button_create);
            mCreateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContentEditText.getText().toString().isEmpty()){
                        mContentEditText.setError("Body is empty");
                        mContentEditText.requestFocus();
                        return;
                    }
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            });



        }
    private void createPdfWrapper() throws FileNotFoundException,DocumentException{

   /*     int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                STORAGE_PERMISSION_REQUEST_CODE);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST_CODE);
            }
            return;
        }else {*/
            createPdf();
       // }
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
                        ActivityCompat.requestPermissions(PdfActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                STORAGE_PERMISSION_REQUEST_CODE);
                    }
                });
                builder.show();

            } //asking permission for first time
            else {
                // Show permission request popup for the first time
                ActivityCompat.requestPermissions(PdfActivity.this,
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
 /*   @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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
        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, output);
        writer.setPdfVersion(PdfWriter.VERSION_1_5);
        writer.setViewerPreferences(PdfWriter.PageModeFullScreen);

        document.open();
        document.add(new Paragraph(mContentEditText.getText().toString()));

        /*PdfPTable table = new PdfPTable(8);
        document.add(Chunk.NEWLINE);
        LineSeparator line = new LineSeparator(1, 100, null, Element.ALIGN_CENTER, -2);
        document.add(line);

        String imageUrl = URI_SEGMENT_UPLOAD+ "447d79660b997ce2007668426c5e8e44.jpg";

        try {
            Image image = Image.getInstance(new URL(imageUrl));
            image.scaleToFit((float)200.0, (float)200.0);
            //image.scaleAbsolute(150f, 150f);
            document.add(image);

        } catch (IOException e) {
            e.printStackTrace();
        }


        document.add(new Paragraph("ITvrach Details Report",FontFactory.getFont(FontFactory.TIMES_BOLD, 18, Font.BOLD, BaseColor.BLUE)));
        document.add(new Paragraph(new Date().toString()));
        document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------"));

        PdfPCell cell = new PdfPCell(new Paragraph("Supplier Information"));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.CYAN);

        String fullname;
        User user = new User();

        fullname = user.getFullname()


        table.addCell("Supplier ID");
        table.addCell(user.setFullname());
        table.addCell("Supplier Name");
        table.addCell("Address");
        table.addCell("Contact Info");

        mlist =
*/
        PdfPTable table = new PdfPTable(3);

        table.setWidthPercentage(100);

        ArrayList<User> list = new ArrayList<>();
        PdfPCell c1 = new PdfPCell(new Phrase("Site Address"));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setColspan(1);
        c1.setBorderWidth(1);
        c1.setPaddingBottom(10);
        c1.setBorderColor(BaseColor.BLACK);
        table.addCell(c1);

        List<User> newList = new ArrayList<User>();
        /*List<Screening> screenings = PojoFactory.getScreenings(connection, day);
        Movie movie;*/
        for (User user : newList) {

       // for(int i=0;i<newList.size();i++) {

            String temp1 = user.getFirstname();
            String temp2 = user.getEmail();
            table.addCell(temp1);
            table.addCell(temp2);
            document.add(table);

        }

        addMetaData(document);
        addTitlePage(document);
        addContent(document);

        document.close();
        previewPdf();



    }

    private void add_image(){
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.lock_icon);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image img = null;
        byte[] byteArray = stream.toByteArray();
        try {
            img = Image.getInstance(byteArray);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
        document.addHeader("Nothing", "No Header");
    }

    private static void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);

    }

    private static void createList(Section subCatPart) {
        List list = new ListItem();
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add((Element) list);
    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

       /* table.setBorderColor(BaseColor.GRAY);
        table.setPadding(4);
        table.setSpacing(4);
        table.setBorderWidth(1);*/

        PdfPCell c1 = new PdfPCell(new Phrase("No."));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Item Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Price"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");

        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");
        table.addCell("1");
        table.addCell("Java With Android");
        table.addCell("$300");

        table.addCell("2");
        table.addCell("PHP with Codeigniter");
        table.addCell("$200");

        subCatPart.add(table);

    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }


    private static void addTitlePage(Document document)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Title of the document", catFont));

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

        document.add(preface);
        // Start a new page
        document.newPage();
    }



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
}
