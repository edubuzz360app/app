package com.sss.foody.box;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.net.URISyntaxException;


public class SubscriptionFragment extends Fragment {

    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    String url;
    Intent intent;
    Appstatus appStatus = new Appstatus();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        webView = (WebView) v.findViewById (R.id.chatView);
        progressBar = (ProgressBar)v.findViewById(R.id.progressbarchat);

        swipeRefreshLayout = v.findViewById(R.id.chatSwipeRefresh);
        webView.getSettings ().setJavaScriptEnabled ( true );
        webView.setWebViewClient ( new MyWebViewClient () );
        webView.clearCache(true);
        webView.clearHistory();
        if (appStatus.isOnline(getActivity())) {
            webView.loadUrl ( "https://foodybox.in/subscription/" );
        } else {
            buildDialog(getActivity()).show();
        }

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
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("SUBSCRIPTIONFRAGMENT");
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
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);

        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("intent")) {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                    if (fallbackUrl != null) {
                        view.loadUrl(fallbackUrl);
                        return true;
                    }
                } catch (URISyntaxException e) {
                    Toast.makeText(getActivity(), "Error Loading Page...", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (url.startsWith("https://wa.me")) {
                view.stopLoading();
                try {

                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url.replace("+",""))));

                    return true;

                } catch (android.content.ActivityNotFoundException ex) {

                    String MakeShortText = "Whatsapp have not been installed in your phone";

                    Toast.makeText(getActivity(), MakeShortText, Toast.LENGTH_SHORT).show();
                }

            } else if (url.startsWith("http") || url.startsWith("https")) {
                if (appStatus.isOnline(getActivity())) {
                    view.loadUrl(url);
                } else {
                    buildDialog(getActivity()).show();
                }
                return true;
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