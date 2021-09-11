package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.detection.R;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SaveSharedPreferences.getUserName(MainActivity.this).length()==0){

        }

        Button imageButton = (Button) findViewById(R.id.btn1);

        if(SaveSharedPreferences.getUserName(MainActivity.this).length()==0){

        } else{
            Intent intent = new Intent(getApplicationContext(), SubActivity.class);
            startActivity(intent);
            finish();
        }

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }


}