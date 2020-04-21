//version 1.12.2
package com.mactso.happytrails.network;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.network.HTPacket.HTHandler;

public class Register {

	public static void initPackets()
	{
		if (MyConfig.aDebugLevel>0) {
			System.out.println ("HT: Register Client message");
		}
		Manager.registerClientMessage(HTPacket.class, HTHandler.class);
	}
	
}
