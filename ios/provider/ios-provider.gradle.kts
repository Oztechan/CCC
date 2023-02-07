plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    libs.plugins.apply {
        id(multiplatform.get().pluginId)
        id(cocoapods.get().pluginId)
    }
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        ProjectSettings.apply {
            summary = PROJECT_NAME
            homepage = HOMEPAGE
            ios.deploymentTarget = IOS_DEPLOYMENT_TARGET
            version = getVersionName(project)
        }

        framework {
            baseName = Modules.IOS.provider.frameworkName
            isStatic = true

            export(project(Modules.IOS.Repository.background))
            export(project(Modules.Client.Core.viewModel))
            export(project(Modules.Client.Core.analytics))
            export(project(Modules.Common.Core.model))

            Modules.Client.ViewModel.apply {
                export(project(main))
                export(project(calculator))
                export(project(currencies))
                export(project(settings))
                export(project(selectCurrency))
                export(project(watchers))
            }
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {

                libs.common.apply {
                    implementation(koinCore)
                    implementation(kermit)
                }

                api(project(Modules.IOS.Repository.background))
                api(project(Modules.Client.Core.viewModel))
                api(project(Modules.Client.Core.analytics))
                api(project(Modules.Common.Core.model))

                Modules.Client.ViewModel.apply {
                    api(project(main))
                    api(project(calculator))
                    api(project(currencies))
                    api(project(settings))
                    api(project(selectCurrency))
                    api(project(watchers))
                }

                Modules.Common.Core.apply {
                    implementation(project(database))
                    implementation(project(network))
                    implementation(project(infrastructure))
                }

                Modules.Common.DataSource.apply {
                    implementation(project(conversion))
                }

                Modules.Client.Core.apply {
                    implementation(project(persistence))
                    implementation(project(shared))
                }
                Modules.Client.Storage.apply {
                    implementation(project(app))
                    implementation(project(calculation))
                }

                Modules.Client.DataSource.apply {
                    implementation(project(currency))
                    implementation(project(watcher))
                }
                Modules.Client.Service.apply {
                    implementation(project(backend))
                }
                Modules.Client.ConfigService.apply {
                    implementation(project(ad))
                    implementation(project(review))
                    implementation(project(update))
                }

                Modules.Client.Repository.apply {
                    implementation(project(adControl))
                    implementation(project(appConfig))
                }

                implementation(project(Modules.Submodules.logmob))
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}
