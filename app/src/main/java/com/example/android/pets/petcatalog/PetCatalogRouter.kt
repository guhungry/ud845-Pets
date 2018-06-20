package com.example.android.pets.petcatalog

class PetCatalogRouter {
    companion object : PetCatalogProtocol.Router {
        override fun presenterFor(view: PetCatalogProtocol.View): PetCatalogProtocol.Presenter {
            val presenter: PetCatalogProtocol.Presenter = PetCatalogPresenter()

            presenter.view = view
            return presenter
        }
    }
}