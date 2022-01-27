package com.example.traveleisure;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.traveleisure.Model.Model;
import com.example.traveleisure.Model.Destination;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DestinationDetails extends Fragment {

    FirebaseUser user;
    String destinationId;
    Destination dst;
    Destination desDel;
    TextView destinationTitle;
    TextView nickname;
    TextView category;
    TextView detailDestination;
    ImageView pictureDestination;
    ImageView edit_btn;
    ImageView deleteDestination;
    ImageView closeWindow;
    CircleImageView profilePic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_destination_details, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        nickname = view.findViewById(R.id.details_nickname);
        destinationTitle = view.findViewById(R.id.details_destinationTitle);
        category = view.findViewById(R.id.details_category);
        detailDestination = view.findViewById(R.id.details_detailDestination);
        closeWindow = view.findViewById(R.id.details_closeImg);
        pictureDestination = view.findViewById(R.id.details_image);
        destinationId = DestinationDetailsArgs.fromBundle(getArguments()).getDestinationId();
        edit_btn= view.findViewById(R.id.details_editImg);
        deleteDestination= view.findViewById(R.id.details_deleteImg);
        profilePic= view.findViewById(R.id.detailsprofile_profile_im);
        edit_btn.setVisibility(View.INVISIBLE);
        deleteDestination.setVisibility(View.INVISIBLE);

        Model.instance.getDestination(destinationId, new Model.GetDestinationListener() {
            @Override
            public void onComplete(Destination destination) {
                dst = destination;
                nickname.setText(dst.getUserName());
                destinationTitle.setText(dst.getTitleDestination());
                category.setText(dst.getCategory());
                detailDestination.setText(dst.getDestination());
                pictureDestination.setImageResource(R.drawable.destination_placeholder);
                if (destination.getImageUrl() != null) {
                    Picasso.get().load(destination.getImageUrl()).placeholder(R.drawable.destination_placeholder).into(pictureDestination);
                }
                if(dst.getUserId().equals(user.getUid())){
                    edit_btn.setVisibility(View.VISIBLE);
                    deleteDestination.setVisibility(View.VISIBLE);
                }

                if( destination.getUserPic()!=null){
                    Picasso.get().load(destination.getUserPic()).placeholder(R.drawable.ic_round_person_grey).into(profilePic);
                }
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DestinationDetailsDirections.ActionDestinationDetailsToEditDestination direction = DestinationDetailsDirections.actionDestinationDetailsToEditDestination(destinationId);
                Navigation.findNavController(getActivity(), R.id.mainactivity_navhost).navigate(direction);
                Log.d("TAG", "Destination Id i sent : " + destinationId);
            }
        });

        closeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        deleteDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance.getDestination(destinationId, new Model.GetDestinationListener() {
                    @Override
                    public void onComplete(Destination destination) {
                        desDel = destination;
                        Model.instance.deleteDestination(desDel, new Model.DeleteDestinationListener() {
                            @Override
                            public void onComplete() {
                                Navigation.findNavController(view).popBackStack();
                            }
                        });
                    }
                });
            }
        });

        return view;
    }
}