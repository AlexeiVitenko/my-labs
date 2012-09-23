package by.bsuir.avdb;

import java.math.BigDecimal;

public class Operation {
    public BigDecimal rate;
    public boolean buy_sell;
    public int count;

    public Operation(BigDecimal rate, boolean flag, int c) {
        this.rate = rate;
        buy_sell = flag;
        count = c;// * (flag ? 1 : -1);
    }
}
