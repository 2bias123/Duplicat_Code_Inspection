package ast

object AstNodeExtensions {

    fun IAstNode.isEquivalentTo(other: IAstNode?, maxDepth: Int = 1000): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this::class != other::class) return false
        if (maxDepth == 0) return false

        val thisChild = firstMeaningfulChild()
        val otherChild = other.firstMeaningfulChild()

        // Both are leaves → compare text directly
        if (thisChild == null && otherChild == null)
            return getText().normalize() == other.getText().normalize()

        // Structural comparison
        var a = thisChild
        var b = otherChild
        while (a != null && b != null) {
            if (!a.isEquivalentTo(b, maxDepth - 1)) return false
            a = a.nextMeaningfulSibling()
            b = b.nextMeaningfulSibling()
        }

        return a == null && b == null
    }

    private fun IAstNode.firstMeaningfulChild(): IAstNode? {
        var child = firstChild
        while (child != null && child.isWhitespaceOrComment)
            child = child.nextSibling
        return child
    }

    private fun IAstNode.nextMeaningfulSibling(): IAstNode? {
        var sibling = nextSibling
        while (sibling != null && sibling.isWhitespaceOrComment)
            sibling = sibling.nextSibling
        return sibling
    }

    private fun String.normalize(): String = replace("\r\n", "\n")
}
