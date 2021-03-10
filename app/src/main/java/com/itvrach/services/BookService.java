package com.itvrach.services;

import com.itvrach.model.ResponseBook;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by engineer on 8/19/2018.
 */

public interface BookService {

    @GET("books/findAll")
    Call<ResponseBook> getFindAll();
}
