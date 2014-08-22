package com.mattibal.meshnet;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import com.mattibal.meshnet.devices.Led1Analog2Device;
import com.mattibal.meshnet.devices.LedLamp1Device;
import com.mattibal.meshnet.devices.LedTestDevice;
import com.mattibal.meshnet.utils.color.AbsoluteColor;
import com.mattibal.meshnet.utils.color.gui.ChromaticityJFrame;

/**
 * Coso di test che fa partire la base della MeshNet, e poi scambia qualche
 * messaggio con i device
 */
public class MeshNetTest {

	public static void main(String[] args) {
		
		try {
			
			Layer3Base base = new Layer3Base();
			
			// Find the serial port device
			CommPortIdentifier portId = null;
			Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
			while(portEnum.hasMoreElements()){
				CommPortIdentifier currPort = (CommPortIdentifier) portEnum.nextElement();
				String portName = currPort.getName();
				if(portName.startsWith("/dev/tty.usbmodem") || portName.startsWith("/dev/ttyACM")){
					portId = currPort;
				}
			}
			if(portId == null){
				throw new IOException("No serial port found matching the name pattern");
			}
			
			SerialRXTXComm serial = new SerialRXTXComm(portId, base);
			Thread.sleep(4000);
			Layer3Base.NetworkSetupThread setup = base.new NetworkSetupThread();
			Thread setupThread = new Thread(setup);
			setupThread.start();
			setupThread.join();
			// Alè, la rete è pronta, adesso posso giocare con i device
			
			Device device = Device.getDeviceFromUniqueId(384932);
			if(device!=null && device instanceof LedTestDevice){
				LedTestDevice ledDevice = (LedTestDevice) device;
				for(int i=0; i<5000; i++){
					ledDevice.setLedState(true);
					Thread.sleep(20);
					ledDevice.setLedState(false);
					Thread.sleep(50);
				}
			} else if(device!=null && device instanceof Led1Analog2Device){
				Led1Analog2Device ledDev = (Led1Analog2Device) device;
				for(int a=0; a<50; a++){
					for(int i=0; i<255; i+=2){
						ledDev.setLedPwmState(ledTable8bit[i]);
						//Thread.sleep(10);
					}
					for(int i=255; i>0; i-=2){
						ledDev.setLedPwmState(ledTable8bit[i]);
						//Thread.sleep(10);
					}
				}
			} else {
				System.out.println("Errore get device");
			}
			
			Device dev = Device.getDeviceFromUniqueId(484333);
			if(dev!=null && dev instanceof LedLamp1Device){
				LedLamp1Device lamp = (LedLamp1Device) dev;
				
				/*for(int i=0; i<6; i++){
					AbsoluteColor color1 = new AbsoluteColor(0, 0, 0);
					lamp.setColor(color1);
					Thread.sleep(1000);
				}*/
				
				
			} else {
				System.out.println("Error getting led lamp device");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	private final static int ledTable8bit[] = new int[]{
			  0,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,
			  1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,   1,
			  1,   2,   2,   2,   2,   2,   2,   2,   2,   3,   3,   3,   3,   3,   4,   4,
			  4,   4,   4,   5,   5,   5,   5,   6,   6,   6,   6,   7,   7,   7,   7,   8,
			  8,   8,   9,   9,   9,  10,  10,  10,  11,  11,  12,  12,  12,  13,  13,  14,
			 14,  15,  15,  15,  16,  16,  17,  17,  18,  18,  19,  19,  20,  20,  21,  22,
			 22,  23,  23,  24,  25,  25,  26,  26,  27,  28,  28,  29,  30,  30,  31,  32,
			 33,  33,  34,  35,  36,  36,  37,  38,  39,  40,  40,  41,  42,  43,  44,  45,
			 46,  46,  47,  48,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59,  60,
			 61,  62,  63,  64,  65,  67,  68,  69,  70,  71,  72,  73,  75,  76,  77,  78,
			 80,  81,  82,  83,  85,  86,  87,  89,  90,  91,  93,  94,  95,  97,  98,  99,
			101, 102, 104, 105, 107, 108, 110, 111, 113, 114, 116, 117, 119, 121, 122, 124,
			125, 127, 129, 130, 132, 134, 135, 137, 139, 141, 142, 144, 146, 148, 150, 151,
			153, 155, 157, 159, 161, 163, 165, 166, 168, 170, 172, 174, 176, 178, 180, 182,
			184, 186, 189, 191, 193, 195, 197, 199, 201, 204, 206, 208, 210, 212, 215, 217,
			219, 221, 224, 226, 228, 231, 233, 235, 238, 240, 243, 245, 248, 250, 253, 255 };

}
