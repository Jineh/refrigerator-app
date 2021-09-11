package activities;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.detection.databinding.FooditemBinding;
//import com.example.myapplication.databinding.FooditemBinding;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<activities.RecipeAdapter.RViewHolder> {
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;
    ArrayList<String> name = new ArrayList<>();

    SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);
    private List<Food> mFoods;
    Context mContext;
    RecyclerView recyclerView;

    RecipeAdapter(Context context, RecyclerView recyclerView, OnListItemSelectedInterface listener){
        this.mContext = context;
        this.mListener = listener;
        this.recyclerView = recyclerView;
    }

    public void setData(List<Food> data){
        mFoods = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        FooditemBinding binding = FooditemBinding.inflate(inflater, parent, false);

        RViewHolder rv = new RViewHolder(binding);
        return rv;
    }

    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {
        Food food = mFoods.get(position);
        holder.mBinding.txt.setText(food.getName());
        holder.mBinding.img.setImageResource(food.getImage());
        
        holder.itemView.setSelected(isItemSelected(position));

    }

    private boolean isItemSelected(int position) {
        return mSelectedItems.get(position, false);
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public class RViewHolder extends RecyclerView.ViewHolder{

        public FooditemBinding mBinding;

        RViewHolder(FooditemBinding binding){
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mListener.onItemSelected(v, getAdapterPosition());

                    toggleItemSelected(position);

                }
            });

        }
        private void toggleItemSelected(int position) {

            if (mSelectedItems.get(position, false)) {
                mSelectedItems.delete(position);
                name.remove(mFoods.get(position).fName);
                notifyItemChanged(position);
            } else {
                mSelectedItems.put(position, true);
                name.add(mFoods.get(position).fName);
                notifyItemChanged(position);
            }
        }

    }


}
