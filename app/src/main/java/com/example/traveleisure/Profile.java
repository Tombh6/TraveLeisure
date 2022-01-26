package com.example.traveleisure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.traveleisure.Adapter.DestinationsAdapter;
import com.example.traveleisure.Model.Model;
import com.example.traveleisure.Model.Destination;
import com.example.traveleisure.Model.User;
//import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.traveleisure.Model.ModelFirebase.setUserAppData;
import static com.example.traveleisure.Model.ModelFirebase.signOut;

public class profile extends Fragment {
    String userId;
    RecyclerView listProfile;
    DestinationsAdapter adapterProfile;
    DestinationViewModel viewModelProfile;
    SwipeRefreshLayout profileRefreshSwipe;
    Button signOut;
    Button editProfileBtn;
    TextView nameUser;
    CircleImageView profilePic;

    public profile(){}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModelProfile = new ViewModelProvider(this).get(DestinationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        userId = User.getInstance().id;



        profilePic= view.findViewById(R.id.profile_profile_im);

        setUserAppData(User.getInstance().email);
        if (User.getInstance().profilePic != null){
            Picasso.get().load(User.getInstance().profilePic).noPlaceholder().into(profilePic);

        }

        nameUser = view.findViewById(R.id.profile_title);
        nameUser.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        signOut = view.findViewById(R.id.signoutBtn);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();

                startActivity(new Intent(getActivity(), Login.class));
            }
        });

        editProfileBtn= view.findViewById(R.id.profile_Edit_Btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_profile_to_editProfile );
            }
        });

        listProfile = view.findViewById(R.id.profile_list);
        listProfile.setHasFixedSize(true);

        profileRefreshSwipe = view.findViewById(R.id.destinationProfile_list_swipe);
        profileRefreshSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Model.instance.refreshAllDestinations(new Model.GetAllDestinationsListener() {
                    @Override
                    public void onComplete() {
                        profileRefreshSwipe.setRefreshing(false);
                    }
                });

            }
        });
        Log.d("EDIT","profile "+User.getInstance().profilePic);
        viewModelProfile.getDataByUser(userId).observe(getViewLifecycleOwner(), destinationUpdateObserver);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    Observer<List<Destination>> destinationUpdateObserver = new Observer<List<Destination>>() {
        @Override
        public void onChanged(List<Destination> destinationArrayList) {
            List<Destination> data = new LinkedList<Destination>();
            for (Destination destination: destinationArrayList)
                data.add(0, destination);

            destinationArrayList = data;
            List<Destination> finalDestinationArrayList = destinationArrayList;
            adapterProfile = new DestinationsAdapter(getContext(), destinationArrayList, new DestinationsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Destination des = finalDestinationArrayList.get(position);
                    String destinationId = des.getId();
                    profileDirections.ActionProfileToDestinationDetails direction = profileDirections.actionProfileToDestinationDetails(destinationId);
                    Navigation.findNavController(getActivity(), R.id.mainactivity_navhost).navigate(direction);
                    Log.d("TAG", "row was clicked " + destinationId);
                }
            });
            listProfile.setLayoutManager(new LinearLayoutManager(getContext()));
            listProfile.setAdapter(adapterProfile);
        }
    };
}