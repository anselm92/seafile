package com.bwksoftware.android.seafile.mapper

import com.bwksoftware.android.seafile.domain.AvatarTemplate
import com.bwksoftware.android.seafile.model.Avatar
import javax.inject.Inject

/**
 * Created by anselm.binninger on 12/10/2017.
 */
class ModelMapper @Inject constructor() {
    fun transformAvatar(avatar: AvatarTemplate): Avatar {
        return Avatar(avatar.url)
    }
}