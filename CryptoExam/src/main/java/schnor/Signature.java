package schnor;

import java.math.BigInteger;

//клас для підпсиу
public  class Signature {
    private BigInteger r;
    private BigInteger s;

    public Signature(BigInteger r, BigInteger s) {
        this.r = r;
        this.s = s;
    }

    public BigInteger getR() {
        return r;
    }

    public BigInteger getS() {
        return s;
    }
}
