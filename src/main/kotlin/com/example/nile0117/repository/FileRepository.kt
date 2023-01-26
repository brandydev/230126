package com.example.nile0117.repository

import com.example.nile0117.domain.entity.File
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FileRepository: JpaRepository<File, UUID> {
    fun findAllByArticleId(articleId: UUID?): List<File>
    fun existsByName(name: String?): Boolean
    fun findByName(name: String?): File
}