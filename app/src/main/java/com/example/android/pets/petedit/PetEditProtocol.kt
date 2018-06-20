package com.example.android.pets.petedit

class PetEditProtocol {
    interface Presenter {
        fun start()
        fun stop()
    }
    interface View {
        fun setTitle(text: String)
    }
}