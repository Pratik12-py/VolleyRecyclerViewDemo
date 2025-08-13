package com.example.volleycalling;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<User> userList;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            userList = new ArrayList<>();
            adapter = new UserAdapter(this, userList);
            recyclerView.setAdapter(adapter);

            fetchData();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void fetchData() {
        String url = "https://api.github.com/users";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        parseData(response);
                    } catch (Exception e) {
                        showError(e);
                    }
                },
                error -> showError(error)
        );

        queue.add(request);
    }

    private void parseData(JSONArray response) throws Exception {
        userList.clear();
        for (int i = 0; i < response.length(); i++) {
            JSONObject obj = response.getJSONObject(i);
            String login = obj.getString("login");
            String avatarUrl = obj.getString("avatar_url");
            userList.add(new User(login, avatarUrl));
        }
        adapter.notifyDataSetChanged();
    }

    private void showError(Exception e) {
        Log.e("VolleyError", "Error: ", e);
        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
    }
}
