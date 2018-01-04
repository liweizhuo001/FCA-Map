import java.util.*;
import java.util.concurrent.*;

class TaskWithResult implements Callable<String>
{
	
	private int id;
	public TaskWithResult(int id)
	{
		this.id=id;
	}
	public String call()
	{
		return "result of TaskWithResult "+id;
	}
}

public class MutiThreadDemo 
{
	public static void main(String[] args)
	{
		ExecutorService exec=Executors.newCachedThreadPool();
		ArrayList<Future<String>> results= new ArrayList<Future<String>>();
		for(int i=0;i<10;i++)
		{
			results.add(exec.submit(new TaskWithResult(i)));
		}
		for(Future<String> fs:results)
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
