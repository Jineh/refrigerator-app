package activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.tensorflow.lite.examples.detection.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import org.tensorflow.lite.examples.detection.databinding.SubactivityMainBinding;
//import Activities.databinding.SubactivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SubActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    ArrayList<Food> mFoods = new ArrayList<>();
    public static Context context;

    ImageView ivMenu;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private ArrayList<String> basket;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;

    FoodAdapter adapter;
    SubactivityMainBinding binding;

    private AlarmManager alarmManager;

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        db = FirebaseFirestore.getInstance();

        binding = SubactivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter = new FoodAdapter(mFoods,getSupportFragmentManager());

        ivMenu = binding.ivMenu;
        drawerLayout = binding.drawer;
        navigationView = binding.navView;
        Toolbar toolbar =binding.toolbar;
        setSupportActionBar(toolbar);



        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new GridLayoutManager(this,3));

        binding.ivMenu.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.LEFT));
        binding.btn3.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddActivity.class);
            startActivity(intent);
        });

        binding.btn4.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SearchRecipeActivity.class);
            startActivity(intent);
        });

        binding.photolistDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btck(1);
            }
        });

        binding.photolistDeletecancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btck(0);
            }
        });

        binding.photolistDeleteok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //삭제
                ArrayList<String> chlist = adapter.checkedlist();
                ArrayList<Integer> polist = adapter.checkedpo();

                for(String str : chlist){
                    db.collection("items").document(SaveSharedPreferences.getKeyForDB(SubActivity.this)).collection("ingredients")
                            .document(str)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context,"식재료가 삭제되었습니다",Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                for(int po :polist){
                    mFoods.remove(po);
                }
                adapter.notifyDataSetChanged();
                btck(0);

                adapter.clearlist();
                mFoods.clear();
                db();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);

            drawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            String title = menuItem.getTitle().toString();

            if(id == R.id.shopping){
                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                startActivity(intent);
            }
            else if(id == R.id.setting){
                Toast.makeText(context, title , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
            else if(id == R.id.tip) {
                Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), NextActivity.class);
                startActivity(intent);
            }

            return true;
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        btck(0);
        mFoods.clear();
        db();
        adapter.notifyDataSetChanged();
    }

    public String change(String name){
        String res = "foods";
        String[] eng = getResources().getStringArray(R.array.eng_name);
        String[] kor = getResources().getStringArray(R.array.kor_name);

        for(int i=0;i<eng.length;i++){
            if(name.equals(kor[i])){
                res = eng[i];
            }
        }
        if(res.equals("red pepper paste")){
            res = "redpepperpaste";
        }
        if(res.equals("sweet potato")){
            res = "sweetpotato";
        }
        if(res.equals("soybean paste")){
            res = "soybeanpaste";
        }
        if(res.equals("quail eggs")){
            res = "quaileggs";
        }

        if(res.equals("greenOnion")){
            res = "greenonion";
        }
        if(res.equals("enokiMushroom")){
            res = "enokimushroom";
        }
        return res;
    }

    private void btck(int n){
        adapter.updateCheckbox(n);
        adapter.notifyDataSetChanged();

        if(n==1){
            binding.photolistDelete.setVisibility(View.GONE);
            binding.photolistDeleteok.setVisibility(View.VISIBLE);
            binding.photolistDeletecancel.setVisibility(View.VISIBLE);
        }
        else{
            binding.photolistDelete.setVisibility(View.VISIBLE);
            binding.photolistDeleteok.setVisibility(View.GONE);
            binding.photolistDeletecancel.setVisibility(View.GONE);
        }

    }

    private void db(){
        db.collection("items").document(SaveSharedPreferences.getKeyForDB(SubActivity.this)).collection("ingredients")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i=0;
                        ArrayList<String> dates = new ArrayList<String>();
                        ArrayList<String> names = new ArrayList<String>();

                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String str = (String) document.get("name");

                                HashMap<String, Object> map = (HashMap<String, Object>) document.getData();

                                String name = (String) map.get("name");
                                String date = (String) map.get("date");


                                String d[] = date.split("-");
                                int yy = Integer.parseInt(d[0]);
                                int mm = Integer.parseInt(d[1]);
                                int dd = Integer.parseInt(d[2]);
                                Calendar day = Calendar.getInstance();
                                day.add(Calendar.DATE, -1);
                                String getTime = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(day.getTime());

                                String current[] = getTime.split("-");
                                int cyy = Integer.parseInt(current[0]);
                                int cmm = Integer.parseInt(current[1]);
                                String cr[] = current[2].split(" ");
                                int cdd = Integer.parseInt(cr[0]);

                                String cur[] = cr[1].split(":");
                                int chh = Integer.parseInt(cur[0]);
                                int cmmm = Integer.parseInt(cur[1]);
                                int css = Integer.parseInt(cur[2]);

                                i++;
                                names.add(name);
                                dates.add(date);

                                Food f = new Food(str, getResources().getIdentifier(change(str), "drawable", getPackageName()), false, document.getId());
                                f.setTime(date);
                                mFoods.add(f);

                            }
                            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                            Intent receiverIntent = new Intent(SubActivity.this, AlarmRecevier.class);
                            receiverIntent.putExtra("notiName", names);
                            receiverIntent.putExtra("notiDate", dates);
                            receiverIntent.putExtra("notiCount", Integer.toString(i));


                            PendingIntent pendingIntent = PendingIntent.getBroadcast(SubActivity.this, 0, receiverIntent, PendingIntent.FLAG_NO_CREATE);


                            if (pendingIntent == null) {
                                // TODO: 이미 설정된 알람이 없는 경우

                            } else {
                                PendingIntent sender = PendingIntent.getBroadcast(SubActivity.this, 0, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.cancel(sender);
                                sender.cancel();
                            }
                            Calendar calendar = Calendar.getInstance();
                            PendingIntent penIntent = PendingIntent.getBroadcast(SubActivity.this, 0, receiverIntent, 0);
//


                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 9);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 00);

                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, penIntent);

                            adapter.notifyDataSetChanged();

                        }
                        else {

                        }
                    }
                });
    }

}