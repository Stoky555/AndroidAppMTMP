package com.example.mtmp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {
    GraphView graphView;
    private ArrayList<PointItem> pointItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // on below line we are initializing our graph view.
        graphView = findViewById(R.id.idGraphView);

        double speed = 0.0, angle = 0.0;
        boolean switchInternet = false;

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            speed = extras.getDouble("speed");
            angle = extras.getDouble("angle");
            switchInternet = extras.getBoolean("switch");
        }

        if(switchInternet){
            initCalculationItemArrayList();
            getOnlineData(speed, angle);
        }
        else{
            getCalculationItemArrayList(speed, angle);

            // on below line we are adding data to our graph view.
            LineGraphSeries<DataPoint> seriesY = new LineGraphSeries<>();
            for (int i = pointItemArrayList.size() - 1; i >= 0; i--) {
                Double y = pointItemArrayList.get(i).getyData();
                Double t = pointItemArrayList.get(i).getTimeData();

                seriesY.appendData(new DataPoint(t, y), true, pointItemArrayList.size());
            }
            seriesY.setColor(Color.BLACK);
            seriesY.setTitle("Y");

            // after adding data to our line graph series.
            // on below line we are setting
            // title for our graph view.
            graphView.setTitle("Fire graph");

            // on below line we are setting
            // text color to our graph view.
            graphView.setTitleColor(R.color.purple_200);

            // on below line we are setting
            // our title text size.
            graphView.setTitleTextSize(18);

            // on below line we are adding
            // data series to our graph view.
            graphView.addSeries(seriesY);

            graphView.getLegendRenderer().setVisible(true);

            graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getViewport().setMaxX(pointItemArrayList.get(0).getTimeData() + 1);

            GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
            gridLabelRenderer.setHorizontalAxisTitle("Time");
            gridLabelRenderer.setVerticalAxisTitle("Y");
        }
    }

    public void getCalculationItemArrayList(Double speed, Double angle) {
        double g = 9.81, timeStep = 0.1, time = 0.0, x = 0.0, y = 0.0;
        double timeStop = (2 * speed * Math.sin(Math.toRadians(angle))) / g;
        initCalculationItemArrayList();

        final int position = 0;
        for(; time < timeStop; time += timeStep){
            x = speed * time * Math.cos(Math.toRadians(angle));
            y = speed * time * Math.sin(Math.toRadians(angle)) - ((g * Math.pow(time, 2)) / 2);

            pointItemArrayList.add(position, new PointItem(time, x, y));
        }

        pointItemArrayList.add(position, new PointItem(timeStop, x, 0.0));
    }

    public void initCalculationItemArrayList(){
        if(pointItemArrayList == null)
            pointItemArrayList = new ArrayList<>();
    }

    public void getOnlineData(Double speed, Double angle){
        RequestQueue volleyQueue = Volley.newRequestQueue(GraphActivity.this);
        // url of the api through which we get random dog images
        String url = "http://10.0.2.2:8081/process_get?angle=" + angle + "&speed=" + speed;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                final int position = 0;

                for (int i = 0; i < response.length(); i++) {
                    // creating a new json object and
                    // getting each object from our json array.
                    try {
                        // we are getting each json object.
                        JSONObject responseObj = response.getJSONObject(i);

                        // now we get our response from API in json object format.
                        // in below line we are extracting a string with
                        // its key value from our json object.
                        // similarly we are extracting all the strings from our json object.
                        Double x = responseObj.getDouble("x");
                        Double y = responseObj.getDouble("y");
                        Double t = responseObj.getDouble("t");

                        pointItemArrayList.add(position, new PointItem(t, x, y));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // on below line we are adding data to our graph view.
                LineGraphSeries<DataPoint> seriesY = new LineGraphSeries<>();
                for (int i = pointItemArrayList.size() - 1; i >= 0; i--) {
                    Double y = pointItemArrayList.get(i).getyData();
                    Double t = pointItemArrayList.get(i).getTimeData();

                    seriesY.appendData(new DataPoint(t, y), true, pointItemArrayList.size()*10000);
                }
                seriesY.setColor(Color.BLACK);
                seriesY.setTitle("Y");

                // after adding data to our line graph series.
                // on below line we are setting
                // title for our graph view.
                graphView.setTitle("Fire graph");

                // on below line we are setting
                // text color to our graph view.
                graphView.setTitleColor(R.color.purple_200);

                // on below line we are setting
                // our title text size.
                graphView.setTitleTextSize(18);

                // on below line we are adding
                // data series to our graph view.
                graphView.addSeries(seriesY);

                graphView.getLegendRenderer().setVisible(true);

                graphView.getViewport().setXAxisBoundsManual(true);
                graphView.getViewport().setMaxX(pointItemArrayList.get(0).getTimeData() + 1);

                GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
                gridLabelRenderer.setHorizontalAxisTitle("Time");
                gridLabelRenderer.setVerticalAxisTitle("Y");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GraphActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });

        // add the json request object created above
        // to the Volley request queue
        volleyQueue.add(jsonArrayRequest);
    }
}
