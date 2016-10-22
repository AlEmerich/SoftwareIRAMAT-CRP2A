package util;

import java.awt.Color;

public enum ColorMaterial
{
    yellow("yellow", 0, Color.yellow), 
    red("red", 1, Color.red), 
    green("green", 2, Color.green), 
    blue("blue", 3, Color.blue), 
    orange("orange", 4, Color.orange), 
    gray("gray", 5, Color.gray), 
    cyan("cyan", 6, Color.cyan), 
    magenta("magenta", 7, Color.magenta), 
    pink("pink", 8, Color.pink);
    
    private Color c;
    
    private ColorMaterial(final String name, final int ordinal, final Color c) {
        this.c = c;
    }
    
    public Color getColor() {
        return this.c;
    }
}
