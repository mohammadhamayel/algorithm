/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public class Heap//heap class contain delet minimum function and ment heap for rebuild it
{
    int size;
   public Node deleteMin(Node []item)//use to delete minimunm element in heap
	{
                //int size=item.length;
		int child;
		Node minElement =new Node(),lastElement;
		if(size>1)
		{
			minElement=item[1];
			lastElement=item[--size];
			int i;
			for(i=1;i*2<=size;i=child)
			{
                            child=i*2;
                            if(child<size&&item[child+1].freq<item[child].freq)
                            child++;
                            if(lastElement.freq>item[child].freq)
                                    item[i]=item[child];
                            else
                                    break;
			}
			item[i]=lastElement;
		return minElement;
		}
                minElement.freq=0;
                return minElement;
	 }
   public void add(Node[]array,Node z)
    {
           size++;
           array[size-1]=z;
    }
   public void mentHeapProperty(Node a[],int i,int size)//use to rebuilt heap
	{
		int l=2*i;
		int r=2*i+1;
		int smallest;
		if(l<=size-1&&a[l].freq<a[i].freq)
			smallest=l;
		else
			smallest=i;
		if(r<=size-1&&a[r].freq<a[smallest].freq)
			smallest=r;
		if(i!=smallest)
		{
                    Node temp=a[i];
                    a[i]=a[smallest];
                    a[smallest]=temp;
                    mentHeapProperty(a,smallest,size);
		}
	}  
   public Node delMin() {
       if(size != 0) {
               Node min = heap[1];
               heap[1] = heap[size];
               size--;
               heapify(1);
               return min;
       }
       return null;
}


}