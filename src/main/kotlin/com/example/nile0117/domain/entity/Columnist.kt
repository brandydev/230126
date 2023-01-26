package com.example.nile0117.domain.entity

import com.example.nile0117.domain.base.BaseEntity
import jakarta.persistence.Entity
import java.util.UUID

@Entity
data class Columnist(
    var articleId: UUID?,
    var name: String?,
    var info: String?,
    var profileImage: String?
): BaseEntity() {
}
