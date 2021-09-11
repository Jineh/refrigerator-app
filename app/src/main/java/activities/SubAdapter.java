package activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.detection.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {

    private ArrayList<String> mData = null ;
    private ArrayList<Date> mmDate = null;
    private ArrayList<String> mDate = null;
    private String mStrDate = " ";

    public ArrayList<String> getmDates(){
        return mDate;
    }
    public ArrayList<String> getmData(){
        return mData;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;
        TextView textView2;
        //Button btn;
        ImageView imgbtn;

        ViewHolder(View itemView) {
            super(itemView) ;
            textView1 = itemView.findViewById(R.id.textView1) ;
            textView2 = itemView.findViewById(R.id.textView2);
            imgbtn = itemView.findViewById(R.id.imageView4);


            // 아이템 클릭 이벤트 처리.
            itemView.findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(v.getContext(), mDateSetListener, year, month, day).show();

                }
            });


        }
        private DatePickerDialog.OnDateSetListener mDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String s = "year is " +year;
                month+=1;

                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth);

                String m = year+"-"+month+"-"+dayOfMonth;
                mDate.set(getAdapterPosition(),m);
                mStrDate = String.format("%d년 %d월 %d일",year, month, dayOfMonth);
                textView2.setText(mStrDate);
            }
        };


        // 뷰 객체에 대한 참조. (hold strong reference)

        //
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    SubAdapter(ArrayList<String> list) {
        mData = list ;
        mDate = new ArrayList<String>(mData.size());
        setDefalutDateFormDate();
    }

    public void setDefalutDateFormDate() {
        for(int i=0;i<mData.size();i++){
            mDate.add(new String());

        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sub_item, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.textView1.setText(text) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }


}