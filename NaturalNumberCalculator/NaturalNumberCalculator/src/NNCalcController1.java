import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * Controller class.
 *
 * @author Rahul Sanghvi
 */
public final class NNCalcController1 implements NNCalcController {

    /**
     * Model object.
     */
    private final NNCalcModel model;

    /**
     * View object.
     */
    private final NNCalcView view;

    /**
     * Useful constants (zero and one added)
     */

    private static final NaturalNumber ZERO = new NaturalNumber2(0);
    private static final NaturalNumber ONE = new NaturalNumber2(1);
    private static final NaturalNumber TWO = new NaturalNumber2(2),
            INT_LIMIT = new NaturalNumber2(Integer.MAX_VALUE);

    /**
     * Updates this.view to display this.model, and to allow only operations
     * that are legal given this.model.
     *
     * @param model
     *            the model
     * @param view
     *            the view
     * @ensures [view has been updated to be consistent with model]
     */
    private static void updateViewToMatchModel(NNCalcModel model, NNCalcView view) {

        NaturalNumber top = model.top();
        NaturalNumber bottom = model.bottom();

        // Checks to see if operation is legal for subtraction and adjusts
        // button accordingly
        if (top.compareTo(bottom) >= 0) {
            view.updateSubtractAllowed(true);
        } else {
            view.updateSubtractAllowed(false);
        }

        // Checks to see if operation is legal for division and adjusts
        // button accordingly
        if (bottom.compareTo(ONE) > 0) {
            view.updateDivideAllowed(true);
        } else {
            view.updateDivideAllowed(false);
        }

        // Checks to see if operation is legal for power and adjusts
        // button accordingly
        if (bottom.compareTo(INT_LIMIT) <= 0) {
            view.updatePowerAllowed(true);
        } else {
            view.updatePowerAllowed(false);
        }

        // Checks to see if operation is legal for root and adjusts
        // button accordingly
        if (bottom.compareTo(TWO) >= 0 && bottom.compareTo(INT_LIMIT) <= 0) {
            view.updateRootAllowed(true);
        } else {
            view.updateRootAllowed(false);
        }
        // Updates top and bottom display
        view.updateTopDisplay(top);
        view.updateBottomDisplay(bottom);

    }

    /**
     * Constructor.
     *
     * @param model
     *            model to connect to
     * @param view
     *            view to connect to
     */
    public NNCalcController1(NNCalcModel model, NNCalcView view) {
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processClearEvent() {
        /*
         * Get alias to bottom from model
         */
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        bottom.clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processSwapEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        NaturalNumber temp = top.newInstance();
        temp.transferFrom(top);
        top.transferFrom(bottom);
        bottom.transferFrom(temp);
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processEnterEvent() {

        // Get NN top and bottom
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        top.copyFrom(bottom);

        // Update model
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processAddEvent() {

        // Get NN top and bottom
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        bottom.add(top);
        top.copyFrom(ZERO);

        // Update model
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processSubtractEvent() {

        // Get NN top and bottom
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        top.subtract(bottom);
        bottom.transferFrom(top);

        // Update model
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processMultiplyEvent() {
        // Get NN top and bottom
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        bottom.multiply(top);
        top.copyFrom(ZERO);

        // Update model
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processDivideEvent() {
        // Get NN top and bottom
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        NaturalNumber temp = top.divide(bottom);
        bottom.transferFrom(top);
        top.transferFrom(temp);

        // Update model
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processPowerEvent() {
        // Get NN top and bottom
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        top.power(bottom.toInt());
        bottom.transferFrom(top);
        top.copyFrom(ZERO);
        updateViewToMatchModel(this.model, this.view);

    }

    @Override
    public void processRootEvent() {
        // Get NN top and bottom
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        top.root(bottom.toInt());
        bottom.transferFrom(top);
        top.copyFrom(ZERO);
        // Update model
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processAddNewDigitEvent(int digit) {
        // Get NN bottom
        NaturalNumber bottom = this.model.bottom();

        // Multiply bottom number by 10 and add digit to NN
        bottom.multiplyBy10(digit);

        // Update model
        updateViewToMatchModel(this.model, this.view);

    }

}
