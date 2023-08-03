package com.example.plantdiseasedetection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {
     final List<String> dataList;
     List<String> filter;
    Context context;
    private OnItemClickListener mListener;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterString = charSequence.toString().toLowerCase().trim();

                List<String> tempList = new ArrayList<>();

                if (charSequence.length()==0){
                    tempList.addAll(dataList);
                }else {

                    for (String item : dataList) {
                        if (item.toLowerCase().contains(filterString)) {
                            tempList.add(item);
                        }
                    }
                }



                FilterResults filterResults = new FilterResults();
                filterResults.values = tempList;
                filterResults.count = tempList.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                filter.clear();
                filter = (List)filterResults.values;
//                filter.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    


    public MyAdapter(Context context,List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.filter = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_disease, parent, false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(filter.get(position));
    }

    @Override
    public int getItemCount() {
        return filter.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.dname);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener!=null && position!=RecyclerView.NO_POSITION) {
                        int k = 0;
                        for(int i=0;i<dataList.size();i++){
                            if(filter.get(position).equals(dataList.get(i))){
                                k=i;
                                break;
                            }
                        }
                        listener.onClick(k);
                    }
                }
            });
        }
    }

}

