package de.rs.globetrotterchat.android.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.rs.globetrotterchat.android.data.model.TranslateResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query

const val BASE_URL = "https://translation.googleapis.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface GoogleTranslationService {
    @POST("language/translate/v2")
    suspend fun translateText(
        @Query("q") text: String,
        @Query("target") targetLanguage: String,
        @Query("key") apiKey: String
    ): TranslateResponse
}

object GoogleTranslationApi {
    val retrofitService: GoogleTranslationService by lazy { retrofit.create(GoogleTranslationService::class.java) }
}


