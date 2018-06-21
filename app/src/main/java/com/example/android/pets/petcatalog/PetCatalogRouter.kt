package com.example.android.pets.petcatalog

import android.content.ContentResolver
import com.example.android.pets.data.PetStore

class PetCatalogRouter {
    companion object : PetCatalogProtocol.Router {
        override fun presenterFor(view: PetCatalogProtocol.View, contentResolver: ContentResolver): PetCatalogProtocol.Presenter {
            val presenter: PetCatalogProtocol.Presenter = PetCatalogPresenter()

            presenter.view = view
            presenter.store = PetStore(contentResolver)
            return presenter
        }
    }
}