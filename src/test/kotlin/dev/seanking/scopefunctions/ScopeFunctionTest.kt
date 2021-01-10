package dev.seanking.scopefunctions

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import org.junit.jupiter.api.Test

class ScopeFunctionTest {
    @Test
    fun `should use _this_ as object reference and return context object using _apply_ function`() {
        // When
        val numbers = mutableListOf<Int>().apply {
            add(1)
            add(2)
        }

        // Then
        assertThat(numbers).containsExactly(1, 2)
    }

    @Test
    fun `should use _it_ as object reference and return context object using _also_ function`() {
        // When
        val numbers = mutableListOf<Int>().also {
            it.add(1)
            it.add(2)
        }

        // Then
        assertThat(numbers).containsExactly(1, 2)
    }

    @Test
    fun `should use _this_ as object reference and return lambda result using _run_ function`() {
        // Given
        val numbers = mutableListOf<Int>()

        // When
        val count = numbers.run {
            add(1)
            add(2)
            count()
        }

        // Then
        assertThat(count).isEqualTo(2)
    }

    @Test
    fun `should use _it_ as object reference and return lambda result using _let_ function`() {
        // Given
        val numbers = mutableListOf<Int>()

        // When
        val count = numbers.let {
            it.add(1)
            it.add(2)
            it.count()
        }

        // Then
        assertThat(count).isEqualTo(2)
    }

    @Test
    fun `should use _this_ as object reference and return lambda result using _with_ function`() {
        // Given
        val numbers = mutableListOf<Int>(1, 2)

        // When
        val count = with(numbers) {
            count()
        }

        // Then
        assertThat(count).isEqualTo(2)
    }

    @Test
    fun `should execute code block for non-null values`() {
        // Given 
        val optionalVal: String? = "Hello"

        // When
        val message = optionalVal?.let { "$it World!" }

        // Then
        assertThat(message).isNotNull().isEqualTo("Hello World!")
    }

    @Test
    fun `should not execute code block for non-null values`() {
        // Given 
        val optionalVal: String? = null

        // When
        val message = optionalVal?.let { "$it World!" }

        // Then
        assertThat(message).isNull()
    }
}
