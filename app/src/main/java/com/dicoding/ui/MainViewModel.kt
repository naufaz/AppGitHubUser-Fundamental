package com.dicoding.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.api.ApiConfig
import com.dicoding.model.Items
import com.dicoding.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<ArrayList<Items>>()
    private val listUsers: LiveData<ArrayList<Items>> = _listUsers

    fun getSearchUsers(): LiveData<ArrayList<Items>> = this.listUsers
    fun setSearchUser(query: String) {
        ApiConfig.getApiService().getUser(query)
            .enqueue(userResponseCallback())
    }

    private fun userResponseCallback() = object : Callback<UserResponse> {
        override fun onResponse(
            call: Call<UserResponse>,
            response: Response<UserResponse>
        ) {
            if (response.isSuccessful) {
                _listUsers.postValue(response.body()?.items)
            }
        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            t.message?.let { Log.d("Failure", it) }
        }
    }
}
