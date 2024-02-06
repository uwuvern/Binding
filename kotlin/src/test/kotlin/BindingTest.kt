import me.ashydev.binding.bindable.Bindable
import me.ashydev.binding.kotlin.types.plusAssign
import me.ashydev.binding.kotlin.types.value
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/*
 * Copyright (c) 2024 Ashley (uwuvern) <uwuvern@outlook.com>
 *
 * This project is licensed under the MIT license, check the root of the project for
 * more information.
 */

class BindingTest {
    private lateinit var bindable: Bindable<Int>
    private lateinit var otherBindable: Bindable<Int>

    @BeforeEach
    fun setUp() {
        bindable = Bindable(0)
        otherBindable = Bindable(0)
    }

    @Test
    fun `plus assign binding test`() {
        otherBindable += bindable

        bindable.value = 100

        assertEquals(100, otherBindable.value)
        assertEquals(100, bindable.value)
    }

    @Test
    fun `minus assign unbinding test`() {
        otherBindable += bindable

        bindable.value = 100

        otherBindable -= bindable

        bindable.value = 200

        assertEquals(100, otherBindable.value)
        assertEquals(200, bindable.value)
    }

    @Test
    fun `bindable value test`() {
        bindable.value = 100

        assertEquals(100, bindable.value)
    }
}