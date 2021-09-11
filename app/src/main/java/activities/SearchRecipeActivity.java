package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.databinding.SearchrecipeMainBinding;

import java.util.ArrayList;

public class SearchRecipeActivity extends AppCompatActivity implements RecipeAdapter.OnListItemSelectedInterface {

    RecyclerView r;
    ArrayList<Food> mFoods = new ArrayList<>();

    private FirebaseFirestore db;
    String usercode = "food";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        SearchrecipeMainBinding binding = SearchrecipeMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecipeAdapter adapter = new RecipeAdapter(this,binding.recyclerview,this);

        db = FirebaseFirestore.getInstance();

        db.collection("items").document(SaveSharedPreferences.getKeyForDB(SearchRecipeActivity.this)).collection("ingredients")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String str = (String)document.get("name");
                                mFoods.add(new Food(str,getResources().getIdentifier(change(str),"drawable",getPackageName()),false,document.getId()));
                            }
                            adapter.notifyDataSetChanged();

                        }
                        else {

                        }
                    }
                });

        binding.recyclerview.setAdapter(adapter);
        adapter.setData(mFoods);
        binding.recyclerview.setLayoutManager(new GridLayoutManager(this,3));

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_recipe);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        r= binding.recyclerview;

        binding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                intent.putExtra("name",adapter.name);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onItemSelected(View v, int position){
        RecipeAdapter.RViewHolder viewHolder = (RecipeAdapter.RViewHolder)r.findViewHolderForAdapterPosition(position);

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