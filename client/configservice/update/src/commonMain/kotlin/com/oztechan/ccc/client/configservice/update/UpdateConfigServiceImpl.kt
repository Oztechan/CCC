package com.oztechan.ccc.client.configservice.update

import com.oztechan.ccc.client.configservice.update.mapper.toUpdateConfigModel
import com.oztechan.ccc.client.core.remoteconfig.BaseConfigService
import com.oztechan.ccc.client.core.remoteconfig.util.parseToObject
import kotlinx.coroutines.CoroutineScope
import com.oztechan.ccc.client.configservice.update.model.UpdateConfig as UpdateConfigModel
import com.oztechan.ccc.client.core.remoteconfig.model.UpdateConfig as UpdateConfigRCModel

internal class UpdateConfigServiceImpl(
    globalScope: CoroutineScope
) : BaseConfigService<UpdateConfigModel>(
    UpdateConfigRCModel().toUpdateConfigModel(),
    KEY_AD_CONFIG,
    globalScope
), UpdateConfigService {

    override fun String?.decode() = runCatching {
        parseToObject<UpdateConfigRCModel>().toUpdateConfigModel()
    }.getOrElse { default }

    companion object {
        private const val KEY_AD_CONFIG = "update_config"
    }
}
