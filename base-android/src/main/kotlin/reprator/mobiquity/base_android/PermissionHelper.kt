package reprator.mobiquity.base_android

interface PermissionHelper {
    fun hasPermissions(permission: List<String>): Boolean

    fun hasPermissions(permission: String): Boolean

    fun hasPermissions(vararg perms: String): Boolean
}