/*
Copyright 2025 FIRST Tech Challenge Team 27468

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains a minimal example of a Linear "OpMode". An OpMode is a 'program' that runs
 * in either the autonomous or the TeleOp period of an FTC match. The names of OpModes appear on
 * the menu of the FTC Driver Station. When an selection is made from the menu, the corresponding
 * OpMode class is instantiated on the Robot Controller and executed.
 *
 * Remove the @Disabled annotation on the next line or two (if present) to add this OpMode to the
 * Driver Station OpMode list, or add a @Disabled annotation to prevent this OpMode from being
 * added to the Driver Station.
 */
@Autonomous(name = "threepointerRED", group = "Match")
public class threepointerRED extends LinearOpMode {

     private DcMotorEx frontLeft = null;
    private DcMotorEx frontRight = null;
    private DcMotorEx backLeft = null;
    private DcMotorEx backRight = null;

    // Mechanism Motors
    private DcMotorEx intake = null;
    private DcMotorEx shooterL= null; // Using DcMotorEx to get velocity
    private DcMotorEx shooterR = null;
    //private DcMotorEx motorLift = null;
    private DcMotorEx transfer= null;
    private Servo blocker = null;
    private Servo targetFlag = null;
    private IMU imu;
    private ElapsedTime runtime = new ElapsedTime();


    private com.qualcomm.hardware.limelightvision.Limelight3A limelight;
    // Servos
    //private Servo servoHold = null;
  
    final double UNBLOCK = 0.4;
    final double BLOCK = 0.15; 
 
    final double TARGETON = 0.3;
    final double TARGETOFF = 0.5;
    final double TARGETFOUND = 0;
    static final double COUNTS_PER_MOTOR_REV = 28; // Swyft 6000 RPM motors
    static final double DRIVE_GEAR_REDUCTION = 1.0;
    static final double WHEEL_DIAMETER_INCHES = 3.75;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                          (WHEEL_DIAMETER_INCHES * Math.PI);
    static final double STRAFE_CORRECTION_MULTIPLIER = 1.1; // Adjust this value to fix imperfect strafing

    // PID constants for heading correction (tune as needed)
    static final double P_STRAFE_GAIN = 0.03;

 
   // static final double CLIMB_POWER = 1.0;

