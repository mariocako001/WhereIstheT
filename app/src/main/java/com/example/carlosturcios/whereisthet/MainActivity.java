// Created by : Mario Cako and Carlos Turcios

package com.example.carlosturcios.whereisthet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Spinner trainSpinner, stopSpinner;
    ArrayAdapter<CharSequence> adapter;

    String trainColor = null;
    String selectedStation = null;

    private String RED_URL = "http://developer.mbta.com/lib/rthr/red.json";
    private String ORANGE_URL = "http://developer.mbta.com/lib/rthr/orange.json";
    private String BLUE_URL = "http://developer.mbta.com/lib/rthr/blue.json";

    String urlInbound = null;
    String urlOutbound = null;

    private JSONObject jsonObject;
    public final static String DEBUG_TAG = "edu.umb.cs443.MYMSG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // allocate the spinners objects
        trainSpinner = (Spinner) findViewById(R.id.trainLine);
        stopSpinner = (Spinner) findViewById(R.id.stationList);
        trainSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        String entry = String.valueOf(trainSpinner.getSelectedItem());
        trainColor = entry;

        TextView inboundTextView = (TextView) findViewById(R.id.inbound);
        TextView outboundTextView = (TextView) findViewById(R.id.outbound);
        inboundTextView.setText(null);
        outboundTextView.setText(null);

        // clear the spinners and the times
        if (entry.equals("-- Select a train --")) {
            final ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this,
                    R.array.empty, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            stopSpinner.setAdapter(dataAdapter);

            inboundTextView.setText(null);
            outboundTextView.setText(null);
        } else if (entry.contentEquals("Red Line")) {

            final ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this,
                    R.array.RedLine, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            stopSpinner.setAdapter(dataAdapter);
            stopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    // set the directions for red line to northbound and southbound
                    TextView northBound = (TextView) findViewById(R.id.inboundText);
                    TextView southBound = (TextView) findViewById(R.id.outboundText);
                    northBound.setText("NorthBound -> Alewife");
                    southBound.setText("SouthBound -> Ashmont/Braintree");

                    // clear the time from the previous onItemSelected
                    TextView inboundTextView = (TextView) findViewById(R.id.inbound);
                    TextView outboundTextView = (TextView) findViewById(R.id.outbound);
                    inboundTextView.setText(null);
                    outboundTextView.setText(null);

                    selectedStation = adapterView.getItemAtPosition(i).toString();

                    Toast.makeText(getApplicationContext(), String.format("Selected: %s, %s", trainColor, selectedStation),
                            Toast.LENGTH_SHORT).show();

                    dataAdapter.notifyDataSetChanged();

                    // set the url to the right station id
                    switch (selectedStation) {

                        case "Alewife":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Alewife_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Alewife_o);
                            break;

                        case "Davis":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Davis_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Davis_o);

                            break;

                        case "Porter":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Porter_Square_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Porter_Square_o);
                            break;

                        case "Harvard":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Harvard_Square_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Harvard_Square_o);
                            break;

                        case "Central":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Central_Square_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Central_Square_o);
                            break;

                        case "Kendall/MIT":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Kendall_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Kendall_o);
                            break;

                        case "Charles/MGH":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Charles_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Charles_o);
                            break;

                        case "Park St":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Park_Street_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Park_Street_o);
                            break;

                        case "Downtown Crossing":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Downtown_Crossing_oi);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Downtown_Crossing_oo);
                            break;

                        case "South Station":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.South_Station_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.South_Station_o);
                            break;

                        case "Broadway":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Broadway_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Broadway_o);
                            break;

                        case "Andrew":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Andrew_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Andrew_o);
                            break;

                        case "JFK/UMASS":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.JFK_b);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.JFK_bi);
                            break;

                        case "North Quincy":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.North_Quincy_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.North_Quincy_o);
                            break;

                        case "Wollaston":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Wollaston_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Wollaston_o);
                            break;

                        case "Quincy Center":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Quincy_Center_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Quincy_Center_o);
                            break;

                        case "Quincy Adams":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Quincy_Adams_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Quincy_Adams_o);
                            break;

                        case "Braintree":
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Braintree);
                            break;

                        case "Savin Hill":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Savin_Hill_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Savin_Hill_o);
                            break;

                        case "Fields Corner":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Fields_Corner_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Fields_Corner_o);
                            break;

                        case "Shawmut":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Shawmut_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Shawmut_o);
                            break;

                        case "Ashmont":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Ashmont_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Ashmont_o);
                            break;
                    }


                    // start downloading
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new MBTAAsyncTaskInbound().execute(urlInbound);
                        new MBTAAsyncTaskOutbound().execute(urlOutbound);
                    } else {
                        Toast.makeText(getApplicationContext(), "No network connection available", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } else if (entry.contentEquals("Orange Line")) {

            final ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this,
                    R.array.OrangeLine, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            stopSpinner.setAdapter(dataAdapter);
            stopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    // set the directions for orange line to northbound and southbound
                    TextView northBound = (TextView) findViewById(R.id.inboundText);
                    TextView southBound = (TextView) findViewById(R.id.outboundText);
                    northBound.setText("NorthBound -> Forest Hills");
                    southBound.setText("SouthBound -> Oak Grove");

                    TextView inboundTextView = (TextView) findViewById(R.id.inbound);
                    TextView outboundTextView = (TextView) findViewById(R.id.outbound);
                    inboundTextView.setText(null);
                    outboundTextView.setText(null);

                    selectedStation = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), String.format("Selected: %s, %s", trainColor, selectedStation),
                            Toast.LENGTH_SHORT).show();

                    dataAdapter.notifyDataSetChanged();


                    // set the url to the right station id
                    switch (selectedStation) {

                        case "Oak Grove":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Oak_Grove_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Oak_Grove_o);
                            break;

                        case "Malden center":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Malden_Center_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Malden_Center_o);
                            break;

                        case "Wellington":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Wellington_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Wellington_o);
                            break;

                        case "Assembly":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Assembly_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Assembly_o);
                            break;

                        case "Sullivan Sq":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Sullivan_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Sullivan_o);
                            break;

                        case "Community College":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Community_College_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Community_College_o);
                            break;

                        case "North Station":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.North_Station_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.North_Station_o);
                            break;

                        case "Haymarket":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Haymarket_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Haymarket_o);
                            break;

                        case "State":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.State_Street_oi);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.State_Street_oo);
                            break;

                        case "Downtown Crossing":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Downtown_Crossing_oi);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Downtown_Crossing_oo);
                            break;

                        case "Chinatown":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Chinatown_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Chinatown_o);
                            break;

                        case "Tufts Medical Center":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Tufts_Medical_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Tufts_Medical_o);
                            break;

                        case "Back Bay":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Back_Bay_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Back_Bay_o);
                            break;

                        case "Mass. Ave":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Mass_Ave_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Mass_Ave_o);
                            break;

                        case "Ruggles":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Ruggles_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Ruggles_o);
                            break;

                        case "Roxbury Crossing":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Roxbury_Crossing_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Roxbury_Crossing_o);
                            break;

                        case "Jackson Sq":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Jackson_Square_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Jackson_Square_o);
                            break;

                        case "Stony Brook":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Stony_Brook_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Stony_Brook_o);
                            break;

                        case "Green St":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Green_Street_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Green_Street_o);
                            break;

                        case "Forest Hills":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Forest_Hills_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Forest_Hills_o);
                            break;
                    }

                    // start downloading
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new MBTAAsyncTaskInbound().execute(urlInbound);
                        new MBTAAsyncTaskOutbound().execute(urlOutbound);
                    } else {
                        Toast.makeText(getApplicationContext(), "No network connection available", Toast.LENGTH_SHORT);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } else if (entry.contentEquals("Green Line")) {

            final ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this,
                    R.array.GreenLine, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            stopSpinner.setAdapter(dataAdapter);
            stopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    TextView inboundTextView = (TextView) findViewById(R.id.inbound);
                    TextView outboundTextView = (TextView) findViewById(R.id.outbound);
                    inboundTextView.setText(null);
                    outboundTextView.setText(null);

                    selectedStation = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), String.format("Selected: %s, %s", trainColor, selectedStation),
                            Toast.LENGTH_SHORT).show();
                    dataAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } else if (entry.contentEquals("Blue Line")) {

            final ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this,
                    R.array.BlueLine, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            stopSpinner.setAdapter(dataAdapter);
            stopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    // set the directions for blue line to westbound and eastbound
                    TextView westbound = (TextView) findViewById(R.id.inboundText);
                    TextView eastbound = (TextView) findViewById(R.id.outboundText);
                    westbound.setText("Westbound -> Bowdoin");
                    eastbound.setText("Eastbound -> Wonderland");

                    TextView inboundTextView = (TextView) findViewById(R.id.inbound);
                    TextView outboundTextView = (TextView) findViewById(R.id.outbound);
                    inboundTextView.setText(null);
                    outboundTextView.setText(null);

                    selectedStation = adapterView.getItemAtPosition(i).toString();

                    Toast.makeText(getApplicationContext(), String.format("Selected: %s, %s", trainColor, selectedStation),
                            Toast.LENGTH_SHORT).show();

                    dataAdapter.notifyDataSetChanged();

                    // set the url to the right station id
                    switch (selectedStation) {

                        case "Bowdoin":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Bowdoin_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Bowdoin_o);
                            break;

                        case "Government Center":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Government_Center_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Government_Center_o);
                            break;

                        case "State":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.State_Street_bi);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.State_Street_bo);
                            break;

                        case "Aquarium":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Aquarium_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Aquarium_o);
                            break;

                        case "Maverick":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Maverick_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Maverick_o);
                            break;

                        case "Airport":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Airport_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Airport_o);
                            break;

                        case "Wood Island":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Wood_Island_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Wood_Island_o);
                            break;

                        case "Orient Heights":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Orient_Heights_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Orient_Heights_o);
                            break;

                        case "Suffolk Downs":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Suffolk_Downs_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Suffolk_Downs_o);
                            break;

                        case "Beachmont":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Beachmont_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Beachmont_o);
                            break;

                        case "Revere Beach":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Revere_Beach_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Revere_Beach_o);
                            break;

                        case "Wonderland":
                            // inbound
                            urlInbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Wonderland_i);
                            // outbound
                            urlOutbound = "https://api-v3.mbta.com/predictions?filter[stop]=" + getResources().getInteger(R.integer.Wonderland_o);
                            break;
                    }


                    // start downloading
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        new MBTAAsyncTaskInbound().execute(urlInbound);
                        new MBTAAsyncTaskOutbound().execute(urlOutbound);
                    } else {
                        Toast.makeText(getApplicationContext(), "No network connection available", Toast.LENGTH_SHORT);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private class MBTAAsyncTaskInbound extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {
                return downloadWeather(urls[0]);
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                TextView inbound = (TextView) findViewById(R.id.inbound);
                inbound.setText(result);
            } else {
                Log.i(DEBUG_TAG, "Result is null");
                Toast.makeText(getApplicationContext(), "No data downloaded. Inbound train might not be available at this time.",
                        Toast.LENGTH_SHORT).show();
            }
        }

        private String downloadWeather(String newUrl) throws IOException {

            InputStream is = null;
            try {
                URL url = new URL(newUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(2000);
                connection.setConnectTimeout(2000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
                is = connection.getInputStream();
                if (is == null) {
                    return null;
                }

                BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                StringBuilder input = new StringBuilder();
                while ((line = bReader.readLine()) != null) {
                    input.append(line);
                }
                jsonObject = new JSONObject(input.toString());

                JSONObject jObject = jsonObject.getJSONArray("data").getJSONObject(0);
                String s = jObject.getJSONObject("attributes").getString("arrival_time");

                DateFormat formatter;
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                Date date = (Date) formatter.parse(s);
                String returnString = String.format("%d : %d : %d", date.getHours(), date.getMinutes(), date.getSeconds());
                return returnString;

            } catch (Exception e) {
                Log.i(DEBUG_TAG, e.toString());
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            return null;
        }
    }

    private class MBTAAsyncTaskOutbound extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {
                return downloadWeather(urls[0]);
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                TextView outbound = (TextView) findViewById(R.id.outbound);
                outbound.setText(result);
            } else {
                Log.i(DEBUG_TAG, "Result is null");
                Toast.makeText(getApplicationContext(), "No data downloaded. Outbound train might not be available at this time.",
                        Toast.LENGTH_SHORT).show();
            }
        }

        private String downloadWeather(String newUrl) throws IOException {

            InputStream is = null;
            try {
                URL url = new URL(newUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(2000);
                connection.setConnectTimeout(2000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
                is = connection.getInputStream();
                if (is == null) {
                    return null;
                }

                BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                StringBuilder input = new StringBuilder();
                while ((line = bReader.readLine()) != null) {
                    input.append(line);
                }
                jsonObject = new JSONObject(input.toString());

                JSONObject jObject = jsonObject.getJSONArray("data").getJSONObject(0);
                String s = jObject.getJSONObject("attributes").getString("arrival_time");

                DateFormat formatter;
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                Date date = (Date) formatter.parse(s);
                String returnString = String.format("%d : %d : %d", date.getHours(), date.getMinutes(), date.getSeconds());
                return returnString;

            } catch (Exception e) {
                Log.i(DEBUG_TAG, e.toString());
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            return null;
        }
    }

}

