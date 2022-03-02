package com.raaf.rickandmorty.ui.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raaf.rickandmorty.R
import com.raaf.rickandmorty.dataModels.Character

class CharactersAdapter(private val layoutManager: GridLayoutManager,
                        private val reviewRV: RecyclerView): PagingDataAdapter<Character,
        CharactersAdapter.CharactersViewHolder>(ReviewDiffItemCallback) {

//  This variable contains value of the saved position obtained from SavedStateHandle
    private var savedPosition: Int? = null

    companion object {
        const val LOADING_ITEM = 0
        const val CHARACTER_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder =
        CharactersViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.card_character, parent, false))

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        if(position == 0) scrollToSavedPosition()
        var character = getItem(position)
        if (character == null) return
        Glide.with(holder.itemView)
            .load(character.image)
            .error(ColorDrawable(Color.GRAY))
            .into(holder.characterImage)
        holder.characterName.text = character.name
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) CHARACTER_ITEM
            else LOADING_ITEM
    }

    //    Restores scrolling after killing process app
    private fun scrollToSavedPosition() {
        if (savedPosition != null) {
            layoutManager.smoothScrollToPosition(reviewRV, null, savedPosition!!)
            savedPosition = null
        }
    }
//
    fun setSavedPosition(position: Int) {
        savedPosition = position
    }

    inner class CharactersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var characterImage: ImageView = itemView.findViewById(R.id.character_i_v)
        var characterName: TextView = itemView.findViewById(R.id.character_name_t_v)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
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