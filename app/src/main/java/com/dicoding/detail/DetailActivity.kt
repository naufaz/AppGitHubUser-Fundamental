package com.dicoding.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.R
import com.dicoding.detail.FollowsAdapter
import com.dicoding.githubuserapp.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val uname = intent.getStringExtra(EXTRA_USERNAME)

        binding.progressBar.visibility = View.VISIBLE

        uname?.let {viewModel.setUserDetail(it) }
        viewModel.getUserDetail().observe(this) { user ->
            user?.let { user ->
                binding.progressBar.visibility = View.GONE
                binding.apply {
                    tvName.text = user.name
                    tvUname.text = user.login
                    "${user.followers}\nFollowers".also { tvFollowers.text = it }
                    "${user.following}\nFollowing".also { tvFollowing.text = it }
                    Glide.with(this@DetailActivity)
                        .load(user.avatarUrl)
                        .centerCrop()
                        .into(avatar)
                }
            }
        }

        viewModel.getErrorLiveData().observe(this) { errorMessage ->
            Toast.makeText(this, "Oops, we found error!\n$errorMessage", Toast.LENGTH_SHORT).show()
        }

        val followsAdapter = FollowsAdapter(this, uname.orEmpty().toString())
        binding.apply {
            viewPager.adapter = followsAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following,
        )
    }
}