package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.tensorflow.lite.examples.detection.R;

public class PopupActivity extends Activity {

    private EditText popupEditText = null;
    private Button popupConfirm = null;
    private Button popupCancel= null;

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        popupEditText = (EditText)findViewById(R.id.popup_editText);
        popupConfirm = (Button)findViewById(R.id.popup_confirm);
        popupCancel = (Button)findViewById(R.id.popup_cancel);

        popupEditText.setText(data);

        popupConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String changeText = popupEditText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("changeText", changeText);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        popupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);

                finish();
            }
        });

    }
}
