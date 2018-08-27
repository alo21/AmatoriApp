package basket.amatoripescara.it.amatoripescara;

import android.graphics.Bitmap;

/**
 * Created by aless on 25/05/2016.
 */
public class News {

    private String mTitolo;
    private String mLinkarticolo;
    private Bitmap mImmagine;

    public String getTitolo() {
        return mTitolo;
    }

    public void setTitolo(String mTitolo) {
        this.mTitolo = mTitolo;
    }

    public String getLinkarticolo() {
        return mLinkarticolo;
    }

    public void setLinkarticolo(String mLinkarticolo) {
        this.mLinkarticolo = mLinkarticolo;
    }

    public Bitmap getImmagine() {
        return mImmagine;
    }

    public void setImmagine(Bitmap mImmagine) {
        this.mImmagine = mImmagine;
    }

}
