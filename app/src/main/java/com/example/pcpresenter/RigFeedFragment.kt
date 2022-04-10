package com.example.pcpresenter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.parse.ParseQuery
import com.parse.ParseUser


class RigFeedFragment(private val user : ParseUser?) : Fragment() {

    val rigs : MutableList<Rig> = mutableListOf()
    private lateinit var swipeContainer : SwipeRefreshLayout
    private lateinit var adapter : RigRecyclerViewAdapter

    @SuppressLint("NotifyDataSetChanged")
    fun refreshList(){
        val query: ParseQuery<Rig> = ParseQuery.getQuery(Rig::class.java)
        // no condition = find all
        // username passed = find only those
        if (user != null)
            query.whereEqualTo(Rig.KEY_UPLOADER, user)
        query.addDescendingOrder("createdAt")
        query.include(Rig.KEY_UPLOADER)
        query.limit = 20
        query.findInBackground{ posts, e ->
            if (e == null) {
                Log.i(MainActivity.TAG, "Rigs:")
                for (post in posts){
                    Log.i(MainActivity.TAG," Rig: ${post.getDescription()} by ${post.getUploader().username}")
                }
                this.rigs.clear()
                this.rigs.addAll(posts)
                adapter.notifyDataSetChanged()
            } else {
                Log.e(MainActivity.TAG, "Failed to load rigs from server")
                Log.e(MainActivity.TAG, e.toString())
            }
        }
        swipeContainer.isRefreshing = false
        Log.i("aaaaa",adapter.itemCount.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_rig_feed_list, container, false)

        val rvPosts = view.findViewById<RecyclerView>(R.id.list)
        // Set the adapter
        rvPosts.layoutManager = LinearLayoutManager(context)
        adapter = RigRecyclerViewAdapter(rigs)
        rvPosts.adapter = adapter

        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.swipeContainer)
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener {
            refreshList()
        }

        refreshList()

        return view
    }
}