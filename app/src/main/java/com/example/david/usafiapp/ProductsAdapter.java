package com.example.david.usafiapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private List<Products> productsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, createdon;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            createdon = (TextView) view.findViewById(R.id.createdon);
        }
    }


    public ProductsAdapter(List<Products> productsList) {
        this.productsList = productsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Products product = productsList.get(position);
        holder.name.setText(product.getName());
        holder.createdon.setText(product.getDate());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
