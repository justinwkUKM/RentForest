package com.example.yeefang.rentforest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by Yee Fang on 10/09/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ImageLoader imageLoader;
    private Context context;

    //List of albums
    List<ItemData> alba;

    public MyAdapter(List<ItemData> alba, Context context){
        super();
        //Getting all the superheroes
        this.alba = alba;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        Log.e("onCreateViewHolder", "viewHolder Created");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final ItemData album =  alba.get(position);

        imageLoader = CustomVolleyRequestRooms.getInstance(context).getImageLoader();
        //imageLoader.get(album.getImageURL(), ImageLoader.getImageListener(holder.imgViewIcon, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.imgViewIcon.setImageUrl(album.getImageURL(), imageLoader);
        holder.txtViewName.setText(album.getName());
        holder.txtViewPrice.setText(album.getPrice());
        holder.txtViewPropertyType.setText(album.getPropertyType());
        holder.txtViewPublicAddress.setText(album.getPublicAddress());



        holder.imgViewIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, RoomRentalList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
            }
        });

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Log.e("OnLongClick", "calling");
                    Toast.makeText(context, "#" +position+" " + album.getName()+
                            " OnLongClick ", Toast.LENGTH_SHORT).show();

                } else {
                    Log.e("OnClick", "Clicked View: ");
                    Toast.makeText(context, "#"+position+" " + album.getName(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return alba.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener {
        public NetworkImageView imgViewIcon;
        public TextView txtViewName, txtViewPrice, txtViewPropertyType, txtViewPublicAddress;
        private ItemClickListener clickListener;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewName = (TextView) itemLayoutView.findViewById(R.id.item_name);
            txtViewPrice = (TextView) itemLayoutView.findViewById(R.id.item_price);
            txtViewPropertyType = (TextView) itemLayoutView.findViewById(R.id.item_property_type);
            txtViewPublicAddress = (TextView) itemLayoutView.findViewById(R.id.item_public_address);
            imgViewIcon = (NetworkImageView) itemLayoutView.findViewById(R.id.item_icon);
            Log.e("ViewHolder", "Constructor Method");
            itemLayoutView.setTag(itemView);
            itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }
}
