package com.kpiroom.bubble.ui.main.fragments.profile.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dichotome.profilebar.stubs.TabListAdapter
import com.dichotome.profilebar.stubs.TabListItem
import com.dichotome.profilebar.ui.tabPager.TabFragment
import com.kpiroom.bubble.R
import kotlinx.android.synthetic.main.profile_tab_uploads.*

class UploadsTabFragment : TabFragment() {
    companion object {
        fun newInstance(tabTitle: String) = UploadsTabFragment().apply {
            title = tabTitle
        }
    }

    private val uploadList = listOf(
        TabListItem(
            "2019 The Amazing Spider-Man #2",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "2018 Star Wars #55",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        ),
        TabListItem(
            "Esteemed comic book author #1",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "Superior comic book writer",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        ),
        TabListItem(
            "2019 Batman: Rebirth #26",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "Superior comic book writer",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        ),
        TabListItem(
            "Esteemed comic book author #1",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "Superior comic book writer",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        ),
        TabListItem(
            "Esteemed comic book author #1",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "Superior comic book writer",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        ),
        TabListItem(
            "Esteemed comic book author #1",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "Superior comic book writer",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        ),
        TabListItem(
            "Esteemed comic book author #1",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "Superior comic book writer",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        ),
        TabListItem(
            "Esteemed comic book author #1",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "Superior comic book writer",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        ),
        TabListItem(
            "Esteemed comic book author #1",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "Superior comic book writer",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        ),
        TabListItem(
            "Esteemed comic book author #1",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "Superior comic book writer",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        ),
        TabListItem(
            "Esteemed comic book author #1",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "Superior comic book writer",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        ),
        TabListItem(
            "Esteemed comic book author #1",
            "https://banner2.kisspng.com/20180405/hde/kisspng-superman-logo-batman-spider-man-computer-icons-superheroes-5ac5ebd3bcd9d8.2131948915229204037735.jpg"
        ),
        TabListItem(
            "Superior comic book writer",
            "https://cdn3.iconfinder.com/data/icons/superheroes-line/256/avengers-superhero-sign-logo-512.png"
        )
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.profile_tab_uploads, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = uploads_recycler

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TabListAdapter(
            uploadList,
            R.layout.tab_item_uploads,
            true
        )
    }
}

