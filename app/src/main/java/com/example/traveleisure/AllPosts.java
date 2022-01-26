package com.example.traveleisure;

import android.content.Context;
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
import android.widget.ProgressBar;
import com.example.traveleisure.Adapter.DestinationsAdapter;
import com.example.traveleisure.Model.Model;
import com.example.traveleisure.Model.ModelFirebase;
import com.example.traveleisure.Model.Destination;
import com.example.traveleisure.Model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;


public class AllPosts extends Fragment {

    RecyclerView list;
    DestinationsAdapter adapter;
    DestinationViewModel viewModel;
    SwipeRefreshLayout allPostsRefreshSwipe;
    Button addBtn;
    ProgressBar pb;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(DestinationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_all_posts, container, false);



        // added button from all post to add post.
        addBtn= view.findViewById(R.id.AllPost_addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_allPosts_to_addPost );
            }
        });

        list = view.findViewById(R.id.main_recycler_v);
        list.setHasFixedSize(true);

        allPostsRefreshSwipe = view.findViewById(R.id.destination_list_swipe);
        allPostsRefreshSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Model.instance.refreshAllDestinations(new Model.GetAllDestinationsListener() {
                    @Override
                    public void onComplete() {
                        allPostsRefreshSwipe.setRefreshing(false);
                    }
                });

            }
        });

        viewModel.getData().observe(getViewLifecycleOwner(), destinationUpdateObserver);
        return view;
    }

    Observer<List<Destination>> destinationUpdateObserver = new Observer<List<Destination>>() {
        @Override
        public void onChanged(List<Destination> destinationArrayList) {
            List<Destination> data = new LinkedList<Recipe>();
            for (Destination destination: destinationArrayList)
                data.add(0, destination);

            destinationArrayList = data;
            List<Destination> finalDestinationArrayList = destinationArrayList;
            adapter = new DestinationsAdapter(getContext(), destinationArrayList, new DestinationsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Destination des = finalDestinationArrayList.get(position);
                    String recipeId = des.getId();
                    AllPostsDirections.ActionAllPostsToRecipeDetails direction = AllPostsDirections.actionAllPostsToDestinationDetails(destinationId);
                    Navigation.findNavController(getActivity(), R.id.mainactivity_navhost).navigate(direction);
                    Log.d("TAG", "row was clicked " + destinationId);
                }
            });
            list.setLayoutManager(new LinearLayoutManager(getContext()));
            list.setAdapter(adapter);
        }
    };
}