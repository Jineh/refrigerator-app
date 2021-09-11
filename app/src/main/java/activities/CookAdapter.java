package activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.tensorflow.lite.examples.detection.R;

import java.util.ArrayList;

public class CookAdapter  extends RecyclerView.Adapter<CookAdapter.ItemViewHolder> {


    private ArrayList<Cook> listdata = new ArrayList<>();

    @NonNull
    @Override
    public CookAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipeitem, viewGroup, false);
        return new
                ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CookAdapter.ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.onBind(listdata.get(i));
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    void addItem(Cook data){
        listdata.add(data);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView recipe_name;
        private ImageView recipe_img;

        public ItemViewHolder(View itemview) {
            super(itemview);
            recipe_name = itemView.findViewById(R.id.foodname);
            recipe_img = itemView.findViewById(R.id.foodimg);

            itemview.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(v,pos);
                        }
                    }
                }
            });
        }

        void onBind(Cook data){
            recipe_name.setText(data.getcName());
            Glide.with(itemView.getContext()).load(data.getImageUrl()).into(recipe_img);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public Cook getItem(int position){ return listdata.get(position); }
}

