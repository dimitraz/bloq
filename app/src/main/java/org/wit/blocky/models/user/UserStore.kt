package org.wit.blocky.models.user

interface UserStore {
    fun findAll(): List<UserModel>
    fun findByAuthId(id: String): UserModel?
    fun findByFbId(id: String): UserModel?
    fun create(entry: UserModel)
    fun update(entry: UserModel)
    fun delete(entry: UserModel)
}