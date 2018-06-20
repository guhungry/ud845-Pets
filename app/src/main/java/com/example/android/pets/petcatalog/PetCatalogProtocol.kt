package com.example.android.pets.petcatalog

import android.content.Context
import android.net.Uri

class PetCatalogProtocol {
    interface View {
        var presenter: Presenter?
    }
    interface Presenter {
        var view : View?

        fun showPetAddScreenFor(context: Context)
        fun showPetEditScreenFor(context: Context, uri: Uri?)
    }
    interface Router {
        fun presenterFor(view: View) : Presenter
    }
}