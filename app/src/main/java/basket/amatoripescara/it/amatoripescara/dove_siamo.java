package basket.amatoripescara.it.amatoripescara;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;


public class dove_siamo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dove_siamo);

        // Inserisce il numero nel dialer
        RelativeLayout relative_click_numero =(RelativeLayout)findViewById(R.id.contatti_telefono);

        relative_click_numero.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:3883752474"));
                startActivity(intent);
            }
        });
        // #################################################


        // Inserisce email nel client di posta
        RelativeLayout relative_click_email =(RelativeLayout)findViewById(R.id.contatti_email);

        relative_click_email.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "segreteria@amatoripescara.it" });
                Intent mailer = Intent.createChooser(intent, "Invia tramite: ");
                startActivity(mailer);
            }
        });
        // #################################################

        // Inserisce posizione in maps
        RelativeLayout relative_click_posizione =(RelativeLayout)findViewById(R.id.posizione_palazzetto);

        relative_click_posizione.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Double myLatitude = 42.4539673;
                Double myLongitude = 14.2268974;
                String labelLocation = "Amatori Pescara Basket";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + myLatitude  + ">,<" + myLongitude + ">?q=<" + myLatitude  + ">,<" + myLongitude + ">(" + labelLocation + ")"));
                startActivity(intent);
            }
        });
        // #################################################


    }
}
