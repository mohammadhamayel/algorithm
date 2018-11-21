 
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JProgressBar;


public class Huffman
{
    Heap heap;
    long fsizeb=0,fsizea=0;
    int []freq;
    Code codes[];
    byte CharSet[];
    int Chars;
    String codeout="";
    InputStream in;
    File fin;
    int dn,bit,extSize,hBytes;
    byte ext[];
    byte []hCodes;
    public void getHeader(String name) throws FileNotFoundException, IOException
    {
            fin=new File(name);
            in=new FileInputStream(fin);

            extSize=in.read();
            ext=new byte[extSize];
            in.read(ext,0,extSize);
            Chars=in.read();
            Chars+=1;
            dn=Chars*2;
            int len1=0;
            CharSet=new byte[dn];
            in.read(CharSet, 0, dn);
            for(int i=0;i<dn;i=i+2)
            {
                len1+=CharSet[i+1];
            }
            hBytes=(len1-1)/8+1;
            hCodes=new byte[hBytes];
            in.read(hCodes, 0,hBytes);
            bit=0;
            codes=new Code[Chars];
            for (int i=0;i<Chars;i++)
            {
                 codes[i]=new Code();
                 codes[i].c=CharSet[i*2];
                 for (int j=0;j<CharSet[i*2+1];j++)
                 {
                     codes[i].addBit((byte)(hCodes[bit/8] >> (7-(bit%8)) & 1));
                     bit++;
                 }
            }
    }
    public Huffman(){}
    //contractor that recive flag for compress=1,for decompress=0, and progress bar read and write
    public Huffman(int comp,String fname,String fout,JProgressBar rdBar,JProgressBar wrBar) throws FileNotFoundException, IOException
    {

        if(comp==1)//in this statment the program make compress
        {
                if (rdBar!=null) rdBar.setValue(0);
                File f=new File(fname);
                fsizeb=f.length();
	        freq=new int[256];
                int []flag=new int[256];
	        InputStream s=new FileInputStream(f);
	        int block=1024;
	        long len=f.length();
	        long b;
	        while(len>0)//read file for get frequinces to each character
	        {
	        	 if(len>block)
	        		 b=block;
	        	 else
	        		 b=len;
	        	 byte []k=new byte[(int)b];
	             s.read(k, 0,(int) b);
	             len-=b;
                     int count=0;
                     if (rdBar!=null) rdBar.setValue((int) ((f.length() - len)*100 / f.length()));
                     for(int i=0;i<flag.length;i++) flag[i]=0;

                     for(int i=0;i<k.length;i++)
                     {
                       if(flag[k[i]<0?k[i]+256:k[i]]!=1)
                       {
                        for(int y=0;y<k.length;y++)
                            if(k[i]==k[y])
                            {
                                count++;
                            }
                         if (count>0)
                         {
                             flag[k[i]<0?k[i]+256:k[i]]=1;
                             freq[k[i]<0?k[i]+256:k[i]]+=count;
                         }
                         count=0;
                       }
                      }
	        }
                s.close();
                int nz=0;
                for(int i=0;i<freq.length;i++)
                    if(freq[i]!=0)
                        nz++;
                int nonzero=nz;
                Node []node=new Node[nz+1];
                Code []clone=new Code[256];
                for(int i=0;i<clone.length;i++)
                    clone[i]=new Code();
                for(int i=1;i<node.length;i++)
                    node[i]=new Node();
                nz=1;
                for(int i=0;i<freq.length;i++)
                {
                    if(freq[i]!=0)
                    {
                        node[nz].freq=freq[i];
                        node[nz].name=(char)i;
                        nz++;
                    }
                }
        heap=new Heap();
       for(int i=node.length/2;i>=1;i--)//get minimum element in the top of heap
            heap.mentHeapProperty(node,i,node.length);
       Node z=new Node();
       heap.size=node.length;
       insert(node,z);//build the three that use to get huff code
       Code C=new Code();
       getByte(node[1],C,clone);//get codes from tree in this function
       File file=new File(fname);
       InputStream scan=new FileInputStream(file);
       len=file.length();
       b=0;
       File fileout=new File(fout+"."+"Huff");
       DataOutputStream o=new DataOutputStream(new FileOutputStream(fileout));
       block=1024;
       byte []buffer=new byte[4096];
       int y=0;
       int bit=0;
       int length=0;
       String exten=fname.substring(fname.lastIndexOf('.')+1,fname.length());
       o.write(exten.length());
       for(int i=0;i<exten.length();i++)
           o.write(exten.charAt(i));
       o.write(nonzero-1);
       for(int i=0;i<clone.length;i++)
       {
           if(clone[i].Bytes[0]!=0)
           {
               o.write((byte)i);
               o.write(clone[i].Bytes[0]);
               for (int j=0;j<clone[i].Bytes[0];j++)
               {
                     buffer[bit/8] += (clone[i].getBit((byte)j)<<(7-(bit%8)));
                     bit++;
               }
           }
       }
       o.write(buffer, 0, (bit-1)/8+1);
       bit=0;
       for (int i=0;i<buffer.length;i++) buffer[i]=0;
       if (wrBar!=null) wrBar.setValue(0);
       while(len>0)//read file and write on the huff file for get compressed file
       {
            if(len>block)
               b=block;
            else
               b=len;
            byte []k=new byte[(int)b];
	    scan.read(k, 0,(int) b);
	    len-=b;
            if (wrBar!=null) wrBar.setValue((int) ((f.length() - len)*100 / f.length()));
            for(int i=0;i<k.length;i++)
            {
                    int l=clone[k[i]<0?k[i]+256:k[i]].Bytes[0];
                    length+=l;
                    for (int n=0;n<l;n++)
                    {

                       y=bit/8;
                       buffer[y] += (clone[k[i]<0?k[i]+256:k[i]].getBit((byte)n)<<(7-(bit%8)));
                       bit++;
                      if ((bit/8)==buffer.length)
                       {
                           // write buffer to file and reset bit counter
                           o.write(buffer,0,buffer.length);
                           for (int ii=0;ii<buffer.length;ii++) buffer[ii]=0;
                           bit=0;
                           y=0;
                       }
                    }
            }

       }
      if (bit>0)
      {
           y=bit/8;
           o.write(buffer,  0, y+1);
      }
      o.write(bit%8);
      o.close();
      fsizea=fileout.length();

      }
 else
        if(comp==0)//this statment use for decompress the file
        {

            getHeader(fname);//this function use to getHeader from Huff file to process them
            for (int i=0;i<Chars-1;i++)
                for (int j=i+1;j<Chars;j++)
                {
                    if (isGreater(codes[i].Bytes,codes[j].Bytes))
                    {
                         Code x=new Code(codes[i]);
                         codes[i].CopyTo(codes[j]);
                         codes[j].CopyTo(x);
                    }
                }

           long len=0;
           len=fin.length();
           fsizea=fin.length();
           len-=dn+hBytes+2+extSize;

           int block=1024;
           long d=0;
           bit=1;
           int bits=0;
           int m=0;
           int C;
           Code code=new Code();
           byte []outbuffer=new byte[4096];
           String exname="";
           for(int i=0;i<ext.length;i++)
               exname+=(char)ext[i];

           File f=new File(fout+"."+exname);
           DataOutputStream out=new DataOutputStream(new FileOutputStream(f));
           if (rdBar!=null) rdBar.setValue(0);
           if (wrBar!=null) wrBar.setValue(0);
           while(len>0)//here program read from .Huff file and write to the file to return the un compressed
            {

                byte []k;
                if(len>block)
                {
                  d=block;
                  k=new byte[(int)d];
                  in.read(k, 0,(int) d);
                  bits=block*8;
                }
                else
                {
                  d=len;
                  k=new byte[(int)d];
                  in.read(k, 0,(int) d);
                  bits=((int)  d * 8) ;

                }
                if (len==d)
                {
                    bits -= 8;
                    bits-= 8-k[(int) d - 1];

                }
                len-=d;
              if (rdBar!=null) rdBar.setValue((int) ((fin.length() - len)*100 / fin.length()));
                if (wrBar!=null) wrBar.setValue((int) ((fin.length() - len)*100 / fin.length()));
                for (bit=0;bit<bits;bit++)
                {
                    code.addBit((byte)(k[bit/8] >> (7-(bit%8)) & 1)) ;
                    if ((C=BinarySearch(code))>=0)
                    {
                         outbuffer[m++]=codes[C].c;
                         if (m==outbuffer.length)
                            {
                               out.write(outbuffer,0,m);
                               m=0;
                            }
                         code=new Code();
                    }

                }
            }
           if (m!=0)
           {
                out.write(outbuffer,0,m);
           }
           out.close();
           fsizeb=f.length();
        }
    }

