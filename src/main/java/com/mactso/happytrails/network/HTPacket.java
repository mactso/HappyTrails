package com.mactso.happytrails.network;

import com.mactso.happytrails.config.MyConfig;
import com.mactso.happytrails.config.TrailBlockManager;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class HTPacket implements IMessage
{
	private String pRegistryName;
	private int pMetaValue;
	private int pSpeedValue;
	
	public HTPacket()
	{
	}
	
	public HTPacket(String newRegistryName, int newMeta, int newSpeedValue)
	{
		this.pRegistryName = newRegistryName;
		this.pMetaValue = newMeta;
		this.pSpeedValue = newSpeedValue;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pRegistryName = ByteBufUtils.readUTF8String (buf);
		pMetaValue= buf.readInt();	
		pSpeedValue = buf.readInt();	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String (buf, pRegistryName);
		buf.writeInt(pMetaValue);
		buf.writeInt(pSpeedValue);
	}
	
	public static class HTHandler implements IMessageHandler<HTPacket, IMessage>
	{
		@Override
		public IMessage onMessage(HTPacket message, MessageContext ctx)
		{
			if (MyConfig.aDebugLevel>0) {
				System.out.println("Message Block Speed: " + message);
			}
			Minecraft.getMinecraft().addScheduledTask(() -> {
				String s = TrailBlockManager.updateRemoveTrailBlockInfo(message.pRegistryName, message.pMetaValue, message.pSpeedValue);
			});
			return null;	
		}
	}
}

