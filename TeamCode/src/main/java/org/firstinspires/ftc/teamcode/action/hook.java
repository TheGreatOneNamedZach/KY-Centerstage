package org.firstinspires.ftc.teamcode.action;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class hook {
    // CONSTRUCT
    // DECLARE NULL
    DcMotor hook;
    Telemetry telemetry;
    // DECLARE CUSTOM
    private static double totalSpeed = 1.00; // Speed multiplier for the slide
    private static final double maxAutoSpeed = 0.6; // Maximum speed the linear slide can operate at AUTOMATICALLY. "Auto" does not stand for "autonomous"
    private static final double hoverSpeed = 0.01; // Speed at which the slide can hover

    // METHODS
    /** Initializes the hook.
     * @param opMode If you are constructing from an Auto or TeleOp, type in "this" without the quotation marks.
     */
    public void init(@NonNull OpMode opMode) {
        HardwareMap hardwareMap = opMode.hardwareMap;
        telemetry = opMode.telemetry;
        hook = hardwareMap.get(DcMotor.class, "hook");
        hook.setDirection(DcMotorSimple.Direction.REVERSE);
        hook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /** Moves the hook. This is best used with a joystick.
     * @apiNote Remember if you are using the "y" of a joystick reverse it!
     * @param power power to move the hook at. This is multiplied by the max speed.
     */
    public void setPower(double power) {
        double hookPower = power * totalSpeed;
            if (hookPower == 0) { // If the hook is not moving AND the limit switch is not pressed...
                // This means the hook needs to hover at its position
                hook.setPower(hoverSpeed);
            } else { // The hook must be moving
                hook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hook.setPower(hookPower);
            }

    }

    /** Fully retracts the hook
     * @apiNote The hook will decrease in power as it approaches the target position.
     */
    public void retract() {
        goToPosition(0);
    }

    /** Sets the multiplier for the maximum speed the hook can travel at.
     * @apiNote This only affects setPower()! This will not affect the automatic movement of the hook.
     * @param speed The new speed multiplier. By default this is 0.75
     */
    public void setMaxSpeed(double speed) {
        totalSpeed = speed;
    }

    /** Goes to the position and hovers there.
     * @apiNote The hook will decrease in power as it approaches the target position.
     * @param ticks Encoder ticks
     */
    public void goToPosition(int ticks) {
        if(hook.getCurrentPosition() >= ticks - 25 && ticks > 25 && hook.getCurrentPosition() <= ticks + 25) {
            /*

                -------------- ticks + 25

                hook ~= ticks (At target position)

                -------------- ticks - 25



                Ticks is greater than 25 */
            hook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            hook.setPower(hoverSpeed);
        } else if (ticks > 25){
            /*  hook (Above target position)

                --------------
                ticks
                --------------

                hook (Below target position)

                Ticks is greater than 25 */
            hook.setTargetPosition(ticks);
            hook.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hook.setPower(Math.min(maxAutoSpeed, (3600 - hook.getCurrentPosition()) * 0.01));
            /* Sets the power to be whichever is smaller:
            A.) A base power of the maximum allowed speed (maxAutoSpeed)
            B.) The deviation from its target position multiplied by 0.01
             */
        } else if (ticks == 0) {
            /*  hook (Above target position)

                -------------- ticks + 6
                ticks

                Ticks is 0 */
            hook.setTargetPosition(ticks);
            hook.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hook.setPower(Math.min(maxAutoSpeed, (hook.getCurrentPosition()) * 0.005));
            /* Sets the power to be whichever is smaller:
            A.) A base power of the maximum allowed speed (maxAutoSpeed)
            B.) The deviation from its target position multiplied by 0.005
             */
        }
    }

    /** Resets the encoder value for the hook. */
    public void resetEncoder() {
        hook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hook.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /** Returns the current position of the hook
     * @return Returns as an int in ticks.
     */
    public int getCurrentPosition() {
        return hook.getCurrentPosition();
    }

    public void telemetryOutput() {
        telemetry.addData("Power", hook);
        telemetry.addData("Mode", hook.getMode().toString());
        telemetry.addData("Ticks", hook.getCurrentPosition());
    }
}
