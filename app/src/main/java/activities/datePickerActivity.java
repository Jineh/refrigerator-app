package activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.tensorflow.lite.examples.detection.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class datePickerActivity extends AppCompatActivity{

    private FirebaseFirestore db;

    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private ArrayList<String> search;
    private String mStrDate = " ";

    private Button add_button;

    private String timestamp;

    private ArrayList<String> dates;
    //private ArrayList<LocalDate> dates;
    //private ArrayList<Calendar> dates;

    private boolean isDetection ;

    @Override
    public void onBackPressed() {
        Intent intent=null;
        if(isDetection) {
            intent = new Intent(getApplicationContext(), AddActivity.class);
            startActivity(intent);
        }
        else
            super.onBackPressed();
        finish();
    }

    public int res;
    public int id;
    public int j;
    public int ld;

    @Override
    public void onResume() {
        super.onResume();
        db.collection("items").document(SaveSharedPreferences.getKeyForDB(datePickerActivity.this))
                .collection("ingredients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    id=0;
                    ld=0;
                    res=0;
                    for (DocumentSnapshot document : task.getResult()) {
                        res++;
                        if(ld < Integer.parseInt(document.getId()) ) {
                            ld = Integer.parseInt(document.getId());
                        }

                    }
                } else {
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        add_button = (Button)findViewById(R.id.button4);

        db = FirebaseFirestore.getInstance();

        Date date = new Date();

        //this.InitializeView();
        //this.InitializeListener();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_datepicker);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent() ;

        if(intent.getBooleanExtra("isDetection",false)==true)
            isDetection = true;
        else isDetection = false;

        TextView editSearch = (TextView) findViewById(R.id.editSearch) ;
        //String search = intent.getStringExtra("contact_phone") ;
        search = (ArrayList<String>) intent.getSerializableExtra("contact_phone");

        if (search != null) {
            //editSearch.setText(search.get(0));

        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.subrc) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        SubAdapter adapter = new SubAdapter(search) ;
        recyclerView.setAdapter(adapter) ;

//        db.collection("items").document("food")
//                .collection("ingredients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    id=0;
//                    ld=0;
//                    res=0;
//                    for (DocumentSnapshot document : task.getResult()) {
//                        res++;
//                        if(ld < Integer.parseInt(document.getId()) ) {
//                            ld = Integer.parseInt(document.getId());
//                        }
//
//                    }
//                } else {
//                }
//            }
//        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dates = adapter.getmDates();
                id = ld +1;

                for(int i=0;i<dates.size();i++){
                    if(dates.get(i).equals("")){
                        onClickShowAlert(view);
                        return;
                    }
                }

                for(int i=0;i<search.size();i++){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", change(search.get(i)));
                    timestamp = dates.get(i);
                    map.put("date", timestamp);


                    db.collection("items").document(SaveSharedPreferences.getKeyForDB(datePickerActivity.this))
                            .collection("ingredients").document(Integer.toString(id)).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(view.getContext(), "추가에 성공했습니다", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "추가에 실패했습니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                    id++;
                }
                ((SubActivity)SubActivity.context).onResume();
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                //Intent intent = new Intent(getApplicationContext(), Notification.class);
                startActivity(intent);
            }
        });

        //updateResult();

    }

    public void onClickShowAlert(View view) {
        AlertDialog.Builder myAlertBuilder =
                new AlertDialog.Builder(datePickerActivity.this);
        // alert의 title과 Messege 세팅
        myAlertBuilder.setTitle("유통기한 설정");
        myAlertBuilder.setMessage("유통기한을 설정하세요.");
        // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
        myAlertBuilder.setPositiveButton("확인",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                return;
            }
        });
        myAlertBuilder.show();
    }

    public void mOnClick(View v){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, mDateSetListener, year, month, day).show();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mStrDate = String.format("%d년 %d월 %d일",year, month+1, dayOfMonth);
            //updateResult();
        }
    };

    public String change(String name){
        String res = name;
        String[] eng = getResources().getStringArray(R.array.eng_name);
        String[] kor = getResources().getStringArray(R.array.kor_name);

        for(int i=0;i<eng.length;i++){
            if(name.equals(eng[i])){
                res = kor[i];
            }
        }
        return res;
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

}