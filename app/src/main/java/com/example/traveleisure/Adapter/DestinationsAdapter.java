package com.example.traveleisure.Adapter;



import android.content.Context;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.traveleisure.Model.Destination;
        import com.example.traveleisure.R;
        import com.example.traveleisure.Model.Destination;
        import com.squareup.picasso.Picasso;
        import java.util.List;
        import de.hdodenhof.circleimageview.CircleImageView;


public class DestinationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private OnItemClickListener listener;
    Context context;
    List<Destination> destinationArrayList;

    public DestinationsAdapter(Context context, List<Destination> destinationArrayList, OnItemClickListener onClickListener) {
        this.context = context;
        this.destinationArrayList = destinationArrayList;
        this.listener = onClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
        return new DestinationsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Destination destination = destinationArrayList.get(position);
        DestinationsViewHolder viewHolder = (DestinationsViewHolder) holder;
        // getImageFromFireBase(destination);
        viewHolder.profilePic.setImageResource(R.drawable.ic_round_person_grey);
        viewHolder.nickname.setText(destination.getUserName());
        viewHolder.destinationTitle.setText(destination.getTitleDestination());
        viewHolder.category.setText(destination.getCategory());
        viewHolder.postImg.setImageResource(R.drawable.icon_upload_image);
        if (destination.getImageUrl() != null) {
            Picasso.get().load(destination.getImageUrl()).placeholder(R.drawable.destination_placeholder).into(viewHolder.postImg);
        }

        if( destination.getUserPic()!=null){
            Picasso.get().load(destination.getUserPic()).placeholder(R.drawable.ic_round_person_grey).into(viewHolder.profilePic);
        }
    }


    @Override
    public int getItemCount() {
        return destinationArrayList.size();
    }

    class DestinationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView profilePic;
        TextView nickname;
        TextView destinationTitle;
        TextView category;
        ImageView postImg;

        public DestinationsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            profilePic=itemView.findViewById(R.id.detailsprofile_profile_im);
            nickname = itemView.findViewById(R.id.listRow_nickname);
            destinationTitle=itemView.findViewById(R.id.listRow_titleRec);
            category= itemView.findViewById(R.id.listRow_category);
            postImg=itemView.findViewById(R.id.listRow_img);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            listener.onItemClick(position);
        }
    }
}
