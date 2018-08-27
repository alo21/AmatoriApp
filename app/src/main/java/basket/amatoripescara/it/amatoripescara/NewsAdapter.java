package basket.amatoripescara.it.amatoripescara;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by aless on 25/05/2016.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    Context context;

    private List<News> newsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titolo;
        public ImageView immagine;

        public MyViewHolder(View view) {
            super(view);
            titolo = (TextView) view.findViewById(R.id.titolo);
            immagine = (ImageView) view.findViewById(R.id.immagine);

            context = view.getContext();


        }
    }


    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final News notizia = newsList.get(position);
        holder.titolo.setText(notizia.getTitolo());

        holder.immagine.setImageBitmap(notizia.getImmagine());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                // Converto l'immagine in un byteArray
                Bitmap bmp = notizia.getImmagine();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.WEBP, 50, stream);
                final byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent (v.getContext(), ArticleView_Activity.class);
                intent.putExtra("TITOLO", notizia.getTitolo());
                intent.putExtra("LINK_ARTICOLO", notizia.getLinkarticolo());
                intent.putExtra("IMMAGINE", byteArray);
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}