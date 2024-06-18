package de.rs.globetrotterchat.android.data.model

data class TranslateResponse(
    val data: TranslationData
) {
    val isSuccessful: Boolean
        get() = data.translations.isNotEmpty()
}

data class TranslationData(
    val translations: List<Translation>
)

data class Translation(
    val translatedText: String
)


