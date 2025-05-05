package ejbs;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.ejb.Stateless;

@Stateless
public class ConvertorBean {
    private BigDecimal yenRate = new BigDecimal("83.0602");
    private BigDecimal euroRate = new BigDecimal("0.0093016");

    public BigDecimal dollarToYen(BigDecimal dollars) {
        BigDecimal result = dollars.multiply(yenRate);
        return result.setScale(2, RoundingMode.UP);
    }

    public BigDecimal yenToEuro(BigDecimal yen) {
        BigDecimal result = yen.multiply(euroRate);
        return result.setScale(2, RoundingMode.UP);
    }
}