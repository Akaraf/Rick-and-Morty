package com.raaf.rickandmorty.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raaf.rickandmorty.R

//wrapper for displaying the status of loading list content and error handling

class LoaderStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder =
        LoaderViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.characters_load_state, parent, false), retry)

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoaderViewHolder(view: View, retry: () -> Unit) : RecyclerView.ViewHolder(view) {

        private val progress: ProgressBar = view.findViewById(R.id.reviews_load_progress_bar)
        private val errorLayout: LinearLayout = view.findViewById(R.id.error_layout)
        private val retryButton: Button = view.findViewById(R.id.retry_button)

        init {
            retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) = when(loadState) {
            is LoadState.Error -> {
                progress.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE
            }
            is LoadState.Loading -> {
                progress.visibility = View.VISIBLE
                errorLayout.visibility = View.GONE
            }
            else -> {
                progress.visibility = View.GONE
                errorLayout.visibility = View.GONE
            }
        }
    }
}