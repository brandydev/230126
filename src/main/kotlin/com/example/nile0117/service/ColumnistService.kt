package com.example.nile0117.service

import com.example.nile0117.domain.entity.Article
import com.example.nile0117.domain.entity.Columnist
import com.example.nile0117.repository.ColumnistRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ColumnistService {

    @Autowired
    lateinit var columnistRepository: ColumnistRepository

    // read(단일 조회)
    fun getColumnistByName(name: String): Columnist? {
        val isExist = columnistRepository.existsByName(name)
        return if (!isExist) {
            null
        } else {
            columnistRepository.findByName(name)
        }
    }

    // Columnist 전체 조회
    /*fun getAllColumnistByName(name: String): Columnist? {
        val isExist = columnistRepository.existsByName(name)
        return if (!isExist){
            null
        } else {
            columnistRepository.findByName(name)
        }
    }*/

    /*fun getColumnistsByName(name: String): Columnist? {
        val isExist = columnistRepository.existsByName(name)
        return if(!isExist){
            null
        } else {

            columnistRepository.findByName(name)
        }
    }*/

    fun getColumnists(): List<Columnist> {

        return columnistRepository.findAllByIdIsNotNull()

    }


}