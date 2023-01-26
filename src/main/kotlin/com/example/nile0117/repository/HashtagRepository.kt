package com.example.nile0117.repository

import com.example.nile0117.domain.entity.Hashtag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface HashtagRepository: JpaRepository<Hashtag, UUID> {
    fun existsByText(text: String): Boolean
    fun findAllByArticleId(articleId: UUID?): List<Hashtag>
    fun findAllByText(text: String): List<Hashtag>
}