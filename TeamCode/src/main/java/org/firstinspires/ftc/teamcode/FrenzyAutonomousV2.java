package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaFieldNavigation;
import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaFieldNavigationWebcam;
import org.firstinspires.ftc.teamcode.FrenzyDriveTrain;
import org.firstinspires.ftc.teamcode.EasyOpenCV;
import org.firstinspires.ftc.teamcode.Vuforia_Identifier;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;



@Autonomous(name = "FrenzyAuto CURRENT", group = "Linear Opmode")
public class FrenzyAutonomousV2 extends LinearOpMode//extends EasyOpenCV
{

    private static final double DEFAULT_POWER = 0.4;
    protected FrenzyDriveTrain driveTrain;
    protected Vuforia_Identifier vuforiaDetect;
    OpenCvInternalCamera phoneCam;
    private static final double CAMERA_X_POSITION = 195;
    private static final double CAMERA_Y_POSITION = 50;

    private static final double CORRECTION_FACTOR = 0.5;

    SkystoneDeterminationPipeline pipeline = null;

    ConceptVuforiaFieldNavigationWebcam pictureDetect;
    public ColorSensor propellorColor;

    static final int SLEEP_TIME = 10;

    static final int shootFirstRotation = 160;
    static final int shootSecondRotation = 55;
    static final int shootThirdRotation = 60;

    protected void createPipeline(double xPosition, double yPosition){
        pipeline = new SkystoneDeterminationPipeline(xPosition, yPosition);
    }