    @Override
    public void runOpMode() {
        ini();
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        if(opModeIsActive()){
            /*
              Motor Sleep required 1000 steps, power 1.0 = 1000;
              Motor Sleep required 1000 steps, power 0.7 = 1550;
              Motor Sleep required 1000 steps, power 0.5 = 1850;
              Motor Sleep required 1000 steps, power 0.1 = 4500;
            */
        
            //new Auton code Starts here
           
            //Shoot 1,2,3 Balls
            
            
            drivetoPosition(200, 0.9, 200);
            turnRighttoPosition (190,0.5,400);
            sleep(100);
            transfer.setPower(0.9);
            intake.setPower(0.9);
            autoAlign(2.0,0.2,20000);
            shootNew(1780,  2000, 0.5);
            transfer.setPower(0);
            intake.setPower(0);
            
            
            //Shoot next 4,5,6 Ball
            drivetoPosition(800, 0.9, 950);
            sleep(100);
            turnLefttoPosition (850,0.5,2000);
            
            transfer.setPower(0.9);
            intake.setPower(0.9);
            drivetoPosition(-1100, 0.9, 1250);
            transfer.setPower(0.5);
            intake.setPower(0.5);
            
            drivetoPosition(1100, 0.9, 1350);
            turnRighttoPosition (840,0.5,2000);
            drivetoPosition(-550, 0.9, 900);
            transfer.setPower(0.9);
            intake.setPower(0.9);
            autoAlign(2.0,0.2,20000);
            shootNew(1780,  2000, 0.5);
            transfer.setPower(0);
            intake.setPower(0);
            
            
        
    
            //Shoot next 7,8,9 Ball
            drivetoPosition(400, 0.9, 500);
            turnLefttoPosition (180,0.5,500);
            strafRighttoPosition(1800, 0.8 ,2400);         
            transfer.setPower(0.9);
            intake.setPower(0.9);
            drivetoPosition(-200, 0.9, 300);
            drivetoPosition(-800, 0.3, 1800);
            drivetoPosition(200, 0.9, 100);
            transfer.setPower(0.5);
            intake.setPower(0.5);
            
            turnRighttoPosition (50,0.5,100);
            strafLefttoPosition(1850, 0.8 ,2500);         
            drivetoPosition(100, 0.9, 100);
            turnRighttoPosition (150,0.5,500);
            transfer.setPower(0.9);
            intake.setPower(0.9);
            autoAlign(2.0,0.2,20000);
            shootNew(1780,  2000, 0.5);
            transfer.setPower(0);
            intake.setPower(0);
            
            drivetoPosition(300, 0.9, 300);
            //arctTurnLefttoPosition(-1000,0.5,1.0, 1000);
            
            
            //arctTurnRighttoPosition(1200,0.9,0.1, 1000);
            
    
            
/*
            drivetoPosition(-1020, 0.9, 1020);
            sleep(100);
            autoAlign(1.5,0.2,20000);
            shootNew(1300,  2000, 0.3);
           
            
             // next 4,5,6
            
            transfer.setPower(0);
            intake.setPower(0);
            turnRighttoPosition (1000,0.7, 1650); 
            transfer.setPower(0.9);
            intake.setPower(0.9);
            drivetoPosition(-1020, 0.9, 1350);
            transfer.setPower(0.7);
            intake.setPower(0.7);
            arctTurnLefttoPosition(2250,0.9,0.8, 2600);
            sleep(100);
            transfer.setPower(0);
            intake.setPower(0);
            autoAlign(1.5,0.2,20000);
            sleep(100);
            shootNew(1310,  2000, 0.3);
           
            //next 7,8,9
           
            transfer.setPower(0.9);
            intake.setPower(0.9);
            arctTurnLefttoPosition(-3150,0.9,0.9, 3600);
            drivetoPosition(-100, 0.9, 100);
            sleep(100);
            transfer.setPower(0.7);
            intake.setPower(0.7);
            drivetoPosition(300, 0.9, 250);
            arctTurnLefttoPosition(3050,0.9,0.2, 3500);
            sleep(100);
            transfer.setPower(0);
            intake.setPower(0);
            autoAlign(1.5,0.2,20000);
            shootNew(1350,  2000, 0.3);
        
            //next 10,11,12
           
            transfer.setPower(0.9);
            intake.setPower(0.9);
            arctTurnLefttoPosition(-2500,0.8,1.0, 2500);
            sleep(100);
            transfer.setPower(0.7);
            intake.setPower(0.7);
            /*
            arctTurnLefttoPosition(3150,0.9,0.1, 3500);
            sleep(100);
            transfer.setPower(0);
            intake.setPower(0);
            autoAlign(2,0.2,10000);
            shootNew(1350,  2000, 0.3);
        */
           
            }
            
        // run until the end of the match (driver presses STOP)
    }
    

    void autoAlign(double errorThreshold, double turnSpeed, int maxLoop){
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
        
        frontLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        boolean targetAligned = false; 
        limelight.start();
        limelight.pipelineSwitch(0);
        targetFlag.setPosition(TARGETON);
        LLResult result = limelight.getLatestResult();
        double ta = 0, tx = 0, ty = 0 ;
        boolean hasTarget = result != null && result.isValid();
    
        if (hasTarget) {
            tx = result.getTx();
            targetFlag.setPosition(TARGETFOUND);
            while(Math.abs(tx) > errorThreshold){
                if (maxLoop < 1) {
                    break;
                }
                maxLoop = maxLoop - 1;
                telemetry.addData("Limelight sees Target?", hasTarget);
                telemetry.addData("Limelight Tx", tx); 
                telemetry.addData("maxloop", maxLoop);
                telemetry.update();     
                
                int dir = 1;
                if(tx < 0) dir = -1;
                frontLeft.setPower(turnSpeed*dir);
                backRight.setPower(-turnSpeed*dir);
                backLeft.setPower(turnSpeed*dir);
                frontRight.setPower(-turnSpeed*dir);
                result = limelight.getLatestResult();
                hasTarget = result != null && result.isValid();
                if (hasTarget) {
                    tx = result.getTx();
                }
            }
            if (Math.abs(tx) <= errorThreshold){
                targetAligned = true;
            }
            telemetry.addData("Limelight Aligned?", targetAligned);
            telemetry.update();     
        }
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
        limelight.stop();
        if (targetAligned = false) {
            targetFlag.setPosition(TARGETOFF);
            
        }
        resetWheels();
        
    }
    

