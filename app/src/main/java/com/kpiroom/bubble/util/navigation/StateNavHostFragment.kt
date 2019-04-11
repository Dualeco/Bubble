package com.kpiroom.bubble.util.navigation

import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment

class StateNavHostFragment : NavHostFragment() {
    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> =
        StateNavigator(requireContext(), childFragmentManager, id)
}