package com.example.android.pets.petedit

import android.content.Context
import android.content.Intent
import android.net.Uri

class PetEditProtocol {
    interface Presenter {
        fun start()
        fun stop()
    }

    interface View {
        fun setTitle(text: String)
    }

    interface Router {
        fun editPetPresenterFor(view: View, uri: Uri?) : Presenter
        fun createEditView(context: Context, uri: Uri? = null)
    }
}