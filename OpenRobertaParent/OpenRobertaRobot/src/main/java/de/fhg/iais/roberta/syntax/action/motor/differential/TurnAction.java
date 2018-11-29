package de.fhg.iais.roberta.syntax.action.motor.differential;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.BlocklyError;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDifferentialMotorVisitor;

/**
 * This class represents the <b>robActions_motorDiff_turn</b> and <b>robActions_motorDiff_turn_for</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motors in pilot mode and turn on given direction.<br/>
 * <br/>
 * The client must provide the {@link TurnDirection} and {@link MotionParam} (distance the robot should cover and speed).
 */
public class TurnAction<V> extends Action<V> {
    private final ITurnDirection direction;
    private final MotionParam<V> param;

    private TurnAction(ITurnDirection direction, MotionParam<V> param, BlocklyBlockProperties properties, BlocklyComment comment, BlocklyError error) {
        super(BlockTypeContainer.getByName("TURN_ACTION"), properties, comment, error);
        Assert.isTrue(direction != null && param != null);
        this.direction = direction;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link TurnAction}. This instance is read only and can not be modified.
     *
     * @param direction {@link TurnDirection} in which the robot will drive; must be <b>not</b> null,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (distance the robot should cover and speed); must be <b>not</b>
     *        null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link TurnAction}.
     */
    private static <V> TurnAction<V> make(
        ITurnDirection direction,
        MotionParam<V> param,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        BlocklyError error) {
        return new TurnAction<V>(direction, param, properties, comment, error);
    }

    /**
     * @return {@link TurnDirection} in which the robot will drive
     */
    public ITurnDirection getDirection() {
        return this.direction;
    }

    /**
     * @return {@link MotionParam} for the motor (speed and distance in which the motors are set).
     */
    public MotionParam<V> getParam() {
        return this.param;
    }

    @Override
    public String toString() {
        return "TurnAction [direction=" + this.direction + ", param=" + this.param + "]";
    }

    @Override
    protected V accept(IVisitor<V> visitor) {
        return ((IDifferentialMotorVisitor<V>) visitor).visitTurnAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields;
        String mode;
        List<Value> values;
        MotionParam<V> mp;
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        fields = helper.extractFields(block, (short) 1);
        mode = helper.extractField(fields, BlocklyConstants.DIRECTION);

        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_TURN) ) {
            values = helper.extractValues(block, (short) 1);
            Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));
            mp = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(expr)).build();
        } else {
            values = helper.extractValues(block, (short) 2);
            Phrase<V> left = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));
            Phrase<V> right = helper.extractValue(values, new ExprParam(BlocklyConstants.DEGREE, BlocklyType.NUMBER_INT));
            MotorDuration<V> md = new MotorDuration<V>(factory.getMotorMoveMode("DEGREE"), helper.convertPhraseToExpr(right));
            mp = new MotionParam.Builder<V>().speed(helper.convertPhraseToExpr(left)).duration(md).build();
        }
        return TurnAction
            .make(factory.getTurnDirection(mode), mp, helper.extractBlockProperties(block), helper.extractComment(block), helper.extractError(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.DIRECTION, getDirection().toString());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.POWER, getParam().getSpeed());

        if ( getParam().getDuration() != null ) {
            Ast2JaxbHelper.addValue(jaxbDestination, getParam().getDuration().getType().toString(), getParam().getDuration().getValue());
        }

        return jaxbDestination;
    }
}
