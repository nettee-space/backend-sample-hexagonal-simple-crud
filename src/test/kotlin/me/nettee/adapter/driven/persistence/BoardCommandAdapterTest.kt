package me.nettee.adapter.driven.persistence

import io.kotest.assertions.eq.eq
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import me.nettee.board.adapter.driven.persistence.BoardCommandAdapter
import me.nettee.board.adapter.driven.persistence.BoardJpaRepository
import me.nettee.board.adapter.driven.persistence.entity.BoardEntity
import me.nettee.board.adapter.driven.persistence.mapper.BoardEntityMapper
import me.nettee.board.application.domain.Board
import me.nettee.board.application.domain.type.BoardStatus
import java.time.Instant
import java.util.Optional

class BoardCommandAdapterTest: FreeSpec ({

    lateinit var repository: BoardJpaRepository
    lateinit var mapper: BoardEntityMapper
    lateinit var adapter: BoardCommandAdapter
    lateinit var domain: Board
    lateinit var savedDomain: Board
    lateinit var entity: BoardEntity
    lateinit var savedEntity: BoardEntity

    beforeTest {
        repository = mockk<BoardJpaRepository>(relaxed = true)
        mapper = mockk<BoardEntityMapper>(relaxed = true)
        adapter = BoardCommandAdapter(repository, mapper)
        domain = mockk<Board>() {
            every { id } returns null
            every { title } returns "Hi"
            every { content } returns "Hello World"
            every { status } returns null
            every { createdAt } returns null
            every { updatedAt } returns null
        }

        savedDomain = mockk<Board>() {
            every { id } returns 1L
            every { title } returns "Hi"
            every { content } returns "Hello World"
            every { status } returns BoardStatus.ACTIVE
            every { createdAt } returns Instant.now()
            every { updatedAt } returns Instant.now()
        }

        entity = mockk<BoardEntity>() {
            every { id } returns null
            every { title } returns "Hi"
            every { content } returns "Hello World"
            every { status } returns null
            every { createdAt } returns null
            every { updatedAt } returns null
        }

        savedEntity = mockk<BoardEntity>() {
            every { id } returns 1L
            every { title } returns "Hi"
            every { content } returns "Hello World"
            every { status } returns BoardStatus.ACTIVE
            every { createdAt } returns Instant.now()
            every { updatedAt } returns Instant.now()
        }
    }

    "Create(boardDomain)" - {
        "save: boardDomain -> boardDomain" {
            every { mapper.toEntity(domain) } returns entity
            every { mapper.toDomain(savedEntity) } returns savedDomain
            every { repository.save(entity) } returns savedEntity

            val result = adapter.create(domain)

            verify { mapper.toEntity(domain) }
            verify { repository.save(entity) }
            verify { mapper.toDomain(savedEntity) }
            result shouldBe savedDomain
        }

        "save: noTitleDomain -> Exception(title cannot be null)" {
            val noTitleDomain = domain
            every { noTitleDomain.title } returns null
            every { mapper.toEntity(noTitleDomain) } throws IllegalArgumentException("title cannot be null")

            val result = shouldThrow<IllegalArgumentException> {
                adapter.create(noTitleDomain)
            }

            verify { mapper.toEntity(noTitleDomain) }
            result.message shouldBe "title cannot be null"
        }

        "save: noContentDomain -> Exception(content cannot be null)" {
            val noContentDomain = domain
            every { noContentDomain.content } returns null
            every { mapper.toEntity(noContentDomain) } throws IllegalArgumentException("content cannot be null")

            val result = shouldThrow<IllegalArgumentException> {
                adapter.create(noContentDomain)
            }

            verify { mapper.toEntity(noContentDomain) }
            result.message shouldBe "content cannot be null"
        }
    }

// 추가 작업 필요
//    "Update(boardDomain)" - {
//        "updateBoard: updatedId -> updateDomain" {
//            val updatedDomain = savedDomain
//            val updatedEntity = savedEntity
//            every { repository.findById(updatedDomain.id) } returns Optional.ofNullable(savedEntity)
//            every { mapper.toDomain(updatedEntity) } returns updatedDomain
//
//            val result = adapter.update(updatedDomain)
//
//            eq(result, updatedDomain)
//        }
//    }
//
// 추가 작업 필요
//    "Delete(boardDomain)" - {
//        "softDelete: id -> void" {
//            val changeStatusRemovedEntity = entity
//            every { changeStatusRemovedEntity.id } returns 1L
//            every { changeStatusRemovedEntity.status } returns BoardStatus.REMOVED
//            every { changeStatusRemovedEntity.createdAt }returns savedEntity.createdAt
//            every { changeStatusRemovedEntity.updatedAt }returns Instant.now()
//            every { repository.findById(savedDomain.id) } returns Optional.of(savedEntity)
//            every { repository.save(changeStatusRemovedEntity) } returns changeStatusRemovedEntity
//            every { mapper.toDomain(savedEntity) } returns savedDomain
//            every { mapper.toEntity(savedDomain) } returns savedEntity
//
//            adapter.delete(savedEntity.id)
//
//            verify { repository.findById(savedDomain.id) }
//            // H2 DB를 이용한 Status 변화 확인 테스트 코드 필요하다고 생각되는 부분
//        }
//    }
})