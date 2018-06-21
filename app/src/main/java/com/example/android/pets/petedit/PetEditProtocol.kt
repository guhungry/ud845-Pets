package com.example.android.pets.petedit

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.android.pets.data.PetStore
import com.example.android.pets.model.PetModel

class PetEditProtocol {
    interface Presenter {
        var view: View?
        var store: PetStore?

        fun start()
        fun stop()

        fun insertPet(pet: PetModel)
    }

    interface View {
        var presenter: Presenter?

        fun setTitle(resourceId: Int)
    }

    interface Router {
        fun presenterFor(view: View, contentResolver: ContentResolver, uri: Uri?): Presenter
        fun createEditView(context: Context, uri: Uri? = null)
    }
}