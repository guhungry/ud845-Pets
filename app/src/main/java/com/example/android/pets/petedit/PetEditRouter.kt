package com.example.android.pets.petedit

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.android.pets.EditorActivity

class PetEditRouter {
    companion object : PetEditProtocol.Router {
        override fun createEditView(context: Context, uri: Uri?) {
            val intent = Intent(context, EditorActivity::class.java)

            if (uri != null) intent.putExtra(EditorActivity.INPUT_URL, uri)
            context.startActivity(intent)
        }

        override fun presenterFor(view: PetEditProtocol.View, uri: Uri?): PetEditProtocol.Presenter {
            val presenter: PetEditProtocol.Presenter = PetEditPresenter(uri)

            presenter.view = view
            return presenter
        }
    }
}