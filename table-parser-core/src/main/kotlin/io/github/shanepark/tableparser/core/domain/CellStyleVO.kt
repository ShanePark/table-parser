package io.github.shanepark.tableparser.core.domain

class CellStyleVO(
    val isThead: Boolean,
    val isNumber: Boolean,
    val classNames: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CellStyleVO) return false

        if (isThead != other.isThead) return false
        if (isNumber != other.isNumber) return false
        if (classNames != other.classNames) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isThead.hashCode()
        result = 31 * result + isNumber.hashCode()
        result = 31 * result + classNames.hashCode()
        return result
    }
}
