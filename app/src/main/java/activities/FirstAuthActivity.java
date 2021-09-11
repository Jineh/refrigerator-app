package activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.detection.R;

public class FirstAuthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_auth_activity);

        if(SaveSharedPreferences.getUserName(FirstAuthActivity.this).length()==0){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else{
            Intent intent = new Intent(getApplicationContext(), SubActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
