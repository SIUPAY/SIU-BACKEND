package siu.siubackend.zkloginsalt.domain.service

import org.springframework.stereotype.Component
import siu.siubackend.zkloginsalt.domain.ZkLoginSalt
import java.util.*

@Component
object ZkLoginSaltFactory {
    fun create(
        oauthUserId: String,
        zkloginSalt: String
    ): ZkLoginSalt {
        return ZkLoginSalt(
            identifier = UUID.randomUUID(),
            oauthUserId = oauthUserId,
            zkloginSalt = zkloginSalt
        )
    }
}
