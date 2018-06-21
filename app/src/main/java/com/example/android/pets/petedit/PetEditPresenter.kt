package com.example.android.pets.petedit

import android.net.Uri
import com.example.android.pets.R
import com.example.android.pets.data.PetStore
import com.example.android.pets.model.PetModel

class PetEditPresenter(var uri: Uri? = null) : PetEditProtocol.Presenter {
    override var view: PetEditProtocol.View? = null
    override var store: PetStore? = null

    private fun isEdit(): Boolean {
        return uri != null
    }

    override fun start() {
        view?.setTitle(title())
    }

    override fun stop() {
        uri = null
        view = null
        store = null
    }

    override fun insertPet(pet: PetModel) {
        val id = store?.insertPet(pet.toContentValues()) ?: 0

        if (id > 0) view?.onSaveSuccess("Pet saved with id: $id")
        else view?.onSaveFail("Can't insert pet")
    }

    private fun title(): Int {
        return if (isEdit()) R.string.editor_activity_title_edit_pet else R.string.editor_activity_title_new_pet
    }
}