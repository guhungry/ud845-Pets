package com.example.android.pets.data.adapters

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.android.pets.R
import com.example.android.pets.model.PetModel

class PetAdapter(context: Context?, cursor: Cursor?) : CursorAdapter(context, cursor, 0) {
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_item_pets, parent, false)

        bindView(view, context, cursor)
        return view
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        if (cursor == null || view == null) return

        val pet = PetModel.fromCursor(cursor)
        view.findViewById<TextView>(R.id.name).text = pet.name
        view.findViewById<TextView>(R.id.breed).text = pet.breed
    }
}