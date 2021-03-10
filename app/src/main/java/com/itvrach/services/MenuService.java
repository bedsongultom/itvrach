package com.itvrach.services;

import com.itvrach.model.ResponseMenu;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by engineer on 2/19/2019.
 */

public interface MenuService {
    @GET("menu/findAll")
    Call<ResponseMenu> getFindAll();
}
