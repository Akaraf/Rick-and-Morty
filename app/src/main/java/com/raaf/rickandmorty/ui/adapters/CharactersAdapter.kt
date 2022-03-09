package com.raaf.rickandmorty.ui.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raaf.rickandmorty.Character.EXTRA_CHARACTER
import com.raaf.rickandmorty.R
import com.raaf.rickandmorty.data.dataModels.Character

class CharactersAdapter : PagingDataAdapter<Character,
        CharactersAdapter.CharactersViewHolder>(ReviewDiffItemCallback) {

    companion object {
        const val LOADING_ITEM = 0
        const val CHARACTER_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder =
        CharactersViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_character, parent, false))

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val character = getItem(position)
        if (character == null) return
        Glide.with(holder.itemView)
            .load(character.image)
            .error(ColorDrawable(Color.GRAY))
            .into(holder.characterImage)
        holder.characterName.text = character.name
        holder.character = character
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) CHARACTER_ITEM
            else LOADING_ITEM
    }

    inner class CharactersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var character: Character? = null
        var characterImage: ImageView = itemView.findViewById(R.id.character_i_v)
        var characterName: TextView = itemView.findViewById(R.id.character_name_t_v)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_CHARACTER, character)
            view.findNavController().navigate(R.id.action_global_characterDetailFragment, bundle)
        }
    }

    object ReviewDiffItemCallback : DiffUtil.ItemCallback<Character>() {

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }
}