    public int BinarySearch(Code code)//this function use to make search on code from .Huff file with file that uncompress in the process
    {
        int min,max,mid;
        boolean found=false;
        min=0;
        max=Chars-1;

        do
        {
            mid=min+((max-min) / 2);
            if (isGreater(code.Bytes,codes[mid].Bytes))
                min=mid+1;
            else
                max=mid-1;
            found=isEqual(codes[mid].Bytes,code.Bytes);
        } while  (min<=max && !found);

        if (found)
            return mid ;
        else
            return -1;

    }

    public boolean isEqual(byte []a,byte[] b)//this function use compare two array of byte to know that are equal or not boolean function
    {
        int i=0;
        boolean ok=true;

        while (i<a.length && ok)
            if (a[i]!=b[i])
                ok=false;
            else
                i++;
        return (i==a.length);
    }

    public boolean isGreater(byte []a,byte[] b)//this function use as utility function that give if the array greater than the other one
    {
        int i=0;
        boolean ok=true;

        while (i<a.length && ok)
            if (a[i]<b[i])
                ok=false;
            else if (a[i]>b[i])
                return true;
            else
                i++;
        return false;
    }

    public void insert(Node[]node,Node z)//function use to build tree using heap
    {
        for(int i=0;i<heap.size-2;i++)
        {
            z=new Node();
            z.left=heap.deleteMin(node);
            z.right=heap.deleteMin(node);
            z.freq=(z.left).freq+(z.right).freq;
            heap.add(node, z);
            for(int y=heap.size/2;y>=1;y--)
                heap.mentHeapProperty(node,y,heap.size);
            insert(node,z);
        }
    }
    public void getByte(Node tree,Code b,Code []clone)//function use to get huff code for character used
    {
        if(tree.left!=null)
        {
            Code c=new Code(b);
            getByte(tree.left,c.addBit((byte)0),clone);
        }
        if(tree.right!=null)
        {
            Code c=new Code(b);
            getByte(tree.right,c.addBit((byte)1),clone);
        }
        if(tree.left==null&&tree.right==null)
        {
            clone[tree.name]=b;
        }
    }
    public void getCodes()//this function use to get character and this binary represntation
    {
         for (int i=0;i<Chars;i++)
           {
               codeout+="("+codes[i].c+")";
               codeout+=codes[i].getCode();
               codeout+="\n";
           }
    }
}