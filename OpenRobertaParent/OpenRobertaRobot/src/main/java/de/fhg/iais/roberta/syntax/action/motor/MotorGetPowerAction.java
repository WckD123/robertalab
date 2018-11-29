package de.fhg.iais.roberta.syntax.action.motor;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.BlocklyError;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IMotorVisitor;

/**
 * This class represents the <b>robActions_motor_getPower</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for returning the power of the motor on given port.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} on which the motor is connected.
 */
public class MotorGetPowerAction<V> extends MoveAction<V> {

    private MotorGetPowerAction(String port, BlocklyBlockProperties properties, BlocklyComment comment, BlocklyError error) {
        super(port, BlockTypeContainer.getByName("MOTOR_GET_POWER_ACTION"), properties, comment, error);
        Assert.isTrue(port != null);

        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorGetPowerAction}. This instance is read only and can not be modified.
     *
     * @param port on which the motor is connected that we want to check; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MotorGetPowerAction}
     */
    private static <V> MotorGetPowerAction<V> make(String port, BlocklyBlockProperties properties, BlocklyComment comment, BlocklyError error) {
        return new MotorGetPowerAction<V>(port, properties, comment, error);
    }

    @Override
    public String toString() {
        return "MotorGetPower [port=" + getUserDefinedPort() + "]";
    }

    @Override
    protected V accept(IVisitor<V> visitor) {
        return ((IMotorVisitor<V>) visitor).visitMotorGetPowerAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = helper.extractFields(block, (short) 1);
        String portName = helper.extractField(fields, BlocklyConstants.MOTORPORT);
        return MotorGetPowerAction
            .make(factory.sanitizePort(portName), helper.extractBlockProperties(block), helper.extractComment(block), helper.extractError(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MOTORPORT, getUserDefinedPort().toString());

        return jaxbDestination;
    }
}
