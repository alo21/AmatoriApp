package basket.amatoripescara.it.amatoripescara;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Archivio extends AppCompatActivity {

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
        setContentView(R.layout.activity_archivio);



        // Parte dove inizia la home con le news

        ParseAllArticles AllArticles = new ParseAllArticles();
        AllArticles.execute();




        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(Archivio.this);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(mAdapter);

        // ##### OnScrollListener ####################




        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        // if number of post from bottom = 2, then load new page.
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount - NUMBER_POST_BOTTOM)

                        {
                            loading = false;
                            pageNumber++;
                            refreshItems(pageNumber);
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });








        //#####################################



       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }

    public void refreshItems(int pageNum) {
        new ParseAllArticles().execute();
    }


    // Fetching all articles with jsoup
    class ParseAllArticles extends AsyncTask<Integer,String,String> {

        Elements articoli;
        Element ttr_content_margin;
        Bitmap bmp;

        @Override
        protected String doInBackground(Integer... params){

            Document doc;
            String pagina = "http://www.amatoripallacanestrope.it/?page_id=4059&paged=" + pageNumber;

            try {

                // Connect to specified link
                doc = Jsoup.connect(pagina).timeout(0).get();

                //Retrieve the specified div
                ttr_content_margin = doc.getElementById("ttr_content_margin");
                articoli = ttr_content_margin.getElementsByClass("articolo");

                for(Element articolo : articoli){

                    News notizia = new News();
                    notizia.setTitolo(articolo.select("h1").text());
                    notizia.setLinkarticolo(articolo.select("a").text());

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

                publishProgress();


            }



            catch (IOException e){
                e.printStackTrace();
            }

            return null;

        }

        protected void onProgressUpdate(String... values){

            super.onProgressUpdate(values);
            TextView Caricmanto = (TextView) findViewById(R.id.caricamento);
            Caricmanto.setVisibility(View.GONE);



        }

        @Override
        protected void onPostExecute(String result){

            // Close the splash activity
            /*if(pageNumber==1) {
                finishActivity(1);
            }*/

            mAdapter.notifyDataSetChanged();
            loading = true;


        }

        @Override
        protected  void onPreExecute()
        {

            // Start splash activity only when start the app
            //if(pageNumber==1) {
                //startActivityForResult(spl, 1);
            //}

        }
    }

    //#######################################################################


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    }