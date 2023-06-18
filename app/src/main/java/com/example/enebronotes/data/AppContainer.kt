package com.example.enebronotes.data

import android.content.Context
import android.content.res.Resources
import com.example.enebronotes.data.auth.AuthRepository
import com.example.enebronotes.data.auth.AuthRepositoryImpl
import com.example.enebronotes.data.cell.CellRepository
import com.example.enebronotes.data.cell.OfflineCellRepository
import com.example.enebronotes.data.folder.FolderRepository
import com.example.enebronotes.data.folder.OfflineFoldersRepository
import com.example.enebronotes.data.note.NotesRepository
import com.example.enebronotes.data.note.OfflineNotesRepository
import com.example.enebronotes.data.storage.StorageRepository
import com.example.enebronotes.data.storage.StorageRepositoryImpl
import com.example.enebronotes.data.tenor_api.NetworkTenorApiRepository
import com.example.enebronotes.data.tenor_api.TenorApiRepository
import com.example.enebronotes.network.TenorApiService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val tenorApiRepository: TenorApiRepository
    val authRepository: AuthRepository
    val folderRepository: FolderRepository
    val storageRepository: StorageRepository
    val noteRepository: NotesRepository
    val cellRepository: CellRepository
    val resources: Resources
}

/**
 * [AppContainer] implementation that provides instance of [AuthRepositoryImpl]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    private val baseUrl = "https://tenor.googleapis.com/v2/"
    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: TenorApiService by lazy {
        retrofit.create(TenorApiService::class.java)
    }

    override val tenorApiRepository: TenorApiRepository by lazy {
        NetworkTenorApiRepository(retrofitService)
    }

    /**
     * Implementation for [AuthRepository]
     */
    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(Firebase.auth)
    }
    override val storageRepository: StorageRepository by lazy {
        StorageRepositoryImpl(Firebase.storage)
    }
    override val folderRepository: FolderRepository by lazy {
        OfflineFoldersRepository(EnebroNotesDatabase.getDatabase(context).folderDao())
    }
    override val noteRepository: NotesRepository by lazy {
        OfflineNotesRepository(EnebroNotesDatabase.getDatabase(context).noteDao())
    }

    override val cellRepository: CellRepository by lazy {
        OfflineCellRepository(EnebroNotesDatabase.getDatabase(context).cellDao())
    }
    override val resources: Resources = context.resources
}