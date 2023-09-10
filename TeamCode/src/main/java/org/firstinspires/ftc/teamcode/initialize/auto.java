package org.firstinspires.ftc.teamcode.initialize;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.action.mecanumDrive;
import org.firstinspires.ftc.teamcode.other.customSensors.imageDetection;

import java.util.Objects;

@Autonomous(name = "Auto", group = "")
public class auto extends OpMode {
    // CONSTRUCT
    public ElapsedTime autoRuntime = new ElapsedTime(); // How long the autonomous has run for
    ElapsedTime actionRuntime = new ElapsedTime(); // How long the current action has run for
    org.firstinspires.ftc.teamcode.action.mecanumDrive mecanumDrive = new mecanumDrive();
    org.firstinspires.ftc.teamcode.other.customSensors.imageDetection imageDetection = new imageDetection();
    // DECLARE NULL
    int tempDuck; // Stores the name of any newly found image. This will be null when no NEW image is found
    // DECLARE CUSTOM
    int robotAction = 0; // Keeps track of which action the bot is currently doing
    double length = 1.0; // The time the action runs for
    double[] distance = {-1, -1, -1, -1};
    int duck = 0; // Stores the name of the found image that has the highest confidence. This is the same as "tempDuck" but is never null
    public String teamColor = "Red"; // Which alliance we are currently on
    Boolean tFInitHasRun = false; // Has the TensorFlow initialise method run already?
    Boolean teamSelected = false; // Has the primary driver selected an alliance?
    Boolean startLeftSide = true;
    Boolean failedColor = false;
    private static double timeLimit = 0.00;

    // METHODS
    /** Initializes the autonomous. */
    public void init(){
        mecanumDrive.init(this);
        imageDetection.init(this);
        mecanumDrive.setMaxSpeed(1.00); // Sets the maximum speed of the wheels. Slow-mode is enabled automatically so this is really 0.5
        mecanumDrive.runWithoutEncoder();
    }

    /** Loops until the start button is pressed. */
    public void init_loop(){
        tempDuck = imageDetection.imageReturn(); // The variable "tempDuck" contains the latest detected image name (if any)
        if(tempDuck != 0){ // If "tempDuck" has a detected image name, it is more recent than what is currently in "duck"
            duck = tempDuck; // Since we know the image name in "duck" is outdated, we update it to whatever image name is in "tempDuck"
        }

        // TELEMETRY
        telemetry.addData("Image", duck); // Displays the name of the latest image to the phone screen
        if(duck == 0) { // If no image has been found, display "Loading..." on the phone screen
            telemetry.addData("Status", "Loading...");
        } else { // If an image has been found, tell the drivers that autonomous can now be run
            telemetry.addData("Status", "Ready! You can now run Autonomous.");
        }
        imageDetection.imageReturnTel(); // Outputs data about all detected images to the phone screen
    }

    /** Runs one time when the autonomous starts. */
    public void start() {
        autoRuntime.reset(); // Resets both timers
        actionRuntime.reset();
    }

    /** Loops until the stop button is pressed. */
    @Override
    public void loop() {
        // TELEMETRY
        telemetry.addData("Time Elapsed For Autonomous", autoRuntime.seconds()); // Time since the autonomous has started
        telemetry.addData("Time Elapsed For Action", actionRuntime.time()); // Displays the current time since the action has started
        telemetry.addData("Robot Action", robotAction); // Displays the current action the robot is on

        if(duck <= 1) {
            if(robotAction == 1) {
                mecanumDrive(1.0, 1, 0, 0);
            } else if (robotAction == 2) {
                mecanumDrive(0.5, 0, 1, 0);
            }
        }

        if (duck == 2) {
            if(robotAction == 1) {
                mecanumDrive(1.5, 1, 0, 0);
            }
        }

        if(duck == 3) {
            if(robotAction == 1) {
                mecanumDrive(1.0, 1, 0, 0);
            } else if (robotAction == 2) {
                mecanumDrive(0.5, 0, -1, 0);
            }
        }
    }

    public void mecanumDrive(double secondsToRunFor, double yAxisPower, double xAxisPower, double rotationInPower) {
        yAxisPower = yAxisPower * -1;
        mecanumDrive.setPower(xAxisPower, yAxisPower, rotationInPower);
        if(actionRuntime.time() >= secondsToRunFor){ // If this action runs longer than it should...
            robotAction++; // Go to the next action
            actionRuntime.reset(); // Reset the timer
            mecanumDrive.setPower(0, 0, 0);
        }
    }

    private void waitThenGoToNextAction(double secondsToWait) {
        if(actionRuntime.time() >= secondsToWait){ // If this action runs longer than it should...
            robotAction++; // Go to the next action
            actionRuntime.reset(); // Reset the timer
        }
    }

    /** Stops the robot. */
    @Override
    public void stop() {
        imageDetection.stop();
        super.stop(); // Stops the OpMode
    }
}
