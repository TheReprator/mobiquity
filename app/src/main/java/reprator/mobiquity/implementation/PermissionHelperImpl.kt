package reprator.mobiquity.implementation

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.scopes.ActivityScoped
import reprator.mobiquity.base_android.PermissionHelper
import reprator.mobiquity.base_android.util.isAndroidMOrLater
import javax.inject.Inject

@ActivityScoped
class PermissionHelperImpl @Inject constructor(private val context: Context) : PermissionHelper {

    override fun hasPermissions(permission: List<String>): Boolean {
        return hasPermissions(context, *permission.toTypedArray())
    }

    override fun hasPermissions(permission: String): Boolean {
        return hasPermissions(context, permission)
    }

    override fun hasPermissions(vararg perms: String): Boolean {
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

    private fun hasPermissions(context: Context, vararg perms: String): Boolean {
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