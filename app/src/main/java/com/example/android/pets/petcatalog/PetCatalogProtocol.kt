package com.example.android.pets.petcatalog

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.android.pets.data.PetStore
import com.example.android.pets.model.PetModel

class PetCatalogProtocol {
    interface View {
        var presenter: Presenter?
    }

    interface Presenter {
        var view: View?
        var store: PetStore?

        fun stop()

        fun showPetAddScreenFor(context: Context)
        fun showPetEditScreenFor(context: Context, uri: Uri?)

        fun deleteAllPets()
        fun insertPet(pet: PetModel)
    }

    interface Router {
        fun presenterFor(view: View, contentResolver: ContentResolver): Presenter
    }
}