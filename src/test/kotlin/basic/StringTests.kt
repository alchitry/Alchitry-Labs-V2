package basic

import com.alchitry.labs2.camelToSnakeCase
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StringTests {
    @Test
    fun camelToSnakeCaseTest() {
        assertEquals("clog2", "clog2".camelToSnakeCase())
        assertEquals("pull_down_fake_2d", "pullDownFake2D".camelToSnakeCase())
        assertEquals("something_normal", "somethingNormal".camelToSnakeCase())
        assertEquals("c_fixed_point", "cFixedPoint".camelToSnakeCase())
    }
}