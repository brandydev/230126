package com.example.nile0117.service

import com.example.nile0117.domain.entity.File
import com.example.nile0117.repository.FileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FileService {

    @Autowired
    lateinit var fileRepository: FileRepository

    // duplicate check
    fun isUniqueFile(name: String): Boolean {
        return !fileRepository.existsByName(name)
    }

    // read
    fun getFileByName(name: String): File? {
        return if (isUniqueFile(name)) {
        //val isExist = fileRepository.existsByName(name)

            fileRepository.findByName(name)

            } else {
               null
            }
        }

}


