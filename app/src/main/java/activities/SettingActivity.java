package activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.examples.detection.R;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SettingActivity extends AppCompatActivity {


    private FirebaseFirestore db;
    private Button logout;
    private TextView encodeTextView;
    private TextView decodeTextView;
    private String encodeStr="";
    private String decodeStr="";
    private Button copyButton;
    private Button editButton;
    private EditText encodeEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        db = FirebaseFirestore.getInstance();

        logout = (Button)findViewById(R.id.logout_button);
        encodeTextView = (TextView)findViewById(R.id.encode_text);
        copyButton = (Button)findViewById(R.id.copy_button);
        editButton = (Button)findViewById(R.id.setting_edit_button);

        String user = SaveSharedPreferences.getUserName(SettingActivity.this);

        encodeTextView.setText(SaveSharedPreferences.getCryptoUserName(SettingActivity.this));

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", SaveSharedPreferences.getCryptoUserName(SettingActivity.this));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(view.getContext(), "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                intent.putExtra("data", SaveSharedPreferences.getCryptoUserName(SettingActivity.this));
                startActivityForResult(intent, 1);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveSharedPreferences.clearUserName(SettingActivity.this);
                Toast.makeText(view.getContext(), "로그아웃 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), FirstAuthActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.w("SettingActivity", "넘어왔어요~");
        //Log.w("SettingActivity","resultCode="+resultCode+" requestC")
/*
        Toast.makeText(this, "넘어왔어요.?!?!?", Toast.LENGTH_SHORT).show();
        encodeTextView.setText("넘어왔어요~");


        String result = data.getStringExtra("changeText");
        if (result != null) {
            encodeTextView.setText(result);
        }*/

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("changeText");
                Log.w("SettingActivity","Conditions All Ok");

                if (result != null) {

                    String decode = null;

                    try {
                        decode = AES256Crypto.AES_Decode(result);
                    } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
                        e.printStackTrace();
                    }

                    if (decode == null){
                        Toast.makeText(this, "유효하지 않은 냉장고 고유키입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        try {
                            if (db.collection("items").document(decode.trim()) == null) {
                                Toast.makeText(this, "유효하지 않은 냉장고 고유키입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(this, "유효하지 않은 냉장고 고유키입니다.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            return;
                        } catch (IllegalArgumentException e){
                            Toast.makeText(this, "유효하지 않은 냉장고 고유키입니다.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            return;
                        }

                    }
                    SaveSharedPreferences.setCryptoUser(SettingActivity.this, result);

                    Log.w("SettingActivity","result is not null "+result);

                    try {
                        SaveSharedPreferences.setKeyForDB(SettingActivity.this, SaveSharedPreferences.getCryptoUserName(SettingActivity.this));
                    } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
                        Toast.makeText(this, "냉장고 고유키가 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                    encodeTextView.setText(SaveSharedPreferences.getCryptoUserName(SettingActivity.this));
                    Toast.makeText(this, "냉장고 고유키가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.w("SettingActivity","result is null");
                }
            }
        }
    }
}