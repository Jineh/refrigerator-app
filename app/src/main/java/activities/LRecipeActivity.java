package activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tensorflow.lite.examples.detection.R;

import java.io.IOException;
import java.util.ArrayList;

public class LRecipeActivity extends AppCompatActivity  {

    String num = new String();
    String url = "https://www.10000recipe.com";
    ImageView imgview;
    TextView food_ingre;
    ListView listView;

    ArrayList<String> ingre = new ArrayList<>();
    ArrayList<String> meth = new ArrayList<>();

    Context context = this;
    methAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lastrecipe_main);

        listView = (ListView) findViewById(R.id.method);
        imgview = (ImageView) findViewById(R.id.imageView2);
        food_ingre = findViewById(R.id.ingre);

        Intent intent2 = getIntent();
        num = intent2.getStringExtra("num");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_lastre);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        getData();

    }

    private void getData(){
        RecipeJsoup rsoupAsynTask = new RecipeJsoup();
        rsoupAsynTask.execute();
    }

    private class RecipeJsoup extends AsyncTask<Void, Void, Void> {

        String img = new String();
        @Override
        protected Void doInBackground(Void... voids){
            try{
                String searchurl = url + num;
                Document doc = Jsoup.connect(searchurl).get();
                Elements ingre_list = doc.select("#divConfirmedMaterialArea > ul > a");
                Elements recipe_img = doc.select("#main_thumbs");
                Elements cook_meth = doc.select("div.view_step");

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for(Element element: ingre_list.select("li")){
                            ingre.add(element.text());
                        }

                        for(Element element: recipe_img){
                            img = element.attr("src");
                        }

                        for(Element element: cook_meth.select(".media-body")){
                            meth.add(element.text());
                        }

                        food_ingre.setText(ingre.toString());

                        adapter = new methAdapter(meth,context);
                        listView.setAdapter(adapter);

                        adapter.notifyDataSetChanged();

                        Glide.with(context).load(img).into(imgview);
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
                //start
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
