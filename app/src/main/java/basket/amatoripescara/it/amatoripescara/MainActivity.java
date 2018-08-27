package basket.amatoripescara.it.amatoripescara;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<News> newsList = new ArrayList<News>();
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private NewsAdapter mAdapter;

    private URL url;

    private int pageNumber = 1;
    private static int NUMBER_POST_BOTTOM = 2;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private String SharedPrefFile = "basket.amatoripescara.it.amatoripescara.keypref";
    private String DeviceToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Se api >= 21, avvio JobScheduler



        if(Build.VERSION.SDK_INT >= 21) {

            JobScheduler mJobScheduler = (JobScheduler)getSystemService( Context.JOB_SCHEDULER_SERVICE );

            ComponentName componentName = new ComponentName(getApplicationContext(), NotificationArticle.class);

            // Controllo nuovi articoli ogno 1 ora
            JobInfo jobInfo = new JobInfo.Builder(1, componentName).setPersisted(true).setPeriodic(3600000).build();

            mJobScheduler.schedule(jobInfo);
        }

        // Parte dove inizia la home con le news

        ParseAllArticles AllArticles = new ParseAllArticles();
        AllArticles.execute();




        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(mAdapter);





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void refreshItems(int pageNum) {
        new ParseAllArticles().execute();
    }


    // Fetching all articles with jsoup
    class ParseAllArticles extends AsyncTask<Void,Void,Void> {

        Elements articoli;
        Element ttr_content_margin;
        Bitmap bmp;
        Intent spl = new Intent(MainActivity.this, splash.class);

        @Override
        protected Void doInBackground(Void... params){

            Document doc;
            String pagina = "http://www.amatoripallacanestrope.it/";

            try {

                // Connect to specified link
                doc = Jsoup.connect(pagina).timeout(0).get();

                //Retrieve the specified div
                ttr_content_margin = doc.getElementById("cycloneslider-articoli-1");
                Elements articoli = ttr_content_margin.getElementsByClass("cycloneslider-caption-more");
                Elements articoli_titoli = ttr_content_margin.getElementsByClass("cycloneslider-caption-title");
                Element titolo_articolo;


                for(Element articolo : articoli){

                    News notizia = new News();
                    notizia.setTitolo(articolo.select("img").attr("title"));
                    notizia.setLinkarticolo(articolo.select("a").attr("href"));

                    if(articolo.select("img").size()>0) {

                        url = new URL(articolo.select("img").attr("src"));
                    }

                    else{
                        url = null;
                    }

                    if(url != null) {
                        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    }

                    else{
                         bmp = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.drawable.amatori_splash);
                    }

                    notizia.setImmagine(bmp);
                    newsList.add(notizia);


                }

            }



            catch (IOException e){
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result){

            // Close the splash activity
            if(pageNumber==1) {
                finishActivity(1);
            }

            mAdapter.notifyDataSetChanged();
            loading = true;


        }

        @Override
        protected  void onPreExecute()
        {

            // Start splash activity only when start the app
            if(pageNumber==1) {
                startActivityForResult(spl, 1);
            }

        }
    }

    //#######################################################################


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            // Handle the camera action
        } else if (id == R.id.nav_societa) {
            Intent intent = new Intent(this, societa.class);
            this.startActivity(intent);

        } else if(id == R.id.nav_archivio) {
            Intent intent = new Intent(this, Archivio.class);
            this.startActivity(intent);

        } else if (id == R.id.nav_dove_siamo) {
            Intent intent = new Intent(this, dove_siamo.class);
            this.startActivity(intent);

        } else if (id == R.id.nav_segnalazioni) {
            Intent intent = new Intent(this, segnalazioni.class);
            this.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
