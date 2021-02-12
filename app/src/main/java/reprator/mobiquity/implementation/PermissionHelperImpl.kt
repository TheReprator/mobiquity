package reprator.mobiquity.implementation

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import reprator.mobiquity.base_android.PermissionHelper
import reprator.mobiquity.base_android.util.isAndroidMOrLater
import javax.inject.Inject

class PermissionHelperImpl @Inject constructor(): PermissionHelper {

   override fun hasPermissions(context: Context, permission: List<String>): Boolean {
        return hasPermissions(context, *permission.toTypedArray())
    }

    override fun hasPermissions(context: Context, permission: String): Boolean {
        return hasPermissions(context, *arrayOf(permission))
    }

    override fun hasPermissions(context: Context, vararg perms: String): Boolean {
        if (isAndroidMOrLater)
            for (perm in perms)
                if (ContextCompat.checkSelfPermission(
                        context,
                        perm
                    ) != PackageManager.PERMISSION_GRANTED
                )
                    return false
        return true
    }
}