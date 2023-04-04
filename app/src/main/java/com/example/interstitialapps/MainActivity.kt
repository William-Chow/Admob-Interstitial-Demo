package com.example.interstitialapps

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class MainActivity : AppCompatActivity() {

    private var tvClick: TextView? = null
    private var btnClick: Button? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "Ads"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvClick = findViewById(R.id.tvClick)
        btnClick = findViewById(R.id.btnClick)
        btnClick?.setOnClickListener {
            showInterstitialAds()
        }
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this) { }
        MobileAds.setRequestConfiguration(RequestConfiguration.Builder().setTestDeviceIds(listOf("6C9450E472AD6147137E786AFE1A5A23")).build())
        loadAds()
    }

    private fun loadAds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            "ca-app-pub-4451186638084451/7655287919",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    Log.i(TAG, "onAdLoaded")
                    Toast.makeText(this@MainActivity, "onAdLoaded()", Toast.LENGTH_SHORT).show()
                    interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null
                            Log.d("TAG", "The ad was dismissed.")
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Called when fullscreen content failed to show.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null
                            Log.d("TAG", "The ad failed to show.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            Log.d("TAG", "The ad was shown.")
                        }
                    }
                    mInterstitialAd?.show(this@MainActivity)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error
                    Log.i(TAG, loadAdError.message)
                    mInterstitialAd = null
                    val error = String.format(
                        "domain: %s, code: %d, message: %s",
                        loadAdError.domain, loadAdError.code, loadAdError.message
                    )
                    Toast.makeText(
                        this@MainActivity, "onAdFailedToLoad() with error: $error", Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun showInterstitialAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
            tvClick?.text = "Ads was loaded"
        } else {
            tvClick?.text = "Ads was not loaded"
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
            if (mInterstitialAd == null) {
                loadAds();
            }
        }
    }
}