    protected void initOpMode(double xPosition, double yPosition){

        FrenzyDriveTrain driveTrain;
        FrenzyDriveTrain.DirectionEnum direction = FrenzyDriveTrain.DirectionEnum.SOUTH;
        this.createPipeline(xPosition, yPosition);
        this.driveTrain = new FrenzyDriveTrain(this.hardwareMap, this.telemetry, direction);
        this.driveTrain.initialize(hardwareMap, telemetry);
        this.vuforiaDetect = new Vuforia_Identifier();
        this.vuforiaDetect.initVuforia();
        this.pictureDetect = new ConceptVuforiaFieldNavigationWebcam();
        this.propellorColor = hardwareMap.get(ColorSensor.class, "propellor_color");

        telemetry.setAutoClear(false);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
//        pipeline = new SkystoneDeterminationPipeline();
        phoneCam.setPipeline(pipeline);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                phoneCam.startStreaming(320,240, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }
        });
    }

    protected int getCorrectedDistance(int originalDistance){
        return (int) (originalDistance * CORRECTION_FACTOR);
    }


    protected SkystoneDeterminationPipeline.RingPosition getRingPosition(){
        SkystoneDeterminationPipeline.RingPosition ringPosition = null;

        sleep(20);

        if (pipeline.position == SkystoneDeterminationPipeline.RingPosition.FOUR) {
            ringPosition = SkystoneDeterminationPipeline.RingPosition.FOUR;
            telemetry.addData("4 Rings Detected!", ringPosition);
            telemetry.update();
        }

        if (pipeline.position == SkystoneDeterminationPipeline.RingPosition.ONE) {
            ringPosition = SkystoneDeterminationPipeline.RingPosition.ONE;
            telemetry.addData("1 Ring Detected!", ringPosition);
            telemetry.update();
        }

        if (pipeline.position == SkystoneDeterminationPipeline.RingPosition.NONE) {
            ringPosition = SkystoneDeterminationPipeline.RingPosition.NONE;
            telemetry.addData("0 Rings Detected!", ringPosition);
            telemetry.update();
        }
        return ringPosition;
    }

    @Override
    public void runOpMode()  {

        initOpMode(CAMERA_X_POSITION, CAMERA_Y_POSITION);

        waitForStart();

        runAutonomous();

        while(opModeIsActive()){
            sleep(100);
        }
    }

    protected void runAutonomous(){
        if (opModeIsActive())
        {

            SkystoneDeterminationPipeline.RingPosition ringPosition = getRingPosition();
            //ALL  AUTONOMOUS MOVE CODE

        }


    }

    protected void dropWobble(SkystoneDeterminationPipeline.RingPosition ringPosition, int wobbleNum){
        switch (ringPosition){
            case NONE:
                sleep(SLEEP_TIME);
                if(wobbleNum != 1){
                }
                else{
                }
                break;
            case ONE:
                if (wobbleNum != 1) {
                }
                else{
                }

                break;
            case FOUR:
                if (wobbleNum != 1){
                    return;
                }

                break;
            default:
                telemetry.addData(String.valueOf(ringPosition), "NO Rings Detected");

        }
        telemetry.update();
        sleep(100);
    }

    protected void getBackToWhiteLine(SkystoneDeterminationPipeline.RingPosition ringPosition){
        switch (ringPosition){
            case NONE:
                break;
            case ONE:
                break;
            case FOUR:
                break;
            default:
                telemetry.addData(String.valueOf(ringPosition), "NO Rings Detected");

        }
    }

    protected void dropSecondWobble(SkystoneDeterminationPipeline.RingPosition ringPosition){
//        this.autoOmni.move(2500, getDefaultPower());
        this.autoOmni.initDriveMotors(hardwareMap, telemetry);
        this.lineDetect(true, 0.08);
//        dropWobble(ringPosition,);

//        switch (ringPosition){
//            case NONE:
//                this.autoOmni.towerOpen();
//                break;
//            case ONE:
//                this.autoOmni.rotate(1000, this.getDefaultPower());
//                this.autoOmni.towerOpen();
//                break;
//            case FOUR:
//                this.autoOmni.move(2250, this.getDefaultPower());
//                sleep(SLEEP_TIME);
//                this.autoOmni.crab(-475, this.getDefaultPower());
//                this.autoOmni.towerOpen();
//                sleep(200);
//                this.autoOmni.crab(1550, this.getDefaultPower());
//                sleep(SLEEP_TIME);
//                this.autoOmni.move(-2200, 0.6);
//                break;
//        }
    }

    protected void getSecondWobble(SkystoneDeterminationPipeline.RingPosition ringPosition){
        this.autoOmni.move(getCorrectedDistance(-2500), getDefaultPower());
        this.autoOmni.move(getCorrectedDistance(-150), 0.4);

//        this.autoOmni.rotate(75, 0.4);

        this.autoOmni.initDriveMotors(hardwareMap, telemetry);
        crabToBlueBack(false);

//        this.autoOmni.crab(getCorrectedDistance(100), getDefaultPower());
        this.autoOmni.move(getCorrectedDistance(300), getDefaultPower());
        this.autoOmni.rotate(getCorrectedDistance(150), getDefaultPower());
        sleep(SLEEP_TIME);
        this.autoOmni.towerClose();
        sleep(500);
//        this.autoOmni.autoTowerHand(500);
        this.wobbleDropThread(autoOmni, 1000, false);
        sleep(500);
//        this.autoOmni.rotate(getCorrectedDistance(-150), getDefaultPower());
        if(ringPosition == SkystoneDeterminationPipeline.RingPosition.FOUR){
            this.autoOmni.move(getCorrectedDistance(2500), getDefaultPower());
            this.autoOmni.towerOpen();
            return;
        }


        if (ringPosition == SkystoneDeterminationPipeline.RingPosition.NONE) {
            this.autoOmni.move(getCorrectedDistance(2500), getDefaultPower());
//            this.autoOmni.initDriveMotors(hardwareMap, telemetry);
//            this.lineDetect(true, 0.3);
            this.autoOmni.move(150, getDefaultPower());
        }
        else{
            this.autoOmni.move(getCorrectedDistance(2300), getDefaultPower());
//            this.autoOmni.initDriveMotors(hardwareMap, telemetry);
//            this.lineDetect(true, 0.08);
        }

    }

    protected void crabToBlueFakeRight(){
        crabToBlueFakeRight(true);
    }

    protected void crabToBlueFakeRight(boolean left){
        double power = left ? -0.2: 0.2;
        while (opModeIsActive()) {
            if (this.autoOmni.colorLeft.blue() < 350) {
                this.autoOmni.crabPower(power);
            } else {
                this.autoOmni.stopNow();
                break;
            }
            sleep(5);
        }
    }

    protected double getDefaultPower(){
        return DEFAULT_POWER;

    }

    protected void leaveBase(SkystoneDeterminationPipeline.RingPosition ringPosition){
        this.autoOmni.move(getCorrectedDistance(100), 0.3);
        sleep(SLEEP_TIME);
        //Crab to avoid rings
        this.autoOmni.crab(getCorrectedDistance(-450), this.getDefaultPower());
        sleep(SLEEP_TIME);
    }

    protected void getCloseToLine(){
        this.autoOmni.move(getCorrectedDistance(2700), this.getDefaultPower());
        sleep(SLEEP_TIME);
    }

    protected void crabToShootPos(){
        this.autoOmni.crab(getCorrectedDistance(650), this.getDefaultPower());
        this.autoOmni.stopNow();
    }

    protected void launch(){
        this.autoOmni.launch();
    }

    protected void moveToLine(SkystoneDeterminationPipeline.RingPosition ringPosition){
        //Leave Base
        leaveBase(ringPosition);
        //Move close enough to the white line
        getCloseToLine();
        //Crab to line for shooting rings
        crabToShootPos();
    }

    protected void crabToBlue(){
        crabToBlue(true);
    }

    protected void crabToBlue(boolean right){
        double power = right ? 0.1:-0.1;
        while (opModeIsActive()) {
            if (this.autoOmni.colorRight.blue() < 180) {
                this.autoOmni.crabPower(power);
            } else {
                this.autoOmni.stopNow();
                break;
            }
            sleep(5);
        }
    }

    protected void crabToBlueBack(boolean right){
        double power = right ? 0.1:-0.1;
        while (opModeIsActive()) {
            if (this.autoOmni.colorBack.blue() < 400) {
                this.autoOmni.crabPower(power);
            } else {
                this.autoOmni.stopNow();
                break;
            }
            sleep(5);
        }
    }

