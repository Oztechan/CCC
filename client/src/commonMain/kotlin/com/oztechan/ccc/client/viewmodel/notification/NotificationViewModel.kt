package com.oztechan.ccc.client.viewmodel.notification

import co.touchlab.kermit.Logger
import com.oztechan.ccc.client.base.BaseData
import com.oztechan.ccc.client.base.BaseSEEDViewModel
import com.oztechan.ccc.client.mapper.toUIModelList
import com.oztechan.ccc.client.model.Notification
import com.oztechan.ccc.client.util.launchIgnored
import com.oztechan.ccc.client.util.toStandardDigits
import com.oztechan.ccc.client.util.toSupportedCharacters
import com.oztechan.ccc.common.db.currency.CurrencyRepository
import com.oztechan.ccc.common.db.notification.NotificationRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NotificationViewModel(
    private val currencyRepository: CurrencyRepository,
    private val notificationRepository: NotificationRepository
) : BaseSEEDViewModel(), NotificationEvent {
    // region SEED
    private val _state = MutableStateFlow(NotificationState())
    override val state = _state.asStateFlow()

    override val event = this as NotificationEvent

    private val _effect = MutableSharedFlow<NotificationEffect>()
    override val effect = _effect.asSharedFlow()

    override val data: BaseData? = null

    init {
        notificationRepository.collectNotifications()
            .onEach {
                _state.update(notificationList = it.toUIModelList())
            }.launchIn(clientScope)
    }

    override fun onBackClick() = clientScope.launchIgnored {
        Logger.d { "NotificationViewModel onBackClick" }
        _effect.emit(NotificationEffect.Back)
    }

    override fun onBaseClick(notification: Notification) = clientScope.launchIgnored {
        Logger.d { "NotificationViewModel onBaseClick $notification" }
        _effect.emit(NotificationEffect.SelectBase(notification))
    }

    override fun onTargetClick(notification: Notification) = clientScope.launchIgnored {
        Logger.d { "NotificationViewModel onTargetClick $notification" }
        _effect.emit(NotificationEffect.SelectTarget(notification))
    }

    override fun onBaseChanged(notification: Notification?, newBase: String) {
        Logger.d { "NotificationViewModel onBaseChanged $notification $newBase" }
        notification?.id?.let {
            notificationRepository.updateBaseById(newBase, it)
        }
    }

    override fun onTargetChanged(notification: Notification?, newTarget: String) {
        Logger.d { "NotificationViewModel onTargetChanged $notification $newTarget" }
        notification?.id?.let {
            notificationRepository.updateTargetById(newTarget, it)
        }
    }

    override fun onAddClick() {
        Logger.d { "NotificationViewModel onAddClick" }
        currencyRepository.getActiveCurrencies().let { list ->
            notificationRepository.addNotification(
                base = list.firstOrNull()?.name ?: "",
                target = list.lastOrNull()?.name ?: ""
            )
        }
    }

    override fun onDeleteClick(notification: Notification) {
        Logger.d { "NotificationViewModel onDeleteClick $notification" }
        notificationRepository.deleteNotification(notification.id)
    }

    override fun onRelationChange(notification: Notification, isGreater: Boolean) {
        Logger.d { "NotificationViewModel onRelationChange $notification $isGreater" }
        notificationRepository.updateRelationById(isGreater, notification.id)
    }

    override fun onRateChange(notification: Notification, rate: String) {
        Logger.d { "NotificationViewModel onRateChange $notification $rate" }
        notificationRepository.updateRateById(
            rate.toSupportedCharacters().toStandardDigits().toDoubleOrNull() ?: 0.0,
            notification.id
        )
    }
    // endregion
}
