package org.firstinspires.ftc.teamcode.other.customSensors;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.*;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

/** This is an interface for this year's image detector. */
public class imageDetection {
    // CONSTRUCT

    // DECLARE NULL
    HardwareMap hardwareMap;
    public TfodProcessor tfod;
    public AprilTagProcessor aprilTag;
    public VisionPortal visionPortal;
    private int label;
    Telemetry telemetry;
    // DECLARE CUSTOM
    private static float confidence = -1;

    // METHODS
    /** Initializes the image detector.
     * @param opMode If you are constructing from an Auto or TeleOp, type in "this" without the quotation marks.
     */
    public void init(@NonNull OpMode opMode){
        hardwareMap = opMode.hardwareMap;
        telemetry = opMode.telemetry;

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder()
                //.setModelAssetName(DEFAULT_TFOD_MODEL_ASSET)
                //.setModelLabels(LABELS)
                .setIsModelTensorFlow2(true)
                //.setIsModelQuantized(true)
                .setModelInputSize(300)
                .setModelAspectRatio(16.0 / 9.0)

                .build();

        aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                //.setDrawCubeProjection(false)
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)

                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));


        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableCameraMonitoring(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        tfod.setMinResultConfidence(0.50f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);
    }

    /** Returns the label of a new image, if any. */
    public int imageReturn() {
        if (tfod != null) {
            // getFreshRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getFreshRecognitions();
            if (updatedRecognitions != null) {
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getConfidence() > confidence && Math.abs(recognition.getRight() - recognition.getLeft()) <= 175) {
                        confidence = recognition.getConfidence();
                        float yPixel = ((recognition.getWidth()/2) + recognition.getLeft());
                        if (yPixel <= (recognition.getWidth()/3)) {
                            label = 1;
                        } else if (yPixel <= (recognition.getWidth()/1.5)) {
                            label = 2;
                        } else {
                            label = 3;
                        }
                    }
                }
                confidence = -1;
                return 0;
            }
        }
        confidence = -1;
        return 0;
    }

    public void imageReturnTel() {
        List<Recognition> recognitionsList = tfod.getRecognitions(); // Creates a list with every image detected
        if (recognitionsList != null) { // This will run if at least one image has been detected
            telemetry.addData("Number Of Images Detected", recognitionsList.size());

            for (Recognition recognition : recognitionsList) {
                // This "for" statement will run for every image detected
                double col = (recognition.getLeft() + recognition.getRight()) / 2; // Finds the "X" position of the image on the webcam stream
                double row = (recognition.getTop() + recognition.getBottom()) / 2; // Finds the "Y" position of the image on the webcam stream
                double width = Math.abs(recognition.getRight() - recognition.getLeft()); // Finds the width of the image on the webcam stream
                double height = Math.abs(recognition.getTop() - recognition.getBottom()); // Finds the heights of the image on the webcam stream

                // Adds a blank line between every image detected
                telemetry.addData("", " ");
                // Displays to the phone screen the name of the image and how confident that it is correct
                telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                telemetry.addData("Pos", (recognition.getWidth()/2) + recognition.getLeft());
                // Displays to the phone screen the X and Y coordinates of the image. The coordinates are the image's position on the webcam stream
                telemetry.addData("- Position (Row/Col)", "%.0f / %.0f", row, col);
                // Displays to the phone screen the size of the image. The size of the image output is the size on the webcam stream
                telemetry.addData("- Size (Width/Height)", "%.0f / %.0f", width, height);
            }
        }

    }

    public AprilTagDetection aprilTagSearch(int id) {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                if (id == detection.metadata.id) {
                    return detection;
                }
                /*
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                 */
            }
        }   // end for() loop

        // Add "key" information to telemetry
        /*
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");
         */
        return null;
    }

    public void stop() {
        tfod.shutdown();
        visionPortal.close();
    }
}