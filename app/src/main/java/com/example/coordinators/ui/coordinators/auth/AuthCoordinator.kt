package com.example.coordinators.ui.coordinators.auth

import LoginScreen
import androidx.compose.runtime.Composable
import com.example.coordinators.ui.coordinators.AppCoordinatorAction
import com.example.coordinators.ui.coordinators.Coordinator
import com.example.coordinators.ui.coordinators.CoordinatorAction
import com.example.coordinators.ui.coordinators.GeneralAction
import com.example.coordinators.ui.coordinators.NavigatorCoordinator
import com.example.coordinators.ui.navigation.NavHostBuilder
import com.example.coordinators.ui.navigation.Navigable
import com.example.coordinators.ui.navigation.Navigator
import com.example.coordinators.ui.screens.login.SettingsScreen

enum class AuthNavigationRoute(override val route: String) : Navigable {
    LOGIN("login"),
    SETTINGS("settings"),
}

sealed class AuthCoordinatorAction : CoordinatorAction {
    data object GoToSettings : AuthCoordinatorAction()
    data object Authenticated : AuthCoordinatorAction()
    data object GoBack : AuthCoordinatorAction()
}

class AuthCoordinator(
    private val parent: NavigatorCoordinator
) : Coordinator {

    override fun setupNavigation(builder: NavHostBuilder) {
        builder.composable(AuthNavigationRoute.LOGIN) {
            LoginScreen(coordinator = this@AuthCoordinator)
        }
        builder.composable(AuthNavigationRoute.SETTINGS) {
            SettingsScreen(coordinator = this@AuthCoordinator)
        }
    }

    @Composable
    override fun handle(action: CoordinatorAction) {
        when (action) {
            is GeneralAction.Done -> println("Done action with data: ${action.data}")
            is GeneralAction.Cancel -> println("Cancelled with reason: ${action.reason}")
            is AuthCoordinatorAction.GoToSettings -> navigate(AuthNavigationRoute.SETTINGS)
            is AuthCoordinatorAction.Authenticated -> parent.handle(AppCoordinatorAction.StartMainFlow)
            is AuthCoordinatorAction.GoBack -> navigate(AuthNavigationRoute.LOGIN)
            is AuthCoordinatorAction.GoToSettings -> navigate(AuthNavigationRoute.SETTINGS)
            else -> throw IllegalArgumentException("Unsupported action")
        }
    }

    override fun navigate(route: Navigable) {
        parent.navigator.navigateTo(route.route)
    }
}
