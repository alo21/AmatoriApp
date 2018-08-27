package basket.amatoripescara.it.amatoripescara;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by aless on 03/08/2016.
 */

// JobService per API > 21

@TargetApi(21)
public class NotificationArticle extends JobService {

    private String last_link = "";
    private String SharedPrefFile = "basket.amatoripescara.it.amatoripescara.keypref";
    private String article_found;
    private String title_article;
    private boolean internet_response;
    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    ParseFirstPage FirstPage = new ParseFirstPage();

    @Override
    public boolean onStartJob(JobParameters params) {

        Context mContext = getApplicationContext();
        SharedPreferences mPrefs = mContext.getSharedPreferences(SharedPrefFile, Context.MODE_PRIVATE);
        last_link = mPrefs.getString("ultimo_link", "");

        // Garantisco i permessi di connettersi a un sito web
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        internet_response = hasActiveInternetConnection();
        if(internet_response == true) {
            FirstPage.execute(mPrefs);
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }

    // Controllo se il telefono è conesso a una rete (mobile o wifi)
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    // Controllo se la rete ha accesso a internet
    public boolean hasActiveInternetConnection() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
            } catch (IOException e) {
            }
        } else {
            Log.d("Error", "No network available!");
        }
        return false;
    }

    class ParseFirstPage extends AsyncTask<SharedPreferences,Void,SharedPreferences> {

        Element articolo;
        Element ttr_content_margin;

        @Override
        protected SharedPreferences doInBackground(SharedPreferences... params){

            Document doc;
            String pagina = "http://www.amatoripallacanestrope.it/?page_id=4059&paged=1";
            
            try {

                // Connect to specified link
                doc = Jsoup.connect(pagina).timeout(0).get();

                //Retrieve the specified div
                ttr_content_margin = doc.getElementById("ttr_content_margin");
                articolo = ttr_content_margin.getElementsByClass("articolo").first();

                article_found = articolo.select("a").text();
                title_article = articolo.select("h1").text();

            }



            catch (IOException e){
                e.printStackTrace();
            }
            return params[0];

        }

        @Override
        protected void onPostExecute(SharedPreferences pre) {


            if(((last_link.equals(article_found)) == false) && article_found != "") {

                NotificationCompat.Builder n = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle("E' stato pubblicato un nuovo articolo!")
                        .setContentText(title_article)
                        .setSound(uri)
                        .setLights(Color.RED, 3000, 3000)
                        .setVibrate(new long[] { 500, 500})
                        .setSmallIcon(R.drawable.logo_notify);

                pre.edit().putString("ultimo_link", article_found).apply();

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0, n.build());
            }
        }


    }


}
