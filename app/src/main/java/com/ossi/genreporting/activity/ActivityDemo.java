package com.ossi.genreporting.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.ossi.genreporting.R;

public class ActivityDemo extends AppCompatActivity {
    String url = "https://genesiscloudapps.com/genreporting/MobileSalarySlip.aspx?Monthno=06&YearNo=2023&Sno=61";
    public static PdfGenerator.XmlToPDFLifecycleObserver xmlToPDFLifecycleObserver;
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_salary_slip);
        xmlToPDFLifecycleObserver = new PdfGenerator.XmlToPDFLifecycleObserver((ComponentActivity) this);
        getLifecycle().addObserver(xmlToPDFLifecycleObserver);

        webView = findViewById(R.id.view_salary);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);

        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPdf(ActivityDemo.this, webView);
            }
        });
    }



    public static void downloadPdf(Context context, View view) {
        PdfGenerator.getBuilder()
                .setContext((ComponentActivity) context)
                .fromViewSource()
                .fromView(view)
                .setFileName("Salary Slip-PDF")
                .setFolderNameOrPath("FolderA")
                .savePDFSharedStorage(xmlToPDFLifecycleObserver)
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                    }

                    @Override
                    public void onStartPDFGeneration() {
                    }

                    @Override
                    public void onFinishPDFGeneration() {
                    }

                    @Override
                    public void showLog(String log) {
                        super.showLog(log);
                        Log.e("error::", log);
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                    }
                });
    }

}
