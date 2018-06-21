package com.example.android.pets.petedit

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.android.pets.EditorActivity
import com.example.android.pets.data.PetStore

class PetEditRouter {
    companion object : PetEditProtocol.Router {
        override fun createEditView(context: Context, uri: Uri?) {
            val intent = Intent(context, EditorActivity::class.java)

            if (uri != null) intent.putExtra(EditorActivity.INPUT_URL, uri)
            context.startActivity(intent)
        }

        override fun presenterFor(view: PetEditProtocol.View, contentResolver: ContentResolver, uri: Uri?): PetEditProtocol.Presenter {
            val presenter: PetEditProtocol.Presenter = PetEditPresenter(uri)

            presenter.view = view
            presenter.store = PetStore(contentResolver)
            return presenter
        }
    }
}