package com.itvrach.www.itvrach;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.itvrach.adapter.UserAdapter;
import com.itvrach.model.ResponseModel;
import com.itvrach.model.User;
import com.itvrach.services.APIClient;
import com.itvrach.services.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recylerViewUsers;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    private List<User> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.setTitle("PROFILE");

        loadDatafromServer();


    }


    private void loadDatafromServer(){
        recylerViewUsers  = (RecyclerView) findViewById(R.id.recyclerview);
        mManager          = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recylerViewUsers.setLayoutManager(mManager);

        UserService userService = APIClient.getClient().create(UserService.class);
        Call<ResponseModel> call = userService.getFindAll();
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                mItems = response.body().getResult();
                mAdapter = new UserAdapter(mItems, getApplicationContext());
                recylerViewUsers.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }


            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                //Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(ProfileActivity.this, "Server Disconnected",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
