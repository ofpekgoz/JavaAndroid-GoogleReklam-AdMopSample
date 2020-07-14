package com.omerfpekgoz.reklamuygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class OyunActivity extends AppCompatActivity {

    private TextView txtOyunEkrani, txtSonuc;
    private Button btnPuanKazan,btnSonrakiBolum;

    private AdView banner;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;

    private CountDownTimer downTimer;  //Sayaç
    private int puan=0;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyun);

        MobileAds.initialize(this,"ca-app-pub-9805287779572710~4577807289");  //Uygulama id

        txtOyunEkrani=findViewById(R.id.txtOyunEkrani);
        txtSonuc=findViewById(R.id.txtSonuc);
        btnPuanKazan=findViewById(R.id.btnPuanKazan);
        btnSonrakiBolum=findViewById(R.id.btnSonrakiBolum);
        banner=findViewById(R.id.banner);


        banner.loadAd(new AdRequest.Builder().build());

        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");  //interstitial test id si
        interstitialAd.loadAd(new AdRequest.Builder().build());

        rewardedVideoAd=MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",new AdRequest.Builder().build()); //Reward Test id si

        //Butonlar ilk olarak görünmez olacak
        btnPuanKazan.setVisibility(View.INVISIBLE);
        btnSonrakiBolum.setVisibility(View.INVISIBLE);


        downTimer=new CountDownTimer(5000,1000) {   //toplam:5 sn ,aralık:1 sn
            @Override
            public void onTick(long millisUntilFinished) {

                txtSonuc.setText("Kalan Süre: "+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {    //Geri sayım bittiğinde
                puan=10;
                txtSonuc.setText("Oyun Bitti \nToplam Puan: "+puan);
                btnSonrakiBolum.setVisibility(View.VISIBLE);  //Buton görünür olacak

            }
        };

        downTimer.start();


        btnSonrakiBolum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (puan>=30){
                    if (interstitialAd.isLoaded()){
                        interstitialAd.show();

                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Sonraki Bölüme Geçmek için 30 Puan Gereklidir.",Toast.LENGTH_LONG).show();
                    btnPuanKazan.setVisibility(View.VISIBLE);  //Butonu görünür kıldık

                }
            }
        });

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                startActivity(new Intent(OyunActivity.this,SonrakiBolumActivity.class));
                finish();
            }
        });



        btnPuanKazan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedVideoAd.isLoaded()){
                    rewardedVideoAd.show();    //Video izlenecek

                }
            }
        });

        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

                puan=puan+20;
                txtSonuc.setText("Oyun Bitti \nToplam Puan: "+puan);
                btnPuanKazan.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });


    }

    @Override
    protected void onResume() {
        rewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        rewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        rewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}
