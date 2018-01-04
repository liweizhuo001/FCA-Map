import java.util.*;
import java.util.concurrent.*;

class TaskWithResult2 implements Callable<Integer>
{
	private int id;
	public TaskWithResult2(int id)
	{
		this.id=id;
	}
	public Integer call()
	{
		return id+1;
	}
}

public class MutiThreadDemo2 
{
	public static void main(String[] args)
	{
		ExecutorService exec=Executors.newCachedThreadPool();
		ArrayList<Future<Integer>> results= new ArrayList<Future<Integer>>();
		for(int i=0;i<10;i++)
		{
			results.add(exec.submit(new TaskWithResult2(i)));
		}
		for(Future<Integer> fs:results)
		{
			try
			{
				System.out.println(fs.get());
			}
			catch(InterruptedException e)
			{
				System.out.println(e);
			}
			catch (ExecutionException e) {
				System.out.println(e);
			}
			finally
			{
				exec.shutdown();
			}
		}
	}

}
