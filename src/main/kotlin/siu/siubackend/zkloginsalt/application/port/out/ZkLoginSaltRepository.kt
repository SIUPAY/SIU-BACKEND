package siu.siubackend.zkloginsalt.application.port.out

import siu.siubackend.zkloginsalt.domain.ZkLoginSalt

interface ZkLoginSaltRepository {
    fun findByOauthUserId(oauthUserId: String): ZkLoginSalt?
    fun save(zkLoginSalt: ZkLoginSalt): ZkLoginSalt
}
