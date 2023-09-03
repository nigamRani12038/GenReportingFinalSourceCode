package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.ComponentActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;

public class ViewSalarySlip extends AppCompatActivity implements View.OnClickListener {
    WebView view_salary;
    String month, year, user_id;
    ImageView download;


    Context context;
    public static PdfGenerator.XmlToPDFLifecycleObserver xmlToPDFLifecycleObserver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_salary_slip);

        SharedPreferences pref = getSharedPreferences("my_pref", MODE_PRIVATE);
        user_id = pref.getString("user_id", null);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            year = bundle.getString("Year");
            month = bundle.getString("Month");
        }

        xmlToPDFLifecycleObserver = new PdfGenerator.XmlToPDFLifecycleObserver((ComponentActivity) this);
        getLifecycle().addObserver(xmlToPDFLifecycleObserver);


        view_salary = findViewById(R.id.view_salary);
        download=findViewById(R.id.download);

        if (Util.isNetworkAvailable(this)) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("genesiscloudapps.com")
                    .appendPath("genreporting")
                    .appendPath("MobileSalarySlip.aspx")
                    .appendQueryParameter("Monthno", month)
                    .appendQueryParameter("YearNo", year)
                    .appendQueryParameter("Sno", user_id);
            String myUrl = builder.build().toString();
            //web.loadUrl("https://genesiscloudapps.com/genreporting/MobileSalarySlip.aspx?Monthno="+Month_str+"&YearNo="+year_str+"&Sno="+user_id);
            view_salary.loadUrl(myUrl);
            view_salary.getSettings().setJavaScriptEnabled(true);
            view_salary.setWebViewClient(new WebViewClient());
            view_salary.getSettings().setLoadWithOverviewMode(true);
            view_salary.getSettings().setUseWideViewPort(true);
            view_salary.getSettings().setBuiltInZoomControls(true);
        }
        else {
            Toast.makeText(this, "Please Check you internet Connection", Toast.LENGTH_SHORT).show();
        }

        download.setOnClickListener(this);
    }

    public void onBackPressed() {
        if (view_salary.canGoBack()) {
            view_salary.goBack();
        } else {
            onBackPressed();
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.download:
                downloadPdf(this,view_salary);
                break;
        }

    }

    public static void downloadPdf(Context context, View view) {
        PdfGenerator.getBuilder()
                .setContext((ComponentActivity) context)
                .fromViewSource()
                .fromView(view)
                .setFileName("Salary_Slip")
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
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                    }
                });
    }

}