/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import javax.swing.JOptionPane;


public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        if(args.length>0)
        {
            Huffman h;
            String name=args[0].substring(args[0].lastIndexOf('\\')+1,args[0].lastIndexOf('.'));
            if(args[0].endsWith("Huff"))
               h=new Huffman(0,args[0],name,null,null);
            else
                h=new Huffman(1,args[0],name,null,null);
            JOptionPane.showMessageDialog(null, "Done","Message",JOptionPane.INFORMATION_MESSAGE);
        }
       else
        {
           frame f=new frame();
           f.main(null);
        }

    }

}