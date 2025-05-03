package system

enum class Permission {
    /**
     * When the permission has not been asked yet
     */
    Unauthorized,

    /**
     * When the permission has been asked and granted
     */
    Granted,

    /**
     * When the permission has been asked and denied
     */
    Denied
}
