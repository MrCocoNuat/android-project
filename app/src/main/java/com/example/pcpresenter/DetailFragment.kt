package com.example.pcpresenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.w3c.dom.Text

//TODO shared elements

class DetailFragment(val rig : Rig) : Fragment() {

    private val multiTransformation = MultiTransformation(CenterCrop(), RoundedCorners(30))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_detail, container, false)

        view.findViewById<TextView>(R.id.tvName).text = rig.getName()
        view.findViewById<TextView>(R.id.tvDescription).text = rig.getDescription()
        view.findViewById<TextView>(R.id.tvCpu).text = rig.getCpu()
        view.findViewById<TextView>(R.id.tvGpu).text = rig.getGpu()
        view.findViewById<TextView>(R.id.tvMem).text = rig.getMem().toString()
        Glide.with(view.findViewById<ImageView>(R.id.ivImageDetail))
            .load(rig.getPhoto().file)
            .transform(multiTransformation)
            .into(view.findViewById<ImageView>(R.id.ivImageDetail))

        return view
    }


}