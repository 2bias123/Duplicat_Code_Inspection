package ast

interface IAstNode {
    val parent: IAstNode?
    val firstChild: IAstNode?
    val nextSibling: IAstNode?
    val prevSibling: IAstNode?
    val isWhitespaceOrComment: Boolean

    fun getText(): String
}
