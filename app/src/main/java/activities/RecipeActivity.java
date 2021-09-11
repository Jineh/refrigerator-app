package activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.tensorflow.lite.examples.detection.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tensorflow.lite.examples.detection.databinding.RecipeMainBinding;

import java.io.IOException;
import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    ArrayList<String> name = new ArrayList<>();

    ArrayList<String> recipeName = new ArrayList<>();
    ArrayList<String> recipeUrl = new ArrayList<>();
    ArrayList<String> recipeNum = new ArrayList<>();

    RecyclerView recyclerView;
    CookAdapter adapter;
    String url = "https://www.10000recipe.com/recipe/list.html?q=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecipeMainBinding binding = RecipeMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.recyclerview2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CookAdapter();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_recipe2);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getData();

        Intent intent = getIntent();
        name = intent.getStringArrayListExtra("name");

        adapter.setOnItemClickListener(new CookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String num = adapter.getItem(position).getcNum();
                Intent intent2 = new Intent(getApplicationContext(), LRecipeActivity.class);
                intent2.putExtra("num",num);
                startActivity(intent2);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void getData(){
        RecipeJsoup rsoupAsynTask = new RecipeJsoup();
        rsoupAsynTask.execute();
    }

    private class RecipeJsoup extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids){
            try{
                String searchurl = url + name.toString();
                Document doc = Jsoup.connect(searchurl).get();
                Elements recipe_list = doc.select("div.common_sp_caption > div.common_sp_caption_tit.line2");
                Elements recipe_img = doc.select("div.common_sp_thumb > a > img");
                Elements recipe_num = doc.select("div.common_sp_thumb > a");

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for(Element element: recipe_list){
                            recipeName.add(element.text());
                        }

                        for(Element element: recipe_img){
                            recipeUrl.add(element.attr("src"));
                        }

                        for(Element element: recipe_num){
                            recipeNum.add(element.attr("href"));
                        }

                        for(int i=0;i<recipeName.size();i++){
                            Cook data = new Cook();
                            data.setCName(recipeName.get(i));
                            data.setCNum(recipeNum.get(i));
                            data.setImageUrl(recipeUrl.get(i));

                            adapter.addItem(data);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

            } catch (IOException e){
            }

            return null;
        }
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
