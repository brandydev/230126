package com.example.nile0117.domain.entity

import com.example.nile0117.domain.base.BaseEntity
import jakarta.persistence.Entity
import java.util.UUID

@Entity
data class Hashtag(
    var articleId: UUID?,
    var text: String?
): BaseEntity() {
}