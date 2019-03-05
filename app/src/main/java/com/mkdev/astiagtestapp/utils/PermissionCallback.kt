package com.mkdev.astiagtestapp.utils

interface PermissionCallback {
    fun onGranted(permissions: Array<String>)
    fun onDenied(permissions: Array<String>)
    fun onShowRationale(permissions: Array<String>)
}