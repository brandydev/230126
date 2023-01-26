package com.example.nile0117.domain.entity

import com.example.nile0117.domain.base.BaseEntity
import com.example.nile0117.domain.enums.FileType
import com.example.nile0117.domain.enums.Location
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.util.UUID

@Entity
data class File(
    var articleId: UUID?,
    var name: String?,
    var path: String?,
    @Enumerated(EnumType.STRING)
    var type: FileType?,
    @Enumerated(EnumType.STRING)
    var location: Location?
    // var size: SizeType? todo: 1. size 구분이 필요한지 여부 확정 2. 필요하다면, 어떤 식으로 구분할지 (+ enum 처리)
): BaseEntity() {
}