//    private boolean isLeftWhite(){
//        telemetry.addData("Left Red", this.autoOmni.colorLeft.red());
//        telemetry.addData("Left Green", this.autoOmni.colorLeft.green());
//        telemetry.addData("Left Blue", this.autoOmni.colorLeft.blue());
//        return this.autoOmni.colorLeft.red() > 400 && this.autoOmni.colorLeft.green() > 400
//                && this.autoOmni.colorLeft.blue() > 400;
//    }

    private boolean isLeftWhite(){
        telemetry.addData("Left Red", this.autoOmni.colorLeft.red());
        telemetry.addData("Left Green", this.autoOmni.colorLeft.green());
        telemetry.addData("Left Blue", this.autoOmni.colorLeft.blue());
        return this.autoOmni.colorLeft.red() > 200 && this.autoOmni.colorLeft.green() > 200
                && this.autoOmni.colorLeft.blue() > 200;
    }

    private boolean isRightWhite(){
        telemetry.addData("Right Red", this.autoOmni.colorRight.red());
        telemetry.addData("Right Green", this.autoOmni.colorRight.green());
        telemetry.addData("Right Blue", this.autoOmni.colorRight.blue());
        return this.autoOmni.colorRight.red() > 200 && this.autoOmni.colorRight.green() > 200
                && this.autoOmni.colorRight.blue() > 200;
    }

