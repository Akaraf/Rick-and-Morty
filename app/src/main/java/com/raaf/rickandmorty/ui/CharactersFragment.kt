package com.raaf.rickandmorty.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raaf.rickandmorty.App
import com.raaf.rickandmorty.R
import com.raaf.rickandmorty.ui.adapters.CharactersAdapter
import com.raaf.rickandmorty.ui.adapters.CharactersLoadStateAdapter
import com.raaf.rickandmorty.ui.extensions.lazyViewModel
import com.raaf.rickandmorty.viewModels.CharactersViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CharactersFragment : Fragment(), View.OnClickListener {

    val charactersVM: CharactersViewModel by lazyViewModel {
        App.appComponent.charactersViewModel().create(it)
    }
    private var charactersJob: Job? = null

    private lateinit var charactersRV: RecyclerView
    private lateinit var characterLM: GridLayoutManager
    private lateinit var charactersAdapter: CharactersAdapter

    private lateinit var loadStateIncludeLayout : FrameLayout
    private lateinit var startProgressBar: ProgressBar
    private lateinit var errorLayout: LinearLayout
    private lateinit var retryButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_characters, container, false)
        charactersRV = view.findViewById(R.id.characters_r_v)
        characterLM = GridLayoutManager(
            requireContext(), 2,
            RecyclerView.VERTICAL, false)
        charactersRV.layoutManager = characterLM
        charactersAdapter = CharactersAdapter(characterLM, charactersRV)
        loadStateIncludeLayout = view.findViewById(R.id.characters_load_state_include_layout)
        startProgressBar = loadStateIncludeLayout.findViewById(R.id.reviews_load_progress_bar)
        errorLayout = loadStateIncludeLayout.findViewById(R.id.error_layout)
        retryButton = loadStateIncludeLayout.findViewById(R.id.retry_button)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCharactersJob()
//        Processing states of data in recycler
//        We can wrap recycler in SwipeRefreshLayout instead of using loader state adapters
        charactersRV.adapter = charactersAdapter.withLoadStateHeaderAndFooter(
            CharactersLoadStateAdapter { charactersAdapter.retry() },
            CharactersLoadStateAdapter { charactersAdapter.retry() }
        )

        charactersAdapter.addLoadStateListener {
            if (it.refresh == LoadState.Loading && charactersAdapter.itemCount == 0) {
                charactersRV.visibility = View.GONE
                errorLayout.visibility = View.GONE
                startProgressBar.visibility = View.VISIBLE
            }
            if (it.refresh != LoadState.Loading && charactersAdapter.itemCount == 0) {
                charactersRV.visibility = View.GONE
                startProgressBar.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE
            }
            if (charactersAdapter.itemCount != 0) {
                charactersRV.visibility = View.VISIBLE
                loadStateIncludeLayout.visibility = View.GONE
            }
        }
        retryButton.setOnClickListener(this)
        setSavedPositionToAdapter(charactersVM.savedPosition)
    }

    override fun onStop() {
        super.onStop()
        savingPagingState()
    }

    override fun onClick(view: View) {
        when (view) {
            retryButton -> {
                charactersAdapter.retry()
            }
        }
    }

    private fun setCharactersJob() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                charactersVM.characters.collectLatest(charactersAdapter::submitData)
            }
        }
    }

    private fun setSavedPositionToAdapter(position: Int?) {
        if (position != null) charactersAdapter.setSavedPosition(position)
    }

//  Passing value to be saved in SavedStateHandle
    private fun savingPagingState() {
        val position = characterLM.findLastVisibleItemPosition() ?: 0
        if (position > 0) charactersVM.saveOffset(position)
    }
}