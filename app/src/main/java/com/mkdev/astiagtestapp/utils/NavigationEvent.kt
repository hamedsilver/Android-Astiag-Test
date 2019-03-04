package com.mkdev.astiagtestapp.utils

class NavigationEvent(val navEvent: NavEvent, vararg val payloads: Any) {
    enum class NavEvent {
        GO_BACK,
        OPEN_DRAWER
    }
}
