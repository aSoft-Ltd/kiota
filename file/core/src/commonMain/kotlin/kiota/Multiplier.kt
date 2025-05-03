@file:JsExport

package kiota

import kotlinx.JsExport

enum class Multiplier(val exponent: Int) {
    Unit(0),
    Kilo(3),
    Mega(6),
    Giga(9),
    Tera(12),
    Peta(15),
    Exa(18),
    Zetta(21),
    Yotta(24);

    override fun toString(): String = if(this == Unit) "" else name.first().uppercase()
}