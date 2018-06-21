package com.example.android.pets.petedit

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.android.pets.data.PetStore
import com.example.android.pets.model.PetModel

class PetEditProtocol {
    interface Presenter {
        val uri: Uri?
        var view: View?
        var store: PetStore?
        var edited: Boolean

        fun start()
        fun stop()

        fun isEdit(): Boolean
        fun insertPet(pet: PetModel)
        fun updatePet(pet: PetModel)
    }

    interface View {
        var presenter: Presenter?

        fun setTitle(resourceId: Int)
        fun onSaveSuccess(message: String)
        fun onSaveFail(message: String)
    }

    interface Router {
        fun presenterFor(view: View, contentResolver: ContentResolver, uri: Uri?): Presenter
        fun createEditView(context: Context, uri: Uri? = null)
    }
}