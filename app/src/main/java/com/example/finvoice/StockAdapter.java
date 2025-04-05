package com.example.finvoice;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    private final List<StockDisplayItem> list;

    public StockAdapter(List<StockDisplayItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StockDisplayItem item = list.get(position);
        holder.date.setText("Date: " + item.getDate());
        holder.close.setText("Close Price: ₹" + item.getClose());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, close;
        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(android.R.id.text1);
            close = itemView.findViewById(android.R.id.text2);
        }
    }
}
