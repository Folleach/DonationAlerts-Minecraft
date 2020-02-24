package com.folleach.gui;

import com.folleach.daintegrate.MathHelper;
import com.folleach.daintegrate.Pallete;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ScrollPanel<T extends IEntry> extends Widget
{
    private final List<T> entries = Lists.<T>newArrayList();
    
    private int x, y, width, height; 
    public int scrollPosition;
    public int scrollbarHeight;
    public int visualHeight;
    public int contentHeight;
    public boolean visible;
    
    public ScrollPanel(int x, int y, int width, int height)
	{
		super(x, y, width, height, "msg");
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
    		fill(width - 4, y+pos, width, pos + 50, Pallete.YELLOW);
    	}
    	int offset = scrollPosition + y;
    	for (int i = 0; i < entries.size(); i++)
    	{
    		entries.get(i).drawEntry(x, offset, mouseX, mouseY, partialTicks);
    		offset += entries.get(i).getHeight();
    	}
    }
    
    public boolean charTyped(char typedChar, int keyCode)
    {
    	for (int i = 0; i < entries.size(); i++)
    		entries.get(i).keyTyped(typedChar, keyCode);
    	return true;
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
    	if (mouseY >= 20)
    		for (int i = 0; i < entries.size(); i++)
    			entries.get(i).mouseClicked(mouseX, mouseY, mouseButton);
    	return true;
    }

    public boolean keyPressed(int a, int b, int c)
	{
		for (int i = 0; i < entries.size(); i++)
			entries.get(i).keyPressed(a, b, c);
		return true;
	}

	public boolean mouseScrolled(double a, double b, double delta)
    {
    	scrollPosition += delta * 30;
    	return true;
	}
    
    public void updateHeight()
	{
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
