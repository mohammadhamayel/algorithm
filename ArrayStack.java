import java.util.Arrays;
public class ArrayStack<T> {
	private Object[] stack;
	//private int n = -1;
	private int stackSize ;
	private int topOfstack =-1;

	public ArrayStack(int capicity) {
		stackSize = capicity;
		stack = new Object[capicity];
		Arrays.fill(stack,"-1");
	}
    
	public void push(T data) {
		if(topOfstack + 1 < stackSize)
		//stack[++n] = data;
			stack[++topOfstack] = data;
		else 
			System.out.println("stack is full");
	}

	public Object pop() {
		if (!isEmpty())
			return stack[topOfstack--];
		return null;
	}
	
	public Object peak(){
		
		return stack[topOfstack];
	}

	public boolean isEmpty() {
		return (topOfstack == -1);
	}
	

	public String toString() {
		String res = "Top-->";
		for (int i = topOfstack; i >= 0; i--)
			res += "[" + stack[i] + "]" + "-->";
		return res;
	}


}