     void shootNew(int targetV,  int unblockmilliseconds, double slowdownPower ){
        blocker.setPosition(BLOCK);
        transfer.setPower(0.9);
        intake.setPower(1);
        shooterL.setPower(1);
        shooterR.setPower(1);
        sleep(200);
        double velocityError = 20;
        double currentVelocity = shooterL.getVelocity();
        while (currentVelocity < (targetV + velocityError)) {
            shooterL.setPower(1);
            shooterR.setPower(1);
            currentVelocity = shooterL.getVelocity();
        }
        shooterL.setPower(slowdownPower);
        shooterR.setPower(slowdownPower);
        blocker.setPosition(UNBLOCK);
        ElapsedTime timer = new ElapsedTime();
        while(timer.milliseconds() <= unblockmilliseconds){
             blocker.setPosition(UNBLOCK);
             currentVelocity = shooterL.getVelocity();
            if( currentVelocity < targetV) {
                shooterL.setPower(1);
                shooterR.setPower(1);
            }
            else {
                shooterL.setPower(slowdownPower);
                shooterR.setPower(slowdownPower);
            }
            telemetry.addData("current velocity", currentVelocity);
            telemetry.update();     
        }
        blocker.setPosition(BLOCK);
        transfer.setPower(0);
        intake.setPower(0);
        shooterL.setPower(0);
        shooterR.setPower(0);
        targetFlag.setPosition(TARGETOFF);
         
    }
    
 
    public void resetWheels () {
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
      
        
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); 
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); 
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); 
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); 
        
        
        frontLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
 
    } 
    
    public void intaketoPosition(int targetPosition, double apower, int sleepms) {
        
        transfer.setPower(0.7);
        intake.setPower(0.7);
        drivetoPosition( targetPosition,apower,sleepms) ;
        transfer.setPower(0);
        intake.setPower(0);
       
        
    }
    
    
    public void drivetoPosition(int targetPosition,double apower, int sleepms) {
        double afrpower = apower;
        double aflpower = apower;
        double abrpower = apower;
        double ablpower = apower;
       
        
        int frtargetPosition = targetPosition;
        int fltargetPosition = targetPosition;
        int bltargetPosition = targetPosition;
        int brtargetPosition = targetPosition;
        
        moveRobottoPosition( frtargetPosition,  fltargetPosition,brtargetPosition,bltargetPosition 
        , afrpower, aflpower, abrpower, ablpower,  sleepms) ;
      
    }

    public void moveRobottoPosition( int frtargetPosition,int fltargetPosition,int brtargetPosition ,int bltargetPosition,
    double afrpower,double aflpower,double abrpower,double ablpower ,
    int sleepms) {
        resetWheels ();
      
        frontRight.setTargetPosition(frtargetPosition);
        frontLeft.setTargetPosition(fltargetPosition);
        backRight.setTargetPosition(brtargetPosition);
        backLeft.setTargetPosition(bltargetPosition);
        
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION); 
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION); 
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION); 
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setPower(afrpower);
        frontLeft.setPower(aflpower);
        backLeft.setPower(ablpower);
        backRight.setPower(abrpower);
        sleep(sleepms);
        
    }
    

    public void arctTurnLefttoPosition(int targetPosition, double apower,double arclevel, int sleepms) {

        arclevel = Math.abs(arclevel);
        if  (arclevel > 1) {
            arclevel = 1;
        }
        apower = Math.abs(apower) ;
     
        double afrpower = apower;
        double abrpower = apower; 
        double aflpower = apower/2; 
        double ablpower = apower * (arclevel);
       
        int frtargetPosition = (int) (targetPosition * afrpower);
        int fltargetPosition = (int) (targetPosition * aflpower);
        int brtargetPosition = (int) (targetPosition * abrpower);
        int bltargetPosition = (int) (-targetPosition * ablpower);
        moveRobottoPosition( frtargetPosition,  fltargetPosition,brtargetPosition,bltargetPosition 
        , afrpower, aflpower, abrpower, ablpower,  sleepms) ;
     
    } 
    

    public void arctTurnRighttoPosition(int targetPosition, double apower,double arclevel, int sleepms) {

        arclevel = Math.abs(arclevel);
        if  (arclevel > 1) {
            arclevel = 1;
        }
        apower = Math.abs(apower) ;
     
        double afrpower = apower;
        double abrpower = apower * (arclevel); 
        double aflpower = apower ; 
        double ablpower = apower;
       
        int frtargetPosition = (int) (targetPosition * afrpower);
        int fltargetPosition = (int) (targetPosition * aflpower);
        int brtargetPosition = (int) (-targetPosition * abrpower);
        int bltargetPosition = (int) (targetPosition * ablpower);
        moveRobottoPosition( frtargetPosition,  fltargetPosition,brtargetPosition,bltargetPosition 
        , afrpower, aflpower, abrpower, ablpower,  sleepms) ;
     
    } 
    

    
    public void turnRighttoPosition(int targetPosition, double apower,int sleepms)
    {
     
        double afrpower = apower;
        double aflpower = apower;
        double abrpower = apower;
        double ablpower = apower;
        int frtargetPosition = -targetPosition;
        int fltargetPosition = targetPosition;
        int brtargetPosition = -targetPosition;
        int bltargetPosition = targetPosition;
        moveRobottoPosition( frtargetPosition,  fltargetPosition,brtargetPosition,bltargetPosition 
        , afrpower, aflpower, abrpower, ablpower,  sleepms) ;
     
    } 
    
    public void turnLefttoPosition(int targetPosition, double apower,int sleepms) {
        double afrpower = apower;
        double aflpower = apower;
        double abrpower = apower;
        double ablpower = apower;
        int frtargetPosition = targetPosition;
        int fltargetPosition = -targetPosition;
        int brtargetPosition = targetPosition;
        int bltargetPosition = -targetPosition;
        moveRobottoPosition( frtargetPosition,  fltargetPosition,brtargetPosition,bltargetPosition 
        , afrpower, aflpower, abrpower, ablpower,  sleepms) ;
     
    }
    public void strafRighttoPosition(int targetPosition,  double apower ,int sleepms) {
        double afrpower = apower;
        double aflpower = apower;
        double abrpower = apower;
        double ablpower = apower;
        int frtargetPosition = -targetPosition;
        int fltargetPosition = targetPosition;
        int brtargetPosition = targetPosition;
        int bltargetPosition = -targetPosition;
        moveRobottoPosition( frtargetPosition,  fltargetPosition,brtargetPosition,bltargetPosition 
        , afrpower, aflpower, abrpower, ablpower,  sleepms) ;
     
    
    }
    public void strafLefttoPosition(int targetPosition , double apower ,int sleepms) {
        double afrpower = apower;
        double aflpower = apower;
        double abrpower = apower;
        double ablpower = apower;
    
        
        
        int frtargetPosition = targetPosition;
        int fltargetPosition = -targetPosition;
        int brtargetPosition = -targetPosition;
        int bltargetPosition = targetPosition;
        moveRobottoPosition( frtargetPosition,  fltargetPosition,brtargetPosition,bltargetPosition 
        , afrpower, aflpower, abrpower, ablpower,  sleepms) ;
     
     }
    
  public double getAngle() {
    return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
  }
  
  public void turnUsingIMU (double targetAngle, double apower, int direction) {
    // Ensure that the OpMode is still active
    double lpower =apower;
    if (opModeIsActive()) {
  
        double currentAngle = getAngle();
        
        frontLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        
        if (targetAngle > 179)
        {
            targetAngle = 179 ;
        } 
        if (targetAngle < -179)
        {
            targetAngle = -179 ;
        }
         
        double absDiffAngle = Math.abs(currentAngle -  targetAngle);
        while ( absDiffAngle > 1)
        { 
            if (absDiffAngle   < 10){
                lpower = direction * 0.2;
                
            }
            else if (absDiffAngle  < 30){
                 lpower = direction * 0.25;
                
             }
             else if (absDiffAngle  < 60){
                 lpower = direction * 0.3;
                
             }
             else {
                 lpower = direction * apower ;
             }
            
            frontLeft.setPower(-lpower);
            frontRight.setPower(lpower);
            backLeft.setPower(-lpower);
            backRight.setPower(lpower);
            sleep(5);
            
            
            currentAngle = getAngle();
             
              // Display it for the driver.
            absDiffAngle = Math.abs(currentAngle -  targetAngle);
            telemetry.addData("ImudiffAngle",absDiffAngle);
            telemetry.addData("ImucurrentAngle",currentAngle);
            telemetry.addData("ImutargetAngle", targetAngle);
            telemetry.update();
             
        }
        // Display it for the driver.
        absDiffAngle = Math.abs(currentAngle -  targetAngle);
        telemetry.addData("ImudiffAngle",absDiffAngle);
        telemetry.addData("ImucurrentAngle",currentAngle);
        telemetry.addData("ImutargetAngle", targetAngle);
        telemetry.update();
             
        
      }
  
     
      // Stop all motion;
      frontLeft.setPower(0);
      backLeft.setPower(0);
      frontRight.setPower(0);
      backRight.setPower(0);
      resetWheels();
 
    }
    
    

    
    public void imuConfigReset()
    {
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                )
        );

        imu.initialize(parameters);
        imu.resetYaw();

    }
    
    
    public void ini(){
        frontLeft = hardwareMap.get(DcMotorEx.class, "motorLeftFront");
        frontRight = hardwareMap.get(DcMotorEx.class, "motorRightFront");
        backLeft   = hardwareMap.get(DcMotorEx.class, "motorLeftBack");
        backRight = hardwareMap.get(DcMotorEx.class, "motorRightBack");

        transfer   = hardwareMap.get(DcMotorEx.class, "Transfermotor");
        intake    = hardwareMap.get(DcMotorEx.class, "motorIntake");
        shooterL = hardwareMap.get(DcMotorEx.class, "motorTurretLeft"); // Mapped as DcMotorEx to read encoder
        shooterR   = hardwareMap.get(DcMotorEx.class, "motorTurretRight");
        //motorLift = hardwareMap.get(DcMotorEx.class, "motorLift");
        //servoHold = hardwareMap.get(Servo.class, "servoHold");

        // --- MOTOR DIRECTION ---
        frontLeft.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setDirection(DcMotorEx.Direction.FORWARD);
        frontRight.setDirection(DcMotorEx.Direction.REVERSE);
        backRight.setDirection(DcMotorEx.Direction.REVERSE);

        shooterR.setDirection(DcMotorEx.Direction.REVERSE);
        shooterL.setDirection(DcMotorEx.Direction.FORWARD);
        intake.setDirection(DcMotorEx.Direction.FORWARD);
        transfer.setDirection(DcMotorEx.Direction.FORWARD);
        //motorLift.setDirection(DcMotorEx.Direction.FORWARD);
        blocker = hardwareMap.get(Servo.class, "servoHold");
        targetFlag = hardwareMap.get(Servo.class, "servoTargetFlag");
        limelight = hardwareMap.get(com.qualcomm.hardware.limelightvision.Limelight3A.class, "limelight");
        //limelight.setPollRateHz(10);
       
        imu = hardwareMap.get(IMU.class, "imu");
        imuConfigReset();
       
       
        // --- MOTOR BEHAVIOR ---
        // Drivetrain and Climber set to BRAKE
        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        //motorLift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        // Intake and Shooters set to FLOAT (Coast)
        intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooterL.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        shooterR.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        transfer.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        // --- ENCODER SETUP FOR SHOOTER ---
        shooterL.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        shooterR.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        // Set other motors to run without encoders
        frontLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        intake.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        shooterR.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        shooterL.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        //motorLift.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        blocker.setDirection(Servo.Direction.FORWARD);
        blocker.setPosition(BLOCK);
        targetFlag.setDirection(Servo.Direction.FORWARD);
        targetFlag.setPosition(TARGETON);
        
    }
}









