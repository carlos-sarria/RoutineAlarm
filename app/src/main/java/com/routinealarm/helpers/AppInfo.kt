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
    val shouldDismiss by remember { mutableStateOf(false) }
    if (shouldDismiss) return

    val pm: PackageManager = appContext.packageManager
    val packageName: String = appContext.packageName
    val info = pm.getPackageInfo(packageName, 0)

    DialogWrapper(
        showCancel = false,
        onDismiss = onDismiss,
        onConfirm = onConfirm
    ) {
        val appName = stringResource(R.string.app_name)
        val version = info.versionName + " - " + info.longVersionCode
        val email : String = stringResource(R.string.email)
        val emailSubject = info.packageName+" "+version
        val emailBody = "Application: " + appName +
                        "\nVersion: " + version +
                        "\nManufacturer: " + android.os.Build.MANUFACTURER +
                        "\nModel: " + android.os.Build.MODEL+
                        "\nDevice: " + android.os.Build.DEVICE+
                        "\n-----------------------\n"

            Column( modifier = Modifier.width(250.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(modifier = Modifier.padding(20.dp), style = MaterialTheme.typography.bodyLarge, text = appName)
            Text(style = MaterialTheme.typography.bodyMedium, text = "Carlos Sarria")
            Text(style = MaterialTheme.typography.bodyMedium, text = info.packageName)
            Text(style = MaterialTheme.typography.bodyMedium, text = version)

            // Button to send an email
            Button(modifier = Modifier.padding(top = 30.dp),
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.setData(Uri.parse("mailto:")) // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                    intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                    intent.putExtra(Intent.EXTRA_TEXT, emailBody)
                    appContext.startActivity(Intent.createChooser(intent,"Choose an Email client: "))
            }) {
                Text(text = stringResource(R.string.contact) )
            }
        }
    }
}