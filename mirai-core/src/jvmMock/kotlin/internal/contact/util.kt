/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.mock.internal.contact

import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.PermissionDeniedException
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.toUHexString

internal fun Member.requireBotPermissionHigherThanThis(msg: String) {
    if (this.permission < this.group.botPermission) return

    throw PermissionDeniedException("bot current permission ${group.botPermission} can't modify $id($permission), $msg")
}

internal infix fun MessageSource.plusMsg(msg: Message): MessageChain = buildMessageChain {
    add(this@plusMsg)
    if (msg is MessageChain) {
        msg.forEach { sub ->
            if (sub !is MessageSource) {
                add(sub)
            }
        }
    } else if (msg !is MessageSource) {
        add(msg)
    }
}

internal suspend fun ExternalResource.mockUploadAudio() = inResource {
    OfflineAudio(
        filename = md5.toUHexString() + ".amr",
        fileMd5 = md5,
        fileSize = size,
        codec = AudioCodec.SILK,
        extraData = null,
    )
}

internal suspend fun ExternalResource.mockUploadVoice() = inResource {
    @Suppress("DEPRECATION")
    Voice(
        fileName = md5.toUHexString() + ".amr",
        md5 = md5,
        fileSize = size,
        _url = "https://www.baidu.com"
    )
}