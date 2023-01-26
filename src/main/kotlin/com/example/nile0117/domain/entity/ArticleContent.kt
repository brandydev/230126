package com.example.nile0117.domain.entity

import com.example.nile0117.domain.base.BaseEntity
import com.example.nile0117.domain.enums.Language
import jakarta.persistence.*
import java.util.*

@Entity
// todo: index 처리 어떻게?
// @Table(indexes = [Index(name = "idx_id_language", columnList = "article_id, language", unique = true)])
data class ArticleContent(
    var articleId: UUID?,
    @Enumerated(EnumType.STRING)
    var language: Language,
    var title: String?,
    var content: String?
): BaseEntity() {
    var description: String? = null
}