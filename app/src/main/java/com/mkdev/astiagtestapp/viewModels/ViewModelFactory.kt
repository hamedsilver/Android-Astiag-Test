package com.mkdev.astiagtestapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mkdev.astiagtestapp.App
import com.mkdev.astiagtestapp.di.DIComponent

class ViewModelFactory(private var app: App) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val t = super.create(modelClass)
        if (t is DIComponent.Injectable) {
            (t as DIComponent.Injectable).inject(app.di)
        }
        return t
    }
}