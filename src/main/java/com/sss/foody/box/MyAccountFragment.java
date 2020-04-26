package com.sss.foody.box;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class MyAccountFragment extends Fragment {
    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    Appstatus appStatus = new Appstatus();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_myaccount, container, false);
        webView = (WebView) v.findViewById (R.id.myaccountView);
        swipeRefreshLayout = v.findViewById(R.id.accountSwipeRefresh);
        progressBar = (ProgressBar)v.findViewById(R.id.progressbaraccount);

        webView.setWebViewClient ( new MyWebViewClient () );

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //improve webView performance
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSaveFormData(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (appStatus.isOnline(getActivity())) {
                    webView.clearCache(true);
                    webView.reload();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    buildDialog(getActivity()).show();
                }
            }
        });
        if (appStatus.isOnline(getActivity())) {
            webView.loadUrl ( "https://foodybox.in/my-account/edit-account/" );
        } else {
            buildDialog(getActivity()).show();
        }


        webView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });
        return v;
    }
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Switch on Internet");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("MYACCOUNTFRAGMENT");
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });

        return builder;
    }
    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);

        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (appStatus.isOnline(getActivity())) {
                view.loadUrl(request.getUrl().toString());
            } else {
                buildDialog(getActivity()).show();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}