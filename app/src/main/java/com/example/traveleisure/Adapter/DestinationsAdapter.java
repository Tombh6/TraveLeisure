package com.example.traveleisure.Adapter;



import android.content.Context;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;
        import com.example.traveleisure.R;
        import com.example.traveleisure.model.Recipe;
        import com.squareup.picasso.Picasso;
        import java.util.List;
        import de.hdodenhof.circleimageview.CircleImageView;


public class DestinationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private OnItemClickListener listener;
    Context context;
    List<Destination> destinationArrayList;

    public RecipesAdapter(Context context, List<Destination> recipeArrayList, OnItemClickListener onClickListener) {
        this.context = context;
        this.destinationArrayList = recipeArrayList;
        this.listener = onClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
        return new RecipesViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Destination recipe = destinationArrayList.get(position);
        RecipesViewHolder viewHolder = (RecipesViewHolder) holder;
        // getImageFromFireBase(recipe);
        viewHolder.profilePic.setImageResource(R.drawable.ic_round_person_grey);
        viewHolder.nickname.setText(destination.getUserName());
        viewHolder.recipeTitle.setText(destination.getTitleRecipe());
        viewHolder.category.setText(destination.getCategory());
        viewHolder.postImg.setImageResource(R.drawable.icon_upload_image);
        if (destination.getImageUrl() != null) {
            Picasso.get().load(recipe.getImageUrl()).placeholder(R.drawable.recipe_placeholder).into(viewHolder.postImg);
        }

        if( recipe.getUserPic()!=null){
            Picasso.get().load(recipe.getUserPic()).placeholder(R.drawable.ic_round_person_grey).into(viewHolder.profilePic);
        }
    }


    @Override
    public int getItemCount() {
        return destinationArrayList.size();
    }

    class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView profilePic;
        TextView nickname;
        TextView destinationTitle;
        TextView category;
        ImageView postImg;

        public RecipesViewHolder(@NonNull View itemView) {
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
