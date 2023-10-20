package com.oztechan.ccc.test

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withoutNameEndingWith
import com.lemonappdev.konsist.api.verify.assert
import org.junit.Test

class PackagingTest {
    @Test
    fun `classes with 'ViewModel' suffix should reside in 'viewmodel' package`() {
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("ViewModel")
            .assert {
                println(it.name)
                it.resideInPackage("..viewmodel..")
            }
    }

    @Test
    fun `classes with 'Repository' suffix should reside in 'repository' package`() {
        Konsist.scopeFromProject()
            .interfaces()
            .withNameEndingWith("Repository")
            .assert {
                println(it.name)
                it.resideInPackage("..repository..")
            }

        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .assert {
                println(it.name)
                it.resideInPackage("..repository..")
            }
    }

    @Test
    fun `classes with 'Storage' suffix should reside in 'storage' package`() {
        Konsist.scopeFromProject()
            .interfaces()
            .withNameEndingWith("Storage")
            .assert {
                println(it.name)
                it.resideInPackage("..storage..")
            }

        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("StorageImpl")
            .assert {
                println(it.name)
                it.resideInPackage("..storage..")
            }
    }

    @Test
    fun `classes with 'DataSource' suffix should reside in 'datasource' package`() {
        Konsist.scopeFromProject()
            .interfaces()
            .withNameEndingWith("DataSource")
            .assert {
                println(it.name)
                it.resideInPackage("..datasource..")
            }

        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("DataSourceImpl")
            .assert {
                println(it.name)
                it.resideInPackage("..datasource..")
            }
    }

    @Test
    fun `classes with 'Service' suffix should reside in 'service' package`() {
        Konsist.scopeFromProject()
            .interfaces()
            .withNameEndingWith("Service")
            .withoutNameEndingWith("ConfigService")
            .assert {
                println(it.name)
                it.resideInPackage("..service..")
            }
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("ServiceImpl")
            .withoutNameEndingWith("ConfigServiceImpl")
            .assert {
                println(it.name)
                it.resideInPackage("..service..")
            }
    }

    @Test
    fun `classes with 'ConfigService' suffix should reside in 'configservice' package`() {
        Konsist.scopeFromProject()
            .interfaces()
            .withNameEndingWith("ConfigService")
            .assert {
                println(it.name)
                it.resideInPackage("..configservice..")
            }
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("ConfigServiceImpl")
            .assert {
                println(it.name)
                it.resideInPackage("..configservice..")
            }
    }

    @Test
    fun `classes with 'Controller' suffix should reside in 'controller' package`() {
        Konsist.scopeFromProject()
            .interfaces()
            .withNameEndingWith("Controller")
            .assert {
                println(it.name)
                it.resideInPackage("..controller..")
            }
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("ControllerImpl")
            .assert {
                println(it.name)
                it.resideInPackage("..controller..")
            }
    }

    @Test
    fun `classes with 'Persistence' suffix should reside in 'persistence' package`() {
        Konsist.scopeFromProject()
            .interfaces()
            .withNameEndingWith("Persistence")
            .assert {
                println(it.name)
                it.resideInPackage("..persistence..")
            }
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("PersistenceImpl")
            .assert {
                println(it.name)
                it.resideInPackage("..persistence..")
            }
    }

    @Test
    fun `classes with 'Api' suffix should reside in 'api' package`() {
        Konsist.scopeFromProject()
            .interfaces()
            .withNameEndingWith("Api")
            .assert {
                println(it.name)
                it.resideInPackage("..api..")
            }
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith("ApiImpl")
            .assert {
                println(it.name)
                it.resideInPackage("..api..")
            }
    }
}
