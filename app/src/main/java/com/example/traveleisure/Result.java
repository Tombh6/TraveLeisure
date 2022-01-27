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
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.traveleisure.Adapter.DestinationsAdapter;
import com.example.traveleisure.Model.Model;
import com.example.traveleisure.Model.Destination;
import java.util.LinkedList;
import java.util.List;

public class Result extends Fragment {

    String category;
    RecyclerView listResults;
    DestinationsAdapter adapterResults;
    DestinationViewModel viewModelResults;
    SwipeRefreshLayout resultsRefreshSwipe;
    TextView noResults;
    ProgressBar pb;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModelResults = new ViewModelProvider(this).get(DestinationViewModel.class);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        category = ResultArgs.fromBundle(getArguments()).getCategory();
        Log.d("TAG","arg_category: "+category);

        noResults= view.findViewById(R.id.result_txtView);
        noResults.setVisibility(View.INVISIBLE);

        listResults= view.findViewById(R.id.result_recycler);
        listResults.setHasFixedSize(true);

        resultsRefreshSwipe = view.findViewById(R.id.destinationResults_list_swipe);
        resultsRefreshSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Model.instance.refreshAllDestinations(new Model.GetAllDestinationsListener() {
                    @Override
                    public void onComplete() {
                        resultsRefreshSwipe.setRefreshing(false);
                    }
                });

            }
        });

        viewModelResults.getDestinationsByCategory(category).observe(getViewLifecycleOwner(), destinationUpdateObserver);
        return view;
    }

    Observer<List<Destination>> destinationUpdateObserver = new Observer<List<Destination>>() {
        @Override
        public void onChanged(List<Destination> destinationArrayList) {
            List<Destination> data = new LinkedList<Destination>();

            for (Destination destination: destinationArrayList)
                data.add(0, destination);
            destinationArrayList = data;

            if(data.size()==0){
                noResults.setVisibility(View.VISIBLE);
            }

            List<Destination> finalDestinationArrayList = destinationArrayList;
            adapterResults = new DestinationsAdapter(getContext(), destinationArrayList, new DestinationsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Destination des = finalDestinationArrayList.get(position);
                    String destinationId = des.getId();
                    ResultDirections.ActionResultToDestinationDetails direction = ResultDirections.actionResultToDestinationDetails(destinationId);
                    Navigation.findNavController(getActivity(), R.id.mainactivity_navhost).navigate(direction);
                    Log.d("TAG", "row was clicked " + destinationId);
                }
            });
            listResults.setLayoutManager(new LinearLayoutManager(getContext()));
            listResults.setAdapter(adapterResults);
        }
    };
}