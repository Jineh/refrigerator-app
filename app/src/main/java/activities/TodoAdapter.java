package activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import org.tensorflow.lite.examples.detection.R;

import java.util.ArrayList;

import io.grpc.internal.FailingClientStream;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private FirebaseFirestore db;
    private ArrayList<Todo> mData = null;
    //private List<Todo> stList;
    public class ViewHolder extends RecyclerView.ViewHolder{

        private ArrayList<String> food;
        public TextView todoName;
        protected TextView textview_todo_item;
        protected ImageButton deleteBt;
        public CheckBox chkSelected;
        private ArrayList<String> arraylist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            db = FirebaseFirestore.getInstance();
            //todoName = this.textview_todo_item;
            //todoName = (TextView) itemView.findViewById(R.id.textview_todo_item);
            this.textview_todo_item = itemView.findViewById(R.id .textview_todo_item);
            this.deleteBt = itemView.findViewById(R.id.button_todo_item);
            chkSelected = (CheckBox) itemView.findViewById(R.id.chkSelected);
            deleteBt.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    db.runTransaction(new Transaction.Function<Void>() {
                        @Nullable
                        @Override
                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                            db.collection("basket_main").document(SaveSharedPreferences.getKeyForDB(itemView.getContext())).collection("basket").document(mData.get(position).getTodoName()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mData.remove(position);
                                    notifyDataSetChanged();
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


        }
    }
    TodoAdapter(ArrayList<Todo> list){
        mData = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item2,parent,false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textview_todo_item.setText(mData.get(position).getTodoName());

        //TodoAdapter.ViewHolder.todoName.setText(mData.get(position).getTodoName());

        holder.chkSelected.setChecked(mData.get(position).isSelected());

        holder.chkSelected.setTag(mData.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                Todo contact = (Todo) cb.getTag();

                contact.setSelected(cb.isChecked());
                mData.get(position).setSelected(cb.isChecked());

                //int c = arraylist.indexOf(str);
                //contact.isSelected(cb.isChecked());
                //mData.get(position).setSelected(cb.isChecked());
            }
        });
    }
    public int getItemCount() {
        return mData.size();
    }


    public ArrayList<Todo> getStudentist() {
        return mData;
    }
}
