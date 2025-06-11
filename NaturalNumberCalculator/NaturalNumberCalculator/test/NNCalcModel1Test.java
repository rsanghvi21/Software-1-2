import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;

public class NNCalcModel1Test {

    /**
     * Test initialization
     */
    @Test
    public void testInit() {
        NNCalcModel model = new NNCalcModel1();
        NaturalNumber top = model.top();
        NaturalNumber bottom = model.bottom();

        assertEquals(0, top.toInt());
        assertEquals(0, bottom.toInt());
    }

    /**
     * Test top NN
     */
    @Test
    public void testTop() {
        NNCalcModel model = new NNCalcModel1();
        NaturalNumber top = model.top();
        top.setFromInt(23);

        assertEquals(23, top.toInt());
    }

    /**
     * Test bottom NN
     */
    @Test
    public void testBottom() {
        NNCalcModel model = new NNCalcModel1();
        NaturalNumber bottom = model.bottom();
        bottom.setFromInt(23);

        assertEquals(23, bottom.toInt());
    }

}
