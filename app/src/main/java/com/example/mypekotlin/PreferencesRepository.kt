package com.example.mypekotlin

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.UUID

class PreferencesRepository private constructor(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope = GlobalScope
){

    val storedId: Flow<UUID> = dataStore.data.map {
        UUID.fromString(it[USER_ID])
    }.distinctUntilChanged()

    suspend fun setStoredId(id: UUID){
        dataStore.edit {
            it[USER_ID] = id.toString()
        }
    }

    val storedLanguagePosition: Flow<Int> = dataStore.data.map {
        it[LANGUAGE_POSITION] ?: 0
    }.distinctUntilChanged()

    suspend fun setLanguagePosition(position: Int){
        dataStore.edit {
            it[LANGUAGE_POSITION] = position
        }
    }

    companion object {
        private val USER_ID = stringPreferencesKey("userId")
        private val LANGUAGE_POSITION = intPreferencesKey("languagePosition")

        private var INSTANCE: PreferencesRepository? = null

        fun initialize(context: Context){
            if(INSTANCE == null){
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile("dataStore")
                }
                INSTANCE = PreferencesRepository(dataStore)


            }
        }

        fun get(): PreferencesRepository {
            return INSTANCE ?: throw IllegalStateException(
                "PreferencesRepository is null"
            )
        }
    }

}