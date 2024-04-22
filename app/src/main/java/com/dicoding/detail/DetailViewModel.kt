package com.dicoding.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.api.ApiConfig
import com.dicoding.model.DetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    val users = MutableLiveData<DetailResponse>()
    private val errorLiveData = MutableLiveData<String>()

    fun setUserDetail(username: String) {
        ApiConfig.getApiService().getDetail(username)
            .enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if (response.isSuccessful) users.postValue(response.body())
                    else errorLiveData.postValue("Request failed: ${response.code()}")
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    errorLiveData.postValue("Request failed: ${t.message}")
                }
            })
    }

    fun getUserDetail() = users

    fun getErrorLiveData() = errorLiveData
}
