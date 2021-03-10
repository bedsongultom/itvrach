package com.itvrach.services;

import com.itvrach.model.ResponseMenu;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by engineer on 3/24/2019.
 */

public interface MoreService {
    @GET("more/findAll")
    Call<ResponseMenu> getFindAll();
}


