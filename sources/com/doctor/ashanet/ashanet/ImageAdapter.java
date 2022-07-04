package com.doctor.ashanet.ashanet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<ImageLiseItem> ImgItems;
    Context context;

    public ImageAdapter(List<ImageLiseItem> imgItems) {
        this.ImgItems = imgItems;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_items, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.name.setText(this.ImgItems.get(position).getName());
        holder.imgs.setImageDrawable(Drawable.createFromPath(this.ImgItems.get(position).getUrl()));
        if (holder.imgs != null) {
            new ImageDownloaderTask().download(this.ImgItems.get(position).getUrl(), holder.imgs);
        }
        holder.imgs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                holder.zoom.setImageDrawable(holder.imgs.getDrawable());
                holder.imgs.setVisibility(8);
                holder.name.setVisibility(8);
                holder.zoom.setVisibility(0);
            }
        });
        holder.zoom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (holder.zoom.getVisibility() == 0) {
                    holder.zoom.setVisibility(8);
                    holder.imgs.setVisibility(0);
                    holder.name.setVisibility(0);
                }
            }
        });
    }

    public int getItemCount() {
        return this.ImgItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgs;
        TextView name;
        RelativeLayout relativeLayout;
        ImageView zoom;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.imgs = (ImageView) itemView.findViewById(R.id.imgs);
            this.zoom = (ImageView) itemView.findViewById(R.id.zoom);
        }
    }
}
