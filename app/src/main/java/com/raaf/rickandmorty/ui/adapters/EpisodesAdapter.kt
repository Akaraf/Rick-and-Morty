package com.raaf.rickandmorty.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDivider
import com.raaf.rickandmorty.R
import com.raaf.rickandmorty.data.dataModels.Episode

class EpisodesAdapter(private val episodes: List<Episode>)
    : RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_episode, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        var episode = episodes[position]
        holder.episodeNameTV.text = episode.name
        holder.episodeDateTV.text = episode.date
        if (position == itemCount-1) holder.episodeDivider.visibility = GONE
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int = episodes.size

    inner class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val episodeNameTV: TextView = itemView.findViewById(R.id.name_episode_t_v)
        val episodeDateTV: TextView = itemView.findViewById(R.id.date_episode_t_v)
        val episodeDivider: MaterialDivider = itemView.findViewById(R.id.episode_divider)
    }
}