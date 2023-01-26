package com.example.nile0117.controller

import com.example.nile0117.domain.entity.File
import com.example.nile0117.service.FileService
import com.example.nile0117.util.exception.NileCommonError
import com.example.nile0117.util.response.NileResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class FileController {

    @Autowired
    lateinit var fileService: FileService

    // read
    // todo: file 저장 시 이름 중복 체크 또는 이름 unique하게 임의 지정 필요
    // todo: 또는 동일한 이름을 갖는 파일 전체를 보여주도록 해도 됨
    // todo: 또는 검색 기준을 이름이 아닌 다른 것으로 잡을 수 있음
    @GetMapping("/file")
    fun getFile(
        @RequestParam("name", required = false, defaultValue = "") name: String?
    ): ResponseEntity<*> {
        if (name.isNullOrBlank()) {
            return ResponseEntity.ok(
                NileResponse(
                    errorCode = NileCommonError.INVALID_PARAMETER.getErrorCode(),
                    status = NileCommonError.INVALID_PARAMETER.getHttpStatus(),
                    message = "file 조회를 위해서는 name 입력이 필요합니다."
                )
            )
        }

        val selectedFile: File? = fileService.getFileByName(name)
        selectedFile?.let {
            return ResponseEntity.ok(
                NileResponse(
                    message = "file이 성공적으로 조회되었습니다.",
                    result = selectedFile
                )
            )
        } ?: return ResponseEntity.ok(
            NileResponse(
                errorCode = NileCommonError.INVALID_NAME.getErrorCode(),
                status = NileCommonError.INVALID_NAME.getHttpStatus(),
                message = "존재하지 않는 file입니다."
            )
        )
    }


}