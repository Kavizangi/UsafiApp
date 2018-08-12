package com.example.david.usafiapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SalesviewAdapter extends RecyclerView.Adapter<SalesviewAdapter.MyViewHolder> {

    private List<Salesview> productsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, createdon;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            createdon = (TextView) view.findViewById(R.id.createdon);
        }
    }


    public SalesviewAdapter(List<Salesview> productsList) {
        this.productsList = productsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sales_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Salesview product = productsList.get(position);
        holder.name.setText(product.getName());
        holder.createdon.setText(product.getDate());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
