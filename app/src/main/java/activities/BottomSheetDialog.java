package activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.databinding.IngredientBottomSheetBinding;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;

public class BottomSheetDialog extends BottomSheetDialogFragment{

    private IngredientBottomSheetBinding binding;
    private String name;
    private String kcal;
    private String carbs;
    private String protein;
    private String fat;
    private String date;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    int ok = 0;

    public BottomSheetDialog(String name, String kcal, String carbs, String protein, String fat, String date){
        this.name = name;
        this.kcal = kcal;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
        String[] split = date.split(" ");
        this.date = split[0];
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = IngredientBottomSheetBinding.inflate(inflater, container, false);
        TextView nameTextview = binding.ingredientNameTextview;
        TextView kcalTextView = binding.kcalTextview;
        TextView proteinTextView = binding.proteinTextview;
        TextView carbsTextView = binding.carbsTextview;
        TextView fatTextView = binding.fatTextview;
        TextView shelfLifeTextview = binding.shelfLifeTextview;

        nameTextview.setText(name);
        shelfLifeTextview.setText(date);
        kcalTextView.setText("열량(kcal)  "+kcal);
        proteinTextView.setText("단백질  "+protein);
        carbsTextView.setText("탄수화물  "+carbs);
        fatTextView.setText("지방  "+fat);

        View view = binding.getRoot();

        setStyle( // Background -> Transparent.
                STYLE_NORMAL,
                R.style.TransparentBottomSheetDialogFragment
        );

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            Button b = binding.bottomCloseButton;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("버튼 클릭", "onClick: 확인 버튼 눌렀다!");
                    dismiss();
                }
            });
    }
}
