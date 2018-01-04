/*
   Copyright 2009 Jean-R茅my Falleri

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package fr.labri.galatea.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.*;

import javax.security.auth.Refreshable;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Concept;
import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;

public class SimpleGSH2 extends Algorithm {
	
	//private HashMap<Concept, Set<Concept>>  hasChildren;
	
	//private HashMap<Concept, Set<Concept>>  hasparents;

	//private ConcurrentSkipListSet<Concept> concepts;
	private Set<Concept> concepts;
	
	
	private ConceptOrder gsh;
		
	private int threads=8;
	
	public SimpleGSH2(Context context) {
		
		super(context);
	}

	@Override
	public void compute() {
		
		//hasChildren=new HashMap<Concept, Set<Concept>>();
		//hasparents=new HashMap<Concept, Set<Concept>>();
		
		gsh = new ConceptOrder();
		//concepts = new ConcurrentSkipListSet<Concept>();
		concepts = new HashSet<Concept>();
		
		//获取线程的个数
		threads = Runtime.getRuntime().availableProcessors();
		//System.out.println("The valable number of threads is "+ threads);
		/*for( Entity e : context.getEntities() )  //尝试把这个线程化
			concepts.add(u(e));*/
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		for(Entity e : context.getEntities())
			tasks.add(new Task(e));
        List<Future<Concept>> results;
		ExecutorService exec = Executors.newFixedThreadPool(threads);
		try
		{
			results = exec.invokeAll(tasks);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
	        results = new ArrayList<Future<Concept>>();
		}
		exec.shutdown();
		for(Future<Concept> con:results)
		{
			try
			{
				concepts.add(con.get());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		//线程化
		/*for( Attribute a : context.getAttributes() )
			concepts.add(v(a));*/
		
		ArrayList<Task2> tasks2 = new ArrayList<Task2>();
		for(Attribute a : context.getAttributes())
			tasks2.add(new Task2(a));
        List<Future<Concept>> results2;
		ExecutorService exec2 = Executors.newFixedThreadPool(threads);
		try
		{
			results2 = exec2.invokeAll(tasks2);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
	        results2 = new ArrayList<Future<Concept>>();
		}
		exec2.shutdown();
		/*//能不能不重新建立新的线程	
		try
		{
			results2 = exec.invokeAll(tasks2);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
	        results2 = new ArrayList<Future<Concept>>();
		}
		exec.shutdown();*/
		
		for(Future<Concept> con:results2)
		{
			try
			{
				concepts.add(con.get());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		
		HashSet<Concept> addedConcepts = new HashSet<Concept>();

		//（有用的，由于指针的关系，递归更新c的内容）不过如果写成while形式的循环再加上一些中间值的判定，应该能节省一些时间。
		for( Concept c: concepts )
		{
			add(c,addedConcepts);
		}

		for( Concept c: concepts ) 
			gsh.getConcepts().add(c);
	}

	private void add(Concept c,Set<Concept> addedConcepts) {
		if ( addedConcepts.contains(c) )
			return;

		Set<Concept> allParents = allGreaters(c);
	
		for( Concept p: allParents) 
			if ( !addedConcepts.contains(p) )
				add(p,addedConcepts);

		addedConcepts.add(c);
		Set<Concept> smallestParents = selectSmallest(allParents);

		for ( Concept parent: smallestParents )
			c.addParent(parent);
	}

	private Set<Concept> allGreaters(Concept c) {
		HashSet<Concept> allGreaters = new HashSet<Concept>();
		for( Concept candidate: concepts)
		{
			/*//减少不必要的开销(待测试)
			if(hasChildren.keySet().contains(candidate))
			{
				if(hasChildren.get(candidate).contains(c))
				{
					allGreaters.add(candidate);	
					continue;
				}
			}*/						
			if ( candidate.isGreaterThan(c) )
			{
				allGreaters.add(candidate);		
				//refreshRelationShip(candidate,c);
			}				
		}
		allGreaters.remove(c);
		return allGreaters;
	}
	
	//效果并不明显
	/*private Set<Concept> allGreaters2(Concept c) 
	{
		HashSet<Concept> allGreaters = new HashSet<Concept>();	
		for( Concept candidate: Refinedconcepts)
		{				
			if ( candidate.isGreaterThan(c) )
			{
				allGreaters.add(candidate);		
			}				
		}
		allGreaters.remove(c);
		return allGreaters;
	}*/

	private Set<Concept> selectSmallest(Set<Concept> concepts) {
		Set<Concept> smallests = new HashSet<Concept>();
		for ( Concept c: concepts )
			push(c,smallests);

		return smallests;
	}

	private void push(Concept c,Set<Concept> smallests) {
		Set<Concept> swap = new HashSet<Concept>();
		for ( Concept current: smallests )
			if ( current.isSmallerThan(c) )
				return;
			else if ( c.isSmallerThan(current) )
			{
				swap.add(current);
				//refreshRelationShip(current,c);
			}
		if ( swap.size() > 0 )
			smallests.removeAll(swap);
			
		smallests.add(c);

	}

	private Concept u(Entity e) {
		Concept c = new Concept();
		c.getIntent().addAll(context.getAttributes(e));
		
		/*Set<Entity> extent = new HashSet<Entity>();
		extent.addAll(context.getEntities());*/
		
		/*Set<Attribute> intend=new HashSet<Attribute>(context.getAttributes()); 
		c.initIntent(intend);*/	
		
		Set<Entity> extent = new HashSet<Entity>(context.getEntities()); //效果是否是一样的?
		
		for( Attribute a: context.getAttributes(e) )
			extent.retainAll(context.getEntities(a));  //效果已经可以了
		
		c.getExtent().addAll(extent);
		//c.initExtent(extent);  //是不是可以节约少量操作?(但似乎较前者内存在增加)
		return c;
	}

	private Concept v(Attribute a) {
		Concept c = new Concept();
		c.getExtent().addAll(context.getEntities(a));
		
		/*Set<Attribute> intent = new HashSet<Attribute>();
		intent.addAll(context.getAttributes());*/
		
		/*Set<Entity> extent = new HashSet<Entity>(context.getEntities()); //效果是否是一样的?
		c.initExtent(extent);	*/	
		
		Set<Attribute> intent = new HashSet<Attribute>(context.getAttributes()); 
		
		for( Entity e: context.getEntities(a) )
			intent.retainAll(context.getAttributes(e));
		
		c.getIntent().addAll(intent);
		//c.initIntent(intent);//是不是可以节约少量操作?(但似乎较前者内存在增加)
		return c;
	}

	@Override
	public ConceptOrder getConceptOrder() {
		return gsh;
	}
	
	
	class Task implements Callable<Concept>
	{
		private Entity entity;
		public Task(Entity entity)
		{
			this.entity=entity;
		}
		public Concept call()
		{
			return u(entity);
		}
	}
	
	class Task2 implements Callable<Concept>
	{
		private Attribute attr;
		public Task2(Attribute attr)
		{
			this.attr=attr;
		}
		public Concept call()
		{
			return v(attr);
		}
	}
	
	
	
	/*//仍有点慢
	public void refreshRelationShip(Concept A,Concept B) //父母在前面，儿子在后面
	{	
		if(hasChildren.keySet().contains(A))
		{
			hasChildren.get(A).add(B);		
		}
		else
		{
			HashSet<Concept> set=new HashSet<Concept>();
			set.add(B);
			hasChildren.put(A, set);
		}		
		if(hasparents.keySet().contains(B))
		{
			hasparents.get(B).add(A);
		}
		else
		{
			HashSet<Concept> set=new HashSet<Concept>();
			set.add(A);
			hasparents.put(B, set);
		}	
		
		//更新hasChildren的部分	（把B的后代包括B加到A以及A的祖先中去)
		Boolean flagA=hasparents.containsKey(A);
		Boolean flagB=hasChildren.containsKey(B);		
		if(flagB)
			hasChildren.get(A).addAll(hasChildren.get(B));
		if(flagA)
		{
			if(flagB)
			{
				for (Concept parent : hasparents.get(A)) 
				{
					hasChildren.get(parent).add(B);
					hasChildren.get(parent).addAll(hasChildren.get(B));
				}
			}
			else 
			{
				for (Concept parent : hasparents.get(A)) 
				{
					hasChildren.get(parent).add(B);
				}
			}
		}
			
		//更新hasParent的部分 (将A的祖先包括A加到B的B儿子里去)		
		if(flagA)
			hasparents.get(B).addAll(hasparents.get(A));
		if(flagB)
		{
			if(flagA)
			{
				for (Concept child : hasChildren.get(B))

				{
					hasparents.get(child).add(A);
					hasparents.get(child).addAll(hasparents.get(A));
				}
			}
			else
			{
				for (Concept child : hasChildren.get(B))
				{
					hasparents.get(child).add(A);
					
				}
			}
		}		
	}*/
	
	

}
