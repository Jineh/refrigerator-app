package activities;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.tensorflow.lite.examples.detection.databinding.FooditemBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
class ViewHolder extends RecyclerView.ViewHolder {
    FooditemBinding mBinding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String name="";
    private String kcal="";
    private String carbs="";
    private String protein="";
    private String fat="";
    private String date="";

    ViewHolder(FooditemBinding binding, FragmentManager f){
        super(binding.getRoot());
        mBinding = binding;
        //Context c = context;
        //FragmentManager fragmentManager = f;
        mBinding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               name = (String) mBinding.txt.getText();
                date = (String) mBinding.date.getText();
                setNutri(name);
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        //new BottomSheetDialog((String) mBinding.txt.getText()).show(f, "tag");
                        new BottomSheetDialog(name, kcal, carbs, protein, fat, date).show(f, "tag");
                    }
                }, 500);
            }
        });
    }

    public void setNutri(String name){
        db.collection("ingredient_detail").document(name).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    kcal = (String)document.get("kcal");
                    carbs = (String)document.get("carbs");
                    protein = (String)document.get("protein");
                    fat = (String)document.get("fat");

                    Log.i("TAG", "onComplete: kcal : "+kcal+" carbs : "+carbs);
                }else{
                    //Toast.makeText(this, "실패",Toast.LENGTH_SHORT).show();
                    Log.i("TAG", "onComplete: 실패함!!!!!!!!!!!!!!!!!!!!!!!!");
                }
            }
        });
    }
}
public class FoodAdapter extends RecyclerView.Adapter<activities.ViewHolder> {


    public List<Food> mFoods;

    ArrayList<String> checkid = new ArrayList<>();
    ArrayList<Integer> checkpo = new ArrayList<>();


    FragmentManager fragmentManager = null;

    private int ck = 0;

    FoodAdapter(List<Food> foods, FragmentManager fragmentManager){
        mFoods = foods;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public activities.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        FooditemBinding binding = FooditemBinding.inflate(inflater, parent, false);
        return new activities.ViewHolder(binding, fragmentManager);

    }

    @Override
    public void onBindViewHolder(@NonNull activities.ViewHolder holder, int position) {
        final Food food = mFoods.get(position);
        holder.mBinding.txt.setText(food.getName());
        holder.mBinding.img.setImageResource(food.getImage());
        holder.mBinding.date.setText(food.time);
        Log.i("TAG", "onBindViewHolder: "+food.time);
        holder.mBinding.checkbox.setOnCheckedChangeListener(null);
        holder.mBinding.checkbox.setChecked(food.getSelected());
        if(ck == 1){
            holder.mBinding.checkbox.setVisibility(View.VISIBLE);
        }
        else
            holder.mBinding.checkbox.setVisibility(View.GONE);

        holder.mBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkid.add(food.docid);
                    checkpo.add(position);
                }
                else{
                    if(checkid.size()==1 && checkpo.size()==1){
                        checkid.clear();
                        checkpo.clear();
                    }
                    else {

                        for(int i=0;i<checkpo.size();i++){
                            if(checkpo.get(i)==position){
                                checkid.remove(food.docid);
                                checkpo.remove(i);
                            }

                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public void updateCheckbox(int n){
        ck = n;
    }

    public ArrayList<String> checkedlist(){
        return checkid;
    }

    public ArrayList<Integer> checkedpo() {
        Collections.sort(checkpo);
        Collections.reverse(checkpo);
        return checkpo; }

    public void clearlist() {checkid.clear(); checkpo.clear(); }


}