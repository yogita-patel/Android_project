package com.example.qrcodescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    EditText qrgtext;
    static TextView result_text;
Button btngenerate,btnscan;
ImageView code;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null && result.getContents() != null)
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Scanned result")
                    .setMessage(result.getContents())
                    .setPositiveButton("copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClipboardManager clipboardManager=(ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData data= ClipData.newPlainText("result",result.getContents());
                            clipboardManager.setPrimaryClip(data);
                        }
                    }).setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrgtext = (EditText)findViewById(R.id.qrgtext);
        code = (ImageView)findViewById(R.id.code);
        //result_text = (TextView)findViewById(R.id.result_text);
        btngenerate = (Button)findViewById(R.id.btngenerate);
        btnscan = (Button)findViewById(R.id.btnscan);
        btngenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = qrgtext.getText().toString();
                if(text != null && !text.isEmpty())
                {
                    MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix=multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                        code.setImageBitmap(bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter text first to genrate QR code",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator=new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });


    }
}
