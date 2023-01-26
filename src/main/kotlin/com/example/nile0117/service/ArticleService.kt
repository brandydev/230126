package com.example.nile0117.service

import com.example.nile0117.domain.entity.*
import com.example.nile0117.domain.enums.FileType
import com.example.nile0117.domain.enums.Location
import com.example.nile0117.domain.enums.Status
import com.example.nile0117.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ArticleService {

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @Autowired
    lateinit var articleContentRepository: ArticleContentRepository

    @Autowired
    lateinit var hashtagRepository: HashtagRepository

    @Autowired
    lateinit var columnistRepository: ColumnistRepository

    @Autowired
    lateinit var fileRepository: FileRepository

    // setting
    fun setArticle(article: Article): Article {
        article.contents = mutableListOf()
        article.contents.addAll(articleContentRepository.findAllByArticleId(article.id))
        article.hashtags = mutableListOf()
        article.hashtags.addAll(hashtagRepository.findAllByArticleId(article.id))
        article.columnist = columnistRepository.findByArticleId(article.id)
        article.files = mutableListOf()
        article.files.addAll(fileRepository.findAllByArticleId(article.id))
        return article
    }

    // duplicate check
    fun isUniqueArticle(slug: String): Boolean {
        return !articleRepository.existsBySlug(slug)
    }

    // create
    @Transactional(rollbackFor = [Exception::class])
    fun addArticle(request: Article): Article? {
        if (isUniqueArticle(request.slug)) {
            val targetArticle = Article(
                request.slug, // todo: toSlug extension 적용
                request.status ?: Status.HIDDEN,
                request.openedAt ?: LocalDateTime.now(), // todo: openedAt 입력 시 오류 발생
                request.nftCreator ?: "unknown"
            )
            articleRepository.save(targetArticle)

            val targetColumnist = Columnist(
                targetArticle.id,
                request.columnist!!.name ?: "NILE Editorial Team",
                request.columnist!!.info ?: "We publish a variety of articles for NILER under the theme of Web3.",
                request.columnist!!.profileImage ?: "empty image" // todo: 기본 이미지 경로 설정
            )
            columnistRepository.save(targetColumnist)

            request.contents.forEach {
                val description = it.description ?: it.content!!.slice(0..1) // description 자동 생성 todo: 글자수 조정
                val targetArticleContent = ArticleContent(
                    targetArticle.id,
                    it.language,
                    it.title,
                    it.content
                )
                targetArticleContent.description = description
                articleContentRepository.save(targetArticleContent)
            }

            request.hashtags.forEach {
                if (!it.text.isNullOrBlank()) {
                    hashtagRepository.save(Hashtag(
                        targetArticle.id,
                        it.text
                    ))
                }
            }

            request.files.forEach {
                if (!it.path.isNullOrBlank()) {
                    fileRepository.save(File(
                        targetArticle.id,
                        it.name ?: (targetArticle.slug + "_" + it.type.toString().lowercase()),
                        it.path ?: "invalid_path",
                        it.type ?: FileType.OTHER,
                        it.location ?: Location.OTHER
                    ))
                }
            }

            return setArticle(targetArticle)
        } else {
            return null
        }
    }

    // read
    fun getArticles(): List<Article> {
        val targetArticles = articleRepository.findAllByIdIsNotNull()
        targetArticles.forEach {
            setArticle(it)
        }

        return targetArticles
    }

    fun getArticleBySlug(slug: String): Article? {
        val isExist = articleRepository.existsBySlug(slug)
        return if (!isExist) {
            null
        } else {
            val targetArticle = articleRepository.findBySlug(slug)
            setArticle(targetArticle!!)
        }
    }

    fun getArticleByHashtag(text: String): List<Article> {
        val hashtags = hashtagRepository.findAllByText(text)
        val articles = mutableListOf<Article>()
        hashtags.forEach {
            val article = articleRepository.findById(it.articleId!!).get()
            articles.add(getArticleBySlug(article.slug)!!)
        }

        return articles
    }

    // update
    @Transactional(rollbackFor = [Exception::class])
    fun updateArticleBySlug(slug: String, request: Article): Article? {
        val isExist = articleRepository.existsBySlug(slug)
        return if (!isExist) {
            null
        } else {
            // todo: 로직 검토 및 수정
            removeArticleBySlug(slug)
            addArticle(request)
        }
    }

    // delete
    @Transactional(rollbackFor = [Exception::class])
    fun removeArticleBySlug(slug: String): Article? {
        val isExist = articleRepository.existsBySlug(slug)
        return if (!isExist) {
            null
        } else {
            val targetArticle = articleRepository.findBySlug(slug)
            articleRepository.deleteById(targetArticle!!.id!!)
            val targetArticleContents = articleContentRepository.findAllByArticleId(targetArticle.id)
            targetArticleContents.forEach {
                articleContentRepository.delete(it)
            }
            // todo: article 삭제 시, hashtag 어떻게 처리할 지 >> 일단은 article-hashtag 1:1 매핑 처리
            val targetHashtags = hashtagRepository.findAllByArticleId(targetArticle.id)
            targetHashtags.forEach {
                hashtagRepository.delete(it)
            }
            // todo: columnist도!
            val targetColumnist = columnistRepository.findByArticleId(targetArticle.id)
            columnistRepository.deleteById(targetColumnist!!.id!!)
            // todo: file도!
            val targetFiles = fileRepository.findAllByArticleId(targetArticle.id)
            targetFiles.forEach {
                fileRepository.delete(it)
            }
            setArticle(targetArticle)
        }
    }
}