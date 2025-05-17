package kiota.file.picker

interface Multiple {
    val max: Int

    companion object : Multiple {
        override val max: Int = Int.MAX_VALUE

        operator fun invoke(max: Int): Multiple = object : Multiple {
            override val max: Int = max
        }
    }
}
