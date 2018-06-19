package com.example.android.pets.model

import com.example.android.pets.data.PetContract

import org.junit.Test
import org.junit.Before
import org.junit.After

import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*

internal class PetModelTest {
    var sut: PetModel? = null
    @Before
    fun setUp() {
        sut = PetModel(id = 1, name = "My Pet", gender = PetContract.Gender.Female.ordinal, age = 23, weight = 4)
    }

    @After
    fun tearDown() {
        sut = null
    }

    @Test
    fun id_WhenInitialValueIs1_ShouldBe1() {
        assertThat(sut?.id, equalTo(1L))
    }
}