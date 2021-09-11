package activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.CircularArray;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.detection.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.grpc.internal.JsonUtil;

public class SearchActivity extends AppCompatActivity{

    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;
    private ArrayList<String> food;
    private String ingredient;
    private  Boolean bt_change;
    private TextView textview;
    Gson gson = new Gson();



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);

        // 리스트를 생성한다.
        list = new ArrayList<String>();
        food = new ArrayList<String>();

        //ck= new int[27];

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list,  this);

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setText("추가하기" );

        button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), datePickerActivity.class);


                intent.putExtra("contact_phone", food) ;
                intent.putExtra("isDetection",false);


                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            int[] ck = new int[arraylist.size()];

            int count = 0;
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                for(int i=0;i<arraylist.size();i++){
                    ck[i] += 0;


                }
                TextView t = (TextView)view.findViewById(R.id.label);
                String str = t.getText().toString();

                int c = arraylist.indexOf(str);

                if (ck[c] == 0){
                    view.setBackgroundColor(Color.GRAY);

                    ck[c]=1;
                    food.add(arraylist.get(c));
                    count++;


                }
                else if(ck[c]==1){
                    view.setBackgroundColor(Color.WHITE);
                    ck[c]=0;
                    food.remove(arraylist.get(c));
                    count --;

                }

                String stu = "";
                stu += count;

                button4.setText("추가하기("+stu+")"  );

            }

        });
    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }

        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    list.add(arraylist.get(i));

                }
                // 검색된 데이터를 리스트에 추가한다.
            }
        }

        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    private void settingList(){

        list.add("더덕");
        list.add("병어");
        list.add("붕장어");
        list.add("우거지");
        list.add("우렁");
        list.add("콩나물");
        list.add("쥐치채");
        list.add("홍어");
        list.add("농어");
        list.add("멍게");
        list.add("묵");
        list.add("양송이버섯");
        list.add("수정과");
        list.add("무");
        list.add("가자미");
        list.add("동태");
        list.add("전어");
        list.add("조기");
        list.add("산자");
        list.add("약과");
        list.add("유과");
        list.add("복숭아");
        list.add("레몬");
        list.add("바나나");
        list.add("카라멜");
        list.add("마스카포네치즈");
        list.add("솜사탕");
        list.add("요거트");
        list.add("녹차");
        list.add("아몬드");
        list.add("캐슈넛");
        list.add("망고");
        list.add("갈릭디핑소스");
        list.add("핫소스");
        list.add("선지");
        list.add("순두부");
        list.add("시금치");
        list.add("쑥");
        list.add("아욱");
        list.add("어묵");
        list.add("재첩");
        list.add("토란");
        list.add("호박잎");
        list.add("홍합");
        list.add("기장");
        list.add("무밥");
        list.add("보리");
        list.add("수수");
        list.add("완두콩");
        list.add("유부");
        list.add("잡곡");
        list.add("차조");
        list.add("찰밥");
        list.add("참치");
        list.add("치즈");
        list.add("현미");
        list.add("흑미");
        list.add("감자");
        list.add("건새우");
        list.add("고추");
        list.add("꽈리고추");
        list.add("닭발");
        list.add("닭");
        list.add("당근");
        list.add("마늘쫑");
        list.add("견과류");
        list.add("멸치");
        list.add("돌나물");
        list.add("문어");
        list.add("미역줄기");
        list.add("브로콜리");
        list.add("새송이버섯");
        list.add("양파");
        list.add("도넛");
        list.add("머핀");
        list.add("모닝빵");
        list.add("식빵");
        list.add("골뱅이");
        list.add("달래");
        list.add("두릅");
        list.add("미나리");
        list.add("북어채");
        list.add("상추");
        list.add("양배추");
        list.add("양상추");
        list.add("부추");
        list.add("오이");
        list.add("오징어");
        list.add("치커리");
        list.add("파래");
        list.add("파");
        list.add("풋고추");
        list.add("풋마늘");
        list.add("피조개");
        list.add("단무지");
        list.add("가지");
        list.add("두부");
        list.add("맛살");
        list.add("메밀");
        list.add("애호박");
        list.add("땅콩");
        list.add("삼치");
        list.add("양미리");
        list.add("연근");
        list.add("우엉");
        list.add("쥐포");
        list.add("율무");
        list.add("호밀");
        list.add("고구마");
        list.add("당면");
        list.add("곤약");
        list.add("전분");
        list.add("꿀");
        list.add("엿");
        list.add("시럽");
        list.add("양갱");
        list.add("젤라틴");
        list.add("조청");
        list.add("포도당");
        list.add("푸딩");
        list.add("강낭콩");
        list.add("녹두");
        list.add("녹두묵");
        list.add("물엿");
        list.add("비지");
        list.add("두유");
        list.add("도토리");
        list.add("도토리묵");
        list.add("들깨");
        list.add("마카다미아넛");
        list.add("보리밥열매");
        list.add("브라질너트");
        list.add("은행");
        list.add("잣");
        list.add("참깨");
        list.add("치아씨");
        list.add("코코넛");
        list.add("피칸");
        list.add("해바라기씨");
        list.add("호두");
        list.add("갓");
        list.add("곰취");
        list.add("고사리");
        list.add("도라지");
        list.add("무순");
        list.add("무청");
        list.add("시래기");
        list.add("뽕잎");
        list.add("부지갱이");
        list.add("비트");
        list.add("생강");
        list.add("섬초롱");
        list.add("세발나물");
        list.add("샐러리");
        list.add("숙주나물");
        list.add("순무");
        list.add("스테비아");
        list.add("쌈추");
        list.add("쑥갓");
        list.add("쑥부쟁이");
        list.add("씀바귀");
        list.add("아스파라거스");
        list.add("얼갈이배추");
        list.add("열무");
        list.add("염교");
        list.add("오이피클");
        list.add("죽순");
        list.add("청경채");
        list.add("총각무");
        list.add("취나물");
        list.add("칠면조");
        list.add("케일");
        list.add("콜라비");
        list.add("콩잎");
        list.add("방울토마토");
        list.add("할라피뇨");
        list.add("파슬리");
        list.add("파프리카");
        list.add("피망");
        list.add("감");
        list.add("곶감");
        list.add("라임");
        list.add("라즈베리");
        list.add("리치");
        list.add("망고스틴");
        list.add("멜론");
        list.add("모과");
        list.add("무화과");
        list.add("배");
        list.add("블루베리");
        list.add("비파");
        list.add("산딸기");
        list.add("살구");
        list.add("수박");
        list.add("오미자");
        list.add("올리브");
        list.add("유자");
        list.add("체리");
        list.add("크랜베리");
        list.add("키위");
        list.add("포도");
        list.add("파인애플");
        list.add("파파야");
        list.add("샤인머스켓");
        list.add("등심");
        list.add("삼겹살");
        list.add("안심");
        list.add("갈매기살");
        list.add("토시살");
        list.add("항정살");
        list.add("소시지");
        list.add("베이컨");
        list.add("순대");
        list.add("양지");
        list.add("육포");
        list.add("달걀");
        list.add("메추리알");
        list.add("메기");
        list.add("명태");
        list.add("미꾸라지");
        list.add("민달고기");
        list.add("민어");
        list.add("연어");
        list.add("임연수어");
        list.add("잉어");
        list.add("고둥");
        list.add("굴");
        list.add("꼬막");
        list.add("다슬기");
        list.add("대수리");
        list.add("대합");
        list.add("바지락");
        list.add("백합");
        list.add("소라");
        list.add("수랑");
        list.add("오분자기");
        list.add("조개");
        list.add("가재");
        list.add("개불");
        list.add("꽃게");
        list.add("대게");
        list.add("게맛살");
        list.add("낙지");
        list.add("세발낙지");
        list.add("미더덕");
        list.add("성게");
        list.add("주꾸미");
        list.add("크릴");
        list.add("한치");
        list.add("해삼");
        list.add("다시마");
        list.add("대황");
        list.add("뜸부기");
        list.add("매생이");
        list.add("연유");
        list.add("모짜렐라 치즈");
        list.add("체다치즈");
        list.add("크림치즈");
        list.add("브리치즈");
        list.add("파마산치즈");
        list.add("블루치즈");
        list.add("까망베르치즈");
        list.add("고다치즈");
        list.add("브릭치즈");
        list.add("리코타치즈");
        list.add("휘핑크림");
        list.add("땅콩 버터");
        list.add("땅콩기름");
        list.add("버터");
        list.add("아몬드유");
        list.add("아보카도유");
        list.add("참기름");
        list.add("식혜");
        list.add("막걸리");
        list.add("맥주");
        list.add("보드카");
        list.add("샴페인");
        list.add("소주");
        list.add("청주");
        list.add("칵테일");
        list.add("간장");
        list.add("연겨자");
        list.add("겨자가루");
        list.add("계피가루");
        list.add("고추냉이");
        list.add("고춧가루");
        list.add("굴소스");
        list.add("깨소금");
        list.add("나토");
        list.add("데리야끼소스");
        list.add("돈가스소스");
        list.add("된장");
        list.add("마요네즈");
        list.add("맛술");
        list.add("민트");
        list.add("바비큐소스");
        list.add("발사믹식초");
        list.add("소금");
        list.add("식초");
        list.add("쌈장");
        list.add("양파가루");
        list.add("정향가루");
        list.add("조미료");
        list.add("초고추장");
        list.add("춘장");
        list.add("칠리소스");
        list.add("칠리가루");
        list.add("토마토소스");
        list.add("페퍼민트");
        list.add("후추");
        list.add("북어");
        list.add("전복");
        list.add("콩");
        list.add("인삼");
        list.add("홍삼");
        list.add("배추");
        list.add("갈비");
        list.add("명이나물");
        list.add("표고버섯");
        list.add("부채살");
        list.add("꽃등심");
        list.add("살치살");
        list.add("업진살");
        list.add("차돌박이");
        list.add("치마살");
        list.add("채끝살");
        list.add("에스프레소");
        list.add("커피");
        list.add("옥수수");
        list.add("초콜릿");
        list.add("팥");
        list.add("밤");
        list.add("마늘");
        list.add("바질");
        list.add("호박");
        list.add("노루궁뎅이버섯");
        list.add("느타리버섯");
        list.add("동충하초");
        list.add("만가닥버섯");
        list.add("목이버섯");
        list.add("큰느타리버섯");
        list.add("귤");
        list.add("머루");
        list.add("갈치");
        list.add("김");
        list.add("까나리");
        list.add("꽃새우");
        list.add("능성어");
        list.add("다금바리");
        list.add("다슬기");
        list.add("다시마");
        list.add("닭고기");
        list.add("닭새우");
        list.add("대구");
        list.add("대문어");
        list.add("대하");
        list.add("도다리");
        list.add("도루묵");
        list.add("돌돔");
        list.add("매리복");
        list.add("매생이");
        list.add("미역");
        list.add("바지락");
        list.add("방어");
        list.add("벤자리");
        list.add("벵에돔");
        list.add("붕어");
        list.add("송어");
        list.add("숭어");
        list.add("아귀");
        list.add("은어");
        list.add("쥐치");
        list.add("참돔");
        list.add("청어");
        list.add("키조개");
        list.add("흰다리새우");
        list.add("햄");
        list.add("연두부");
        list.add("포기김치");
        list.add("백김치");
        list.add("총각김치");
        list.add("황도");
        list.add("돈가스");
        list.add("동그랑땡");
        list.add("고등어");
        list.add("멸치액젓");
        list.add("새우");
        list.add("고추참치");
        list.add("포도씨유");
        list.add("요구르트");
        list.add("콩기름");
        list.add("사이다");
        list.add("고추장");
        list.add("알로에");
        list.add("케찹");
        list.add("딸기");
        list.add("한라봉");
        list.add("마가린");
        list.add("딸기잼");
        list.add("사과잼");
        list.add("블루베리잼");
        list.add("포도잼");
        list.add("망고잼");
        list.add("고기만두");
        list.add("김치만두");
        list.add("스위트칠리소스");
        list.add("허니머스타드");
        list.add("생수");
        list.add("콜라");
        list.add("검은콩");
        list.add("오렌지주스");
        list.add("초코우유");
        list.add("커피우유");
        list.add("바나나우유");
        list.add("부침가루");
        list.add("튀김가루");
        list.add("빵가루");
        list.add("꽁치");
        list.add("카놀라유");
        list.add("옥수수유");
        list.add("짜장소스");
        list.add("카레스프");
        list.add("골드키위");
        list.add("매실");
        list.add("복분자");
        list.add("오렌지");
        list.add("토마토");
        list.add("석류");
        list.add("사과");
        list.add("소고기");
        list.add("계란");
        list.add("팽이버섯");
        list.add("대파");
        list.add("돼지고기");
        list.add("소세지");
        list.add("깻잎");

    }
}