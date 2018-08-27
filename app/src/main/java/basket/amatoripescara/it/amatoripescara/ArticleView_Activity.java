package basket.amatoripescara.it.amatoripescara;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class ArticleView_Activity extends AppCompatActivity {

    private Bitmap bmp;

    private InputStream link, res2;

    private String linkArticolo;
    private String articolo = "";
    int img = 1;
    URL url;

    Bitmap resizedBitmap;

    private LinearLayout myLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        myLayout = (LinearLayout)findViewById(R.id.linear_layout);


        TextView TVtitolo = (TextView)findViewById(R.id.titolo);
        ImageView IVimmagine = (ImageView)findViewById(R.id.immagine);

        Bundle bundle = getIntent().getExtras();

        linkArticolo = bundle.getString("LINK_ARTICOLO");

        TVtitolo.setText(bundle.getString("TITOLO"));

        // Ri-converto byteArray in bitmap
        byte[] byteArray =bundle.getByteArray("IMMAGINE");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        IVimmagine.setImageBitmap(bmp);


        ParseAllArticles AllArticles = new ParseAllArticles();
        AllArticles.execute();


    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 6;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public final Bitmap decodeSampledBitmapFromResource(InputStream res, InputStream res2, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(res, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;


        try {
            res.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(res2, null, options);
    }




    class ParseAllArticles extends AsyncTask<Integer,String,String> {

        Elements paragrafi, header1, header2, header3, header4, header5, header6;

        @Override
        protected String doInBackground(Integer... params){

            Document doc;
            try {

                // Connect to specified link
                doc = Jsoup.connect(linkArticolo).timeout(0).get();


                //Retrieve the specified div
                paragrafi = doc.getElementsByClass("ttr_article").select("p, h1, h2, h3, h4, h5, h6");

                for(Element paragrafo : paragrafi) {

                        articolo = articolo + "\n\n" + paragrafo.text();
                        img = 0;

                    publishProgress();

                }

            }



            catch (IOException e){
                e.printStackTrace();
            }



            return "Executed";

        }

        protected void onProgressUpdate(String... values){

            super.onProgressUpdate(values);

            TextView TVarticolo = (TextView) findViewById(R.id.articolo);
            TVarticolo.setText(articolo);



        }

        protected void onPostExecute(String res_titolo){

            if(linkArticolo.toString().equals("http://www.amatoripallacanestrope.it/?p=4279")) {

                TextView allerta = new TextView(getApplicationContext());
                allerta.setText("PER PRENOTARE IL TUO ABBONEMANTO ACCEDI AL SITO TRAMITE BROWSER");
                allerta.setTextSize(20);
                allerta.setPadding(0,0,0,16);
                allerta.setGravity(Gravity.CENTER);
                allerta.setTextColor(Color.BLACK);
                myLayout.addView(allerta);
            }
        }

        protected void onPreExecuted(String res){

        }

    }

}
