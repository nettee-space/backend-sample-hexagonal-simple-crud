package me.nettee.core.jpa

import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.SpringExtension
import me.nettee.core.jpa.config.JpaConfig
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.ComponentScan

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackageClasses = [JpaConfig::class])
abstract class JpaTransactionalFreeSpec(body: FreeSpec.() -> Unit) : FreeSpec(body) /* super(body) */ {
    override fun extensions() = listOf(SpringExtension)
}