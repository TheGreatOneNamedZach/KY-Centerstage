package org.firstinspires.ftc.teamcode.action;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class arm {
    // CONSTRUCT
    // DECLARE NULL
    DcMotor arm;
    TouchSensor touchSensor; // True when pressed
    Telemetry telemetry;
    // DECLARE CUSTOM
    private static double totalSpeed = 1.00; // Speed multiplier for the slide
    private static final double maxAutoSpeed = 0.6; // Maximum speed the linear slide can operate at AUTOMATICALLY. "Auto" does not stand for "autonomous"
    private static final double hoverSpeed = 0.01; // Speed at which the slide can hover

    // METHODS
    /** Initializes the arm.
     * @param opMode If you are constructing from an Auto or TeleOp, type in "this" without the quotation marks.
     */
    public void init(@NonNull OpMode opMode) {
        HardwareMap hardwareMap = opMode.hardwareMap;
        telemetry = opMode.telemetry;
        arm = hardwareMap.get(DcMotor.class, "Arm");
        touchSensor = hardwareMap.get(TouchSensor.class, "Touch Sensor");
        arm.setDirection(DcMotorSimple.Direction.REVERSE);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /** Moves the arm. This is best used with a joystick.
     * @apiNote Remember if you are using the "y" of a joystick reverse it!
     * @param power power to move the arm at. This is multiplied by the max speed.
     */
    public void setPower(double power) {
        double armPower = power * totalSpeed;

        if (!(touchSensorPressed() && armPower < 0)) { // Everything BUT moving the arm down when fully retracted.
            if (armPower == 0 && !touchSensorPressed()) { // If the arm is not moving AND the limit switch is not pressed...
                // This means the arm needs to hover at its position
                arm.setPower(hoverSpeed);
            } else if(armPower == 0) { // If the arm is not moving AND the limit switch is pressed...
                // This means the arm is fully retracted. This is the perfect opportunity to recalibrate the encoders
                arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            } else { // The arm must be moving
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                arm.setPower(armPower);
            }
        }
    }

    /** Fully retracts the arm
     * @apiNote The arm will decrease in power as it approaches the target position.
     */
    public void retract() {
        goToPosition(0);
    }

    /** Sets the multiplier for the maximum speed the arm can travel at.
     * @apiNote This only affects setPower()! This will not affect the automatic movement of the arm.
     * @param speed The new speed multiplier. By default this is 0.75
     */
    public void setMaxSpeed(double speed) {
        totalSpeed = speed;
    }

    /** Goes to the designated junction when that button is pressed.
     * @apiNote The arm will decrease in power as it approaches the target position.
     * @param low Low junction button
     * @param middle Middle junction button
     * @param high High junction button
     */
    public void goToJunctionsOnPress(@NonNull Boolean low, @NonNull Boolean middle, @NonNull Boolean high) {
        if (low) {
            goToPosition(1400);
        } else if (middle) {
            goToPosition(2260);
        } else if (high) {
            goToPosition(3000);
        }
    }

    /** Goes to the position and hovers there.
     * @apiNote The arm will decrease in power as it approaches the target position.
     * @param ticks Encoder ticks
     */
    public void goToPosition(int ticks) {
        if(arm.getCurrentPosition() >= ticks - 25 && ticks > 25 && arm.getCurrentPosition() <= ticks + 25) {
            /*

                -------------- ticks + 25

                ARM ~= ticks (At target position)

                -------------- ticks - 25



                Ticks is greater than 25 */
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            arm.setPower(hoverSpeed);
        } else if (ticks > 25){
            /*  ARM (Above target position)

                --------------
                ticks
                --------------

                ARM (Below target position)

                Ticks is greater than 25 */
            arm.setTargetPosition(ticks);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setPower(Math.min(maxAutoSpeed, (3600 - arm.getCurrentPosition()) * 0.01));
            /* Sets the power to be whichever is smaller:
            A.) A base power of the maximum allowed speed (maxAutoSpeed)
            B.) The deviation from its target position multiplied by 0.01
             */
        } else if (ticks == 0) {
            /*  ARM (Above target position)

                -------------- ticks + 6
                ticks

                Ticks is 0 */
            arm.setTargetPosition(ticks);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            arm.setPower(Math.min(maxAutoSpeed, (arm.getCurrentPosition()) * 0.005));
            /* Sets the power to be whichever is smaller:
            A.) A base power of the maximum allowed speed (maxAutoSpeed)
            B.) The deviation from its target position multiplied by 0.005
             */

            if(arm.getCurrentPosition() <= 6 && touchSensorPressed()) {
                // This means the arm is fully retracted. This is the perfect opportunity to recalibrate the encoders
                arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                arm.setPower(0);
            }
        }
    }

    /** Resets the encoder value for the arm. */
    public void resetEncoder() {
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /** Returns the current position of the arm
     * @return Returns as an int in ticks.
     */
    public int getCurrentPosition() {
        return arm.getCurrentPosition();
    }

    /** Returns if the touch sensor is pressed or not.
     * @return Returns as a Boolean. True if pressed. False if not.
     */
    public Boolean touchSensorPressed() {
        return touchSensor.isPressed();
    }

    public void telemetryOutput() {
        telemetry.addData("Power", arm);
        telemetry.addData("Touch Sensor", touchSensorPressed());
        telemetry.addData("Mode", arm.getMode().toString());
        telemetry.addData("Ticks", arm.getCurrentPosition());
    }
}