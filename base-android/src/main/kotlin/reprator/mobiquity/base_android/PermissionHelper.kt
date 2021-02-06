package reprator.mobiquity.base_android

import android.content.Context

interface PermissionHelper {
    fun hasPermissions(context: Context, permission: List<String>): Boolean

    fun hasPermissions(context: Context, permission: String): Boolean

    fun hasPermissions(context: Context, vararg perms: String): Boolean
}