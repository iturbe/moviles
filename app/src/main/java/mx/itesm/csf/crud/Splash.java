package mx.itesm.csf.crud;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import mx.itesm.csf.crud.Controladores.Servicios;
import mx.itesm.csf.crud.Login.MainActivity;

import static android.net.Uri.parse;

public class Splash extends AppCompatActivity {

    private long wait = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_splash);

        // OCULTAR BARRA DE NAVEGACIÓN
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //obtener número aleatorio
        Random rand = new Random();
        int  n = rand.nextInt(3);

        String URL = "";

        //mostrar imagen aleatoria
        switch (n) {
            case 0:
                URL = Servicios.SPLASH4;
                break;
            case 1:
                URL = Servicios.SPLASH5;
                break;
            case 2:
                URL = Servicios.SPLASH6;
                break;
            default:
                URL = Servicios.BLACK;
                break;
        }

        Uri imageUri = Uri.parse(URL);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.sdvImage);
        draweeView.setImageURI(imageUri);

        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                finish();
                Intent intent = new Intent().setClass(Splash.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //clear previous activities
                startActivity(intent);
            }
        };

        (new Timer()).schedule(task, wait);
    }
}
