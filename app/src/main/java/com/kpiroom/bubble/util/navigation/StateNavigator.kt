package com.kpiroom.bubble.util.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

@Navigator.Name("state_fragment")
class StateNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {
/*
    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? = with(manager.beginTransaction()) {

        val tag = destination.id.toString()

        val currentFragment = manager.primaryNavigationFragment
        currentFragment?.let {
            detach(it)
        }

        val fragment = manager.findFragmentByTag(tag)?.also {
            attach(it)
        } ?: run {
            instantiateFragment(
                context,
                manager,
                destination.className,
                args
            ).also {
                add(containerId, it, tag)
            }
        }

        setPrimaryNavigationFragment(fragment)
        setReorderingAllowed(true)
        commit()

        destination
    }
    */
}