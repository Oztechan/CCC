import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    libs.plugins.apply {
        id(multiplatform.get().pluginId)
        id(cocoapods.get().pluginId)
    }
}

kotlin {
    @Suppress("OPT_IN_USAGE")
    targetHierarchy.default()

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
            export(project(Modules.Common.Core.model))

            Modules.Client.Core.apply {
                export(project(viewModel))
                export(project(analytics))
                export(project(shared))
            }

            Modules.Client.ViewModel.apply {
                export(project(main))
                export(project(calculator))
                export(project(currencies))
                export(project(settings))
                export(project(selectCurrency))
                export(project(watchers))
                export(project(premium))
                export(project(premium))
            }
        }

        targets.withType<KotlinNativeTarget> {
            binaries.withType<Framework> {
                linkerOpts.add("-lsqlite3")
            }
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val iosMain by getting {
            dependencies {
                libs.common.apply {
                    implementation(koinCore)
                    implementation(kermit)
                }

                api(project(Modules.IOS.Repository.background))
                api(project(Modules.Common.Core.model))

                Modules.Client.Core.apply {
                    api(project(viewModel))
                    api(project(analytics))
                    api(project(shared))
                }

                Modules.Client.ViewModel.apply {
                    api(project(main))
                    api(project(calculator))
                    api(project(currencies))
                    api(project(settings))
                    api(project(selectCurrency))
                    api(project(watchers))
                    api(project(premium))
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

                implementation(Submodules.logmob)
            }
        }
    }
}
