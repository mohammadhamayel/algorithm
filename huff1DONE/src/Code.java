/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



public class Code//this class use to transact with binary code
{
    byte []Bytes=new byte[32+1];
    byte c;
    
    public Code()
    {

    }
    public Code(Code b)
    {
      CopyTo(b);
    }

    public void CopyTo(Code b)//this function use to copy array of byte to another array
    {
        int i;
        for (i=0;i<33;i++)
            Bytes[i]=b.Bytes[i];
        c=b.c;
    }

    byte getBit(int bitNo)//this function to return bit by sent them location of it
    {
        return (byte) (Bytes[bitNo/8+1] >> (7-(bitNo%8)) & 1);
    }

    Code addBit(byte bitValue)//this function use to add bits to byte
    {
        Bytes[Bytes[0]/8+1] += (bitValue<<(7-(Bytes[0]%8)));
        Bytes[0]++;
        return this;
    }

    String getCode()//this function use to return the code representation for all characters used
    {
        String S;
        S="";
        for (int i=0;i<Bytes[0];i++)
        {
            if (getBit(i)==1)
                S+="1";
            else
                S+="0";
        }
        return S;
    }
}
