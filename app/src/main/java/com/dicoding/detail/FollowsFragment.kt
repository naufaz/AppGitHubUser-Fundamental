package com.dicoding.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.FragmentFollowsBinding
import com.dicoding.ui.UserAdaptor

class FollowsFragment : Fragment(R.layout.fragment_follows) {

    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowsViewModel
    private lateinit var adapter: UserAdaptor
    private lateinit var username: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowsBinding.bind(view)

        username = arguments?.getString(DetailActivity.EXTRA_USERNAME).toString()

        adapter = UserAdaptor()
        adapter.notifyDataSetChanged()

        binding.rvFollows.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FollowsFragment.adapter
        }

        showLoading(true)

        viewModel = ViewModelProvider(this)[FollowsViewModel::class.java]
        val isFollowers = arguments?.getInt(ARG_SECTION_NUMBER) == 1
        viewModel.getFollows(username, isFollowers).observe(viewLifecycleOwner) { users ->
            if (users != null) {
                adapter.setListUser(users)
                showLoading(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "arg_section_number"

        fun newInstance(index: Int, username: String) = FollowsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, index)
                putString(DetailActivity.EXTRA_USERNAME, username)
            }
        }
    }
}