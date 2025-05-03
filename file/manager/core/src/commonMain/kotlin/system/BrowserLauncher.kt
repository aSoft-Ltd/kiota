package system

import koncurrent.Later

/**
 * Can ask the system to open a particular file
 *
 * In the browser, this will open a new tab with the file
 * In the native platform, this will open the file with the default application
 */
interface BrowserLauncher {
    fun open(url: String): Later<String>
}