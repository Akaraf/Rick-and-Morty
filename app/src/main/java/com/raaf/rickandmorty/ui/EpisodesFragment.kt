package com.raaf.rickandmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raaf.rickandmorty.App
import com.raaf.rickandmorty.R
import com.raaf.rickandmorty.ui.adapters.EpisodesAdapter
import com.raaf.rickandmorty.ui.extensions.lazyViewModel
import com.raaf.rickandmorty.ui.utils.setToolbarTitle
import com.raaf.rickandmorty.viewModels.EpisodesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EpisodesFragment : Fragment() {

    private val episodesVM: EpisodesViewModel by lazyViewModel {
        App.appComponent.episodesViewModel().create(it)
    }
    private lateinit var episodesRV: RecyclerView
    private lateinit var episodesAdapter: EpisodesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEpisodesId(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_episode, container, false)
        episodesRV = view.findViewById(R.id.episodes_r_v)
        episodesRV.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEpisodes()
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(requireActivity().findViewById(R.id.toolbar), getString(R.string.episodes))
    }

    private fun setEpisodes() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                val episodes = withContext(Dispatchers.IO) {
                    if (!episodesVM.isOnlyOneEpisode) episodesVM.getEpisodes()
                    else listOf(episodesVM.getEpisode())
                }
                if (episodes.isNotEmpty()) {
                    episodesAdapter = EpisodesAdapter(episodes)
                    episodesRV.adapter = episodesAdapter
                }
            }
        }
    }

    private fun setEpisodesId(bundle: Bundle?) {
        val isIdNotEmpty = episodesVM.setEpisodesIdFromBundle(bundle)
        if (!isIdNotEmpty) {
            Toast.makeText(requireContext(), getString(R.string.no_data), Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        }
    }
}