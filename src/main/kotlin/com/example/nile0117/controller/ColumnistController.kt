package com.example.nile0117.controller

import com.example.nile0117.domain.entity.Columnist
import com.example.nile0117.service.ColumnistService
import com.example.nile0117.util.exception.NileCommonError
import com.example.nile0117.util.response.NileResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ColumnistController {

    @Autowired
    lateinit var columnistService: ColumnistService

    // read
    // todo: columnist 저장 시 이름 중복 체크 또는 이름 unique하게 임의 지정 필요  -> article/columnist 1:1 mapping이라서 중복 체크 굳이 x
    // todo: 또는 동일한 이름을 갖는 칼럼니스트 전체를 보여주도록 해도 됨(이거)
    // todo: 또는 검색 기준을 이름이 아닌 다른 것으로 잡을 수 있음
    // >> file과 동일한 에러

    // Columnist 전체조회
    @GetMapping("/columnists")
    fun getColumnists(): ResponseEntity<*> {
        val selectedColumnists = columnistService.getColumnists()

        return ResponseEntity.ok(
            NileResponse(
                message = "전체 Columnist 조회에 성공했습니다",
                result = selectedColumnists
            )
        )
    }

    // Columnist 단일조회
    @GetMapping("/columnist")
    fun getColumnist(
        @RequestParam("name", required = false, defaultValue = "") name: String?
    ): ResponseEntity<*> {
        if (name.isNullOrBlank()) {
            return ResponseEntity.ok(
                NileResponse(
                    errorCode = NileCommonError.INVALID_PARAMETER.getErrorCode(),
                    status = NileCommonError.INVALID_PARAMETER.getHttpStatus(),
                    message = "columnist 조회를 위해서는 name 입력이 필요합니다"
                )
            )
        }

        val selectedColumnist: Columnist? = columnistService.getColumnistByName(name)
        selectedColumnist?.let {
            return ResponseEntity.ok(
                NileResponse(
                    message = "columnist가 성공적으로 조회되었습니다.",
                    result = selectedColumnist
                )
            )
        } ?: return ResponseEntity.ok(
            NileResponse(
                errorCode = NileCommonError.INVALID_NAME.getErrorCode(),
                status = NileCommonError.INVALID_NAME.getHttpStatus(),
                message = "존재하지 않는 columnist입니다."
            )
        )
    }


    //Columnist 전체 조회
    /*
    @GetMapping("/allcolumnist")
    fun getAllColumnist(
        @RequestParam("name", required = false, defaultValue = "") name: String?

    ) : ResponseEntity<*> {
        if(name.isNullOrBlank()) {
            return ResponseEntity.ok(
                NileResponse(
                    errorCode = NileCommonError.INVALID_PARAMETER.getErrorCode(),
                    status = NileCommonError.INVALID_PARAMETER.getHttpStatus(),
                    message = "전체 columnist 조회를 위해서는 columnist name 입력이 필요합니다"

                )
            )
        }

    val selectedAllColumnist: Columnist? = columnistService.getColumnistByName(name)
    selectedAllColumnist?.let {
        return ResponseEntity.ok(
            NileResponse(
                message = "모든 Columnist들의 name이 성공적으로 조회되었습니다",
                result = selectedAllColumnist
            )
        )

    } ?: return ResponseEntity.ok(
        NileResponse(
            errorCode = NileCommonError.INVALID_NAME.getErrorCode(),
            status = NileCommonError.INVALID_NAME.getHttpStatus(),
            message = "모든 Columnist들의 name 조회가 되지 않았습니다"
        )
    )

}*/


}