package com.example.mtmp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.Image;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnimationActivity extends AppCompatActivity {
    private ArrayList<PointItem> pointItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

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

            ImageView image = findViewById(R.id.image);
            animation(pointItemArrayList, image);
        }
    }

    public void getCalculationItemArrayList(Double speed, Double angle) {
        double g = 9.81, timeStep = 0.1, time = 0.0, x = 0.0, y = 0.0;
        double timeStop = (2 * speed * Math.sin(Math.toRadians(angle))) / g;
        initCalculationItemArrayList();

        final int position = 0;
        for(; time < timeStop; time += timeStep){
            x = speed * time * Math.cos(Math.toRadians(angle));
            y = speed * time * Math.sin(Math.toRadians(angle)) - (g * Math.pow(time, 2)) / 2;

            pointItemArrayList.add(position, new PointItem(time, x, y));
        }

        pointItemArrayList.add(position, new PointItem(timeStop, x, 0.0));
    }

    public void initCalculationItemArrayList(){
        if(pointItemArrayList == null)
            pointItemArrayList = new ArrayList<>();
    }

    private void animation(List<PointItem> points, ImageView imageView){
        points = reverseArrayList(points);

        ObjectAnimator[][] animators = new ObjectAnimator[2][points.size()];
        for (int i = 0; i < animators[0].length; i++){

            ObjectAnimator animX = ObjectAnimator.ofFloat(imageView, "translationX", points.get(i).getxData().floatValue());
            animX.setInterpolator(new LinearInterpolator());
            animX.setDuration(25);

            ObjectAnimator animY = ObjectAnimator.ofFloat(imageView, "translationY", -points.get(i).getyData().floatValue());
            animY.setInterpolator(new LinearInterpolator());
            animY.setDuration(25);

            animators[0][i] = animX;
            animators[1][i] = animY;
        }

        AnimatorSet animatorSetX = new AnimatorSet();
        AnimatorSet animatorSetY = new AnimatorSet();

        for (int i = 1; i < animators[0].length; i++){
            animatorSetX.play(animators[0][i]).after(animators[0][i-1]);
            animatorSetY.play(animators[1][i]).after(animators[1][i-1]);
        }

        animatorSetX.start();
        animatorSetY.start();
    }

    public void getOnlineData(Double speed, Double angle){
        RequestQueue volleyQueue = Volley.newRequestQueue(AnimationActivity.this);
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
                ImageView image = findViewById(R.id.image);
                animation(pointItemArrayList, image);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AnimationActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });

        // add the json request object created above
        // to the Volley request queue
        volleyQueue.add(jsonArrayRequest);
    }

    // Takes an arraylist as a parameter and returns
    // a reversed arraylist
    public List<PointItem> reverseArrayList(List<PointItem> alist)
    {
        // Arraylist for storing reversed elements
        ArrayList<PointItem> revArrayList = new ArrayList<PointItem>();
        for (int i = alist.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }

        // Return the reversed arraylist
        return revArrayList;
    }
}
