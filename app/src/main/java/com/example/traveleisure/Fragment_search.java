package com.example.traveleisure;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;


public class Fragment_search extends Fragment {

    Button Asia;
    Button Africa;
    Button Australia;
    Button Europe;
    Button SouthAmerica;
    Button Antarctica;
    Button NorthAmerica;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Asia= view.findViewById(R.id.search_Asia);
        Africa= view.findViewById(R.id.search_Africa);
        Australia= view.findViewById(R.id.search_Australia);
        Europe = view.findViewById(R.id.search_Europe);
        SouthAmerica= view.findViewById(R.id.search_SouthAmerica);
        Antarctica=view.findViewById(R.id.search_Antarctica);
        NorthAmerica= view.findViewById(R.id.search_NorthAmerica);

        Asia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Asia");
                Navigation.findNavController(view).navigate(action);
            }
        });

        Africa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Africa");
                Navigation.findNavController(view).navigate(action);
            }
        });

        Australia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Australia");
                Navigation.findNavController(view).navigate(action);
            }
        });

        Europe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Europe");
                Navigation.findNavController(view).navigate(action);
            }
        });

        SouthAmerica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("SouthAmerica");
                Navigation.findNavController(view).navigate(action);
            }
        });


        Antarctica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("Antarctica");
                Navigation.findNavController(view).navigate(action);
            }
        });

        NorthAmerica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_searchDirections.ActionFragmentSearchToResult action = Fragment_searchDirections.actionFragmentSearchToResult("NorthAmerica");
                Navigation.findNavController(view).navigate(action);
            }
        });
        return view;
    }
}