package com.example.android.pets.petedit

import android.content.Context
import android.net.Uri

class PetEditProtocol {
    interface Presenter {
        var view: View?

        fun start()
        fun stop()
    }

    interface View {
        var presenter: Presenter?
        fun setTitle(text: String)
    }

    interface Router {
        fun presenterFor(view: View, uri: Uri?): Presenter
        fun createEditView(context: Context, uri: Uri? = null)
    }
}