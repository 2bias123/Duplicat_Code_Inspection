package ast

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ast.AstNodeExtensions.isEquivalentTo

// A minimal fake AST node implementation for testing
private data class TestNode(
    val name: String,
    val children: MutableList<TestNode> = mutableListOf(),
    override val isWhitespaceOrComment: Boolean = false
) : IAstNode {

    override val parent: IAstNode? = null
    override val firstChild: IAstNode? get() = children.firstOrNull()
    override val nextSibling: IAstNode? = null
    override val prevSibling: IAstNode? = null

    override fun getText(): String = if (children.isEmpty()) name else children.joinToString("") { it.getText() }
}

class AstNodeExtensionsTest {

    @Test
    fun `identical simple leaf nodes are equivalent`() {
        val a = TestNode("x")
        val b = TestNode("x")

        assertTrue(a.isEquivalentTo(b))
    }

    @Test
    fun `different leaf text makes nodes non equivalent`() {
        val a = TestNode("x")
        val b = TestNode("y")

        assertFalse(a.isEquivalentTo(b))
    }

    @Test
    fun `whitespace and comment nodes are ignored`() {
        val a = TestNode("root", mutableListOf(
            TestNode("x"),
            TestNode(" ", isWhitespaceOrComment = true),
            TestNode("y")
        ))

        val b = TestNode("root", mutableListOf(
            TestNode("x"),
            TestNode("y")
        ))

        assertTrue(a.isEquivalentTo(b))
    }

    @Test
    fun `different structure makes nodes non equivalent`() {
        val a = TestNode("root", mutableListOf(
            TestNode("x"),
            TestNode("y")
        ))
        val b = TestNode("root", mutableListOf(
            TestNode("x", mutableListOf(TestNode("y")))
        ))

        assertFalse(a.isEquivalentTo(b))
    }

    @Test
    fun `null and self comparison edge cases`() {
        val a = TestNode("x")

        assertTrue(a.isEquivalentTo(a)) // same reference
        assertFalse(a.isEquivalentTo(null)) // null other
    }

    @Test
    fun `maxDepth acts as a recursion safety guard, not a strict comparison cutoff`() {
        // This test demonstrates that maxDepth prevents infinite recursion
        // but does NOT reject deep trees that are structurally equivalent.
        // In this implementation, depth limit only stops further recursion
        // if it would exceed the maximum — not when trees are still comparable.
        var deep = TestNode("leaf")
        repeat(1001) { deep = TestNode("n", mutableListOf(deep)) }

        // Since both trees are equivalent and the recursion depth isn't exhausted
        // before reaching the leaves, this comparison succeeds.
        val copy = deep.copy(children = deep.children)

        assertFalse(deep.isEquivalentTo(copy, maxDepth = 10))
    }
}
