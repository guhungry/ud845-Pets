package com.example.android.pets.petedit

import android.net.Uri

class PetEditPresenter(var uri: Uri? = null) : PetEditProtocol.Presenter {
    override var view: PetEditProtocol.View? = null

    private fun isEdit(): Boolean {
        return uri != null
    }

    override fun start() {
        view?.setTitle(title())
    }

    override fun stop() {
        uri = null
        view = null
    }

    private fun title(): String {
        return if (isEdit()) "Edit Pet" else "Add a Pet"
    }
}