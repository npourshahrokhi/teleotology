package com.doctor.ashanet.ashanet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

public class CartableAdapter extends RecyclerView.Adapter<ViewHolder> {
    public static int fid;
    public static int userid;
    Context context;
    List<CartableItems> items;

    public CartableAdapter(List<CartableItems> items2, Context context2) {
        this.items = items2;
        this.context = context2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cartable_list_items, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(this.items.get(position).getTitles());
        holder.description.setText(this.items.get(position).getDescriptions());
        if (this.items.get(position).getRead().intValue() == 1) {
            holder.read.setImageResource(R.drawable.read);
        } else {
            holder.read.setImageResource(R.drawable.unread);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CartableAdapter.userid = Integer.valueOf(CartableAdapter.this.items.get(position).getMsg().toString()).intValue();
                CartableAdapter.fid = Integer.valueOf(CartableAdapter.this.items.get(position).getId().toString()).intValue();
                Intent myIntent = new Intent(CartableAdapter.this.context, CartableView.class);
                new balinidata();
                myIntent.setFlags(67108864);
                new Cartable().finish();
                CartableAdapter.this.context.startActivity(myIntent);
            }
        });
    }

    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        TextView ids;
        ImageView read;
        RelativeLayout relativeLayout;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.cartabletitle);
            this.description = (TextView) itemView.findViewById(R.id.description);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rltid);
            this.read = (ImageView) itemView.findViewById(R.id.read);
        }
    }
}