//    public void lineDetect(boolean forward) {
//        boolean leftDone = false;
//        boolean rightDone = false;
//        double power = forward ? 0.1: -0.1;
//        this.autoOmni.movePower(power);
//        while(!(leftDone && rightDone) && opModeIsActive()){
//            if(this.isLeftWhite() && !leftDone ){
////                this.autoOmni.rightCorrect(power);
//                this.autoOmni.leftStop();
//                leftDone = true;
//            }
//            if (this.isRightWhite() && !rightDone)
//            {
////                this.autoOmni.leftCorrect(power);
//                this.autoOmni.rightStop();
//                rightDone = true;
//            }
//            sleep(5);
//        }
//        this.autoOmni.stopNow();
//    }


    public void lineDetect(boolean forward, double powerIn) {
        this.telemetry.setAutoClear(true);
        boolean leftDone = false;
        boolean rightDone = false;
        double power = forward ? powerIn: -powerIn;
        this.autoOmni.movePower(power);
        while(opModeIsActive()){
            if (this.isRightWhite()) {
                this.autoOmni.leftCorrect(power);
//                this.autoOmni.rightStop();
                rightDone = true;
            }else{
                rightDone = false;
            }

            if(this.isLeftWhite()){
                this.autoOmni.rightCorrect(power);
//                this.autoOmni.leftStop();
                leftDone = true;
            }else{
                leftDone =false;
            }
            telemetry.update();
            if(leftDone && rightDone){
                break;
            }
            sleep(5);
        }
        this.autoOmni.stopNow();
    }




    public static class SkystoneDeterminationPipeline extends OpenCvPipeline
    {
        /*
         * An enum to define the skystone position
         */
        public enum RingPosition
        {
            FOUR,
            ONE,
            NONE
        }

        /*
         * Some color constants
         */
        static final Scalar BLUE = new Scalar(0, 0, 255);
        static final Scalar GREEN = new Scalar(0, 255, 0);

        /*
         * The core values which define the location and size of the sample regions
         */
//        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(195,50);
        private Point REGION1_TOPLEFT_ANCHOR_POINT = null;

        //WE HAVE DOUBLED THIS FOR TESTING, THE ORIGINAL VALUES ARE 35 AND 25!!!!!!!!! -Shawn
        static final int REGION_WIDTH = 80;
        static final int REGION_HEIGHT = 70;

        final int FOUR_RING_THRESHOLD = 140;
        final int ZERO_RING_THRESHOLD = 128;

//        Point region1_pointA = new Point(
//                REGION1_TOPLEFT_ANCHOR_POINT.x,
//                REGION1_TOPLEFT_ANCHOR_POINT.y);
//        Point region1_pointB = new Point(
//                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
//                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        Point region1_pointA = null;
        Point region1_pointB = null;

        /*
         * Working variables
         */
        Mat region1_Cb;
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();

        int avg1;

        // Volatile since accessed by OpMode thread w/o synchronization
        public volatile RingPosition position = RingPosition.NONE;
        public SkystoneDeterminationPipeline(double xPosition, double yPosition){
            super();
            this.REGION1_TOPLEFT_ANCHOR_POINT = new Point(xPosition, yPosition);
            this.region1_pointA = new Point(
                    REGION1_TOPLEFT_ANCHOR_POINT.x,
                    REGION1_TOPLEFT_ANCHOR_POINT.y);
            this.region1_pointB = new Point(
                    REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                    REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
        }
        /*
         * This function takes the RGB frame, converts to YCrCb,
         * and extracts the Cb channel to the 'Cb' variable
         */
        void inputToCb(Mat input)
        {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }

        @Override
        public void init(Mat firstFrame)
        {
            inputToCb(firstFrame);

            region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
        }

        @Override
        public Mat processFrame(Mat input)
        {
            inputToCb(input);

            avg1 = (int) Core.mean(region1_Cb).val[0];

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            position = RingPosition.NONE; // Record our analysis
            if(avg1 > FOUR_RING_THRESHOLD){
                position = RingPosition.FOUR;
            }else if (avg1 < ZERO_RING_THRESHOLD){
                position = RingPosition.NONE;
            }else{
                position = RingPosition.ONE;
            }

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill

            return input;
        }

        public int getAnalysis()
        {
            return avg1;
        }
    }

    protected void wobbleDropThread(final FrenzyDriveTrain driveTrain, final long time, final boolean up) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                telemetry.setAutoClear(false);
                while (opModeIsActive()) {

                    break;
                }
            }
        });
        t1.start();
    }

}