package com.folleach.gui;

import java.util.List;

import org.lwjgl.input.Mouse;

import com.folleach.daintegrate.MathHelper;
import com.folleach.daintegrate.Pallete;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ScrollPanel<T extends IEntry> extends Gui {
    private final List<T> entries = Lists.<T>newArrayList();
    
    private int x, y, width, height; 
    public int scrollPosition;
    public int scrollbarHeight;
    public int visualHeight;
    public int contentHeight;
    public boolean visible;
    
    public ScrollPanel(int x, int y, int width, int height) {
    	this.x = x;
    	this.y = y;
    	this.width = width;
    	this.height = height;
    	this.visualHeight = height - y;
	}
    
    public void drawPanel(int mouseX, int mouseY, float partialTicks)
    {
    	if (-scrollPosition > contentHeight - visualHeight)
    		scrollPosition = -(contentHeight - visualHeight);
    	
    	if (scrollPosition > 0)
    		scrollPosition = 0;
    	
    	if (contentHeight > visualHeight) {
    		int pos = MathHelper.convertRange(-scrollPosition, 0, contentHeight - visualHeight, 0, height - 50);
    		drawRect(width - 4, y+pos, width, pos + 50, Pallete.YELLOW);
    	}
    	int offset = scrollPosition + y;
    	for (int i = 0; i < entries.size(); i++)
    	{
    		entries.get(i).drawEntry(x, offset, mouseX, mouseY, partialTicks);
    		offset += entries.get(i).getHeight();
    	}
    
    }
    
    public void keyTyped(char typedChar, int keyCode)
    {
    	for (int i = 0; i < entries.size(); i++)
    		entries.get(i).keyTyped(typedChar, keyCode);
    }
    
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
    	if (mouseY >= 20)
    	for (int i = 0; i < entries.size(); i++)
    		entries.get(i).mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public void handleMouseInput() {
    	int wheel = Mouse.getDWheel();
    	if (wheel != 0)
    		scrollPosition += wheel / 5;
    }
    
    public void updateHeight() {
    	contentHeight = 0;
    	for (int i = 0; i < entries.size(); i++)
    		contentHeight += entries.get(i).getHeight();
    }
    
    public void addEntry(T entry) {
    	contentHeight += entry.getHeight();
    	entries.add(entry);
    }
    
    public List<T> getEntries() {
    	return entries;
    }
    
    public void clearEntries() {
    	contentHeight = 0;
    	this.entries.clear();
    }
    
    public void removeAt(int index) {
    	contentHeight -= entries.get(index).getHeight();
    	entries.remove(index);
    }
    
    public void removeEntry(T entry) {
    	entries.remove(entry);
    }
}
