package com.example.android.pets.petcatalog

import android.content.Context
import android.net.Uri
import com.example.android.pets.data.PetStore
import com.example.android.pets.model.PetModel
import com.example.android.pets.petedit.PetEditRouter

class PetCatalogPresenter : PetCatalogProtocol.Presenter {
    override var view: PetCatalogProtocol.View? = null
    override var store: PetStore? = null

    override fun stop() {
        view = null
        store = null
    }

    override fun showPetAddScreenFor(context: Context) {
        showPetEditScreenFor(context, null)
    }

    override fun showPetEditScreenFor(context: Context, uri: Uri?) {
        PetEditRouter.createEditView(context, uri)
    }

    override fun deleteAllPets() {
        store?.deleteAllPets()
    }

    override fun insertPet(pet: PetModel) {
        store?.insertPet(pet.toContentValues())
    }
}