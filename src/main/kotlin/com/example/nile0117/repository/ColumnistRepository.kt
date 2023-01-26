package com.example.nile0117.repository

import com.example.nile0117.domain.entity.Columnist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ColumnistRepository: JpaRepository<Columnist, UUID> {
    fun findByArticleId(articleId: UUID?): Columnist?
    fun findAllByIdIsNotNull(): List<Columnist>
    fun existsByName(name: String?): Boolean
    fun findByName(name: String?): Columnist
}