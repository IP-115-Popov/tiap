package ru.sergey.domain.UseCase

import ru.sergey.domain.repository.FileStorage

class UploadUseCase(val storage : FileStorage) {
    fun execute(strings: List<String>)
    {
        storage.saveStrings(strings)
    }
}