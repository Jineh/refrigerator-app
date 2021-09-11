package activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import org.tensorflow.lite.examples.detection.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasketActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    Button insertButton;
    EditText todoEdit;
    private ArrayList<Todo> todoArrayList;
    private TodoAdapter todoAdapter;
    private ArrayList<String> food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_main);
        db = FirebaseFirestore.getInstance();
        food = new ArrayList<String>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoArrayList = new ArrayList<>();
        todoAdapter = new TodoAdapter(todoArrayList);
        recyclerView.setAdapter(todoAdapter);

        db.collection("basket_main").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("TAG", "listen:error", error);
                    return;
                }
                todoArrayList.clear();
                db.collection("basket_main").document(SaveSharedPreferences.getKeyForDB(BasketActivity.this)).collection("basket").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = (String)document.get("name");
                                Todo todo = new Todo(name);
                                todoArrayList.add(todo);
                                todoAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        });


        insertButton = (Button) findViewById(R.id.button_insert_main);
        todoEdit = (EditText) findViewById(R.id.edit_todo_main);

        insertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Todo newTodo = new Todo(todoEdit.getText().toString());
                todoArrayList.add(newTodo);
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", newTodo.getTodoName());
                db.runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        db.collection("basket_main").document(SaveSharedPreferences.getKeyForDB(BasketActivity.this)).collection("basket").document(newTodo.getTodoName()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                todoAdapter.notifyDataSetChanged();
                                todoEdit.setText(null);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                        return null;
                    }
                });

            }
        });
        Button button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() { //다음으로 화면 이동
            @Override
            public void onClick(View view) {
                String data = "";
                List<Todo> mData = ((TodoAdapter) todoAdapter)
                        .getStudentist();

                for (int i = 0; i < mData.size(); i++) {
                    Todo singleStudent = mData.get(i);
                    if (singleStudent.isSelected()) {
                        data = singleStudent.getTodoName().toString();
                        food.add(data);
                        db.runTransaction(new Transaction.Function<Void>() {
                            @Nullable
                            @Override
                            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                db.collection("basket_main").document(SaveSharedPreferences.getKeyForDB(BasketActivity.this)).collection("basket").document(singleStudent.getTodoName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        todoArrayList.remove(singleStudent);
                                    }
                                });
                                return null;
                            }
                        });
                    }
                }
                Intent intent = new Intent(getApplicationContext(), datePickerActivity.class);


                intent.putExtra("contact_phone", food);


                startActivity(intent);

            }
        });
    }


}