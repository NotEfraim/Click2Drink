package com.capstone.click2drink;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SquareGallonFragment extends Fragment {

    public static final String SQUARE_URL = "http://resources.click2drinkph.store/php/getSquareGallon.php";
    private RecyclerView holder;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager manager;
    private List<Product_Dispenser> squareGallon;
    private LottieAnimationView progress;
    private TextView error;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.square_layout,container,false);

        // No content
        error = root.findViewById(R.id.error);
        //
        progress = root.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        //Fragment with Recycler View
        holder = root.findViewById(R.id.recycler);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        holder.setLayoutManager(manager);
        squareGallon = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, SQUARE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            //Getting Response form api (Base Url)
                            JSONArray array = new JSONArray(response);

                            // Image , price at name lang need natin sa product

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);

                                String shop_id = object.getString("shop_id");
                                String product_id = object.getString("prod_id");
                                String image_url = object.getString("image_url");
                                String product_name = object.getString("prod_name");
                                String product_price = object.getString("prod_price");
                                String location = object.getString("location");
                                Product_Dispenser data = new Product_Dispenser(product_id,product_name,product_price,image_url,location,shop_id);

                                squareGallon.add(data);


                            }
                        } catch (Exception e) {
                             error.setVisibility(View.VISIBLE);
//                            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        }

                        mAdapter = new DispenserAdapter(getContext(), squareGallon);
                        holder.setAdapter(mAdapter);
                        progress.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getContext()).add(stringRequest);

        return root;
    }
}
