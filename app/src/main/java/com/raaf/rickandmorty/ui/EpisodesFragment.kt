package com.raaf.rickandmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EpisodesFragment : Fragment(), View.OnClickListener{

    private val episodesVM: EpisodesViewModel by lazyViewModel {
        App.appComponent.episodesViewModel().create(it)
    }
    private lateinit var episodesRV: RecyclerView
    private lateinit var episodesAdapter: EpisodesAdapter
    private lateinit var loadStateIncludeLayout : FrameLayout
    private lateinit var startProgressBar: ProgressBar
    private lateinit var errorLayout: LinearLayout
    private lateinit var retryButton: Button

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
        changeLoadStateLayout(2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setEpisodesId(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_episodes, container, false)
        episodesRV = view.findViewById(R.id.episodes_r_v)
        loadStateIncludeLayout = view.findViewById(R.id.episodes_load_state_include_layout)
        startProgressBar = loadStateIncludeLayout.findViewById(R.id.reviews_load_progress_bar)
        errorLayout = loadStateIncludeLayout.findViewById(R.id.error_layout)
        retryButton = loadStateIncludeLayout.findViewById(R.id.retry_button)
        retryButton.setOnClickListener(this)
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
        changeLoadStateLayout(0)
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(requireActivity().findViewById(R.id.toolbar), getString(R.string.episodes))
    }

    override fun onClick(view: View) {
        when (view) {
            retryButton -> {
                setEpisodes()
                changeLoadStateLayout(0)
            }
        }
    }

    private fun setEpisodes() {
        lifecycleScope.launch(coroutineExceptionHandler) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                val episodes = withContext(Dispatchers.IO) {
                    if (!episodesVM.isOnlyOneEpisode) episodesVM.getEpisodes()
                    else listOf(episodesVM.getEpisode())
                }
                if (episodes.isNotEmpty()) {
                    episodesAdapter = EpisodesAdapter(episodes)
                    episodesRV.adapter = episodesAdapter
                    changeLoadStateLayout(1)
                } else changeLoadStateLayout(2)
            }
        }
    }

    private fun changeLoadStateLayout(loadState: Int) {
        when (loadState) {
            //Loading
            0 -> {
                loadStateIncludeLayout.visibility = VISIBLE
                startProgressBar.visibility = VISIBLE
                errorLayout.visibility = GONE
            }
            //Loaded
            1 -> {
                loadStateIncludeLayout.visibility = GONE
            }
            //Load error
            2 -> {
                loadStateIncludeLayout.visibility = VISIBLE
                startProgressBar.visibility = GONE
                errorLayout.visibility = VISIBLE
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