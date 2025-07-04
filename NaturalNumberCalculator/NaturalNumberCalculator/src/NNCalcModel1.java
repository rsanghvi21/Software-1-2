import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * Model class.
 *
 * @author Rahul Sanghvi
 */
public final class NNCalcModel1 implements NNCalcModel {

    /**
     * Model variables.
     */
    private final NaturalNumber top, bottom;

    /**
     * No argument constructor.
     */
    public NNCalcModel1() {
        this.top = new NaturalNumber2();
        this.bottom = new NaturalNumber2();
    }

    @Override // Need to override because
    public NaturalNumber top() {
        return this.top; // Returns top NN
    }

    @Override
    public NaturalNumber bottom() {
        return this.bottom; // Returns bottom NN
    }

}
