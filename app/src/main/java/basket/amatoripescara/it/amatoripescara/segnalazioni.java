package basket.amatoripescara.it.amatoripescara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class segnalazioni extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazioni);

        Button send_error_button = (Button)findViewById(R.id.invia_segnalazione_button);

        // Inserisce email nel client di posta

        send_error_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "alessandro.losavio.21@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "[Segnalazione errore] App v. " + BuildConfig.VERSION_CODE
                        + ", Mod. " + android.os.Build.MODEL + ", Os. " + android.os.Build.VERSION.RELEASE);
                Intent mailer = Intent.createChooser(intent, "Invia tramite: ");
                startActivity(mailer);
            }
        });
        // #################################################

    }
}
