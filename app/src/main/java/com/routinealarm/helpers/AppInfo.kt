package com.routinealarm.helpers

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.routinealarm.GlobalData.Companion.appContext
import com.routinealarm.R


@Composable
fun AppInfo(
    onConfirm :  () -> Unit = {},
    onDismiss :  () -> Unit = {},
) {
    var shouldDismiss by remember { mutableStateOf(false) }
    if (shouldDismiss) return

    val pm: PackageManager = appContext.getPackageManager()
    val packageName: String = appContext.getPackageName()
    val info = pm.getPackageInfo(packageName, 0)

    DialogWrapper(
        showCancel = false,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    ) {
        val appName = stringResource(R.string.app_name)
        val version = info.versionName + " - " + info.longVersionCode
        val email : String = stringResource(R.string.email)
        val emailSubject = appName+" "+version

            Column( modifier = Modifier.width(250.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.bodyLarge, text = appName)
            Text(style = MaterialTheme.typography.bodyMedium, text = "Carlos Sarria")
            Text(style = MaterialTheme.typography.bodyMedium, text = info.packageName)
            Text(style = MaterialTheme.typography.bodyMedium, text = " Version: "+version)

            // Button to send an email
            Button(modifier = Modifier.padding(top = 30.dp),
                onClick = {
                val i = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
                i.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                i.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                appContext.startActivity(Intent.createChooser(i,"Choose an Email client : "))
            }) {
                Text(text = stringResource(R.string.contact) )
            }
        }
    }
}