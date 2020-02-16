package mustafaozhan.github.com.mycurrencies.function.extension

import mustafaozhan.github.com.mycurrencies.model.Rates
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DataExtTest {

    @Test
    fun `calculate results by base`() {
        val date = "12:34:56 01.01.2020"
        val base = "EUR"
        val target = "USD"
        val rates = Rates(base, date, uSD = 5.0)

        assertEquals(
            rates.calculateResult(target, "5.0"),
            25.0,
            0.001
        )
    }
}
