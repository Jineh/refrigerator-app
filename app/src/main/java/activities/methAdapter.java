package activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.tensorflow.lite.examples.detection.R;

import java.util.ArrayList;

public class methAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;
    private LayoutInflater inflate;
    private activities.SearchAdapter.ViewHolder viewHolder;


    public methAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_listview2, viewGroup, false);

        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        TextView label = (TextView) convertView.findViewById(R.id.label);
        label.setText(list.get(position));


        return convertView;
    }

    void addItem(String data){
        list.add(data);
    }

    class ViewHolder {
        public TextView label;
    }


}
