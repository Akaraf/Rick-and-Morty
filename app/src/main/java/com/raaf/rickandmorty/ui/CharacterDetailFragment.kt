package com.raaf.rickandmorty.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.raaf.rickandmorty.App
import com.raaf.rickandmorty.R
import com.raaf.rickandmorty.data.dataModels.Character
import com.raaf.rickandmorty.ui.extensions.lazyViewModel
import com.raaf.rickandmorty.utils.setToolbarTitle
import com.raaf.rickandmorty.viewModels.CharacterDetailViewModel

class CharacterDetailFragment : Fragment(), View.OnClickListener {

    private lateinit var characterIV: ShapeableImageView
    private lateinit var characterNameTV: TextView
    private lateinit var species: TextView
    private lateinit var doteIV: ImageView
    private lateinit var status: TextView
    private lateinit var characterLocation: TextView
    private lateinit var openEpisodesFAB: ExtendedFloatingActionButton

    private val characterDetailVM: CharacterDetailViewModel by lazyViewModel {
        App.appComponent.characterDetailViewModel().create(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        characterDetailVM.setCharacterFromBundle(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_character_detail,
            container,
            false)
        characterIV = view.findViewById(R.id.character_detail_i_v)
        characterNameTV = view.findViewById(R.id.detail_character_name_t_v)
        species = view.findViewById(R.id.species_t_v)
        doteIV = view.findViewById(R.id.image_dote)
        status = view.findViewById(R.id.status_t_v)
        characterLocation = view.findViewById(R.id.location_t_v)
        openEpisodesFAB = view.findViewById(R.id.open_episodes)
        openEpisodesFAB.setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillUI(characterDetailVM.character)
    }

    override fun onResume() {
        super.onResume()
        characterDetailVM.character?.let {
            setToolbarTitle(
                requireActivity().findViewById(R.id.toolbar),
                it.name)
        }
    }

    override fun onClick(view: View) {
        when (view) {
            openEpisodesFAB -> {
                findNavController().navigate(
                    R.id.action_characterDetailFragment_to_episodesFragment,
                    characterDetailVM.getEpisodesBundle())
            }
        }
    }

    private fun fillUI(character: Character?) {
        if (character == null) {
            Toast.makeText(requireContext(), getString(R.string.no_data), Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
            return
        }
        Glide.with(requireContext())
            .load(character.image)
            .error(ColorDrawable(Color.GRAY))
            .into(characterIV)
        characterNameTV.text = character.name
        species.text = character.species
        if (character.status.isNotEmpty()) status.text = character.status
        else doteIV.visibility = GONE
        characterLocation.text = getString(R.string.location) + " " + character.location.name
    }
}